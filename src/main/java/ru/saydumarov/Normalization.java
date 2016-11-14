package ru.saydumarov;

import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.commons.math3.util.FastMath;

import java.io.FileNotFoundException;
import java.util.TreeMap;

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

    public static double[][] distance(double[][] data){
        TreeMap<Double, SparseRealVector> mapData = new TreeMap<Double, SparseRealVector>();
//        TreeMap<Double, SparseDoubleMatrix1D> mapData = new TreeMap<Double, SparseDoubleMatrix1D>();
        int rows = getAmountOfUsers(data);
//        SparseDoubleMatrix1D v = new SparseDoubleMatrix1D(rows);

        SparseRealVector v = new OpenMapRealVector(rows);
        for (int i = 0; i < rows; i++){
                if (mapData.get(new Double(data[i][0])) != null){
                    v = mapData.get(new Double(data[i][0]));
                    v.setEntry((int)data[i][1], data[i][2]);
                    mapData.put(new Double(data[i][0]), v);
                }
                else {
                    v.setEntry((int)data[i][1], data[i][2]);
                    mapData.put(new Double(data[i][0]), v);
                }
            if ((i % (Math.floor(data.length/100)) == 0) || (i == data.length - 1)){
                System.out.println("Выполнена " + i + "ая итерация");
            }
        }


        double min = mapData.get(new Double(1)).getL1Distance(mapData.get(new Double(2)));
        double max = mapData.get(new Double(1)).getL1Distance(mapData.get(new Double(2)));;

        for (Double i = mapData.firstKey(); i < mapData.lowerKey(mapData.lastKey()); i = mapData.higherKey(i)){
            long startI = System.currentTimeMillis();
            for (Double j = mapData.lastKey(); j > i; j = mapData.lowerKey(j)){
                long startJ = System.currentTimeMillis();
                SparseRealVector vector1 = mapData.get(i);
                SparseRealVector vector2 = mapData.get(j);
                max = FastMath.max(vector1.getL1Distance(vector2), max);
                min = FastMath.min(vector1.getL1Distance(vector2), min);
//                min = Math.min(vector1.getL1Distance(vector2), min);
                long endJ = System.currentTimeMillis();
                System.out.println("Время на " + j + "ую итерацию по j: " + (endJ - startJ));
            }
            long endI = System.currentTimeMillis();
            System.out.println("Время на " + i + "ую итерацию по i: " + (endI - startI));
        }

        System.out.println("Максимум: " + max + "; Минимум: " + min);
        double range = max - min;
        double[][] a = new double[1000][2];

        for (int i = 0; i < 1000; i++){
            a[i][0] = i * (range/1000) + min;
            a[i][1] = 0;
        }

        for (Double i = mapData.firstKey(); i < mapData.lowerKey(mapData.lastKey()); i = mapData.higherKey(i)){
            long start = System.currentTimeMillis();
            for (Double j = mapData.lastKey(); j > i; j = mapData.lowerKey(j)){
                SparseRealVector vector1 = mapData.get(i);
                SparseRealVector vector2 = mapData.get(j);
                int iterator = (int)FastMath.floor((vector1.getL1Distance(vector2) - min)/(range/1000));
                a[iterator][1]++;
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        }

        return a;
    }

//    public static double[][] anotherDistance(double[][] data){
//        int rows = getAmountOfUsers(data);
//        DoubleMatrix2D doubleMatrix2D = new DenseDoubleMatrix2D(data.length, rows);
//        SparseDoubleMatrix1D v = new SparseDoubleMatrix1D(rows);
//
//    }

    public static void main(String[] args){
        String fileNameOut = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\output.txt";
        String fileNameIn = "D:\\Progay\\IDEA Workspace\\Influence Metrics\\actions_influence.txt";
        double[][] array = new double[0][];
        try {
            array = FileWorker.read(fileNameIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        array = normalize(array);
        double[][] distanceRange = distance(array);

        FileWorker.write(fileNameOut, distanceRange);
    }
}
