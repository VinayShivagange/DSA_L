package org.example.String;

import java.util.ArrayList;
import java.util.List;

/**
 * ASCII Cipher Decoder
 * 
 * Problem: Decode a string where each number represents an ASCII value
 * that should be converted to its corresponding character.
 * 
 * Example:
 * Input: "101971151121"
 * Output: "easy"
 * 
 * Explanation:
 * - 101 -> 'e'
 * - 97 -> 'a'
 * - 115 -> 's'
 * - 121 -> 'y'
 * 
 * Constraint: Output contains only lowercase English alphabets (a-z, ASCII 97-122)
 * 
 * Challenge: ASCII values can be 2 digits (97-99) or 3 digits (100-122),
 * so we need to determine where to split the string.
 */
public class ASCIIDecoder {
    
    /**
     * Decode the cipher string by converting ASCII values to characters
     * 
     * Approach: Since all values are 97-122 (lowercase letters):
     * - 97-99 are 2 digits
     * - 100-122 are 3 digits
     * 
     * We can use greedy parsing: try 3 digits first, if valid use it,
     * otherwise try 2 digits.
     */
    public static String decode(String cipher) {
        if (cipher == null || cipher.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        int i = 0;
        
        while (i < cipher.length()) {
            // Try 3 digits first (for values 100-122)
            if (i + 3 <= cipher.length()) {
                int threeDigit = Integer.parseInt(cipher.substring(i, i + 3));
                if (threeDigit >= 100 && threeDigit <= 122) {
                    result.append((char) threeDigit);
                    i += 3;
                    continue;
                }
            }
            
            // Try 2 digits (for values 97-99)
            if (i + 2 <= cipher.length()) {
                int twoDigit = Integer.parseInt(cipher.substring(i, i + 2));
                if (twoDigit >= 97 && twoDigit <= 99) {
                    result.append((char) twoDigit);
                    i += 2;
                    continue;
                }
            }
            
            // If we can't parse, there's an error
            throw new IllegalArgumentException(
                "Invalid cipher at position " + i + ": cannot decode");
        }
        
        return result.toString();
    }
    
    /**
     * Alternative approach: Recursive backtracking
     * This approach tries all possible splits and finds the valid one
     */
    public static String decodeRecursive(String cipher) {
        if (cipher == null || cipher.isEmpty()) {
            return "";
        }
        
        List<String> result = new ArrayList<>();
        if (decodeHelper(cipher, 0, new StringBuilder(), result)) {
            return result.get(0);
        }
        
        throw new IllegalArgumentException("Cannot decode cipher: " + cipher);
    }
    
    private static boolean decodeHelper(String cipher, int index, 
                                       StringBuilder current, List<String> result) {
        if (index == cipher.length()) {
            result.add(current.toString());
            return true;
        }
        
        // Try 2 digits
        if (index + 2 <= cipher.length()) {
            int twoDigit = Integer.parseInt(cipher.substring(index, index + 2));
            if (twoDigit >= 97 && twoDigit <= 122) {
                current.append((char) twoDigit);
                if (decodeHelper(cipher, index + 2, current, result)) {
                    return true;
                }
                current.deleteCharAt(current.length() - 1);
            }
        }
        
        // Try 3 digits
        if (index + 3 <= cipher.length()) {
            int threeDigit = Integer.parseInt(cipher.substring(index, index + 3));
            if (threeDigit >= 100 && threeDigit <= 122) {
                current.append((char) threeDigit);
                if (decodeHelper(cipher, index + 3, current, result)) {
                    return true;
                }
                current.deleteCharAt(current.length() - 1);
            }
        }
        
        return false;
    }
    
    /**
     * Optimized approach: Since we know the constraint (97-122),
     * we can use a more efficient greedy algorithm
     */
    public static String decodeOptimized(String cipher) {
        if (cipher == null || cipher.isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        int i = 0;
        
        while (i < cipher.length()) {
            int asciiValue;
            
            // Check if we can form a valid 3-digit number (100-122)
            if (i + 3 <= cipher.length()) {
                int candidate = Integer.parseInt(cipher.substring(i, i + 3));
                if (candidate >= 100 && candidate <= 122) {
                    asciiValue = candidate;
                    i += 3;
                } else if (i + 2 <= cipher.length()) {
                    // Try 2 digits (97-99)
                    candidate = Integer.parseInt(cipher.substring(i, i + 2));
                    if (candidate >= 97 && candidate <= 99) {
                        asciiValue = candidate;
                        i += 2;
                    } else {
                        throw new IllegalArgumentException(
                            "Invalid ASCII value at position " + i);
                    }
                } else {
                    throw new IllegalArgumentException(
                        "Incomplete cipher at position " + i);
                }
            } else if (i + 2 <= cipher.length()) {
                // Only 2 digits remaining
                int candidate = Integer.parseInt(cipher.substring(i, i + 2));
                if (candidate >= 97 && candidate <= 122) {
                    asciiValue = candidate;
                    i += 2;
                } else {
                    throw new IllegalArgumentException(
                        "Invalid ASCII value at position " + i);
                }
            } else {
                throw new IllegalArgumentException(
                    "Incomplete cipher at position " + i);
            }
            
            result.append((char) asciiValue);
        }
        
        return result.toString();
    }
    
    /**
     * Encode a string to ASCII cipher (reverse operation)
     */
    public static String encode(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        StringBuilder cipher = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c < 97 || c > 122) {
                throw new IllegalArgumentException(
                    "Character '" + c + "' is not a lowercase letter");
            }
            cipher.append((int) c);
        }
        
        return cipher.toString();
    }
    
    public static void main(String[] args) {
        System.out.println("=== ASCII Cipher Decoder ===\n");
        
        // Test Case 1: Given example
        String cipher1 = "101971151121";
        System.out.println("Test 1:");
        System.out.println("Input:  " + cipher1);
        System.out.println("Output: " + decode(cipher1));
        System.out.println("Expected: easy");
        System.out.println("Match: " + decode(cipher1).equals("easy"));
        System.out.println();
        
        // Verify encoding
        System.out.println("Encoding verification:");
        System.out.println("'easy' -> " + encode("easy"));
        System.out.println("Decoding back: " + decode(encode("easy")));
        System.out.println();
        
        // Test Case 2: All 2-digit codes (97-99)
        String cipher2 = "979899";
        System.out.println("Test 2:");
        System.out.println("Input:  " + cipher2);
        System.out.println("Output: " + decode(cipher2));
        System.out.println("Expected: abc");
        System.out.println("Match: " + decode(cipher2).equals("abc"));
        System.out.println();
        
        // Test Case 3: All 3-digit codes (100-122)
        String cipher3 = "100101102";
        System.out.println("Test 3:");
        System.out.println("Input:  " + cipher3);
        System.out.println("Output: " + decode(cipher3));
        System.out.println("Expected: def");
        System.out.println("Match: " + decode(cipher3).equals("def"));
        System.out.println();
        
        // Test Case 4: Mixed 2 and 3 digit codes
        String cipher4 = "97100101102";
        System.out.println("Test 4:");
        System.out.println("Input:  " + cipher4);
        System.out.println("Output: " + decode(cipher4));
        System.out.println("Expected: abcde");
        System.out.println("Match: " + decode(cipher4).equals("abcde"));
        System.out.println();
        
        // Test Case 5: Single word
        String cipher5 = encode("hello");
        System.out.println("Test 5:");
        System.out.println("Input:  " + cipher5);
        System.out.println("Output: " + decode(cipher5));
        System.out.println("Expected: hello");
        System.out.println("Match: " + decode(cipher5).equals("hello"));
        System.out.println();
        
        // Test Case 6: Edge cases
        System.out.println("Test 6: Edge Cases");
        System.out.println("Empty string: '" + decode("") + "'");
        System.out.println("Single letter 'z': " + decode("122"));
        System.out.println("Single letter 'a': " + decode("97"));
        System.out.println();
        
        // Test Case 7: Verify all approaches give same result
        System.out.println("Test 7: Compare Approaches");
        String testCipher = "101971151121";
        String result1 = decode(testCipher);
        String result2 = decodeOptimized(testCipher);
        String result3 = decodeRecursive(testCipher);
        System.out.println("Greedy:      " + result1);
        System.out.println("Optimized:   " + result2);
        System.out.println("Recursive:   " + result3);
        System.out.println("All match: " + 
            (result1.equals(result2) && result2.equals(result3)));
    }
}
