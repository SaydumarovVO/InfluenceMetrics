package ru.saydumarov;

import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.commons.math3.util.FastMath;

import java.util.TreeMap;

public class MapVector extends TreeMap<Double, Double> {

//    public static Double iterator (Double i, MapVector dataVector, MapVector mapVector){
//        if ((dataVector.higherKey(i) != null) & (mapVector.higherKey(i) != null)){
//            return Math.min(dataVector.higherKey(i), mapVector.higherKey(i));
//        }
//        if ((dataVector.higherKey(i) == null) & (mapVector.higherKey(i) != null)){
//            return mapVector.higherKey(i);
//        }
//        if ((dataVector.higherKey(i) != null) & (mapVector.higherKey(i) == null)){
//            return dataVector.higherKey(i);
//        }
//        if ((dataVector.higherKey(i) == null) & (mapVector.higherKey(i) == null)){
//            return i + 1;
//        }
//        else {
//            return i;
//        }
//    }
//
//    public static double getL1Distance(MapVector dataVector, MapVector mapVector){
//        double norma = 0;
//        Double i = Math.min(dataVector.firstKey(), mapVector.firstKey());
//
//        while (i <= Math.max(mapVector.lastKey(), dataVector.lastKey())){
//            if ((dataVector.get(i) != null) & (mapVector.get(i) != null)){
//                norma += Math.abs(dataVector.get(i) - mapVector.get(i));
//            }
//            if ((dataVector.get(i) != null) & (mapVector.get(i) == null)){
//                norma += Math.abs(dataVector.get(i));
//            }
//            if ((dataVector.get(i) == null) & (mapVector.get(i) != null)){
//                norma += Math.abs(mapVector.get(i));
//            }
//            i = iterator(i, dataVector, mapVector);
//        }
//        return norma;
//    }

    public static double getL1Distance(SparseRealVector dataVector, SparseRealVector mapVector){
        double norma = 0;
        for (int i = 0; i < dataVector.getDimension(); i++){
            norma = FastMath.abs(dataVector.getEntry(i) - mapVector.getEntry(i));
        }
        return norma;
    }

    public static double getL2Distance(SparseDoubleMatrix1D dataVector, SparseDoubleMatrix1D mapVector){
        double norma = dataVector.zDotProduct(dataVector)*dataVector.zDotProduct(dataVector) + mapVector.zDotProduct(mapVector)*mapVector.zDotProduct(mapVector) - mapVector.zDotProduct(dataVector)*mapVector.zDotProduct(dataVector);
        return FastMath.sqrt(norma);
    }
}
