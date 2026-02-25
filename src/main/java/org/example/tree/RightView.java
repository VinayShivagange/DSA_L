package org.example.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RightView {

    /*
    *           1
    *       2      3
    *           4     5
    *        6
    *         7
    * o/p -  1,3,5,6,7
    * */

    public static void main(String[] args) {
        Node<Integer> head = new Node<>(1);
        head.left = new Node<>(2);
        head.right = new Node<>(3);
        head.right.left = new Node<>(4);
        head.right.right = new Node<>(5);
        head.right.left.left = new Node<>(6);
        head.right.left.left.right = new Node<>(7);
        var view = new ArrayList<Integer>();
        getRightView(head, view, 0);
        view.forEach( data-> System.out.println(data));
    }


    public static void getRightView(Node<Integer> node, List<Integer> rightView, int depth){

        if(Objects.isNull(node)) {
            return;
        }
        if(depth == rightView.size()){
            rightView.add(node.data);
        }


        // left and right works both
        getRightView(node.left, rightView, depth+1);
        getRightView(node.right, rightView, depth+1);
    }
}
