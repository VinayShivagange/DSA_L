package org.example.stack;

import java.util.Comparator;

public class MaxStack<T extends Comparable<T>> {
    int INITIAL_CAPACITY=16;
    int size=0;
    Node<T>[] bucket;
    T maxNode = null;

    MaxStack(){
        bucket = new Node[INITIAL_CAPACITY];
    }

    public void push(T data){
        if(maxNode ==null || maxNode.compareTo(data) < 0){
            maxNode = data;
        }
        bucket[size++]= new Node<>(data,maxNode);
    }

    //5->6->7->1->2
    //
    public  T pop(){
        Node<T> ref = bucket[--size];
        if( ref.maxData.compareTo(ref.data) > 0){
            maxNode = ref.data;
        }
        return  ref.data;
    }

    public  T peak(){
        int index= size-1;
        return bucket[index].data;
    }


    public  T getMax(){
        int index= size-1;
        return bucket[index].maxData;
    }

    static class Node<T>{
        T data;
        T maxData;

        Node( T data, T maxData){
            this.data =  data;
            this.maxData =  maxData;
        }
    }


    public static void main(String[] args) {
        MaxStack<Integer> m1 = new MaxStack<>();
        m1.push(5);
        System.out.println(m1.getMax());
        m1.push(6);
        System.out.println(m1.getMax());
        m1.push(7);
        System.out.println(m1.getMax());
        System.out.println(m1.peak());
        System.out.println(m1.getMax());
        m1.push(1);
        m1.push(2);
        System.out.println(m1.getMax());
        System.out.println(m1.pop());
        System.out.println(m1.peak());
        System.out.println(m1.pop());
        System.out.println(m1.pop());
        System.out.println(m1.getMax());
        m1.push(23);
        System.out.println(m1.getMax());
        m1.push(1);
        m1.push(2);
        m1.push(3);
        System.out.println(m1.getMax());
        System.out.println(m1.pop());
        System.out.println(m1.pop());
        System.out.println(m1.pop());
        System.out.println(m1.pop());
        System.out.println(m1.getMax());


    }
}
