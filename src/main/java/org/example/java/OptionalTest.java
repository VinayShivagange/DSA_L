package org.example.java;

import java.util.Optional;

public class OptionalTest {

    public static void main(String[] args) {
        Optional<OptionalTest> test = Optional.ofNullable(null);

        //System.out.println(test.orElse());

        Test t1 = null;

    }

    public static void getDeafult1(){
        System.out.println("Testing");
    }

}

