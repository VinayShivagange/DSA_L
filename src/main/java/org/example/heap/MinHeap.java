package org.example.heap;

// Implementing "Min Heap"
public class MinHeap {

    int [] minHeap ;
    int size;
    int capacity;

    MinHeap(int capacity){
        this.capacity = capacity;
        this.size = 0;
        minHeap = new int [capacity];
        minHeap[0] = 0;
    }

    //[0,1,]
    public void add(Integer data){
        if(size == capacity){
            return;
        }
        minHeap[++size] = data;
        int index = size;
        int parent = index /2;

        while (index >1 && minHeap[parent] < minHeap[index] ){
            swap(minHeap, parent, index);
            index= parent;
            parent = index /2;
        }
    }

    void swap(int [] minHeap, int index, int swapIndex ){
        int temp = minHeap[index];
        minHeap[index] =  minHeap[swapIndex];
        minHeap[swapIndex] = temp;
    }

    public Integer pop(){
        if(size < 1){
            return null;
        }
       int returnValue = minHeap[1];
       minHeap[1] = minHeap[size];
       int index = 1;
       while (index < size /2){
           int left = index *2;
           int right = index *2+1;
          if(minHeap[index] < minHeap[left] || minHeap[index] < minHeap[right]){
              if(minHeap[left] < minHeap[right] ){
                  swap(minHeap, index, right);
              }else {
                  swap(minHeap, index, left);
              }
          }
           index++;
       }

        return returnValue;
    }



    public static void main(String[] args) {
        // Test case
        MinHeap minHeap = new MinHeap(10);
        minHeap.add(5);
        minHeap.add(1);
        minHeap.add(4);
        minHeap.add(7);
        minHeap.add(8);
        minHeap.add(9);
        minHeap.add(3);
        minHeap.add(1);
        minHeap.add(2);
        System.out.println(minHeap.pop());
        System.out.println(minHeap.pop());
        System.out.println(minHeap.pop());
    }
}
