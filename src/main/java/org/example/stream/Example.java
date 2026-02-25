package org.example.stream;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Example {

    public static void main(String[] args) {
        steamExample();
    }

    public static void steamExample (){

        List<Integer> list = Arrays.asList(1,2,3,4,5,6);
        List<String> listString = Arrays.asList("Vinay", "Dinesh","Ranjan1","Dinesh");

       List<Integer> ans= list.stream().filter( data -> data >5).toList();
       ans.forEach( an -> System.out.println(an));

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);

        scores.forEach((key, value) -> {
            System.out.println(key);
            System.out.println(value);
        });

        System.out.println("Map the data");
        listString.stream().map(String::toUpperCase).forEach(System.out::println);

        System.out.println("Distinct the data");
        listString.stream().distinct().forEach(System.out::println);

        System.out.println("Multiple operations");
        listString.stream().filter( data -> data.length() >5).map(String::toUpperCase).sorted().forEach(System.out::println);

        System.out.println("Check condition");
        listString.stream().takeWhile( data -> data.equals("Vinay")).forEach(System.out::println);

        List<Integer> intVals = Arrays.asList(100,200,500,400,300);
        List<Integer> val200 = intVals.stream().filter( data -> data >200).toList();
        System.out.println("Values greater than 200 : " + val200);

        int sum = intVals.stream().reduce(0 , Integer::sum);
        System.out.println(" Int values for the : " + val200);
        int divide = intVals.stream().reduce(0 , (data1,data2)-> (data1+data2)/2);
        System.out.println("Values greater than 200 : " + val200);

        boolean anyMatch = intVals.stream().anyMatch( a -> a>500);

        System.out.println("Any Match greater than 500  : " + anyMatch);
        System.out.println("Find any of integer : " + intVals.stream().filter(d -> d>300).findAny());
        // This return a stream not a
        System.out.println("Find first of integer : " + intVals.stream().filter(d ->{

            System.out.println("testing");
            return d>300;}));
        List<String> strList = Arrays.asList("Apple", "Graphes","Kiwi","Goa","Mango");

        Map<Integer, List<String>> fruitLengthCount = strList.stream().collect(Collectors.groupingBy(String::length));
        System.out.println("Group By the steam data: " + fruitLengthCount );

        Map<Integer, Long> fruitCount = strList.stream().collect(Collectors.groupingBy(String::length, Collectors.counting()));

        System.out.println("Group By the steam data Length: " + fruitLengthCount );


        String [] fruits = {"Apple","Mango","Mango", "Jack fruit", "Graphes","Kiwi","Gao"};
       System.out.println(" Manipulated string"+ Stream.of(fruits).map(String::toUpperCase).collect(Collectors.groupingBy(String::length)));

       System.out.println("Converting array to map :" +Arrays.stream(fruits).map(String::toUpperCase).collect(Collectors.toMap(key-> key , String::length, Integer::sum)));
       System.out.println("Converting array to map :" +Arrays.stream(fruits).map(String::toUpperCase).collect(Collectors.toMap(key-> key.charAt(0), val->val, (ext , rep)-> ext+"&"+rep)));

       System.out.println("Unique values of string :" +  Arrays.stream(fruits).map( data -> data.split("")).flatMap(Arrays::stream).map(d->d.toUpperCase()).distinct().toList());


       // Optional exsercise :

        Optional<Integer> int1 = Optional.of(1);
        Optional<Integer> int2 = Optional.of(2);
        Optional<Integer> int3 = Optional.empty();
        Optional<Integer> int4 = Optional.of(4);

        List<Optional<Integer>> list1 = Arrays.asList(int1, int2, int3, int4);

        System.out.println("Optional Excersise: " + list1.stream().map(Optional::stream).toList());
        System.out.println("Optional Excersise: " + list1.stream().flatMap(Optional::stream).toList());

        Stream<String> s1 = Stream.of("A", "B");
        Stream<String> s2 = Stream.of("C", "D");
        Stream<String> s3 = Stream.of("E", "F");

        //Stream<String> twoStrem = Stream.concat(s1,s2);
        //System.out.println("Stream of two :" + twoStrem.toList());
        Stream<String> stream = Stream.of(s1,s2,s3).flatMap(Function.identity());
        System.out.println("Stream of two :" + stream.toList());

        //find the first non-repeating element at any given point.
        String [] fruits1 = {"Apple","Apple","Mango","Mango", "Jack fruit", "Graphes","Kiwi","Gao"};
        //System.out.println("Print not repetable element :"+ Stream.of(fruits1).collect(Collectors.groupingBy(LinkedHashMap::new,Collectors.counting())));

       List<Employee> employee =  List.of(new Employee("VInay","vinay","Male"),new Employee("VInay1","vinay1","Male"),new Employee("Divya","divya","female"));
        Map<String, List<Employee>> map=employee.stream().collect(Collectors.groupingBy(employee1 -> employee1.gender));
        System.out.println(map + "EMployee group by : %s");
    }



    //Arrays.as(new Employee("VInay","vinay","Male"))


}

class Employee{
    String name;
    String address;
    String gender;

    public Employee(String name, String address, String gender) {
        this.name = name;
        this.address = address;
        this.gender = gender;
    }
}