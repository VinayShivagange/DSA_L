package org.example.array;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class UniqueNumberOfOccurance {


    public static void main(String[] args) {
      System.out.println(isOccuranceValid(new int[]{-1,-1,-1,-3,-1,-1}));
     /// System.out.println(isOccuranceValid(new int[]{1,2}));
     // System.out.println(isOccuranceValid(new int[]{-3,0,1,-3,1,1,1,-3,10,0}));
    }


    public static boolean isOccuranceValid(int[] arr ){
        AtomicBoolean res = new AtomicBoolean(true);
        Map<Integer,Integer> mapCount = new HashMap<>();
        int maxVal =0;

        //[1,2,2,1,1,3]
        //{1->3, 2->2, 3->1}
        for (int j : arr) {
            int count=  mapCount.getOrDefault(j, 0)+1;
            if(count > maxVal){
                maxVal = count;
            }
            mapCount.put(j,count);
        }

        int[] unique = new int[maxVal];
        int finalMaxVal = maxVal;
        mapCount.values().forEach(val->{
            if(unique[val % finalMaxVal] !=0){
                res.set(false);
                return;
            }
            unique[val % finalMaxVal] = 1;
        });

       /* Set<Integer> uniqueVal = new HashSet<>(mapCount.size());
        mapCount.values().forEach( val->{
            if(uniqueVal.contains(val)){
                res.set(false);
                return;
            }
            uniqueVal.add(val);
        }
        );*/


        return res.get();
    }
}
