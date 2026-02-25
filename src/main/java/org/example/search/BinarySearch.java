package org.example.search;

public class BinarySearch {

    public static void main(String[] args) {
        System.out.println(search(new int[]{1,2,3,4,5},5));
        System.out.println(search(new int[]{1,2,3,4,5},1));
        System.out.println(search(new int[]{1,2,3,4,5},2));
        System.out.println(search(new int[]{1,2,3,4,5},3));
        System.out.println(search(new int[]{1,2,3,4,5},7));
    }


    // Merge intervals - [1,3][2,6][8,10][9,18] - > [ 1,6][8,10][15,18]
    public static boolean search(int[] arr, int key){

        int start =0;
        int end = arr.length-1;
        int mid = (start +end)/2;

        while (start <= end){
            if(arr[mid]== key){
                return true;
            }
            if(arr[mid] < key){
                start++;
            }else{
                end--;
            }
            mid = (start +end)/2;
        }

        return false;
    }
}
