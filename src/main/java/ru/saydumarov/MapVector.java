package ru.saydumarov;

import jsat.classifiers.DataPoint;
import jsat.linear.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

public class MapVector{

    private static ConcurrentSkipListMap<String, Integer> reMapLabels(ConcurrentSkipListMap<Integer, String> labels){
        ConcurrentSkipListMap<String, Integer> temp = new ConcurrentSkipListMap<>();
        HashSet<String> set = new HashSet<>(labels.values());
        int number = 0;
        for(String string : set){
            temp.put(string, number);
            number++;
        }
        FileWorker.write("D:\\Progay\\IDEA Workspace\\Influence Metrics\\map.txt", temp);
        return temp;
    }

    private static int indexOfMax (Vec vec){
        int index = 0;
        double max = 0;
        for (IndexValue indexValue : vec) {
            if (indexValue.getValue() > max) {
                max = indexValue.getValue();
                index = indexValue.getIndex();
            }
        }
        return index;
    }

    public static Matrix matrix(ConcurrentSkipListMap<Integer, String> labels){
        ConcurrentSkipListMap<String, Integer> map = reMapLabels(labels);
        SparseMatrix sparseMatrix = new SparseMatrix(map.size(), labels.size());
        for (String string : map.keySet()){
            for (Integer integer : labels.keySet()){
                if (labels.get(integer).equals(string)){
                    sparseMatrix.set(map.get(string), integer, 1);
                }
            }
        }
        return sparseMatrix;
    }

    static Vec vectorOfCategories(Matrix matrix, List<DataPoint> data){
        DenseVector vector = new DenseVector(matrix.rows());
        for (DataPoint dp : data){
            matrix.multiply(dp.getNumericalValues(), 1, vector);
        }
        return vector.divide(vector.pNorm(1));
    }

    public static HashMap<Integer, Integer> distribution (List<Vec> vecs){
        HashMap<Integer, Integer> map = new HashMap<>();
        for(Vec vec : vecs){
            if (map.get(indexOfMax(vec)) != null){
                int k = map.get(indexOfMax(vec));
                map.put(indexOfMax(vec), k + 1);
            }
            else {
                map.put(indexOfMax(vec), 1);
            }
        }
        return map;
    }
}
