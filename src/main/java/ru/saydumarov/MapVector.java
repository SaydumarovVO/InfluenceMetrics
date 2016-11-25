package ru.saydumarov;

import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.commons.math3.util.FastMath;

import java.util.TreeMap;

public class MapVector extends TreeMap<Double, Double> {
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
