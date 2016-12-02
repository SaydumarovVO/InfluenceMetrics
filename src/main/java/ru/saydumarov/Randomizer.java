package ru.saydumarov;

import java.util.HashSet;
import java.util.NavigableSet;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListSet;

public class Randomizer {

    public static ConcurrentSkipListSet<Double> random(NavigableSet<Double> set, int numberOfValues, int maxValue, int seed){

        ConcurrentSkipListSet<Double> generated = new ConcurrentSkipListSet<>();
        Random r = new Random(System.currentTimeMillis() + seed);
        while (generated.size() < numberOfValues) {
            Double a = Double.valueOf(r.nextInt(maxValue));
            if (set.contains(a)){
                generated.add(a);
                set.remove(a);
//                System.out.println("Я нашёл подходящее случайное число " + a);
            }
//            else{
//                System.out.println("Я проверил " + a + ", но оно не подошло:(");
//            }
        }
        return generated;
    }



}
