package org.example.wayfair;

import java.util.*;

public class AddTwoString {

    //Add two big string
    // s1 = "12345678"
    //s2  ="98989"
    //output = "12444667"
    public static void main(String[] args) {
        System.out.println("Please provide the input");
        System.out.println(addSum("9999,331,111,111,111","99422222221111"));
    }

    public static String addSum(String s1 , String s2){
        StringBuilder total = new StringBuilder();
        boolean hasComma = false;
        if(Objects.isNull(s1) || s1.isBlank() || Objects.isNull(s2) || s2.isEmpty()){
            return "";
        }

        if(s1.contains(",") || s2.contains(",")){
            hasComma = true;
            s1 = s1.replace(",","");
            s2 = s2.replace(",","");
        }

        int lenght = Math.max(s1.length(), s2.length());
        int carry = 0; int j = s1.length()-1; int k = s2.length()-1;
        for(int i=0; i < lenght; i++){
             var c1 = i < s1.length()? s1.charAt(j) - '0' : 0;
             var c2 = i < s2.length()? s2.charAt(k) - '0' : 0;
             var sum = c1 + c2 + carry;
             total.append(sum%10);
             carry = sum/10;
             j--;k--;
        }
        if(carry !=0){
            total.append(carry);
        }
        if(hasComma){
            var totalLength = total.length();
            var count =0;
            //198753333332222
            for(int i=0; i < totalLength-3; i=i+3){

                total.insert(i+count+3,",");
                count++;
            }
        }

        return total.reverse().toString() ;
    }
}
