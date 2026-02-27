package org.example.array;

/**
 * Matrix Rotation - 90 Degrees Clockwise In-Place
 * 
 * Task: Rotate a square matrix 90 degrees clockwise in-place.
 * 
 * Approach:
 * 1. Transpose the matrix (swap matrix[i][j] with matrix[j][i])
 * 2. Reverse each row
 * 
 * Time Complexity: O(n^2) where n is the size of the matrix
 * Space Complexity: O(1) - in-place rotation
 */
public class MatrixRotation {
    
    /**
     * Rotate matrix 90 degrees clockwise in-place
     * 
     * @param matrix Square matrix to rotate
     */
    public static void rotate90Clockwise(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix.length != matrix[0].length) {
            return;
        }
        
        int n = matrix.length;
        
        // Step 1: Transpose the matrix (swap elements across diagonal)
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                // Swap matrix[i][j] with matrix[j][i]
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        
        // Step 2: Reverse each row
        for (int i = 0; i < n; i++) {
            int left = 0;
            int right = n - 1;
            while (left < right) {
                int temp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = temp;
                left++;
                right--;
            }
        }
    }
    
    /**
     * Alternative approach: Rotate layer by layer
     * This is more intuitive but slightly more complex
     */
    public static void rotate90ClockwiseLayerByLayer(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix.length != matrix[0].length) {
            return;
        }
        
        int n = matrix.length;
        
        // Rotate layer by layer
        for (int layer = 0; layer < n / 2; layer++) {
            int first = layer;
            int last = n - 1 - layer;
            
            for (int i = first; i < last; i++) {
                int offset = i - first;
                
                // Save top
                int top = matrix[first][i];
                
                // Move left to top
                matrix[first][i] = matrix[last - offset][first];
                
                // Move bottom to left
                matrix[last - offset][first] = matrix[last][last - offset];
                
                // Move right to bottom
                matrix[last][last - offset] = matrix[i][last];
                
                // Move top to right
                matrix[i][last] = top;
            }
        }
    }
    
    /**
     * Rotate matrix 90 degrees counter-clockwise in-place
     */
    public static void rotate90CounterClockwise(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix.length != matrix[0].length) {
            return;
        }
        
        int n = matrix.length;
        
        // Step 1: Transpose
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        
        // Step 2: Reverse each column (instead of row)
        for (int j = 0; j < n; j++) {
            int top = 0;
            int bottom = n - 1;
            while (top < bottom) {
                int temp = matrix[top][j];
                matrix[top][j] = matrix[bottom][j];
                matrix[bottom][j] = temp;
                top++;
                bottom--;
            }
        }
    }
    
    /**
     * Print matrix for debugging
     */
    public static void printMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            System.out.println("Empty matrix");
            return;
        }
        
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        System.out.println("=== Matrix Rotation 90 Degrees Clockwise ===");
        
        // Test Case 1: 3x3 matrix
        int[][] matrix1 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        
        System.out.println("Original 3x3 matrix:");
        printMatrix(matrix1);
        
        rotate90Clockwise(matrix1);
        System.out.println("After 90° clockwise rotation:");
        printMatrix(matrix1);
        
        // Expected:
        // 7  4  1
        // 8  5  2
        // 9  6  3
        
        // Test Case 2: 4x4 matrix
        int[][] matrix2 = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        
        System.out.println("Original 4x4 matrix:");
        printMatrix(matrix2);
        
        rotate90Clockwise(matrix2);
        System.out.println("After 90° clockwise rotation:");
        printMatrix(matrix2);
        
        // Expected:
        // 13  9  5  1
        // 14  10  6  2
        // 15  11  7  3
        // 16  12  8  4
        
        // Test Case 3: Layer-by-layer approach
        int[][] matrix3 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        
        System.out.println("Original matrix (for layer-by-layer):");
        printMatrix(matrix3);
        
        rotate90ClockwiseLayerByLayer(matrix3);
        System.out.println("After 90° clockwise rotation (layer-by-layer):");
        printMatrix(matrix3);
        
        // Test Case 4: Counter-clockwise rotation
        int[][] matrix4 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        
        System.out.println("Original matrix (for counter-clockwise):");
        printMatrix(matrix4);
        
        rotate90CounterClockwise(matrix4);
        System.out.println("After 90° counter-clockwise rotation:");
        printMatrix(matrix4);
        
        // Expected:
        // 3  6  9
        // 2  5  8
        // 1  4  7
    }
}
