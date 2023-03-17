/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import controller.controller;
import controller.action;

/**
 *
 * @author eloytolosa
 */
public class ModelCalculator implements Runnable {

    private static int OUTPUT_DATA_SIZE = 100;         // this is the value to output the data
    private static int MAX_DATA_SIZE = 1000000;   // this is the value to use to calculate CM. To be correct, it must be high enough

    private Function function;
    private controller controller;
    private double[] vector;

    public ModelCalculator(Function function, controller controller) {
        this.function = function;
        this.controller = controller;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void generateRandomData() {
        vector = new double[MAX_DATA_SIZE];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = (int)(Math.random() * (vector.length + 1));
        }
    }

    // Calculate the mode using a Map in O(n)
    public double[][] N() throws InterruptedException {

        Long start = System.nanoTime();

        HashMap<Double, Integer> freq = new HashMap<Double, Integer>();

        // set the frequency table
        for (int i = 0; i < vector.length; i++) {
            Double number = vector[i];
            Integer frequency = freq.get(number);

            if (frequency == null) {
                freq.put(number, 1);
            } else {
                freq.put(number, frequency+1);
            }
        }

        // iterate over the frequency table to get highest value
        Integer maxCount = Integer.MIN_VALUE;
        Double maxInteger = 0.0;

        for (Entry<Double, Integer> set : freq.entrySet()) {
            Double n = set.getKey();
            Integer f = set.getValue();
            if (f > maxCount) {
                maxCount = f;
                maxInteger = n;
            }
        }
        
        Long elapsed = (System.nanoTime() - start)/1000000;
        System.out.println("Elapsed time: "+elapsed+" ms!");

        // calculate multiplicative constant in our function knowing that
        // time = CM * n
        // so ->  CM = time/n
        Double cm = calculateCM(elapsed, Function.N);
        System.out.println("CM is: " +cm+" !");

        // with the CM, we can now calculate the time it would take 
        // for every N possible value as this
        // time = CM * N

        double[] y = new double[OUTPUT_DATA_SIZE];
        double[] x = new double[OUTPUT_DATA_SIZE];
        for (int j = 0; j < x.length; j++) {
            y[j] = cm*j;
            x[j] = j;
        }

        
        return new double[][] { x, y };
    }

    private double Log2(int n) {
        return Math.log10(n)/Math.log10(2);
    } 

    public double[][] NLogN() throws InterruptedException {

        Long start = System.nanoTime();

        // first we sort the vector O(n*log(n))
        Arrays.sort(this.vector);

        // then we iterate through the vector to count how many times
        // each number has appeared. Whenever we find a new number with a
        // count higher than the previous max, we update it
        // this is O(n) IF AND ONLY IF the array is sorted

        // Thus making this algorithm O(n*log(n)) because
        // O(n) + O(n*log(n)) == O(n*log(n))

        double maxNumber, maxFrequency, previousNumber, currentNumber, currentFrequency;
        previousNumber = vector[0];
        maxNumber = -1;
        maxFrequency = -1;
        currentFrequency = 0;

        for (int i = 0; i < vector.length; i++) {
            currentNumber = vector[i];
            // if the current number is different from the previous
            // this means we have to check wether the count was bigger or not
            if (currentNumber != previousNumber) {
                
                // if the current frequency is higher than the previous max frequency
                // this means we have to update the new frequency
                if (currentFrequency > maxFrequency) {
                    maxNumber = previousNumber;
                    maxFrequency = currentFrequency;
                } 
                // otherwise, we just move to the next number
                // but we put currentFrequency to 1
                else {
                    currentFrequency = 1;
                }

            }
            // if the current number is the same, we only have to update the
            // current frequency
            else {
                currentFrequency++;
            }
            
            previousNumber = currentNumber;

        }
        Long elapsed = (System.nanoTime() - start)/1000000;
        System.out.println("Elapsed time: "+elapsed+" ms!");

        // calculate multiplicative constant in our function knowing that
        // time = CM * n*log(n)
        // so ->  CM = time/n*log(n)
        Double cm = calculateCM(elapsed, Function.N_LOG_N);
        System.out.println("CM is: " +cm+" !");

        // with the CM, we can now calculate the time it would take 
        // for every N possible value as this
        // time = CM * n*log(n)

        double[] y = new double[OUTPUT_DATA_SIZE];
        double[] x = new double[OUTPUT_DATA_SIZE];
        for (int j = 0; j < x.length; j++) {
            y[j] = cm*((j)*Log2(j));
            x[j] = j;
        }        
        
        return new double[][] { x, y };
    }

    public double[][] NSquared() throws InterruptedException {

        Long start = System.nanoTime();
        Double z = 0.0;
        for (int i = 0; i < vector.length; i++) {
            for (int j = 0; j < vector.length; j++) {
                z = vector[i]*vector[j];
            }
        }
        Long elapsed = (System.nanoTime() - start)/1000000;
        System.out.println("Elapsed time: "+elapsed+" ms!");

        // calculate multiplicative constant in our function knowing that
        // time = CM * n^2
        // so ->  CM = time/n^2
        Double cm = calculateCM(elapsed, Function.N_SQUARED);
        System.out.println("CM is: " +cm+" !");

        // with the CM, we can now calculate the time it would take 
        // for every N possible value as this
        // time = CM * N^2

        double[] y = new double[OUTPUT_DATA_SIZE];
        double[] x = new double[OUTPUT_DATA_SIZE];
        for (int j = 0; j < x.length; j++) {
            y[j] = cm*j*j;
            x[j] = j;
        }

        return new double[][] { x, y };
    }

    private Double calculateCM(Long elapsedTime, Function func) {
        Double denominator = 0.0;
        switch (func) {
            case N:
                denominator = (double) vector.length;
                break;
            case N_LOG_N:
                denominator = ((vector.length)*Log2(vector.length));
                break;
            case N_SQUARED:
                denominator = (double) vector.length*vector.length;
                break;
        }

        return elapsedTime/denominator;
    }

    @Override
    public void run() {
        try {
            double[][] outputData = null;
            generateRandomData();
            switch (function) {
                case N:
                    outputData = N();
                    break;
                case N_LOG_N:
                    outputData = NLogN();
                    break;
                case N_SQUARED:
                    outputData = NSquared();
                    break;
            }
            controller.comunicate(action.PAINT, outputData);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
