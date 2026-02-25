package org.example.array;

import com.sun.jdi.Value;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    static class Pair<K,V>{
        K key;
        V value;
    }
    public static void main(String[] args) {
System.out.println(getSumPairIndex(new int[]{2,3,4},6));
System.out.println(getSumPairIndexTwoPointers(new int[]{2,3,4,8,10},13));


    }


    public static int [] getSumPairIndex(int[] arr, int sum){
        int[] res = new int[2];
        Map<Integer, Integer> map = new HashMap<>();

       for(int i=0;i<arr.length;i++){
            if(map.containsKey(sum - arr[i])){
                res[0] = map.get(sum - arr[i]);
                res[1] = i;
                break;
            }
            map.put(arr[i],i);
        }
        return res;
    }


    public static int [] getSumPairIndexTwoPointers(int[] arr, int sum){
        int[] res = new int[2];
        int j=0;

        for(int i=arr.length-1;i>=0 & j< arr.length;){
            int temp = arr[i]+arr[j];

            if(temp == sum){
                res[0]=i;
                res[1]=j;
                break;
            }

            if((temp/2)  > arr[i]){
                i++;
            }else {
                j++;
            }

        }
        return res;
    }





}
