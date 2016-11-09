package ru.saydumarov;

import java.util.TreeMap;

public class MapVector extends TreeMap<Double, Double> {

    public static Double iterator (Double i, MapVector dataVector, MapVector mapVector){
        if ((dataVector.higherKey(i) != null) & (mapVector.higherKey(i) != null)){
            return Math.min(dataVector.higherKey(i), mapVector.higherKey(i));
        }
        if ((dataVector.higherKey(i) == null) & (mapVector.higherKey(i) != null)){
            return mapVector.higherKey(i);
        }
        if ((dataVector.higherKey(i) != null) & (mapVector.higherKey(i) == null)){
            return dataVector.higherKey(i);
        }
        if ((dataVector.higherKey(i) == null) & (mapVector.higherKey(i) == null)){
            return i + 1;
        }
        else {
            return i;
        }
    }

    public static double getL1Distance(MapVector dataVector, MapVector mapVector){
        double norma = 0;
        Double i = Math.min(dataVector.firstKey(), mapVector.firstKey());

        while (i <= Math.max(mapVector.lastKey(), dataVector.lastKey())){
            if ((dataVector.get(i) != null) & (mapVector.get(i) != null)){
                norma += Math.abs(dataVector.get(i) - mapVector.get(i));
            }
            if ((dataVector.get(i) != null) & (mapVector.get(i) == null)){
                norma += Math.abs(dataVector.get(i));
            }
            if ((dataVector.get(i) == null) & (mapVector.get(i) != null)){
                norma += Math.abs(mapVector.get(i));
            }
            i = iterator(i, dataVector, mapVector);
        }
        return norma;
    }

    public double getL2Distance(MapVector dataVector, MapVector mapVector){
        double norma = 0;
        Double i = Math.min(dataVector.firstKey(), mapVector.firstKey());

        while (i <= Math.max(mapVector.lastKey(), dataVector.lastKey())){
            if ((dataVector.get(i) != null) & (mapVector.get(i) != null)){
                norma += Math.pow(dataVector.get(i) - mapVector.get(i), 2);
            }
            if ((dataVector.get(i) != null) & (mapVector.get(i) == null)){
                norma += Math.pow(dataVector.get(i), 2);
            }
            if ((dataVector.get(i) == null) & (mapVector.get(i) != null)){
                norma += Math.pow(mapVector.get(i), 2);
            }
            i = iterator(i, dataVector, mapVector);
        }
        return Math.sqrt(norma);
    }
}
