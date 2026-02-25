package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");
        int k = 899898888;
        String val = "ran";
        int h=val.hashCode();
        System.out.println("Logical shit of k: " + (h >>> 16));
        System.out.println("Hashcode : "+val.hashCode());
        System.out.println("XOR shit of k value: " + ( (h=val.hashCode()) ^ (h >>> 16)));
        System.out.println("And shit of k value: " + ( 15 & 2323));

    }
}