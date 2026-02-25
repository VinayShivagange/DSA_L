package org.example.calix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeakElement {

    public static void main(String[] args) {
        // [2 , 3, 4 ,5 ,1,6]
        // i=0 , j=i+1 , k = j+1
         System.out.println(findPeakElement(Arrays.asList(2,8,7,5,1,6)));
         System.out.println(findPeakElement(Arrays.asList(10, 20, 15, 2, 23, 90, 80)));
    }

    // Using pointer
    private static int findPeakElement(List<Integer> array){
        int index=0 ;
        var i=0; var j = i+1 ; var k = j+1;
        for( i=0 ; k < array.size(); i++){
            if(array.get(i) < array.get(j) && array.get(j) > array.get(k)){
                index =j;
            }
            j++;
            k++;
        }

        List<String> answer = new ArrayList<>(12);
       System.out.println( "Size" +answer.size());
        return index;
    }


    // Using Binary search
    private static int findPeakElementSearch(List<Integer> array){
        boolean left = true;
        boolean right = true;

        //  [2 , 3, 4 ,5 ,1,6]
        for(int i=0; i< array.size();i++){

            if( i>0 && array.get(i-1) <= array.get(i)){
                left =true;
            }

            if( i< array.size()-1 && array.get(i) < array.get(i+1)){
                right =true;
            }

            int [][] ans = {
                    {1,2,3},
                    {1,2,3},
                    {1,2,3},
            };
            int l=ans.length;
            int max =0;
            for(int m=0;m<3;m++){
                int sum=0;
                for (int n=0;n<2;n++){
                  sum += ans[m][n];
                }
                if(max<sum){
                    max =sum;
                }
            }

        }

        return 0;
    }
}
