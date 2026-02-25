package org.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JavaSorting {

    public static void main(String[] args) {
        List<String> strList = Arrays.asList("Vinay","Ranjan2","Bengal");
        List<Integer> intList = Arrays.asList(1,4,2,5,3);
       // Collections.sort(strList);
        Collections.sort(intList);
        intList.sort(Comparator.reverseOrder());
        System.out.printf("Natural sorting mechanism :%s%n", strList);
        System.out.printf("Natural sorting mechanism :%s%n", intList);
        strList.sort(String::compareTo);
        System.out.printf("Natural sorting mechanism :%s%n", strList);

        strList.sort(Comparator.reverseOrder());
        System.out.printf("Natural sorting mechanism :%s%n", strList);

        System.out.printf("Stream sorting mechanism :%s%n",strList.stream().sorted((a,b)-> a.length()< b.length() ? 0:-1).toList());
        System.out.printf("Stream Rever mechanism :%s%n",strList.stream().sorted(Comparator.reverseOrder()).toList());
        System.out.printf("Natural sorting mechanism :%s%n", strList);


    }
}
