package org.example.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class LRUCache <K,V>{
    Node<K,V>  tail;
    Node<K,V>  head;
    int capacity;
    int realSize;
    Map<K, Node<K,V>> cache;


    static class Node<K,V>{
        K k;
        V V;
        Node<K,V> next;
        Node<K,V> prev;

        public Node(K k, V v) {
            this.k = k;
            V = v;
        }
    }

    LRUCache(int capacity){
        this.capacity = capacity;
        cache =  new HashMap<>(capacity);
        tail = new Node<>(null, null);
        head = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public void add( K k , V v){
        Node<K, V> temp= cache.get(k);
        if(temp!=null){
          remove(temp);
        }
        if( capacity == realSize){
            remove(tail.prev);
        }
        insert(new Node<>(k, v));
    }


    public V get(K k){
        Node<K, V> temp= cache.get(k);
        if(temp !=null){
            remove(temp);
            insert(temp);
            return temp.V;
        }
        return null;
    }

    // 5 - >6 ->7
    public void remove ( Node<K, V> node){
        Node<K,V> temp = node.next;
        temp.prev = node.prev;
        node.prev.next = temp;
        node.next = null;
        node.prev = null;
        cache.remove(node.k);
        realSize--;

    }

    public void insert ( Node<K, V> node ){
        cache.put(node.k , node);
        Node<K,V> temp = head.next;
        head.next =node;
        node.prev = head;
        node.next= temp ;
        temp.prev = node;
        realSize++;
    }

    public static void main(String[] args) {
        LRUCache<String , Integer> lru =  new LRUCache<>(5);
        lru.add("user1",1);
        lru.add("user2",1);
        lru.add("user3",1);
        lru.add("user4",1);
        lru.add("user5",1);
        lru.add("user6",1);
        lru.add("user1",(lru.get("user1") == null ? 0 :lru.get("user1"))+1);
    }
}