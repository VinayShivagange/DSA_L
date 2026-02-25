package org.example.tree;

import java.util.Arrays;
import java.util.List;

public class BinaryTreeUtil<E> {

    public Node<E> formBinaryTree(List<E> data){

        return convert(data, 0, data.size()-1);
    }

    public Node<E> convert(List<E> data, int start , int end){
        if(start > end){
            return null;
        }
        int mid = end+start/2;
        Node<E> root = new Node<>(data.get(mid));

        root.left = convert(data, start, mid-1);
        root.right = convert(data, mid+1, end);

        return root;
    }

    public static void main(String[] args) {
        BinaryTreeUtil<Integer> util = new BinaryTreeUtil<>();
        Node<Integer> root=util.formBinaryTree(Arrays.asList(1,2,3,4,5));
    }
}
