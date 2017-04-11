package ru.saydumarov;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

/**
 * Created by Валерий on 09.04.2017.
 */
public class Correlation {
    public static void pearsons (int n, String fileNameOut, double accumulator[][]){
        double[][] pearsons = new double[n][n];
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix pearsonsCorrelationMatrix = pearsonsCorrelation.computeCorrelationMatrix(accumulator);
        System.out.println("Корреляция Пирсона: ");
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                pearsons[i][j] = pearsonsCorrelationMatrix.getEntry(i, j);
                System.out.print(pearsonsCorrelationMatrix.getEntry(i, j) + " ");
            }
            System.out.println();
        }

        FileWorker.write(fileNameOut, 11, pearsons);
    }

    public static void spearman (int n, String fileNameOut, double accumulator[][]){
        double[][] spearman = new double[n][n];
        SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();
        RealMatrix spearmansCorrelationMatrix = spearmansCorrelation.computeCorrelationMatrix(accumulator);
        System.out.println("Корреляция Спирмена: ");
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                spearman[i][j] = spearmansCorrelationMatrix.getEntry(i, j);
                System.out.print(spearmansCorrelationMatrix.getEntry(i, j) + " ");
            }
            System.out.println();
        }

        FileWorker.write(fileNameOut, 12, spearman);
    }
}
