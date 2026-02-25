package org.example.tree;

public class Node<E> {

    E data;
    Node<E> left;
    Node<E> right;

    public Node(E data) {
        this.data = data;
    }

}
