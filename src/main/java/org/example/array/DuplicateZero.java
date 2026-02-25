package org.example.array;

import java.util.Arrays;

public class DuplicateZero {

    public static void main(String[] args) {
        int [] arr = new int[]{0,12, 1, 0, 0};
        duplicateZeros(arr);
        System.out.println(Arrays.toString(arr));

    }

        //[1, 0, 0,  0, 2] - > [ 1,0,0,2,3,0,0] adding the new memory
        // l = 5 ,  [1,0,0,0,0] with out new  memory
        // find the zero
        public static void duplicateZeros(int[] arr) {

            int i=0,zeroCount=0;
            for(;i<arr.length-zeroCount;i++){
                if( i == arr.length-zeroCount-1 && arr[i] ==0){
                    continue;
                }
                if(arr[i] ==0 ){
                    zeroCount++;
                }
            }
            for(int j=0;j<arr.length-1;j++){
                if(arr[j] ==0 ){
                    shift(arr, j);
                    arr[j+1] = 0;
                    j++;
                }

            }

        }

        //[12, 0, 1, 3,0]
        public static void shift(int[] arr, int start){
            if(start < arr.length-1){
                int temp = arr[start];
                while(start < arr.length-1){
                    int ref= arr[start+1];
                    arr[start+1] = temp;
                    temp = ref;
                    start++;
                }
            }
        }

}
