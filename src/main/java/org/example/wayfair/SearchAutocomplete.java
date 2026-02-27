package org.example.wayfair;

import java.util.*;

/**
 * Search/Autocomplete: Working with Tries or String prefixes
 * 
 * Common problems:
 * - Implement autocomplete functionality
 * - Search suggestions based on prefix
 * - Find all words with given prefix
 * - Longest common prefix
 * - Word search in 2D grid
 */
public class SearchAutocomplete {
    
    /**
     * Trie Node for autocomplete
     */
    static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEndOfWord;
        List<String> words; // Store words ending at this node for faster retrieval
        
        TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
            words = new ArrayList<>();
        }
    }
    
    /**
     * Trie implementation for autocomplete
     */
    static class AutocompleteTrie {
        private TrieNode root;
        
        public AutocompleteTrie() {
            root = new TrieNode();
        }
        
        /**
         * Insert a word into the trie
         */
        public void insert(String word) {
            TrieNode current = root;
            
            for (char c : word.toCharArray()) {
                current.children.putIfAbsent(c, new TrieNode());
                current = current.children.get(c);
                current.words.add(word); // Store word at each node for autocomplete
            }
            
            current.isEndOfWord = true;
        }
        
        /**
         * Search if a word exists in the trie
         */
        public boolean search(String word) {
            TrieNode current = root;
            
            for (char c : word.toCharArray()) {
                if (!current.children.containsKey(c)) {
                    return false;
                }
                current = current.children.get(c);
            }
            
            return current.isEndOfWord;
        }
        
        /**
         * Check if any word starts with the given prefix
         */
        public boolean startsWith(String prefix) {
            TrieNode current = root;
            
            for (char c : prefix.toCharArray()) {
                if (!current.children.containsKey(c)) {
                    return false;
                }
                current = current.children.get(c);
            }
            
            return true;
        }
        
        /**
         * Get all words with the given prefix (autocomplete)
         */
        public List<String> autocomplete(String prefix) {
            List<String> results = new ArrayList<>();
            TrieNode current = root;
            
            // Navigate to the prefix node
            for (char c : prefix.toCharArray()) {
                if (!current.children.containsKey(c)) {
                    return results; // No words with this prefix
                }
                current = current.children.get(c);
            }
            
            // Collect all words from this node
            collectWords(current, prefix, results);
            return results;
        }
        
        /**
         * Get top K suggestions based on prefix
         */
        public List<String> autocompleteTopK(String prefix, int k) {
            List<String> allResults = autocomplete(prefix);
            return allResults.subList(0, Math.min(k, allResults.size()));
        }
        
        /**
         * DFS to collect all words from a node
         */
        private void collectWords(TrieNode node, String prefix, List<String> results) {
            if (node.isEndOfWord) {
                results.add(prefix);
            }
            
            for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
                collectWords(entry.getValue(), prefix + entry.getKey(), results);
            }
        }
        
        /**
         * Get all words in the trie
         */
        public List<String> getAllWords() {
            List<String> words = new ArrayList<>();
            collectWords(root, "", words);
            return words;
        }
    }
    
    /**
     * Find longest common prefix of an array of strings
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        
        String prefix = strs[0];
        
        for (int i = 1; i < strs.length; i++) {
            while (!strs[i].startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) {
                    return "";
                }
            }
        }
        
        return prefix;
    }
    
    /**
     * Find all words in a 2D grid that match words in dictionary
     * Word can be formed by adjacent cells (up, down, left, right)
     */
    public static List<String> wordSearch(char[][] board, Set<String> words) {
        List<String> results = new ArrayList<>();
        int rows = board.length;
        int cols = board[0].length;
        
        // Build trie from dictionary
        AutocompleteTrie trie = new AutocompleteTrie();
        for (String word : words) {
            trie.insert(word);
        }
        
        // DFS from each cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dfs(board, i, j, trie.root, "", results, new boolean[rows][cols]);
            }
        }
        
        return results;
    }
    
    private static void dfs(char[][] board, int row, int col, TrieNode node,
                           String current, List<String> results, boolean[][] visited) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length || 
            visited[row][col]) {
            return;
        }
        
        char c = board[row][col];
        if (!node.children.containsKey(c)) {
            return;
        }
        
        node = node.children.get(c);
        current += c;
        
        if (node.isEndOfWord && !results.contains(current)) {
            results.add(current);
        }
        
        visited[row][col] = true;
        
        // Explore neighbors
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        
        for (int i = 0; i < 4; i++) {
            dfs(board, row + dx[i], col + dy[i], node, current, results, visited);
        }
        
        visited[row][col] = false;
    }
    
    /**
     * Find all strings that match a pattern with wildcards
     * Pattern: "a*b" matches "acb", "aab", etc. (* matches any sequence)
     */
    public static List<String> patternMatch(String pattern, List<String> words) {
        List<String> results = new ArrayList<>();
        
        for (String word : words) {
            if (matchesPattern(pattern, word)) {
                results.add(word);
            }
        }
        
        return results;
    }
    
    private static boolean matchesPattern(String pattern, String word) {
        return matchesPatternHelper(pattern, 0, word, 0);
    }
    
    private static boolean matchesPatternHelper(String pattern, int pIdx, 
                                                String word, int wIdx) {
        if (pIdx == pattern.length() && wIdx == word.length()) {
            return true;
        }
        
        if (pIdx >= pattern.length() || wIdx > word.length()) {
            return false;
        }
        
        if (pattern.charAt(pIdx) == '*') {
            // * can match 0 or more characters
            return matchesPatternHelper(pattern, pIdx + 1, word, wIdx) ||
                   (wIdx < word.length() && 
                    matchesPatternHelper(pattern, pIdx, word, wIdx + 1));
        } else if (wIdx < word.length() && 
                   pattern.charAt(pIdx) == word.charAt(wIdx)) {
            return matchesPatternHelper(pattern, pIdx + 1, word, wIdx + 1);
        }
        
        return false;
    }
    
    public static void main(String[] args) {
        System.out.println("=== Search/Autocomplete ===");
        
        // Test 1: Basic autocomplete
        AutocompleteTrie trie = new AutocompleteTrie();
        trie.insert("apple");
        trie.insert("app");
        trie.insert("application");
        trie.insert("apply");
        trie.insert("apt");
        trie.insert("banana");
        trie.insert("band");
        
        System.out.println("\n1. Autocomplete for 'app':");
        System.out.println(trie.autocomplete("app"));
        
        System.out.println("\n2. Top 3 suggestions for 'app':");
        System.out.println(trie.autocompleteTopK("app", 3));
        
        System.out.println("\n3. Search 'apple': " + trie.search("apple"));
        System.out.println("   Starts with 'ban': " + trie.startsWith("ban"));
        
        // Test 2: Longest common prefix
        String[] words = {"flower", "flow", "flight"};
        System.out.println("\n4. Longest Common Prefix:");
        System.out.println("Words: " + Arrays.toString(words));
        System.out.println("LCP: " + longestCommonPrefix(words));
        
        // Test 3: Word search in 2D grid
        char[][] board = {
            {'o', 'a', 'a', 'n'},
            {'e', 't', 'a', 'e'},
            {'i', 'h', 'k', 'r'},
            {'i', 'f', 'l', 'v'}
        };
        Set<String> dictionary = new HashSet<>(Arrays.asList("oath", "pea", "eat", "rain"));
        System.out.println("\n5. Word Search in 2D Grid:");
        System.out.println("Found words: " + wordSearch(board, dictionary));
        
        // Test 4: Pattern matching
        List<String> wordList = Arrays.asList("apple", "application", "apt", "banana");
        System.out.println("\n6. Pattern Matching 'a*p':");
        System.out.println("Matches: " + patternMatch("a*p", wordList));
    }
}
