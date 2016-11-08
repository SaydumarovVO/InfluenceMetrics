package ru.saydumarov;

import java.util.TreeMap;

public class MapVector extends TreeMap<Double, Double> {

    MapVector mapVector = new MapVector();

    public double getL1Distance(MapVector dataVector){
        double norma = 0;
        for (Double i = new Double(Math.min(dataVector.firstKey(), mapVector.firstKey())); i <= new Double(Math.max(dataVector.lastKey(), mapVector.lastKey())); i = new Double(Math.min(dataVector.ceilingKey(i), mapVector.ceilingKey(i)))){
            if ((dataVector.get(i) != null) & (mapVector.get(i) != null)){
                norma += Math.abs(dataVector.get(i) - mapVector.get(i));
            }
            if ((dataVector.get(i) != null) & (mapVector.get(i) == null)){
                norma += Math.abs(dataVector.get(i));
            }
            if ((dataVector.get(i) == null) & (mapVector.get(i) != null)){
                norma += Math.abs(mapVector.get(i));
            }
        }
        return norma;
    }

    public double getL2Distance(MapVector dataVector){
        double norma = 0;
        for (Double i = Math.min(dataVector.firstKey(), mapVector.firstKey()); i <= Math.max(dataVector.lastKey(), mapVector.lastKey()); i = Math.min(dataVector.ceilingKey(i), mapVector.ceilingKey(i))){
            if ((dataVector.get(i) != null) & (mapVector.get(i) != null)){
                norma += Math.pow(dataVector.get(i) - mapVector.get(i), 2);
            }
            if ((dataVector.get(i) != null) & (mapVector.get(i) == null)){
                norma += Math.pow(dataVector.get(i), 2);
            }
            if ((dataVector.get(i) == null) & (mapVector.get(i) != null)){
                norma += Math.pow(mapVector.get(i), 2);
            }
        }
        return Math.sqrt(norma);
    }
}
