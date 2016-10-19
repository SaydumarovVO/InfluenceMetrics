package ru.saydumarov;


import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class FileWorker {
    public static void write(String fileName, double[][] data) {
        File file = new File(fileName);
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
            } finally {
                out.close();
            }
        } catch(IOException e) {
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
