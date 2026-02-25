package org.example.String;

import java.util.HashMap;
import java.util.Map;

public class LongSubArrayOfString {


    public static void main(String[] args) {
        subArray("abcbadbcbb");
    }



    private static int subArray(String str){
        Map<Character,Integer > charCount =  new HashMap<>();
        int subLenght=0;
        int i=0,j=0;
        while (i< str.length() && j< str.length() ){

            if(charCount.get(str.charAt(j)) == null){
                charCount.put(str.charAt(j), 1);
                j++;
            }else {
                charCount.remove(str.charAt(j));
                subLenght = j-i;
                i++;
            }

        }
        return subLenght;
    }
}
