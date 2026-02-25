# Trees Learning Guide

## Table of Contents
1. [Tree Fundamentals](#tree-fundamentals)
2. [Binary Tree](#binary-tree)
3. [Binary Search Tree](#binary-search-tree)
4. [Tree Traversals](#tree-traversals)
5. [Common Tree Problems](#common-tree-problems)
6. [Advanced Tree Topics](#advanced-tree-topics)
7. [Practice Problems](#practice-problems)
8. [Code Examples](#code-examples)

---

## Tree Fundamentals

### What is a Tree?
A tree is a hierarchical data structure consisting of nodes connected by edges. Each tree has:
- **Root**: Topmost node
- **Parent/Child**: Relationship between nodes
- **Leaf**: Node with no children
- **Depth**: Distance from root
- **Height**: Maximum depth

### Tree Properties
- **Acyclic**: No cycles
- **Connected**: All nodes reachable
- **N nodes = N-1 edges**

### Tree Terminology
- **Ancestor**: Any node on path from root to current node
- **Descendant**: Any node in subtree of current node
- **Sibling**: Nodes with same parent
- **Subtree**: Tree formed by node and all descendants

---

## Binary Tree

### Definition
A binary tree is a tree where each node has at most 2 children (left and right).

### Types of Binary Trees

**1. Full Binary Tree**
- Every node has 0 or 2 children

**2. Complete Binary Tree**
- All levels filled except possibly last
- Last level filled left to right

**3. Perfect Binary Tree**
- All internal nodes have 2 children
- All leaves at same level

**4. Balanced Binary Tree**
- Height difference between left and right subtrees ≤ 1

### Binary Tree Node Structure

```python
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
```

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    
    TreeNode(int val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }
}
```

```cpp
struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    
    TreeNode(int val) : val(val), left(nullptr), right(nullptr) {}
};
```

---

## Binary Search Tree

### Definition
A Binary Search Tree (BST) is a binary tree where:
- Left subtree contains values < node
- Right subtree contains values > node
- Both subtrees are also BSTs

### BST Properties
- **In-order traversal** gives sorted sequence
- **Search**: O(log n) average, O(n) worst
- **Insert**: O(log n) average, O(n) worst
- **Delete**: O(log n) average, O(n) worst

### BST Operations

#### Search
```python
def search(root, val):
    if not root or root.val == val:
        return root
    if val < root.val:
        return search(root.left, val)
    return search(root.right, val)
```

```java
public TreeNode search(TreeNode root, int val) {
    if (root == null || root.val == val) {
        return root;
    }
    if (val < root.val) {
        return search(root.left, val);
    }
    return search(root.right, val);
}
```

#### Insert
```python
def insert(root, val):
    if not root:
        return TreeNode(val)
    if val < root.val:
        root.left = insert(root.left, val)
    else:
        root.right = insert(root.right, val)
    return root
```

```java
public TreeNode insert(TreeNode root, int val) {
    if (root == null) {
        return new TreeNode(val);
    }
    if (val < root.val) {
        root.left = insert(root.left, val);
    } else {
        root.right = insert(root.right, val);
    }
    return root;
}
```

#### Delete
```python
def deleteNode(root, key):
    if not root:
        return root
    
    if key < root.val:
        root.left = deleteNode(root.left, key)
    elif key > root.val:
        root.right = deleteNode(root.right, key)
    else:
        # Node to delete found
        if not root.left:
            return root.right
        elif not root.right:
            return root.left
        
        # Node with two children
        # Get inorder successor
        root.val = minValue(root.right)
        root.right = deleteNode(root.right, root.val)
    
    return root

def minValue(node):
    current = node
    while current.left:
        current = current.left
    return current.val
```

```java
public TreeNode deleteNode(TreeNode root, int key) {
    if (root == null) {
        return root;
    }
    
    if (key < root.val) {
        root.left = deleteNode(root.left, key);
    } else if (key > root.val) {
        root.right = deleteNode(root.right, key);
    } else {
        // Node to delete found
        if (root.left == null) {
            return root.right;
        } else if (root.right == null) {
            return root.left;
        }
        
        // Node with two children
        // Get inorder successor
        root.val = minValue(root.right);
        root.right = deleteNode(root.right, root.val);
    }
    
    return root;
}

private int minValue(TreeNode node) {
    TreeNode current = node;
    while (current.left != null) {
        current = current.left;
    }
    return current.val;
}
```

---

## Tree Traversals

### 1. Pre-order (Root → Left → Right)
```python
def preorder(root):
    if root:
        print(root.val)
        preorder(root.left)
        preorder(root.right)
```

```java
public void preorder(TreeNode root) {
    if (root != null) {
        System.out.println(root.val);
        preorder(root.left);
        preorder(root.right);
    }
}
```

**Iterative:**
```python
def preorderIterative(root):
    if not root:
        return []
    stack = [root]
    result = []
    while stack:
        node = stack.pop()
        result.append(node.val)
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)
    return result
```

```java
public List<Integer> preorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;
    
    Stack<TreeNode> stack = new Stack<>();
    stack.push(root);
    
    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        result.add(node.val);
        if (node.right != null) {
            stack.push(node.right);
        }
        if (node.left != null) {
            stack.push(node.left);
        }
    }
    return result;
}
```

### 2. In-order (Left → Root → Right)
```python
def inorder(root):
    if root:
        inorder(root.left)
        print(root.val)
        inorder(root.right)
```

```java
public void inorder(TreeNode root) {
    if (root != null) {
        inorder(root.left);
        System.out.println(root.val);
        inorder(root.right);
    }
}
```

**Iterative:**
```python
def inorderIterative(root):
    stack = []
    result = []
    current = root
    while stack or current:
        while current:
            stack.append(current)
            current = current.left
        current = stack.pop()
        result.append(current.val)
        current = current.right
    return result
```

```java
public List<Integer> inorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    Stack<TreeNode> stack = new Stack<>();
    TreeNode current = root;
    
    while (!stack.isEmpty() || current != null) {
        while (current != null) {
            stack.push(current);
            current = current.left;
        }
        current = stack.pop();
        result.add(current.val);
        current = current.right;
    }
    return result;
}
```

### 3. Post-order (Left → Right → Root)
```python
def postorder(root):
    if root:
        postorder(root.left)
        postorder(root.right)
        print(root.val)
```

```java
public void postorder(TreeNode root) {
    if (root != null) {
        postorder(root.left);
        postorder(root.right);
        System.out.println(root.val);
    }
}
```

**Iterative:**
```python
def postorderIterative(root):
    if not root:
        return []
    stack = [root]
    result = []
    while stack:
        node = stack.pop()
        result.append(node.val)
        if node.left:
            stack.append(node.left)
        if node.right:
            stack.append(node.right)
    return result[::-1]
```

```java
public List<Integer> postorderIterative(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) return result;
    
    Stack<TreeNode> stack = new Stack<>();
    stack.push(root);
    
    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        result.add(node.val);
        if (node.left != null) {
            stack.push(node.left);
        }
        if (node.right != null) {
            stack.push(node.right);
        }
    }
    Collections.reverse(result);
    return result;
}
```

### 4. Level-order (BFS)
```python
from collections import deque

def levelOrder(root):
    if not root:
        return []
    queue = deque([root])
    result = []
    while queue:
        level_size = len(queue)
        level = []
        for _ in range(level_size):
            node = queue.popleft()
            level.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        result.append(level)
    return result
```

```java
import java.util.*;

public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    
    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        List<Integer> level = new ArrayList<>();
        
        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.poll();
            level.add(node.val);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        result.add(level);
    }
    return result;
}
```

---

## Common Tree Problems

### 1. Maximum Depth
```python
def maxDepth(root):
    if not root:
        return 0
    return 1 + max(maxDepth(root.left), maxDepth(root.right))
```

```java
public int maxDepth(TreeNode root) {
    if (root == null) {
        return 0;
    }
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
}
```

### 2. Same Tree
```python
def isSameTree(p, q):
    if not p and not q:
        return True
    if not p or not q:
        return False
    return (p.val == q.val and 
            isSameTree(p.left, q.left) and 
            isSameTree(p.right, q.right))
```

```java
public boolean isSameTree(TreeNode p, TreeNode q) {
    if (p == null && q == null) {
        return true;
    }
    if (p == null || q == null) {
        return false;
    }
    return p.val == q.val && 
           isSameTree(p.left, q.left) && 
           isSameTree(p.right, q.right);
}
```

### 3. Invert Binary Tree
```python
def invertTree(root):
    if not root:
        return None
    root.left, root.right = root.right, root.left
    invertTree(root.left)
    invertTree(root.right)
    return root
```

```java
public TreeNode invertTree(TreeNode root) {
    if (root == null) {
        return null;
    }
    TreeNode temp = root.left;
    root.left = root.right;
    root.right = temp;
    invertTree(root.left);
    invertTree(root.right);
    return root;
}
```

### 4. Symmetric Tree
```python
def isSymmetric(root):
    if not root:
        return True
    return isMirror(root.left, root.right)

def isMirror(left, right):
    if not left and not right:
        return True
    if not left or not right:
        return False
    return (left.val == right.val and 
            isMirror(left.left, right.right) and 
            isMirror(left.right, right.left))
```

```java
public boolean isSymmetric(TreeNode root) {
    if (root == null) {
        return true;
    }
    return isMirror(root.left, root.right);
}

private boolean isMirror(TreeNode left, TreeNode right) {
    if (left == null && right == null) {
        return true;
    }
    if (left == null || right == null) {
        return false;
    }
    return left.val == right.val && 
           isMirror(left.left, right.right) && 
           isMirror(left.right, right.left);
}
```

### 5. Path Sum
```python
def hasPathSum(root, targetSum):
    if not root:
        return False
    if not root.left and not root.right:
        return root.val == targetSum
    return (hasPathSum(root.left, targetSum - root.val) or 
            hasPathSum(root.right, targetSum - root.val))
```

```java
public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) {
        return false;
    }
    if (root.left == null && root.right == null) {
        return root.val == targetSum;
    }
    return hasPathSum(root.left, targetSum - root.val) || 
           hasPathSum(root.right, targetSum - root.val);
}
```

### 6. Lowest Common Ancestor (BST)
```python
def lowestCommonAncestor(root, p, q):
    if p.val < root.val and q.val < root.val:
        return lowestCommonAncestor(root.left, p, q)
    if p.val > root.val and q.val > root.val:
        return lowestCommonAncestor(root.right, p, q)
    return root
```

```java
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (p.val < root.val && q.val < root.val) {
        return lowestCommonAncestor(root.left, p, q);
    }
    if (p.val > root.val && q.val > root.val) {
        return lowestCommonAncestor(root.right, p, q);
    }
    return root;
}
```

### 7. Validate BST
```python
def isValidBST(root):
    def validate(node, min_val, max_val):
        if not node:
            return True
        if node.val <= min_val or node.val >= max_val:
            return False
        return (validate(node.left, min_val, node.val) and 
                validate(node.right, node.val, max_val))
    
    return validate(root, float('-inf'), float('inf'))
```

```java
public boolean isValidBST(TreeNode root) {
    return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
}

private boolean validate(TreeNode node, long minVal, long maxVal) {
    if (node == null) {
        return true;
    }
    if (node.val <= minVal || node.val >= maxVal) {
        return false;
    }
    return validate(node.left, minVal, node.val) && 
           validate(node.right, node.val, maxVal);
}
```

### 8. Kth Smallest in BST
```python
def kthSmallest(root, k):
    stack = []
    current = root
    count = 0
    
    while stack or current:
        while current:
            stack.append(current)
            current = current.left
        current = stack.pop()
        count += 1
        if count == k:
            return current.val
        current = current.right
```

```java
public int kthSmallest(TreeNode root, int k) {
    Stack<TreeNode> stack = new Stack<>();
    TreeNode current = root;
    int count = 0;
    
    while (!stack.isEmpty() || current != null) {
        while (current != null) {
            stack.push(current);
            current = current.left;
        }
        current = stack.pop();
        count++;
        if (count == k) {
            return current.val;
        }
        current = current.right;
    }
    return -1;
}
```

### 9. Binary Tree Maximum Path Sum
```python
def maxPathSum(root):
    max_sum = float('-inf')
    
    def maxGain(node):
        nonlocal max_sum
        if not node:
            return 0
        
        left_gain = max(maxGain(node.left), 0)
        right_gain = max(maxGain(node.right), 0)
        
        current_path = node.val + left_gain + right_gain
        max_sum = max(max_sum, current_path)
        
        return node.val + max(left_gain, right_gain)
    
    maxGain(root)
    return max_sum
```

```java
class Solution {
    private int maxSum = Integer.MIN_VALUE;
    
    public int maxPathSum(TreeNode root) {
        maxGain(root);
        return maxSum;
    }
    
    private int maxGain(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);
        
        int currentPath = node.val + leftGain + rightGain;
        maxSum = Math.max(maxSum, currentPath);
        
        return node.val + Math.max(leftGain, rightGain);
    }
}
```

### 10. Construct Tree from Preorder and Inorder
```python
def buildTree(preorder, inorder):
    if not preorder or not inorder:
        return None
    
    root_val = preorder[0]
    root = TreeNode(root_val)
    root_index = inorder.index(root_val)
    
    root.left = buildTree(preorder[1:root_index+1], inorder[:root_index])
    root.right = buildTree(preorder[root_index+1:], inorder[root_index+1:])
    
    return root
```

```java
public TreeNode buildTree(int[] preorder, int[] inorder) {
    Map<Integer, Integer> inMap = new HashMap<>();
    for (int i = 0; i < inorder.length; i++) {
        inMap.put(inorder[i], i);
    }
    return buildTree(preorder, 0, preorder.length - 1, 
                     inorder, 0, inorder.length - 1, inMap);
}

private TreeNode buildTree(int[] preorder, int preStart, int preEnd,
                          int[] inorder, int inStart, int inEnd,
                          Map<Integer, Integer> inMap) {
    if (preStart > preEnd || inStart > inEnd) {
        return null;
    }
    
    TreeNode root = new TreeNode(preorder[preStart]);
    int rootIndex = inMap.get(root.val);
    int numsLeft = rootIndex - inStart;
    
    root.left = buildTree(preorder, preStart + 1, preStart + numsLeft,
                         inorder, inStart, rootIndex - 1, inMap);
    root.right = buildTree(preorder, preStart + numsLeft + 1, preEnd,
                          inorder, rootIndex + 1, inEnd, inMap);
    
    return root;
}
```

---

## Advanced Tree Topics

### 1. Segment Tree
Used for range queries and updates.

```python
class SegmentTree:
    def __init__(self, arr):
        self.n = len(arr)
        self.size = 1
        while self.size < self.n:
            self.size *= 2
        self.tree = [0] * (2 * self.size)
        self.build(arr)
    
    def build(self, arr):
        for i in range(self.n):
            self.tree[self.size + i] = arr[i]
        for i in range(self.size - 1, 0, -1):
            self.tree[i] = self.tree[2*i] + self.tree[2*i+1]
    
    def update(self, index, value):
        index += self.size
        self.tree[index] = value
        while index > 1:
            index //= 2
            self.tree[index] = self.tree[2*index] + self.tree[2*index+1]
    
    def query(self, l, r):
        l += self.size
        r += self.size
        res = 0
        while l < r:
            if l % 2 == 1:
                res += self.tree[l]
                l += 1
            if r % 2 == 1:
                r -= 1
                res += self.tree[r]
            l //= 2
            r //= 2
        return res
```

### 2. Trie (Prefix Tree)
```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True
    
    def search(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.is_end
    
    def startsWith(self, prefix):
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        return True
```

### 3. AVL Tree (Self-Balancing)
```python
class AVLNode:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None
        self.height = 1

def getHeight(node):
    if not node:
        return 0
    return node.height

def getBalance(node):
    if not node:
        return 0
    return getHeight(node.left) - getHeight(node.right)

def rotateRight(y):
    x = y.left
    T2 = x.right
    
    x.right = y
    y.left = T2
    
    y.height = 1 + max(getHeight(y.left), getHeight(y.right))
    x.height = 1 + max(getHeight(x.left), getHeight(x.right))
    
    return x

def rotateLeft(x):
    y = x.right
    T2 = y.left
    
    y.left = x
    x.right = T2
    
    x.height = 1 + max(getHeight(x.left), getHeight(x.right))
    y.height = 1 + max(getHeight(y.left), getHeight(y.right))
    
    return y
```

---

## Practice Problems

### Easy
1. Maximum Depth of Binary Tree
2. Same Tree
3. Invert Binary Tree
4. Symmetric Tree
5. Path Sum
6. Minimum Depth of Binary Tree
7. Balanced Binary Tree
8. Diameter of Binary Tree

### Medium
9. Binary Tree Level Order Traversal
10. Construct Binary Tree from Preorder and Inorder
11. Validate Binary Search Tree
12. Kth Smallest Element in BST
13. Lowest Common Ancestor of BST
14. Binary Tree Maximum Path Sum
15. Serialize and Deserialize Binary Tree
16. Count Complete Tree Nodes
17. Flatten Binary Tree to Linked List
18. Binary Tree Right Side View

### Hard
19. Binary Tree Postorder Traversal (Iterative)
20. Recover Binary Search Tree
21. Binary Tree Zigzag Level Order
22. Binary Tree Maximum Path Sum (with negative values)
23. Serialize and Deserialize N-ary Tree
24. Count Univalue Subtrees

---

## Code Examples

### Complete Binary Tree Implementation

```python
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class BinaryTree:
    def __init__(self):
        self.root = None
    
    def insert(self, val):
        if not self.root:
            self.root = TreeNode(val)
            return
        
        queue = [self.root]
        while queue:
            node = queue.pop(0)
            if not node.left:
                node.left = TreeNode(val)
                return
            elif not node.right:
                node.right = TreeNode(val)
                return
            else:
                queue.append(node.left)
                queue.append(node.right)
    
    def printTree(self, traversal='inorder'):
        if traversal == 'inorder':
            self._inorder(self.root)
        elif traversal == 'preorder':
            self._preorder(self.root)
        elif traversal == 'postorder':
            self._postorder(self.root)
        elif traversal == 'levelorder':
            self._levelorder(self.root)
        print()
    
    def _inorder(self, root):
        if root:
            self._inorder(root.left)
            print(root.val, end=' ')
            self._inorder(root.right)
    
    def _preorder(self, root):
        if root:
            print(root.val, end=' ')
            self._preorder(root.left)
            self._preorder(root.right)
    
    def _postorder(self, root):
        if root:
            self._postorder(root.left)
            self._postorder(root.right)
            print(root.val, end=' ')
    
    def _levelorder(self, root):
        if not root:
            return
        queue = [root]
        while queue:
            node = queue.pop(0)
            print(node.val, end=' ')
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
```

### BST Implementation

```python
class BST:
    def __init__(self):
        self.root = None
    
    def insert(self, val):
        self.root = self._insert(self.root, val)
    
    def _insert(self, root, val):
        if not root:
            return TreeNode(val)
        if val < root.val:
            root.left = self._insert(root.left, val)
        else:
            root.right = self._insert(root.right, val)
        return root
    
    def search(self, val):
        return self._search(self.root, val)
    
    def _search(self, root, val):
        if not root or root.val == val:
            return root
        if val < root.val:
            return self._search(root.left, val)
        return self._search(root.right, val)
    
    def delete(self, val):
        self.root = self._delete(self.root, val)
    
    def _delete(self, root, val):
        if not root:
            return root
        if val < root.val:
            root.left = self._delete(root.left, val)
        elif val > root.val:
            root.right = self._delete(root.right, val)
        else:
            if not root.left:
                return root.right
            elif not root.right:
                return root.left
            root.val = self._minValue(root.right)
            root.right = self._delete(root.right, root.val)
        return root
    
    def _minValue(self, root):
        current = root
        while current.left:
            current = current.left
        return current.val
```

---

## Learning Strategy

### Week 1: Fundamentals
- Understand tree structure
- Learn basic traversals
- Practice recursive thinking

### Week 2: Binary Trees
- Solve basic binary tree problems
- Master DFS and BFS
- Understand tree properties

### Week 3: Binary Search Trees
- Learn BST operations
- Practice validation problems
- Understand BST properties

### Week 4: Advanced Topics
- Complex tree problems
- Tree construction
- Path and sum problems

---

## Common Patterns

1. **Recursive DFS**: Most tree problems use recursion
2. **Iterative BFS**: Level-order problems
3. **Two Trees**: Compare or merge operations
4. **Path Problems**: Track path from root to leaf
5. **Property Validation**: Check tree properties
6. **Construction**: Build tree from array/string

---

## Tips for Interviews

1. **Always clarify**: Empty tree, single node, duplicates
2. **Think recursively**: Natural for trees
3. **Consider iterative**: Sometimes required or more efficient
4. **Handle edge cases**: Null nodes, single child
5. **Optimize**: Consider time and space complexity
6. **Test**: Walk through examples

---

## Resources

- LeetCode Tree Tag
- Binary Tree Visualizer
- Tree Traversal Animations
- Practice: 50+ tree problems recommended
