# Graphs Learning Guide

## Table of Contents
1. [Graph Fundamentals](#graph-fundamentals)
2. [Graph Representation](#graph-representation)
3. [Graph Traversal](#graph-traversal)
4. [Shortest Path Algorithms](#shortest-path-algorithms)
5. [Minimum Spanning Tree](#minimum-spanning-tree)
6. [Topological Sort](#topological-sort)
7. [Union-Find](#union-find)
8. [Common Graph Problems](#common-graph-problems)
9. [Practice Problems](#practice-problems)
10. [Code Examples](#code-examples)

---

## Graph Fundamentals

### What is a Graph?
A graph is a data structure consisting of:
- **Vertices (Nodes)**: Points in the graph
- **Edges**: Connections between vertices
- **Directed/Undirected**: Edges may have direction
- **Weighted/Unweighted**: Edges may have weights

### Graph Types

**1. Directed Graph (Digraph)**
- Edges have direction
- (u, v) ≠ (v, u)

**2. Undirected Graph**
- Edges have no direction
- (u, v) = (v, u)

**3. Weighted Graph**
- Edges have associated weights/costs

**4. Cyclic/Acyclic**
- Cyclic: Contains cycles
- Acyclic: No cycles (DAG - Directed Acyclic Graph)

### Graph Terminology
- **Path**: Sequence of vertices connected by edges
- **Cycle**: Path that starts and ends at same vertex
- **Connected**: All vertices reachable from any vertex
- **Degree**: Number of edges incident to vertex
- **In-degree/Out-degree**: For directed graphs

---

## Graph Representation

### 1. Adjacency List (Most Common)

**Java Implementation:**
```java
import java.util.*;

class Graph {
    private int V;
    private List<List<Integer>> adj;
    
    public Graph(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }
    
    public void addEdge(int u, int v) {
        adj.get(u).add(v);
        // For undirected graph, also add: adj.get(v).add(u);
    }
    
    public List<Integer> getNeighbors(int v) {
        return adj.get(v);
    }
    
    public int getV() {
        return V;
    }
}

// Weighted Graph
class WeightedGraph {
    private int V;
    private List<List<int[]>> adj; // [neighbor, weight]
    
    public WeightedGraph(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }
    
    public void addEdge(int u, int v, int weight) {
        adj.get(u).add(new int[]{v, weight});
    }
    
    public List<int[]> getNeighbors(int v) {
        return adj.get(v);
    }
}
```

### 2. Adjacency Matrix

```java
class GraphMatrix {
    private int V;
    private int[][] adjMatrix;
    
    public GraphMatrix(int V) {
        this.V = V;
        adjMatrix = new int[V][V];
    }
    
    public void addEdge(int u, int v) {
        adjMatrix[u][v] = 1;
        // For undirected: adjMatrix[v][u] = 1;
    }
    
    public void addWeightedEdge(int u, int v, int weight) {
        adjMatrix[u][v] = weight;
    }
    
    public boolean hasEdge(int u, int v) {
        return adjMatrix[u][v] != 0;
    }
}
```

### 3. Edge List

```java
class EdgeListGraph {
    private List<int[]> edges; // [u, v, weight]
    
    public EdgeListGraph() {
        edges = new ArrayList<>();
    }
    
    public void addEdge(int u, int v, int weight) {
        edges.add(new int[]{u, v, weight});
    }
}
```

---

## Graph Traversal

### 1. Depth-First Search (DFS)

**Recursive DFS:**
```java
class DFS {
    private boolean[] visited;
    private Graph graph;
    private List<Integer> result;
    
    public DFS(Graph graph) {
        this.graph = graph;
        this.visited = new boolean[graph.getV()];
        this.result = new ArrayList<>();
    }
    
    public List<Integer> dfs(int start) {
        result.clear();
        Arrays.fill(visited, false);
        dfsHelper(start);
        return result;
    }
    
    private void dfsHelper(int v) {
        visited[v] = true;
        result.add(v);
        
        for (int neighbor : graph.getNeighbors(v)) {
            if (!visited[neighbor]) {
                dfsHelper(neighbor);
            }
        }
    }
    
    // DFS for all components
    public List<Integer> dfsAll() {
        result.clear();
        Arrays.fill(visited, false);
        for (int i = 0; i < graph.getV(); i++) {
            if (!visited[i]) {
                dfsHelper(i);
            }
        }
        return result;
    }
}
```

**Iterative DFS:**
```java
public List<Integer> dfsIterative(int start) {
    List<Integer> result = new ArrayList<>();
    boolean[] visited = new boolean[graph.getV()];
    Stack<Integer> stack = new Stack<>();
    
    stack.push(start);
    visited[start] = true;
    
    while (!stack.isEmpty()) {
        int v = stack.pop();
        result.add(v);
        
        for (int neighbor : graph.getNeighbors(v)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                stack.push(neighbor);
            }
        }
    }
    
    return result;
}
```

### 2. Breadth-First Search (BFS)

```java
import java.util.*;

class BFS {
    private Graph graph;
    
    public BFS(Graph graph) {
        this.graph = graph;
    }
    
    public List<Integer> bfs(int start) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[graph.getV()];
        Queue<Integer> queue = new LinkedList<>();
        
        queue.offer(start);
        visited[start] = true;
        
        while (!queue.isEmpty()) {
            int v = queue.poll();
            result.add(v);
            
            for (int neighbor : graph.getNeighbors(v)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        
        return result;
    }
    
    // BFS with level information
    public List<List<Integer>> bfsLevels(int start) {
        List<List<Integer>> levels = new ArrayList<>();
        boolean[] visited = new boolean[graph.getV()];
        Queue<Integer> queue = new LinkedList<>();
        
        queue.offer(start);
        visited[start] = true;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>();
            
            for (int i = 0; i < levelSize; i++) {
                int v = queue.poll();
                level.add(v);
                
                for (int neighbor : graph.getNeighbors(v)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
            levels.add(level);
        }
        
        return levels;
    }
}
```

---

## Shortest Path Algorithms

### 1. BFS for Unweighted Graphs

```java
public int shortestPathBFS(int start, int end) {
    boolean[] visited = new boolean[graph.getV()];
    int[] distance = new int[graph.getV()];
    Arrays.fill(distance, -1);
    Queue<Integer> queue = new LinkedList<>();
    
    queue.offer(start);
    visited[start] = true;
    distance[start] = 0;
    
    while (!queue.isEmpty()) {
        int v = queue.poll();
        
        if (v == end) {
            return distance[v];
        }
        
        for (int neighbor : graph.getNeighbors(v)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                distance[neighbor] = distance[v] + 1;
                queue.offer(neighbor);
            }
        }
    }
    
    return -1; // No path found
}
```

### 2. Dijkstra's Algorithm

```java
import java.util.*;

class Dijkstra {
    private WeightedGraph graph;
    
    public Dijkstra(WeightedGraph graph) {
        this.graph = graph;
    }
    
    public int[] shortestPath(int start) {
        int V = graph.getV();
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{start, 0});
        boolean[] visited = new boolean[V];
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (int[] neighbor : graph.getNeighbors(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];
                
                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }
        
        return dist;
    }
    
    // Get shortest path to specific node
    public List<Integer> getPath(int start, int end) {
        int V = graph.getV();
        int[] dist = new int[V];
        int[] parent = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[start] = 0;
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{start, 0});
        boolean[] visited = new boolean[V];
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            
            if (visited[u]) continue;
            visited[u] = true;
            
            if (u == end) break;
            
            for (int[] neighbor : graph.getNeighbors(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];
                
                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }
        
        // Reconstruct path
        List<Integer> path = new ArrayList<>();
        if (parent[end] == -1 && start != end) {
            return path; // No path
        }
        
        int current = end;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        Collections.reverse(path);
        return path;
    }
}
```

### 3. Bellman-Ford Algorithm

```java
class BellmanFord {
    private List<int[]> edges; // [u, v, weight]
    private int V;
    
    public BellmanFord(int V, List<int[]> edges) {
        this.V = V;
        this.edges = edges;
    }
    
    public int[] shortestPath(int start) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        
        // Relax edges V-1 times
        for (int i = 0; i < V - 1; i++) {
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], weight = edge[2];
                if (dist[u] != Integer.MAX_VALUE && 
                    dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                }
            }
        }
        
        // Check for negative cycles
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], weight = edge[2];
            if (dist[u] != Integer.MAX_VALUE && 
                dist[u] + weight < dist[v]) {
                // Negative cycle detected
                return null;
            }
        }
        
        return dist;
    }
}
```

### 4. Floyd-Warshall Algorithm

```java
class FloydWarshall {
    public int[][] allPairsShortestPath(int[][] graph) {
        int V = graph.length;
        int[][] dist = new int[V][V];
        
        // Initialize distance matrix
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else if (graph[i][j] != 0) {
                    dist[i][j] = graph[i][j];
                } else {
                    dist[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        
        // Floyd-Warshall algorithm
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && 
                        dist[k][j] != Integer.MAX_VALUE &&
                        dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        
        return dist;
    }
}
```

---

## Minimum Spanning Tree

### 1. Kruskal's Algorithm

```java
import java.util.*;

class Kruskal {
    class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }
        
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) return false;
            
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }
    
    public List<int[]> mst(int V, List<int[]> edges) {
        // edges: [u, v, weight]
        Collections.sort(edges, (a, b) -> a[2] - b[2]);
        
        UnionFind uf = new UnionFind(V);
        List<int[]> mst = new ArrayList<>();
        
        for (int[] edge : edges) {
            if (uf.union(edge[0], edge[1])) {
                mst.add(edge);
                if (mst.size() == V - 1) break;
            }
        }
        
        return mst;
    }
}
```

### 2. Prim's Algorithm

```java
class Prim {
    public List<int[]> mst(WeightedGraph graph, int start) {
        int V = graph.getV();
        boolean[] inMST = new boolean[V];
        int[] key = new int[V];
        int[] parent = new int[V];
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        key[start] = 0;
        pq.offer(new int[]{start, 0});
        
        List<int[]> mst = new ArrayList<>();
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            
            if (inMST[u]) continue;
            inMST[u] = true;
            
            if (parent[u] != -1) {
                mst.add(new int[]{parent[u], u, key[u]});
            }
            
            for (int[] neighbor : graph.getNeighbors(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];
                
                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    parent[v] = u;
                    pq.offer(new int[]{v, key[v]});
                }
            }
        }
        
        return mst;
    }
}
```

---

## Topological Sort

### Kahn's Algorithm (BFS)

```java
class TopologicalSort {
    public List<Integer> topologicalSort(Graph graph) {
        int V = graph.getV();
        int[] inDegree = new int[V];
        
        // Calculate in-degrees
        for (int i = 0; i < V; i++) {
            for (int neighbor : graph.getNeighbors(i)) {
                inDegree[neighbor]++;
            }
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);
            
            for (int neighbor : graph.getNeighbors(u)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // Check for cycle
        if (result.size() != V) {
            return new ArrayList<>(); // Cycle exists
        }
        
        return result;
    }
}
```

### DFS-based Topological Sort

```java
class TopologicalSortDFS {
    public List<Integer> topologicalSort(Graph graph) {
        int V = graph.getV();
        boolean[] visited = new boolean[V];
        boolean[] recStack = new boolean[V];
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                if (dfs(i, graph, visited, recStack, stack)) {
                    return new ArrayList<>(); // Cycle detected
                }
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }
    
    private boolean dfs(int v, Graph graph, boolean[] visited, 
                       boolean[] recStack, Stack<Integer> stack) {
        visited[v] = true;
        recStack[v] = true;
        
        for (int neighbor : graph.getNeighbors(v)) {
            if (!visited[neighbor]) {
                if (dfs(neighbor, graph, visited, recStack, stack)) {
                    return true; // Cycle detected
                }
            } else if (recStack[neighbor]) {
                return true; // Cycle detected
            }
        }
        
        recStack[v] = false;
        stack.push(v);
        return false;
    }
}
```

---

## Union-Find

```java
class UnionFind {
    private int[] parent;
    private int[] rank;
    private int count; // Number of components
    
    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }
    
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return false; // Already connected
        }
        
        // Union by rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        count--;
        return true;
    }
    
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }
    
    public int getCount() {
        return count;
    }
}
```

---

## Common Graph Problems

### 1. Number of Islands

```java
class NumberOfIslands {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    dfs(grid, i, j);
                    count++;
                }
            }
        }
        
        return count;
    }
    
    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || 
            grid[i][j] == '0') {
            return;
        }
        
        grid[i][j] = '0'; // Mark as visited
        
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
}
```

### 2. Clone Graph

```java
import java.util.*;

class Node {
    public int val;
    public List<Node> neighbors;
    
    public Node(int val) {
        this.val = val;
        this.neighbors = new ArrayList<>();
    }
}

class CloneGraph {
    private Map<Node, Node> visited = new HashMap<>();
    
    public Node cloneGraph(Node node) {
        if (node == null) return null;
        
        if (visited.containsKey(node)) {
            return visited.get(node);
        }
        
        Node clone = new Node(node.val);
        visited.put(node, clone);
        
        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(cloneGraph(neighbor));
        }
        
        return clone;
    }
}
```

### 3. Course Schedule

```java
class CourseSchedule {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        int[] inDegree = new int[numCourses];
        for (int[] prereq : prerequisites) {
            graph.get(prereq[1]).add(prereq[0]);
            inDegree[prereq[0]]++;
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int count = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            count++;
            
            for (int neighbor : graph.get(course)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return count == numCourses;
    }
}
```

### 4. Word Ladder

```java
class WordLadder {
    public int ladderLength(String beginWord, String endWord, 
                           List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;
        
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);
        
        int level = 1;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            
            for (int i = 0; i < size; i++) {
                String current = queue.poll();
                
                if (current.equals(endWord)) {
                    return level;
                }
                
                char[] chars = current.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    char original = chars[j];
                    
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == original) continue;
                        
                        chars[j] = c;
                        String newWord = new String(chars);
                        
                        if (wordSet.contains(newWord) && 
                            !visited.contains(newWord)) {
                            visited.add(newWord);
                            queue.offer(newWord);
                        }
                    }
                    chars[j] = original;
                }
            }
            level++;
        }
        
        return 0;
    }
}
```

### 5. Network Delay Time

```java
class NetworkDelayTime {
    public int networkDelayTime(int[][] times, int n, int k) {
        // Build graph
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] time : times) {
            graph.get(time[0]).add(new int[]{time[1], time[2]});
        }
        
        // Dijkstra's algorithm
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;
        
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{k, 0});
        boolean[] visited = new boolean[n + 1];
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (int[] neighbor : graph.get(u)) {
                int v = neighbor[0];
                int weight = neighbor[1];
                
                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }
        
        int maxTime = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                return -1;
            }
            maxTime = Math.max(maxTime, dist[i]);
        }
        
        return maxTime;
    }
}
```

---

## Practice Problems

### Easy
1. Number of Islands
2. Clone Graph
3. Find the Town Judge
4. Find Center of Star Graph
5. All Paths From Source to Target

### Medium
6. Course Schedule
7. Course Schedule II
8. Word Ladder
9. Network Delay Time
10. Cheapest Flights Within K Stops
11. Redundant Connection
12. Accounts Merge
13. Pacific Atlantic Water Flow
14. Rotting Oranges
15. Surrounded Regions

### Hard
16. Critical Connections
17. Alien Dictionary
18. Reconstruct Itinerary
19. Longest Increasing Path in Matrix
20. Minimum Height Trees

---

## Learning Strategy

### Week 1: Fundamentals
- Graph representation
- DFS and BFS
- Basic graph problems

### Week 2: Shortest Path
- BFS for unweighted
- Dijkstra's algorithm
- Bellman-Ford

### Week 3: Advanced Algorithms
- Minimum Spanning Tree
- Topological Sort
- Union-Find

### Week 4: Complex Problems
- Advanced graph problems
- Cycle detection
- Strongly Connected Components

---

## Tips for Interviews

1. **Choose Representation**: Adjacency list vs matrix
2. **Traversal Choice**: DFS vs BFS
3. **Cycle Detection**: Use visited + recStack for directed
4. **Shortest Path**: BFS for unweighted, Dijkstra for weighted
5. **Optimization**: Consider time/space tradeoffs
6. **Edge Cases**: Empty graph, single node, disconnected

---

## Resources

- LeetCode Graph Tag
- Graph Algorithm Visualizations
- Practice: 50+ graph problems recommended
