package org.example.preffix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrefixTree {

    public static void main(String[] args) {
        Trie t1 = new Trie();
        t1.insert("vin");
        t1.insert("vinay");
        t1.insert("vinu");
System.out.println(t1.getPrefix("vi"));
    }
}


class TrieNode {
  Map<Character, TrieNode> childern = new HashMap<>();
  boolean isEndOfChar= false;
}


class Trie {

    TrieNode root;

    Trie(){
        root = new TrieNode();
    }


    public void insert(String word){
        if(word == null || word.isEmpty()){
            return;
        }
        TrieNode current = root;
        for(Character c: word.toCharArray()){
           current= current.childern.computeIfAbsent(c, k-> new TrieNode());

        }
        current.isEndOfChar = true;
    }

    public List<String> getPrefix(String word){
        List<String> res = new ArrayList<>();
        if(word == null || word.isEmpty()){
            return res;
        }
        TrieNode current = root;
        for(Character c: word.toCharArray()){
            if(current.childern.get(c)==null){
                return res;
            }
            current = current.childern.get(c);
        }

        dfs(current, word, res);
        return res;
    }
    /*
    * {    ,v,}
    *     {,i,}
    *     {,n,}
    *
    * */
    public void dfs(TrieNode node , String word , List<String> res){
        if(node.isEndOfChar){
            res.add(word);
        }
        node.childern.forEach((k,v)->{
            dfs(node.childern.get(k), word+k,res);
        });
    }


}