package org.example.array;


/*
        Count the number of subarrays where max - min <= limit.
        Given:
        nums = [1, 3, 2, 1, 4]
        limit = 2
        Output: 10
*
* */
public class CountSubArray {

    public static void main(String[] args) {
        subArrayCount(new int[]{1,3,2,1,4},2);
    }

    public static int subArrayCount(int[] nums, int limit){
        int count = 0;
        int n = nums.length;

        // Try all possible subarrays
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                // Find min and max in subarray [i, j]
                int min = nums[i];
                int max = nums[i];

                for (int k = i; k <= j; k++) {
                    min = Math.min(min, nums[k]);
                    max = Math.max(max, nums[k]);
                }

                if (max - min <= limit) {
                    count++;
                }
            }
        }

        return count;
    }
}
