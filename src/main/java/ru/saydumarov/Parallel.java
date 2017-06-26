package ru.saydumarov;

import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.commons.math3.util.FastMath;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class Parallel extends Thread {

    private int setId;
    private ConcurrentSkipListMap<Double, SparseRealVector> outsideData;
    private CopyOnWriteArrayList<double[][]> results = new CopyOnWriteArrayList<>();
    private ConcurrentSkipListSet<Double> setOfKeys = new ConcurrentSkipListSet<>();
    private CopyOnWriteArrayList<ConcurrentSkipListSet<Double>> accumSet = new CopyOnWriteArrayList<>();

    public Parallel(int setId, ConcurrentSkipListMap<Double, SparseRealVector> outsideData, CopyOnWriteArrayList<double[][]> results, ConcurrentSkipListSet<Double> setOfKeys, CopyOnWriteArrayList<ConcurrentSkipListSet<Double>> accumSet){
        this.setId = setId;
        this.outsideData = outsideData;
        this.results = results;
        this.setOfKeys = setOfKeys;
        this.accumSet = accumSet;
    }

    private ConcurrentSkipListSet<Double> accum = new ConcurrentSkipListSet<>();


    public double[][] distance(ConcurrentSkipListMap<Double, SparseRealVector> mapData, int l, ConcurrentSkipListSet<Double> keySet){

        double min = 0;
        double max = 2;
        double range = max - min;
        double[][] a = new double[1001][2];

        for (int i = 0; i <= 1000; i++){
            a[i][0] = i * (range/1000) + min;
            a[i][1] = 0;
        }

        ConcurrentSkipListSet<Double> iSequence = Randomizer.random(keySet, 1000, 977827, l);
        Iterator iIterator = iSequence.iterator();
        accum.addAll(iSequence);
        long start = System.currentTimeMillis();
        while (iIterator.hasNext()){
            Object i = iIterator.next();

            ConcurrentSkipListSet<Double> jSequence = Randomizer.random(keySet, 1000, 977827, l);
            accum.addAll(jSequence);
            Iterator<Double> jIterator = jSequence.iterator();
            long startI = System.currentTimeMillis();
            while(jIterator.hasNext()){
                Object j = jIterator.next();
                long startJ = System.currentTimeMillis();
                SparseRealVector vector1 = mapData.get(i);
                SparseRealVector vector2 = mapData.get(j);
//                int iterator = (int)FastMath.floor((MapVector.getSumMinDistance(vector1, vector2) - min)/(range/1000));
                int iterator = (int)FastMath.floor((vector1.getL1Distance(vector2) - min)/(range/1000));
                a[iterator][1]++;
                long endJ = System.currentTimeMillis();

//                System.out.println("Время на " + j + " итерацию по j: " + " в треде номер " + l + " " + (endJ - startJ));
            }
            long endI = System.currentTimeMillis();
//            System.out.println("Время на " + i + "ую итерацию по i в треде номер " + l + " " + (endI - startI));
        }
        long end = System.currentTimeMillis();
        System.out.println("На расчёт 4 000 000 расстояний было затрачено: " + (end - start));

//        for (Double i = mapData.ceilingKey(new Double(l)); i < mapData.floorKey(mapData.lowerKey(mapData.lastKey()) - l); i = mapData.higherKey(mapData.higherKey(mapData.higherKey(mapData.higherKey(i))))){
//            long startI = System.currentTimeMillis();
//            long endJ = System.currentTimeMillis();
//            for (Double j = mapData.lastKey(); j > i; j = mapData.lowerKey(j)){
//                SparseRealVector vector1 = mapData.get(i);
//                SparseRealVector vector2 = mapData.get(j);
//                int iterator = (int)FastMath.floor((vector1.getL1Distance(vector2) - min)/(range/1000));
//                a[iterator][1]++;
//                if ((j % 1000) == 0){
//                    long startJ = endJ;
//                    endJ = System.currentTimeMillis();
//                    System.out.println("Время на 1000 " + j + " итераций по j: " + (endJ - startJ));
//                }
//            }
//            long endI = System.currentTimeMillis();
//            System.out.println("Время на " + i + "ую итерацию по i: " + (endI - startI));
//        }

        return a;
    }

    public void run(){
        results.add(distance(outsideData, setId, setOfKeys));
        accumSet.add(accum);

//        System.out.println("Размер results внутри треда" + results.size());
    }
}
