package ru.saydumarov;

import org.apache.commons.math3.linear.*;

import java.io.FileNotFoundException;

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

//        int subsidiaryMax = 0;
//        int k = 0;
//        for (int i = 0; i < subsidiaryArray.length; i++){
//            if (subsidiaryArray[i] > subsidiaryMax){
//                subsidiaryMax = subsidiaryArray[i];
//                k = i;
//            }
//        }
//        System.out.println(subsidiaryMax + " " + k);

        for (int i = 0; i < data.length; i++){
            data[i][2] = data[i][2]/subsidiaryArray[(int) data[i][0]];
        }
        return data;
    }


    public static double[][] distance(double[][] data){
        SparseRealMatrix matrixData = null;
        int rows = data.length;
        matrixData.createMatrix(rows, rows);

        for (int i = 0; i < rows; i++){
            matrixData.setEntry((int)data[i][0], (int)data[i][1], data[i][2]);
        }

        double min = matrixData.getRowVector(0).getL1Distance(matrixData.getRowVector(1));
        double max = matrixData.getRowVector(0).getL1Distance(matrixData.getRowVector(1));

        for (int i = 0; i < rows - 1; i++){
            for (int j = rows - 1; j > i; j--){
                SparseRealVector vector1 = (SparseRealVector) matrixData.getRowVector(i);
                SparseRealVector vector2 = (SparseRealVector) matrixData.getRowVector(j);
                if (vector1.getL1Distance(vector2) >= max){
                    max = vector1.getL1Distance(vector2);
                }
                if (vector1.getL1Distance(vector2) <= min){
                    min = vector1.getL1Distance(vector2);
                }
            }
        }
        System.out.println("Максимум: " + max + "; Минимум: " + min);
        double range = max - min;
        double[][] a = new double[1000][2];

        for (int i = 0; i < 1000; i++){
            a[i][0] = i * (range/1000) + min;
            a[i][1] = 0;
        }

        int iterator = 0;
        for (int i = 0; i < rows; i++){
            for (int j = rows - 1; j > i; j--){
                SparseRealVector vector1 = (SparseRealVector) matrixData.getRowVector(i);
                SparseRealVector vector2 = (SparseRealVector) matrixData.getRowVector(j);
                iterator = (int)Math.floor((vector1.getL1Distance(vector2) - min)/(range/1000));
                a[iterator][1]++;
            }
        }

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

        array = normalize(array);
        double[][] distanceRange = distance(array);

        FileWorker.write(fileNameOut, distanceRange);
    }
}
