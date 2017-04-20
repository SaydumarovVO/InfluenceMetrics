package ru.saydumarov;

import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.SparseRealVector;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;


public class Normalization{


    public static int amountOfUsers (double[][] data){
        HashSet<Double> set = new HashSet<Double>();
        for (int i = 0; i < data.length; i++){
            set.add(data[i][0]);
        }
        return set.size();
    }

    public static int amountOfInfluentalUsers (double data[][]){
        HashSet<Double> set = new HashSet<Double>();
        for (int i = 0; i < data.length; i++){
            set.add(data[i][1]);
        }
        return set.size();
    }

    public static List<List<Double>> normalize (List<List<Double>> data){

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

    public static double[][] normalize (double[][] data){
        double max = 0;

        for (int i = 0; i < data.length; i++){
            if (data[i][0] >= max){
                max = data[i][0];
            }
        }

        int[] subsidiaryArray = new int[(int) max + 1];
        for (int i = 0; i < data.length; i++){
             subsidiaryArray[(int) data[i][0]] += data[i][2];
        }

        for (int i = 0; i < data.length; i++){
            data[i][2] = data[i][2]/subsidiaryArray[(int) data[i][0]];
        }
        return data;
    }


//    public static int getAmountOfUsers(double[][] data){
//        int max = 0;
//        for (int i = 0; i < data.length; i++){
//            if (data[i][0] > max){
//                max = (int) data[i][0];
//            }
//        }
//        System.out.println("Количество агентов в сети " + max);
//        return max;
//    }

    //Нумерация компонент вектора влияний, соотношение их с реальными пользователями
    public static HashMap<Double, Integer> getInfluentialUsers(List<List<Double>> data){
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


//    public static HashMap<Double, Integer> getInfluentialUsers(double[][] data){
//        HashMap<Double, Integer> hashMap = new HashMap<>();
//        Integer v = 0;
//        for (int i = 0; i < data.length; i++){
//            if (!hashMap.containsKey(data[i][1])){
//                hashMap.put(new Double(data[i][1]), v);
//                if ((i % (Math.floor(data.length/100)) == 0) || (i == data.length - 1)){
//                    System.out.println("Параметры метода getInfluentUsers: i = " + i + "; k = " + hashMap.get(data[i][1]) + "; v = " + v);
//                }
//                v++;
//            }
//        }
//        return hashMap;
//    }


    public static double summator(double[][] data){
        double sum = 0;
        for (int i = 0; i < data.length; i++){
            sum += data[i][1];
        }
        return sum;
    }

    public static ConcurrentSkipListMap<Double, SparseRealVector> toMapTransform(List<List<Double>> data){
        ConcurrentSkipListMap<Double, SparseRealVector> mapData = new ConcurrentSkipListMap<>();
        HashMap<Double, Integer> mapping = getInfluentialUsers(data);
        int columns = mapping.keySet().size();
        System.out.println("Число влиятельных пользователей: " + columns);
        for (List<Double> row : data){
            SparseRealVector v = new OpenMapRealVector(columns);
            if (mapData.get(row.get(0)) != null){
                v = mapData.get(row.get(0));
                v.setEntry(mapping.get(row.get(1)), row.get(2));
                mapData.put(row.get(0), v);
            }
            else {
                v.setEntry(mapping.get(row.get(1)), row.get(2));
                mapData.put(row.get(0), v);
            }
//            if ((i % (Math.floor(data.length/100)) == 0) || (i == data.length - 1)){
//                System.out.println("Выполнена " + i + "ая итерация");
//            }
        }
        return mapData;
    }


//    public static ConcurrentSkipListMap<Double, SparseRealVector> toMapTransform(double[][] data){
//        ConcurrentSkipListMap<Double, SparseRealVector> mapData = new ConcurrentSkipListMap<>();
//        HashMap<Double, Integer> mapping = getInfluentialUsers(data);
//        int columns = mapping.keySet().size();
//        System.out.println("Число влиятельных пользователей: " + columns);
//        for (int i = 0; i < data.length; i++){
//            SparseRealVector v = new OpenMapRealVector(columns);
//            if (mapData.get(new Double(data[i][0])) != null){
//                v = mapData.get(new Double(data[i][0]));
//                v.setEntry(mapping.get(new Double(data[i][1])), data[i][2]);
//                mapData.put(new Double(data[i][0]), v);
//            }
//            else {
//                v.setEntry(mapping.get(new Double(data[i][1])), data[i][2]);
//                mapData.put(new Double(data[i][0]), v);
//            }
////            if ((i % (Math.floor(data.length/100)) == 0) || (i == data.length - 1)){
////                System.out.println("Выполнена " + i + "ая итерация");
////            }
//        }
//        return mapData;
//    }

    public static ConcurrentSkipListMap<Double, Double> influenceCounter(double[][] data){
        ConcurrentSkipListMap<Double, Double> map = new ConcurrentSkipListMap<>();
        for (int i = 0; i < data.length; i++){
            if (map.get(data[i][1]) != null){
                Double k = map.get(data[i][1]) + data[i][2];
                map.put(data[i][1], k);
            }
            else {
                map.put(data[i][1], data[i][2]);
            }
        }
        return map;
    }


    public static double[][] aggregate(CopyOnWriteArrayList<double[][]> results){
        double[][] a = results.get(0);
        for (int i = 0; i < results.get(0).length; i++){
            a[i][1] += results.get(1)[i][1] + results.get(2)[i][1] + results.get(3)[i][1];
        }
        return a;
    }

    public static void main(String[] args){
        String fileNameOut = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\output";
        String fileNameIn = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\actions_influence.txt";

        double[][] array = new double[0][];
        try {
            array = FileWorker.read(fileNameIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        List<List<Double>> filteredList = Filter.globalFilter(array, Filter.influenceFilter(influenceCounter(array)));

        String fileNameForInfluenceAnalysis = fileNameOut + "Influence.txt";

        filteredList = normalize(filteredList);
//        FileWorker.writeInfluence(fileNameForInfluenceAnalysis, filteredList);

        ConcurrentSkipListMap<Double, SparseRealVector> list = toMapTransform(filteredList);

//        FileWorker.write(fileNameOut + ".txt", list);

        System.out.println(amountOfUsers(array));

        int n = 10;
        int amountOfCores = 4;

        double[][] accumulator = new double[1001][n];
        ConcurrentSkipListSet<Double> setOfKeys = new ConcurrentSkipListSet<>(list.keySet());
        CopyOnWriteArrayList<ConcurrentSkipListSet<Double>> setOfSets = new CopyOnWriteArrayList<>();

        for(int i = 0; i < n; i++){
            CopyOnWriteArrayList<double[][]> results = new CopyOnWriteArrayList<>();
            ArrayList<Parallel> concurrencyList = new ArrayList<>();
            for (int j = 0; j < amountOfCores; j++){
                concurrencyList.add(new Parallel(j, list, results, setOfKeys, setOfSets));
            }

            for(Parallel temp : concurrencyList){
                temp.start();
            }

            for(Parallel temp : concurrencyList){
                try {
                    temp.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            double[][] distanceRange = aggregate(results);


            for (int j = 0; j <= 1000; j++){
                accumulator[j][i] = distanceRange[j][1];
            }

            FileWorker.write(fileNameOut, i, distanceRange);
            int numberOfFile = i + 1;
            System.out.println("Записан " + numberOfFile + " файл");

//            System.out.println("Элементов в сете сетов:" + setOfSets.size());
//            Iterator iterator = setOfSets.get(i).iterator();
//
//            while(iterator.hasNext()){
//                System.out.println("Элемент сета сетов " + i + ": " + iterator.next());
//            }
//
//            System.out.println("Всего элементов в " + i + "ом сете сетов: " + setOfSets.get(i).size());
        }

//        Correlation.pearsons(n, fileNameOut, accumulator);
//        Correlation.spearman(n, fileNameOut, accumulator);

//        System.out.println("Корреляция сетов: ");
//        for (int i = 0; i < 4*n; i++){
//            for (int j = 0; j < 4*n; j++){
//                System.out.print(Randomizer.setsRandomChecker(setOfSets.get(i), setOfSets.get(j)) + " ");
//            }
//            System.out.println();
//        }
    }
}
