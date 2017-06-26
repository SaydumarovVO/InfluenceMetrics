package ru.saydumarov;

import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.linear.IndexValue;
import jsat.linear.SparseVector;
import jsat.linear.Vec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;


public class Clustering {


    static SimpleDataSet createDataSet(ConcurrentSkipListMap<Integer, SparseVector> data){
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        for (Integer k : data.keySet()){
            dataPoints.add(new DataPoint(data.get(k)));
        }
        return new SimpleDataSet(dataPoints);
    }

//    public static HashMap<Integer, Double> trueCorrelation(List<DataPoint> dataPoints){
//        HashMap<Integer, Double> map = new HashMap<>();
//        for (DataPoint dp : dataPoints){
//            Vec vec = dp.getNumericalValues();
//            for (IndexValue iv : vec) {
//                if (map.get(iv.getIndex()) != null) {
//                    double temp = map.get(iv.getIndex());
//                    map.put(iv.getIndex(), temp + iv.getValue());
//                } else {
//                    map.put(iv.getIndex(), iv.getValue());
//                }
//            }
//        }
//        return map;
//    }
}
