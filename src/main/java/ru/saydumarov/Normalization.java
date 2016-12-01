package ru.saydumarov;

import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.SparseRealVector;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;


public class Normalization{


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


    public static int getAmountOfUsers(double[][] data){
        int max = 0;
        for (int i = 0; i < data.length; i++){
            if (data[i][0] > max){
                max = (int) data[i][0];
            }
        }
        System.out.println("Количество агентов в сети " + max);
        return max;
    }


    public static int getMaxValue(ConcurrentSkipListMap<Double, Integer> map){
        Integer max = 0;
        for (Map.Entry<Double, Integer> entry: map.entrySet()){
            if(entry.getValue() >= max){
                max = entry.getValue();
            }
        }
        System.out.println("Число влиятельных пользователей: " + max);
        return max.intValue();
    }

    public static HashMap<Double, Integer> getInfluentialUsers(double[][] data){
        HashMap<Double, Integer> hashMap = new HashMap<>();
        Integer v = 0;
        for (int i = 0; i < data.length; i++){
            if (!hashMap.containsKey(data[i][1])){
                hashMap.put(new Double(data[i][1]), v);
                if ((i % (Math.floor(data.length/100)) == 0) || (i == data.length - 1)){
                    System.out.println("Параметры метода getInfluentUsers: i = " + i + "; k = " + hashMap.get(data[i][1]) + "; v = " + v);
                }
                v++;
            }
        }
        return hashMap;
    }


    public static ConcurrentSkipListMap<Double, SparseRealVector> toMapTransform(double[][] data){
        ConcurrentSkipListMap<Double, SparseRealVector> mapData = new ConcurrentSkipListMap<Double, SparseRealVector>();
        HashMap<Double, Integer> mapping = Normalization.getInfluentialUsers(data);
        int columns = mapping.keySet().size();
        System.out.println("Число влиятельных пользователей: " + columns);
        SparseRealVector v = new OpenMapRealVector(columns);
        for (int i = 0; i < data.length; i++){
            if (mapData.get(new Double(data[i][0])) != null){
                v = mapData.get(new Double(data[i][0]));
                v.setEntry(mapping.get(new Double(data[i][1])), data[i][2]);
                mapData.put(new Double(data[i][0]), v);
            }
            else {
                v.setEntry(mapping.get(new Double(data[i][1])), data[i][2]);
                mapData.put(new Double(data[i][0]), v);
            }
            if ((i % (Math.floor(data.length/100)) == 0) || (i == data.length - 1)){
                System.out.println("Выполнена " + i + "ая итерация");
            }
        }
        return mapData;
    }


    public static double[][] aggregate(ArrayList<double[][]> results){
        double[][] a = results.get(0);
        for (int i = 0; i < 1000; i++){
            a[i][1] += results.get(1)[i][1] + results.get(2)[i][1] + results.get(3)[i][1];
        }
        return a;
    }

    public static void main(String[] args){
        String fileNameOut = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\output.txt";
        String fileNameIn = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\actions_influence.txt";

        Set<Double> aggregator = new HashSet<Double>();

        double[][] array = new double[0][];
        try {
            array = FileWorker.read(fileNameIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        array = normalize(array);

//        int test = getAmountOfUsers(array);

        ConcurrentSkipListMap<Double, SparseRealVector> list = toMapTransform(array);

        HashSet<Double> keySet = (HashSet<Double>) list.keySet();

        ArrayList<double[][]> results = new ArrayList<>();
        ArrayList<Parallel> concurrencyList = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            concurrencyList.add(new Parallel(i, list, results, keySet));
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

        FileWorker.write(fileNameOut, distanceRange);
    }
}
