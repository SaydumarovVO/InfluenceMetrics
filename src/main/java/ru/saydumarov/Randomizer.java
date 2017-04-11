package ru.saydumarov;

import java.util.Random;
import java.util.concurrent.ConcurrentSkipListSet;

public class Randomizer {

    public static ConcurrentSkipListSet<Double> random(ConcurrentSkipListSet<Double> set, int numberOfValues, int maxValue, int seed){

        ConcurrentSkipListSet<Double> generated = new ConcurrentSkipListSet<>();
        Random r = new Random();
        while (generated.size() < numberOfValues) {
            Double a = Double.valueOf(r.nextInt(maxValue));
            if (set.contains(a)){
                generated.add(a);
//                set.remove(a);
//                System.out.println("Я нашёл подходящее случайное число " + a);
            }
//            else{
//                System.out.println("Я проверил " + a + ", но оно не подошло:(");
//            }
        }
        return generated;
    }

    public static double setsRandomChecker(ConcurrentSkipListSet<Double> set1, ConcurrentSkipListSet<Double> set2){
        ConcurrentSkipListSet<Double> set = new ConcurrentSkipListSet<>(set1);
        set.addAll(set2);
        return ((set2.size() - (set.size() - set1.size()))*1.0/set.size());
    }

}
