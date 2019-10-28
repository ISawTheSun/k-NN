package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static int n; //liczba atrybutow
    static int k;

    public static void main(String[] args) {

        File irisData = new File("iris.data.normalized.txt");
        File irisDataTest = new File("iris.data.normalized.test.txt");
        k = enterK();
        List<Iris> data = getData(irisData);

        //System.out.println(doSingleTest(enterValues(), data, k));
        System.out.println(doMultipleTest(getData(irisDataTest), getData(irisData), k));

    }

    public static Iris enterValues(){
        List<Double> dataTest = new ArrayList<>();
        System.out.println("Enter attribute values: ");
        for (int i = 1; i < n+1; i++) {
            Scanner in = new Scanner(System.in);
            try {
                System.out.print(i+": ");
                dataTest.add(in.nextDouble());
            }catch (InputMismatchException e){
                System.err.println("enter double value");
                i--;
            }
        }

        return new Iris(dataTest);
    }

    public static int enterK(){
        System.out.print("Enter k: ");
        Scanner in = new Scanner(System.in);
        k = in.nextInt();
        return k;
    }

    public static String doSingleTest (Iris test, List<Iris> data, int k){
        String [] nearestTypes = new String[k];

        Map <Iris, Double> Irislengths = new HashMap<>();
        List<Double> lengths = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            double len = getLength(test.getAtrybuty(), data.get(i).getAtrybuty());
            Irislengths.put(data.get(i), len);
            lengths.add(len);
        }

        for (int i = 0; i < k; i++) {
            double min = Collections.min(lengths);
            for (Iris key: Irislengths.keySet()) {
                if(min == Irislengths.get(key)){
                    nearestTypes[i] = key.getAtrybutDecyzyjny();
                    break;
                }
            }
            lengths.remove(lengths.indexOf(min));
        }

        String result = "";
        int maxCount = 0;
        for (String type: nearestTypes){
            int counter = 0;
            for (int i = 0; i < nearestTypes.length; i++) {
                if(type.equals(nearestTypes[i]))
                    counter++;
            }
            if(maxCount < counter) {
                maxCount = counter;
                result = type;
            }
        }

        return result;
    }

    public static String doMultipleTest(List<Iris>data1, List<Iris> data2, int k){
        int matches = 0;
        for (Iris test : data1) {
            String result = doSingleTest(test, data2, k);
            System.out.println(result);
            if(result.equals(test.getAtrybutDecyzyjny()))
                matches++;
        }

        return "matches: " + matches + "/"+data1.size();
    }


    public static Double getLength(List<Double> A, List<Double> B){
        List<Double> tmp = new ArrayList<>();
        double sum = 0;
        for (int i = 0; i < A.size(); i++) {
            sum += Math.pow(A.get(i)-B.get(i), 2);
        }
        return Math.sqrt(sum);
    }



    public static List<Iris> getData(File file) {
        List<Iris> data = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int f;
            while ((f = fis.read()) != -1)
                sb.append((char) f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pattern p = Pattern.compile("([\\d.,]+)([\\w-]+)");
        Matcher m = p.matcher(sb);

        List<Double> atrybuty = null;
        while (m.find()) {
            atrybuty = new ArrayList<>();
            String [] tmp = m.group(1).split(",");
            for (int i = 0; i < tmp.length; i++) {
                atrybuty.add(Double.parseDouble(tmp[i]));
            }

            data.add(new Iris(atrybuty, m.group(2)));
        }
        n = atrybuty.size();

        return data;
    }
}
