package org.example.LinkedList;

public class FindMidLinkedList {

    public static void main(String[] args) {

        LinkedList<Integer> l2 = new LinkedList<>();
        l2.add(1);
        l2.add(2);
        l2.add(3);
        l2.add(4);
        l2.add(5);
        l2.add(5);
        System.out.println(mid(l2));
    }

    static Integer mid( LinkedList<Integer> l2  ){

        if(l2.size <0){
            return null;
        }

        if(l2.size <3){
            return l2.head.t;
        }


        LinkedList.Node<Integer> slowPointer= l2.head ;
        LinkedList.Node<Integer> fastPointer= l2.head.next.next;

        while (fastPointer!=null && fastPointer.next!=null && slowPointer !=null){
            slowPointer=  slowPointer.next;
            fastPointer=  fastPointer.next.next;
        }

        return l2.size%2 ==0?  slowPointer.t: slowPointer.next.t;
    }
}
