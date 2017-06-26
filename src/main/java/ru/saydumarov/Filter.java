package ru.saydumarov;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;


public class Filter {
//    Из созданной с помощью метода InfluenceClustering.influenceCounter "карты" вида
//    "Номер влиятельного пользователя:оказанное им суммарное влияние" производится отсеивание наименее влиятельных
    static ConcurrentSkipListMap<Double, Double> influenceFilter(ConcurrentSkipListMap<Double, Double> map, int influence){
        ConcurrentSkipListMap<Double, Double> temp = new ConcurrentSkipListMap<>();
        for (Double k : map.keySet()){
            if (map.get(k) >= influence){
                temp.put(k, map.get(k));
            }
        }
        return temp;
    }


//  Из журнала действий удаляются те действия, которые сынициированы влиятельными пользователями с наименьшей влиятельностью.
//  Производится запись журнала не в виде двумерного массива, а в виде списка списков
    static List<List<Double>> globalFilter(double[][] rawData, ConcurrentSkipListMap<Double, Double> mostPowerfulCruelUsersMap){
        List<List<Double>> list = new ArrayList<>();
        for (double[] aRawData : rawData) {
            for (Double k : mostPowerfulCruelUsersMap.keySet()) {
                if (k.equals(aRawData[1])) {
                    List<Double> row = new ArrayList<>();
                    for (double anARawData : aRawData) {
                        row.add(anARawData);
                    }
                    list.add(row);
                }
            }
        }
        return list;
    }

    static List<List<String>> usersFilter(ConcurrentSkipListMap<Double, Double> influentalUsers, List<List<String>> id){
        List<List<String>> result = new ArrayList<>();
        for (List<String> row : id){
            if(influentalUsers.keySet().contains(Double.parseDouble(row.get(0)))){
                List<String> temp = new ArrayList<>();
                temp.addAll(row);
                temp.add(String.valueOf(influentalUsers.get(Double.parseDouble(row.get(0)))));
                result.add(temp);
            }
        }
        return result;
    }
}
