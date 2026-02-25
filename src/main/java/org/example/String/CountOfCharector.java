package org.example.String;

public class CountOfCharector {

    //abaabbc
    // buf -a1b1  , i=0 < lenght , count=0;
    public String coutChar(String val){
        StringBuilder str = new StringBuilder();
        int i=0;
        int j=i+1;
        for(;j<val.length();j++){
           if( val.charAt(i) == val.charAt(j)){
                continue;
           }
            str.append(val.charAt(i)).append(j - i);
           i=j;
        }

        if(i < val.length() && j==val.length() ){
            str.append(val.charAt(j-1)).append(j - i);
        }

      return  str.toString();
    }

}

class Main{

    public static void main(String[] args) {
        System.out.println(new CountOfCharector().coutChar("abaabbcran"));
    }
}