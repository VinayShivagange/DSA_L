package org.example.array;

import java.util.Arrays;

public class RemoveDuplicate {


    public static int[] removeDuplicatesSorted(int[] numbers) {
        if (numbers.length == 0) return numbers;

        // 1. Sort the array so duplicates are adjacent
        Arrays.sort(numbers);

        // 2. Use a pointer to track the position of unique elements
        int j = 0;
        for (int i = 0; i < numbers.length - 1; i++) {
            if (numbers[i] != numbers[i + 1]) {
                numbers[j++] = numbers[i];
            }
        }
        numbers[j++] = numbers[numbers.length - 1];

        // 3. Return a trimmed version of the array
        return Arrays.copyOf(numbers, j);
    }


    public static void main(String[] args) {
        removeDuplicatesSorted(new int[]{1,2,3,4,2,1});
    }
}
