package ru.saydumarov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;


public class Filter {
    public static ConcurrentSkipListMap<Double, Double> influenceFilter(ConcurrentSkipListMap<Double, Double> map){
        ConcurrentSkipListMap<Double, Double> temp = new ConcurrentSkipListMap<Double, Double>();
        for (Double k : map.keySet()){
//            Здесь стоит параметр 91, с помощью которого выделяется 5% наиболее влиятельных пользователей
            if (map.get(k) >= 91){
                temp.put(k, map.get(k));
            }
        }
        return temp;
    }


    public static List<List<Double>> globalFilter(double[][] rowData, ConcurrentSkipListMap<Double, Double> mostPowerfulCruelUsersMap){
        List<List<Double>> list = new ArrayList<>();
        for (int i = 0; i < rowData.length; i++){
            for (Double k : mostPowerfulCruelUsersMap.keySet()){
                if (k.equals(rowData[i][1])){
                    List<Double> row = new ArrayList<>();
                    for (int j = 0; j < rowData[i].length; j++){
                        row.add(rowData[i][j]);
//                        System.out.print(row.get(j) + " ");
                    }
//                    System.out.println();
                    list.add(row);
                }
            }
        }
        return list;
    }
}
