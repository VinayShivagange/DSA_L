package org.example.heap;

import java.util.*;

public class TopKElement {

    //[1,1,1,2,2,3], k = 2    output = [1,2]
    /*
    * { [1,3],[2,2],[3,1]} -> [ [3],[2],[1],,] - > 1,2
    * */
    public List<Integer> topKElementArray(int[] arr, int k){
        if(arr.length == 0){
            return null;
        }

        List<Integer> res = new ArrayList<>(k);
        List<Integer>[] bucket = new List[arr.length];
        Map<Integer,Integer> valMap =  new HashMap<>();
        for (int j : arr) {
            valMap.put(j, valMap.getOrDefault(j, 0) + 1);
        }

        valMap.forEach((key, val) -> {
            if(bucket[val]==null){
                bucket[val] = new ArrayList<>();
            }
            bucket[val].add(key);
        });

        for(int j= bucket.length-1 ; j >=0  & res.size() < k; j--){
            if(bucket[j]!=null){
                res.addAll(bucket[j]);
            }

        }




        return res;
    }

    public int [] topKElementPriorityQueue(int[] arr, int k){
        if(arr.length == 0){
            return null;
        }

        Map<Integer,Integer> valMap =  new HashMap<>();
        for (int j : arr) {
            valMap.put(j, valMap.getOrDefault(j, 0) + 1);
        }

        PriorityQueue<Integer> heap = new PriorityQueue<>((a,b)->{
            return valMap.get(a).compareTo(valMap.get((b)));
        });
        valMap.forEach((key, val) -> {
            heap.add(key);
            if(heap.size() > k){
                heap.poll();
            }
        });


        return  heap.stream().mapToInt(Integer::intValue).toArray();
    }


    public static void main(String[] args) {
        TopKElement k1 = new TopKElement();
       System.out.println( k1.topKElementPriorityQueue(new int[]{4,1,-1,2,-1,2,3}, 2));
    }
}
