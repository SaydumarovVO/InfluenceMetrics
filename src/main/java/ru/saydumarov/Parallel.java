package ru.saydumarov;

import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentSkipListMap;

public class Parallel extends Thread {

    private int setId;
    private ConcurrentSkipListMap<Double, SparseRealVector> outsideData;
    private ArrayList<double[][]> results = new ArrayList<>();
    public HashSet<Double> setOfKeys = new HashSet<>();

    public Parallel(int setId, ConcurrentSkipListMap<Double, SparseRealVector> outsideData, ArrayList<double[][]> results, HashSet<Double> setOfKeys){
        this.setId = setId;
        this.outsideData = outsideData;
        this.results = results;
        this.setOfKeys = setOfKeys;
    }

    public double[][] distanceL1(ConcurrentSkipListMap<Double, SparseRealVector> mapData, int l, HashSet<Double> keySet){

        double min = 0;
        double max = 2;

        System.out.println("Максимум: " + max + "; Минимум: " + min);
        double range = max - min;
        double[][] a = new double[1000][2];

        for (int i = 0; i < 1000; i++){
            a[i][0] = i * (range/1000) + min;
            a[i][1] = 0;
        }

        for (Double i = mapData.ceilingKey(new Double(l)); i < mapData.floorKey(mapData.lowerKey(mapData.lastKey()) - l); i = mapData.higherKey(mapData.higherKey(mapData.higherKey(mapData.higherKey(i))))){
            long startI = System.currentTimeMillis();
            long endJ = System.currentTimeMillis();
            for (Double j = mapData.lastKey(); j > i; j = mapData.lowerKey(j)){
                SparseRealVector vector1 = mapData.get(i);
                SparseRealVector vector2 = mapData.get(j);
                int iterator = (int)FastMath.floor((vector1.getL1Distance(vector2) - min)/(range/1000));
                a[iterator][1]++;
                if ((j % 1000) == 0){
                    long startJ = endJ;
                    endJ = System.currentTimeMillis();
                    System.out.println("Время на 1000 " + j + " итераций по j: " + (endJ - startJ));
                }
            }
            long endI = System.currentTimeMillis();
            System.out.println("Время на " + i + "ую итерацию по i: " + (endI - startI));
        }

        return a;
    }

    public void run(){
        results.set(setId, distanceL1(outsideData, setId, setOfKeys));
    }
}
