package org.example.String;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermutationOfString {

    static Set<String> res = new HashSet<>();

    public static void main(String[] args) {
        System.out.println(new PermutationOfString().permutation("VIM"));
        generatePermutations("VIM",0 ,3);
        System.out.println(res);
    }

    // VIM -  VIM , IVM, IMV .
    public List<String> permutation(String str){
        int length = str.length();
        Set<String> res = new HashSet<>();
        char[] arr = str.toCharArray();
        for(int i=0 ; i<length ; i++){
            for(int j=0 ; j<length ; j++) {
                char[] temp = arr.clone();
                char ref = temp[i];
                temp[i]= temp[j];
                temp[j] = ref;
                res.add(new String(temp));
            }
        }
        return  res.stream().toList();
    }

    private String shifting(int index , String str , int last){
        char [] temp = new char[str.length()];
        temp[index] = str.charAt(index);
        for(int i=0 ; i<str.length() && last < str.length();i++){
            if( i != index){
                temp[i]= str.charAt(last);
            }
        }

        return new String(temp);
    }

    private static void generatePermutations(String str, int left, int right) {
        if (left == right) {
            res.add(str);
        } else {
            for (int i = left; i < right; i++) {
                // 1. Swap the current character to the front
                str = swap(str, left, i);

                // 2. Recurse for the rest of the string
                generatePermutations(str, left + 1, right);

                // 3. Backtrack: Swap back to restore original string for next loop
                str = swap(str, left, i);
            }
        }
    }

    private static String swap(String a, int i, int j) {
        char[] charArray = a.toCharArray();
        char temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }
}
