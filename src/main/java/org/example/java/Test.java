package org.example.java;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Test {

    // Random rnd = new Random();
    //rnd.nextInt(26);
    public static void main(String[] args) {
        System.out.println(generateRandom(25));
    }

    // [0,0,2,1,0] , 2 ,
    //
    public static int[] generateRandom(int size){
        int [] res= new int[size];
        int count =1;
        Random rnd = new Random();
        for(int i=0; i< size;){
            int index = rnd.nextInt(size);
            if(res[index]==0){
                res[index] =count++;
                i++;
            }
        }
        for(int i : res){
            System.out.println(i);
        }
        return res;
    }
   static Map<String, Map<String,Integer>> warehouse = new HashMap<>();

    public static Map<String, Map<String,Integer>> getAllocation(Map<String,Integer> order){
        Map<String, Map<String,Integer>> res = new HashMap<>();

        warehouse.forEach((w,pro)->{
            pro.forEach((p, s)->{
               Integer count = order.get(p);
               if( count !=null && count < s  && res.get(w).get(p)==null){
                   Map<String,Integer> alloc =  res.getOrDefault(w, new HashMap<>());
                   alloc.put(p , count);
                   res.put(w, alloc);
               }

            });
        });

        //check all allocation for the order
         boolean checkOrder =res.values().containsAll(order.values());

        return checkOrder ? res: null;
    }


}