package ru.saydumarov;

import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.commons.math3.util.FastMath;

public class MapVector{


    public static double getSumMinDistance(SparseRealVector v1, SparseRealVector v2){
        double d = 0;
        for(int i = 0; i < v1.getDimension(); i++){
            d += FastMath.min(v1.getEntry(i), v2.getEntry(i));
        }
        return d;
    }
}
