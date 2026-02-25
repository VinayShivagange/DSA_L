package org.example.array;

import java.util.Arrays;

public class RotateArray {

    public static void main(String[] args) {
        // 34512
        System.out.println(Arrays.toString(rotateArray(new int[]{1, 2, 3, 4, 5,6,7}, 3)));
        System.out.println(Arrays.toString(rotateRight(new int[]{1, 2, 3, 4, 5,6,7}, 3)));
    }

    // via reverse method
    private static int[] rotateArray(int[] ints, int key ) {

       for(int i=0; i<ints.length/2 ; i++){
           int temp = ints[i];
           ints[i] = ints[ints.length-1-i];
           ints[ints.length-1-i]=temp;
       }

        for(int i=0; i<key/2 ; i++){
            int temp = ints[i];
            ints[i] = ints[key-1-i];
            ints[key-1-i]=temp;
        }

        for(int i=0; i<(ints.length-key)/2 ; i++){
            int temp = ints[i+key];
            ints[i+key] = ints[ints.length-1-i];
            ints[ints.length-1-i]=temp;
        }


        return ints;
    }


    public static int[] rotateRight(int[] ints, int key) {
        if (ints == null || ints.length <= 1) return ints;

        // Ensure key is within array bounds
        key = key % ints.length;
        if (key == 0) return ints;

        // 1. Reverse the entire array
        reverse(ints, 0, ints.length - 1);

        // 2. Reverse the first 'key' elements
        reverse(ints, 0, key - 1);

        // 3. Reverse the remaining elements
        reverse(ints, key, ints.length - 1);

        return ints;
    }

    // Helper method to keep the code DRY (Don't Repeat Yourself)
    private static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }




}
