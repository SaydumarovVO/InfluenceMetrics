package ru.saydumarov;


import jsat.classifiers.DataPoint;
import jsat.linear.IndexValue;
import jsat.linear.Vec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class FileWorker {
    public static void write(String fileName, int numberOfFile, double[][] data) {
        File file = new File(fileName + numberOfFile + ".txt");
        int columns = data[0].length;


        try {
            if(!file.exists()){
                file.createNewFile();
            }

            try (PrintWriter out = new PrintWriter(file.getAbsoluteFile())) {
                for (double[] aData : data) {
                    for (int j = 0; j < columns; j++) {
                        out.print(aData[j] + " ");
                    }
                    out.println();
                }
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void write(String fileName, int numberOfExperiment, String identifier, double[][] array){
        File file = new File(fileName + numberOfExperiment + "-" + identifier + ".txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for (int i = 0; i < array.length; i++){
                    for (int j = 0; j < array[0].length; j++){
                        out.print(array[i][j] + " ");
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

    public static void write(String fileName, int numberOfExperiment, String identifier, int[] array){
        File file = new File(fileName + numberOfExperiment + "-" + identifier + ".txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for (int i = 0; i < array.length; i++){
                    out.println(array[i]);
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

    public static void write(String fileName, List<List<String>> data){
        File file = new File(fileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for (List<String> row : data){
                    for (String string : row){
                        out.print(string + " ");
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

    public static void write(String fileName, ConcurrentSkipListMap<String, Integer> data){
        File file = new File(fileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for (String i : data.keySet()){
                    out.println(i + " " + data.get(i));
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

    public static void write(String fileName, int numberOfExperiment, int identifier, HashMap<Integer, Double> data){
        File file = new File(fileName + numberOfExperiment + identifier + ".txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for (Integer i : data.keySet()){
                    out.println(i + " " + data.get(i));
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

    public static void write(String fileName, int numberOfExperiment, HashMap<Integer, Integer> data){
        File file = new File(fileName + numberOfExperiment + ".txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for (Integer i : data.keySet()){
                    out.println(i + " " + data.get(i));
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

    public static void write(String fileName, int numberOfExperiment, int numberOfFile, List<DataPoint> data){
        File file = new File(fileName + numberOfExperiment + "-" + numberOfFile + ".txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for (DataPoint dataPoint : data){
                    Vec vec = dataPoint.getNumericalValues();
                    Iterator<IndexValue> iterator = vec.iterator();
                    while (iterator.hasNext()){
                        IndexValue indexValue = iterator.next();
                        out.print(indexValue.getIndex() + "," + indexValue.getValue() + "; ");
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

    public static void write(String fileName, int numberOfExperiment, int numberOfFile, Vec vec){
        File file = new File(fileName + numberOfExperiment + "-" + numberOfFile + ".txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                Iterator<IndexValue> iterator = vec.iterator();
                while (iterator.hasNext()){
                    IndexValue indexValue = iterator.next();
                    out.println(indexValue.getIndex() + "," + indexValue.getValue() + "; ");
                }
                out.println();
            }
            finally {
                out.close();
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void write(String fileName, ConcurrentSkipListMap<Integer, SparseRealVector> data){
//        File file = new File(fileName);
//
//        try{
//            if(!file.exists()){
//                file.createNewFile();
//            }
//
//            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
//
//            try {
//                for(SparseRealVector v : data.values()){
//                    for (int i = 0; i < v.getDimension(); i++){
//                        out.print(v.getEntry(i) + " ");
//                    }
//                    out.println();
//                }
//            }
//            finally {
//                out.close();
//            }
//        }
//        catch(IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

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

    public static List<List<String>> readId(String fileName) throws FileNotFoundException{
        List<List<String>> matrix = new ArrayList<List<String>>();

        Scanner sсanner = new Scanner(new File(fileName), "UTF-8");


        for (int i = 0; sсanner.hasNext(); i++)
        {
            Scanner s = new Scanner(sсanner.nextLine());
            List<String> row = new LinkedList<String>();
            while (s.hasNext())
            {
                row.add(s.next());
            }
            matrix.add(row);
            s.close();
        }
        sсanner.close();
        return matrix;
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
