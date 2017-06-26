package ru.saydumarov;

import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.SeedSelectionMethods;
import jsat.clustering.evaluation.DaviesBouldinIndex;
import jsat.clustering.kmeans.ElkanKMeans;
import jsat.clustering.kmeans.KMeansPDN;
import jsat.linear.SparseVector;
import jsat.linear.Vec;
import jsat.linear.distancemetrics.EuclideanDistance;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class InfluenceClustering {

//  Нормализация векторов влияния на 1
    private static List<List<Double>> normalize (List<List<Double>> data){

        List<List<Double>> temp = new ArrayList<>();

        Map<Double, Double> subsidiaryMap = new TreeMap<>();
        for (List<Double> row : data){
            if (subsidiaryMap.get(row.get(0)) != null){
                double d = row.get(2) + subsidiaryMap.get(row.get(0));
                subsidiaryMap.put(row.get(0), d);
            }
            else {
                subsidiaryMap.put(row.get(0), row.get(2));
            }
        }

        for (List<Double> row : data){
            List<Double> row1 = new ArrayList<>();
            row1.add(row.get(0));
            row1.add(row.get(1));
            double d = row.get(2)/subsidiaryMap.get(row.get(0));
            row1.add(d);
            temp.add(row1);
        }
        return temp;
    }

//  Нумерация компонент вектора влияний, соотношение их с реальными пользователями
    private static HashMap<Double, Integer> reMapInfluentialUsers(List<List<Double>> data){
        HashMap<Double, Integer> hashMap = new HashMap<>();
        Integer v = 0;
        for (List<Double> row : data){
            if (!hashMap.containsKey(row.get(1))){
                hashMap.put(row.get(1), v);
                v++;
            }
        }
        return hashMap;
    }

//  Перенумерация пользователей
    private  static HashMap<Double, Integer> reMapUsers(List<List<Double>> data){
        HashMap<Double, Integer> hashMap = new HashMap<>();
        Integer v = 0;
        for (List<Double> row : data){
            if(!hashMap.containsKey(row.get(0))){
                hashMap.put(row.get(0), v);
                v++;
            }
        }
        return hashMap;
    }

//  Преобразование журнала действий в карту пользователей и соответствующих им векторов влияния
    private static ConcurrentSkipListMap<Integer, SparseVector> toMapTransform(List<List<Double>> data){
        ConcurrentSkipListMap<Integer, SparseVector> mapData = new ConcurrentSkipListMap<>();
        HashMap<Double, Integer> mappingInfluential = reMapInfluentialUsers(data);
        HashMap<Double, Integer> mappingUsers = reMapUsers(data);
        int columns = mappingInfluential.keySet().size();
        int strings = mappingUsers.keySet().size();
        System.out.println("Число влиятельных пользователей: " + columns);
        System.out.println("Число пользователей: " + strings);
        for (List<Double> row : data){
            SparseVector v = new SparseVector(columns);
            if (mapData.get(mappingUsers.get(row.get(0))) != null){
                v = mapData.get(mappingUsers.get(row.get(0)));
                v.set(mappingInfluential.get(row.get(1)), row.get(2));
                mapData.put(mappingUsers.get(row.get(0)), v);
            }
            else {
                v.set(mappingInfluential.get(row.get(1)), row.get(2));
                mapData.put(mappingUsers.get(row.get(0)), v);
            }
        }
        return mapData;
    }



//    Создание "карты" вида "Номер влиятельного пользователя:оказанное им суммарное влияние"
    static ConcurrentSkipListMap<Double, Double> influenceCounter(double[][] data){
        ConcurrentSkipListMap<Double, Double> map = new ConcurrentSkipListMap<>();
        for (double[] aData : data) {
            if (map.get(aData[1]) != null) {
                Double k = map.get(aData[1]) + aData[2];
                map.put(aData[1], k);
            } else {
                map.put(aData[1], aData[2]);
            }
        }
        return map;
    }

//Преобразование данных, связанных с категориями пользователей
    private static ConcurrentSkipListMap<Integer, String> labelMap(String labels, List<List<Double>> data){
        ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>();
        HashMap<Double, Integer> mappingInfluential = reMapInfluentialUsers(data);
        List<List<String>> id = new ArrayList<>();
        try {
            id = FileWorker.readId(labels);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(id.size());
        for (List<String> row : id){
            String value = "";
            for (int i = 1; i < row.size(); i++){
                value += row.get(i) + " ";
            }
            map.put(mappingInfluential.get(Double.valueOf(row.get(0))), value);
        }
        return map;
    }


    public static void main(String[] args){
//      Пути к папкам для входных и выходных данных
        String fileNameOut = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\test experiment\\output";
        String fileNameOutVec = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\test experiment\\Statistics\\vector";
        String fileNameOutStatistics = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\test experiment\\Statistics\\clusters";
        String fileNameIn = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\actions_influence.txt";
        String labels = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\labled.txt";
//        String fileNameOut1  = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\test experiment\\Statistics\\output";

//      Чтение журнала действий, запись в массив
        double[][] array = new double[0][];
        try {
            array = FileWorker.read(fileNameIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


//      Переменная, показывающая нижний порог того числа сынициированных влиятельным пользователем действий, после которого
//      влиятельный пользователь может считаться достаточно "влиятельным". При помощи параметра 284, выделяется 2% наиболее
//      влиятельных пользователей, параметр 91 позволяет выделить 5%
        int influenceThreshold = 284;

//      Создание журнала действий, из которого "выброшены" записи о действиях, сынициированных влиятельными пользователями с
//      показателем влиятельности ниже influenceThreshold. Преобразование журнала из двумерного массива в список списков
        List<List<Double>> filteredList = Filter.globalFilter(array, Filter.influenceFilter(influenceCounter(array), influenceThreshold));


//      Нормировка действий на единицу
        filteredList = normalize(filteredList);

//      Создание "карты" лэйблов вида "Влиятельный пользователь:метка сферы интересов"
        ConcurrentSkipListMap<Integer, String> labelMap = labelMap(labels, filteredList);

//      Преобразование журнала действий в "карту" вида "Пользователь:вектор влияния"
        ConcurrentSkipListMap<Integer, SparseVector> list = toMapTransform(filteredList);

//      Параллелизация, разбиение на 4 потока - по числу процессоров
        ExecutorService executorService = Executors.newFixedThreadPool(4);

//      Число экспериментов по проведению кластеризации
        int amountOfExperiments = 50;

//      Счётчик размеров кластеризаций и "хранилище" индексов Дэвиса-Болдина
        double[][] arrayOfEvaluations = new double[amountOfExperiments][2];

        for (int numberOfExperiment = 0; numberOfExperiment < amountOfExperiments; numberOfExperiment++){
//          Старт счётчика времени
            long start = System.currentTimeMillis();

//          Преобразование списка векторов данных в специализированный тип для наборов данных
            SimpleDataSet dataSet = Clustering.createDataSet(list);
//          Переменная для расчёта индекса Дэвиса-Болдина
            DaviesBouldinIndex daviesBouldinIndex = new DaviesBouldinIndex();
//          Объект класса, позволяющего реализовать кластеризацию методом kMeansPDN. Входные параметры конструктора:
//          метод Elkan K-Means, основанный на евклидовой метрике, выборе начальных точек методом K-Means++ с помощью генератора случайных чисел
            KMeansPDN kMeansPDN = new KMeansPDN(new ElkanKMeans(new EuclideanDistance(), new Random(), SeedSelectionMethods.SeedSelection.KPP));
//          Создание одной "кластеризации" - списка, каждый элемент которого - кластер (список точек - объектов класса DataPoint)
            List<List<DataPoint>> arr = kMeansPDN.cluster(dataSet, 54, 56, executorService);
//          Список векторов в пространстве категорий для данной кластеризации
            List<Vec> vecs = new ArrayList<>();


            arrayOfEvaluations[numberOfExperiment][0] = arr.size();
            System.out.println("Получилось " + arrayOfEvaluations[numberOfExperiment][0] + " кластеров");
            arrayOfEvaluations[numberOfExperiment][1] = daviesBouldinIndex.evaluate(arr);
            System.out.println("Индекс Дэйвиса-Болдина равен " + arrayOfEvaluations[numberOfExperiment][1]);


            long finish = System.currentTimeMillis();
            System.out.println("Затраченное время 1: " + (finish - start));

//          Создание массива, в котором содержатся размеры каждого кластера данной кластеризации
            int[] arrayOfClusterSizes = new int[arr.size()];

            for (int i = 0; i < arr.size(); i++){
                System.out.println((i + 1) + "-ый кластер получился размером: " + arr.get(i).size());
                arrayOfClusterSizes[i] = arr.get(i).size();
//              Запись каждого кластера
                List<DataPoint> dataPointList = arr.get(i);
                FileWorker.write(fileNameOut, numberOfExperiment, i, dataPointList);
//              Запись вектора категорий для каждого кластера
                Vec vec = MapVector.vectorOfCategories(MapVector.matrix(labelMap), dataPointList);
                vecs.add(vec);
                FileWorker.write(fileNameOutVec, numberOfExperiment, i, vec);

//                HashMap<Integer, Double> values = Clustering.trueCorrelation(dataPointList);
//                FileWorker.write(fileNameOut1, numberOfExperiment, i, values);
            }

//          Распределение по наиболее значимым категориям
            HashMap<Integer, Integer> distribution = MapVector.distribution(vecs);
            System.out.println("Число значимых категорий в данной кластеризации: " + distribution.size());
            FileWorker.write(fileNameOut, numberOfExperiment, distribution);
            FileWorker.write(fileNameOutStatistics, numberOfExperiment, "size", arrayOfClusterSizes);

            System.out.println("Выполнен " + numberOfExperiment + "ый шаг. Осталось ещё " + (amountOfExperiments - numberOfExperiment - 1) + " шагов");
        }

        FileWorker.write(fileNameOutStatistics, -1, "amount", arrayOfEvaluations);
        executorService.shutdown();
    }
}
