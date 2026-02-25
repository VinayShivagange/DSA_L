package org.example.tree;

import java.util.*;

public class LevelOrderTraversal {

    public static void main(String[] args) {
        Node<Integer> head = new Node<>(1);
        head.left = new Node<>(2);
        head.right = new Node<>(3);
        head.right.left = new Node<>(4);
        head.right.right = new Node<>(5);
        head.right.left.left = new Node<>(6);
        head.right.left.left.left = new Node<>(8);
        head.right.left.left.right = new Node<>(7);
        var view = new ArrayList<Integer>();
        var temp = new LinkedList<Node<Integer>>();
        temp.push(head);
        levelOrder(view, temp);
        view.forEach(System.out::println);
    }

    public static void levelOrder(List<Integer> view, Queue<Node<Integer>> temp){

        if(temp.isEmpty()){
            return;
        }
        Node<Integer> node = temp.poll();
        view.add(node.data);

        if(Objects.nonNull(node.left)){
            temp.add(node.left);
        }

        if(Objects.nonNull(node.right)){
            temp.add(node.right);
        }
        levelOrder(view,temp);

    }

}
