package org.example.LinkedList;


import java.util.Arrays;
import java.util.List;

public class ReverseLinkedList {

    public static void main(String[] args) {
        LinkedList<Integer> l1 = new LinkedList<>();
        l1.add(1);
        l1.add(2);
        l1.add(3);
        l1.add(4);
        l1.add(5);
        l1.add(6);
        reverse(l1);
        print(l1.head);
        LinkedList<Integer> l2 = new LinkedList<>();
        l2.add(1);
        l2.add(2);
        l2.add(3);
        l2.add(4);
        l2.add(5);
        l2.add(6);
        reverse(l1);
        print(l1.head);
        iteraterReverse(l2);
        print(l2.head);

    }

    private static void print( LinkedList.Node<Integer> head ){
        while (head!=null){
            System.out.println(head.t);
            head = head.next;
        }
    }
    //1->2->3->4->5  - input

    // 5->4->3->2->1 - output
    private static  void iteraterReverse(LinkedList<Integer> l1){

        if(null == l1){
            return;
        }

        LinkedList.Node<Integer> head = l1.head;
        LinkedList.Node<Integer> current = head;
        LinkedList.Node<Integer> next = null;
        LinkedList.Node<Integer> temp = null;
        while (current.next!=null){
            next = current.next;
            current.next= temp;
            temp = current;
            current= next;
        }


    }


    //1->2->3->4->5

    // 5->4->3->2->1
    private static  void reverse(LinkedList<Integer> l1){
        rec(l1.head,null);
    }


    static void rec(LinkedList.Node<Integer> cur, LinkedList.Node<Integer> prev){

        if(cur.next !=null){
            rec(cur.next, cur);
        }
        cur.next=prev;
    }
}


