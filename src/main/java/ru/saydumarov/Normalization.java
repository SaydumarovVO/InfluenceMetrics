package ru.saydumarov;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.FileNotFoundException;
import java.lang.Object.*;

public abstract class Normalization extends Object{
    public static double[][] rowNormalize (double[][] data){
        int rows = data.length;
        int columns = data[0].length;

        RealMatrix matrixData = MatrixUtils.createRealMatrix(data);
        double[] a = new double[rows];
        for (int i = 0; i < rows; i++){
            RealVector vector = matrixData.getRowVector(i);
            a[i] = vector.getL1Norm();
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = data[i][j]/a[i];
            }
        }

        return data;
    }

    public static double [][] distance (double[][] data){
        int rows = data.length;

        RealMatrix matrixData = MatrixUtils.createRealMatrix(data);
        double[][] a = new double[rows][rows];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < rows; j++){
                RealVector vector1 = matrixData.getRowVector(i);
                RealVector vector2 = matrixData.getRowVector(j);
                a[i][j] = vector1.getL1Distance(vector2);
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

        array = rowNormalize(array);
        double[][] metrics = distance(array);

//        for (int i = 0; i < array.length; i++){
//            for (int j = 0; j < array[0].length; j++){
//                System.out.print(array[i][j] + " ");
//            }
//            System.out.println();
//        }

        for (int i = 0; i < metrics.length; i++){
            for (int j = 0; j <= metrics[0].length; j++){
                System.out.print(metrics[i][j] + " ");
            }
            System.out.println();
        }

        FileWorker.write(fileNameOut, metrics);
    }
}
