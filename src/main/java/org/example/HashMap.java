package org.example;

import java.util.Objects;


public class HashMap<K,V> {

    static final int INITIAL_CAPACITY = 16;
    Node<K,V>[] bucket = new Node[INITIAL_CAPACITY];
    double thershold = 0.75;
    int size = 0;


    public void resize(){

        Node<K,V>[] oldBuk= bucket ;
        int oldSize= size;
        int newSize = (int) (oldSize+oldSize *0.75);
        Node<K,V>[] newBuk= new Node[newSize] ;

        for(int i=0; i< bucket.length; i++){
           Node<K,V> ref= oldBuk[i];
            while(null != ref){
                int bukIndex = newSize-1 & hash(ref.key) ;
                if (newBuk[bukIndex] == null) {
                    newBuk[bukIndex] = new Node<>(ref.key, ref.value);
                } else {
                    Node<K, V> temp = newBuk[bukIndex];
                    while (null != temp.next) {
                        temp = temp.next;
                    }
                    temp.next = new Node<>(ref.key, ref.value);
                }
                ref=ref.next;
            }
        }
        bucket = newBuk;
    }

    public V get(K key){

        if( bucket[bucket.length-1 & hash(key)] != null) {
            Node<K,V> ref = bucket[bucket.length-1 & hash(key)];
            while (null != ref) {
                if (ref.key.hashCode() == key.hashCode() && ref.key.equals(key)) {
                   return ref.value;
                }
                ref = ref.next;
            }
        }

        return null;
    }


    // [ [1,1]-> , ]
    public void put(K k , V v){
        int n= bucket.length;
        int i;

        if(size/bucket.length > thershold){
            resize();
        }


        if( bucket[i=n-1 & hash(k)] == null){
            bucket[i] = new Node<>(k,v);
        }else {
            Node<K, V> temp= bucket[i];
            Node<K, V> e = null;
            Node<K, V> last = null;
            while(null!=temp){
                if(temp.key.hashCode() == k.hashCode() && temp.key.equals(k)){
                    e = temp;
                    last = null;
                    break;
                }else {
                    last = temp;
                    temp = temp.next;
                }
            }
          if(Objects.nonNull(e)){
              e.value = v;
          }

          if(Objects.nonNull(last)){
              last.next = new Node<>(k,v);
          }
        }
        size++;
    }


    static int hash(Object obj){
        int i;
      return null == obj ? 0:  ((i=obj.hashCode()*3) ^ i >> 16);
    }

    public static void main(String[] args) {
        HashMap<String,Integer> map = new HashMap<>();
        map.put("1",1);
        map.put("11",1);
        map.put("21",1);
        map.put("31",1);
        map.put("32",1);
        map.put("33",1);
        map.put("34",1);
        map.put("35",1);
        map.put("36",1);
        map.put("37",1);
        map.put("38",12323);
        map.put("38",323);
        map.put("41",1);
        map.put("431",1);
        map.put("441",1);
        map.put("32",1);
        map.put("12",1);
        map.put("41",1);
        map.put("51",1);
        map.put("12",1);
        map.put("12",1);
        map.put("111",2);
        map.put("1",3);
        map.put("1",4);
        map.put("2",4);
        map.put("3",4);
        map.put("2",1);
        System.out.println(map.get("2"));
        System.out.println(map.get("1"));
        System.out.println(map.get("21"));
        System.out.println(map.get("38"));
        System.out.println(map.get("431"));
    }


}


class Node<K, V> {
    int hash;
    K  key;
    V  value;
    Node<K, V> next ;

    Node (K k , V v){
        this.key = k;
        this.value = v;
    }

}