package ru.saydumarov;

import org.apache.commons.math3.linear.*;

import java.io.FileNotFoundException;
import java.util.*;

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
        return max;
    }

    public static boolean checkOrder(double[][] data){
        double[] a = new double[data.length];
        for (int i = 0; i < data.length; i++){
            a[i] = data[i][0];
        }
        Arrays.sort(a);
        boolean k = true;
        for (int i = 1; i < a.length; i++){
            if ((a[i] != a[i-1]) & (a[i] != a[i-1] + 1)){
                k = false;
            }
        }
        return k;
    }

    public static double[][] distance(double[][] data){
        TreeMap<Double, SparseRealVector> mapData = new TreeMap<Double, SparseRealVector>();
        int rows = getAmountOfUsers(data);
        SparseRealVector v = new OpenMapRealVector(rows);
        for (int i = 0; i < data.length; i++){
            if (mapData.get(new Double(data[i][0])) != null){
                v = mapData.get(new Double(data[i][0]));
                v.setEntry((int)data[i][1], data[i][2]);
                mapData.put(new Double(data[i][0]), v);
            }
            else {
                v.setEntry((int)data[i][1], data[i][2]);
                mapData.put(new Double(data[i][0]), v);
            }
        }

        double min = mapData.get(new Double(1)).getL1Distance(mapData.get(new Double(2)));
        double max = mapData.get(new Double(1)).getL1Distance(mapData.get(new Double(2)));

        for (Double i = mapData.firstKey(); i < mapData.lowerKey(mapData.lastKey()); i = mapData.ceilingKey(i)){
            for (Double j = mapData.lastKey(); j > i; j = mapData.lowerKey(j)){
                SparseRealVector vector1 = mapData.get(i);
                SparseRealVector vector2 = mapData.get(j);
                if (vector1.getL1Distance(vector2) >= max) {
                    max = vector1.getL1Distance(vector2);
                }
                if (vector1.getL1Distance(vector2) <= min) {
                    min = vector1.getL1Distance(vector2);
                }
            }
        }


//        for (int i = 1; i < rows - 1; i++) {
//            for (int j = rows - 1; j > i; j--) {
//                SparseRealVector vector1 = mapData.get(new Double(i));
//                SparseRealVector vector2 = mapData.get(new Double(j));
//                if (vector1.getL1Distance(vector2) >= max) {
//                    max = vector1.getL1Distance(vector2);
//                }
//                if (vector1.getL1Distance(vector2) <= min) {
//                    min = vector1.getL1Distance(vector2);
//                }
//            }
//        }

        System.out.println("Максимум: " + max + "; Минимум: " + min);
        double range = max - min;
        double[][] a = new double[1000][2];

        for (int i = 0; i < 1000; i++){
            a[i][0] = i * (range/1000) + min;
            a[i][1] = 0;
        }

        int iterator = 0;
        for (Double i = mapData.firstKey(); i < mapData.lowerKey(mapData.lastKey()); i = mapData.ceilingKey(i)){
            for (Double j = mapData.lastKey(); j > i; j = mapData.lowerKey(j)){
                SparseRealVector vector1 = mapData.get(i);
                SparseRealVector vector2 = mapData.get(j);
                iterator = (int)Math.floor((vector1.getL1Distance(vector2) - min)/(range/1000));
                a[iterator][1]++;
            }
        }
//        for (int i = 1; i < rows; i++){
//            for (int j = rows - 1; j > i; j--){
//                SparseRealVector vector1 = mapData.get(new Double(i));
//                SparseRealVector vector2 = mapData.get(new Double(j));
//                iter = (int)Math.floor((vector1.getL1Distance(vector2) - min)/(range/1000));
//                a[iter][1]++;
//            }
//        }

        return a;
    }

    public static void main(String[] args){
        String fileNameOut = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\output.txt";
        String fileNameIn = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\actions_influence.txt";
        double[][] array = new double[0][];
        try {
            array = FileWorker.read(fileNameIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        boolean k = checkOrder(array);
//        System.out.print(k);

        array = normalize(array);
        double[][] distanceRange = distance(array);

        FileWorker.write(fileNameOut, distanceRange);
    }
}
