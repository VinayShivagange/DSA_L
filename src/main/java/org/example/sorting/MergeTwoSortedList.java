package org.example.sorting;

public class MergeTwoSortedList {
    // [1,2,3] [4,5,6]

    public static void main(String[] args) {

        mergeArray(new int[]{1,2,6,7,8,9}, new int[]{3,4,5,6});
    }


    private static int[] mergeArray(int [] arr1 , int [] arr2){

        int i=0,j=0;
        int counter=0;
        int [] res = new int[arr1.length+arr2.length];
        // [1,2,3] [4,5,6]
        while (i<arr1.length || j <arr2.length){
             int temp= i<arr1.length ?arr1[i]: Integer.MAX_VALUE;
             int temp2= j<arr2.length ?arr2[j]: Integer.MAX_VALUE;
            if(temp<temp2){
                res[counter++]=arr1[i++];
            }else {
                res[counter++]=arr2[j++];
            }
        }
        return res;
    }
}
