package org.example.array;

import java.security.KeyPair;

public class MaximumArray {

    //[-2,1,3,4,-1,2,1,-5,4]

    public int getMaxSumArray(int[] arr ){

        int i=0,sum=0;
        while (i< arr.length){
            int temp = arr[i];
            int j=i+1;
            for(;j<arr.length;j++){
                temp += arr[j];
                if(temp < 0){
                    break;
                }
                if(temp > sum){
                    sum =temp;
                }
            }
            i=i++;
        }
        return sum;
    }

    //[-2,1,-3,4,-1,2,1,-5,4]
    public int maxSubarry(int[] arr){
        int sum=0, currentSum=0;
        for (int j : arr) {
            currentSum += j;
            if (currentSum < 0) {
                currentSum = 0;
            }
            if (currentSum > sum) {
                sum = currentSum;
            }
        }
        return sum;
    }
}



class Main{

    public static void main(String[] args) {
        System.out.println(new MaximumArray().getMaxSumArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
        System.out.println(new MaximumArray().maxSubarry(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
    }
}
