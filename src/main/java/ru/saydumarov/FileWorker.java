package ru.saydumarov;


import org.apache.commons.math3.linear.SparseRealVector;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class FileWorker {
    public static void write(String fileName, int numberOfFile, double[][] data) {
        File file = new File(fileName + numberOfFile + ".txt");
        int rows = data.length;
        int columns = data[0].length;


        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        out.print(data[i][j] + " ");
                    }
                    out.println();
                }
            }
            finally {
                out.close();
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(String fileName, ConcurrentSkipListMap<Double, SparseRealVector> data){
        File file = new File(fileName);

        try{
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for(SparseRealVector v : data.values()){
                    for (int i = 0; i < v.getDimension(); i++){
                        out.print(v.getEntry(i) + " ");
                    }
                    out.println();
                }
            }
            finally {
                out.close();
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeInfluence(String fileName, List<List<Double>> data){
        File file = new File(fileName);

        try{
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for(List<Double> k : data){
                    for (Double m : k){
                        out.print(m + " ");
                    }
                    out.println();
                }
            }
            finally {
                out.close();
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeInfluence(String fileName, ConcurrentSkipListMap<Double, Double> data){
        File file = new File(fileName);

        try{
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for(Double k : data.keySet()){
                    out.println(k + " " + data.get(k));
                }
            }
            finally {
                out.close();
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0; i < ret.length; i++){
            ret[i] = list.get(i);
        }
        return ret;
    }

    public static double[][] read(String fileName) throws FileNotFoundException {
        List<List<Integer>> matrix = new ArrayList<List<Integer>>();

        Scanner sсanner = new Scanner(new File(fileName));

        for (int i = 0; sсanner.hasNext(); i++)
        {
            Scanner s = new Scanner(sсanner.nextLine());
            List<Integer> row = new LinkedList<Integer>();
            while (s.hasNextInt())
            {
                row.add(s.nextInt());
            }
            matrix.add(row);
            s.close();
        }
        sсanner.close();

        int[][] result = new int[matrix.size()][];
        for (int i = 0; i < result.length; i++)
        {
            List row = matrix.get(i);
            result[i] = FileWorker.toIntArray(row);
        }

        double[][] res = new double[result.length][result[0].length];

        for (int i = 0; i < result.length; i++){
            for (int j = 0; j < result[0].length; j++){
                res[i][j] = (double)result[i][j];
            }
        }

        return res;
    }
}
