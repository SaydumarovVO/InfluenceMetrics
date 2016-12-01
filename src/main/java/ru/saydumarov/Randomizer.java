package ru.saydumarov;

import java.util.HashSet;
import java.util.Random;

public class Randomizer {

    public static HashSet<Double> random(HashSet<Double> hashSet, int numberOfValues, int maxValue){

        HashSet<Double> generated = new HashSet<Double>();
        Random r = new Random();
        while (generated.size() < numberOfValues) {
            Double a = Double.valueOf(r.nextInt(maxValue));
            if (hashSet.contains(a)){
                generated.add(a);
                hashSet.remove(a);
            }
        }
        return generated;
    }



}
