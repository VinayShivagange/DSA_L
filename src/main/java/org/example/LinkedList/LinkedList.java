package org.example.LinkedList;

import java.util.Objects;

public class LinkedList<T> {
    int size=0;
    Node<T> head= null ;
    Node<T> tail=null ;


    boolean add( T t){
        if(head == null){
            head= new Node<>(t);
            tail= head;
        }else{
            tail.next= new Node<>(t);
            tail=tail.next;
        }
        size++;
        return true;
    }



    static class Node<T>{
        T t;

        public Node(T t) {
            this.t = t;
        }

        Node<T> next;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(t, node.t) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
        }

        @Override
        public int hashCode() {
            return Objects.hash(t, next, prev);
        }

        Node<T> prev;

    }
}


