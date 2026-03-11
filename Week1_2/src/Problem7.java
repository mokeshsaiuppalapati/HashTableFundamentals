import java.util.*;
import java.util.concurrent.*;

class Problem7 {

    class TrieNode {
        HashMap<Character, TrieNode> children = new HashMap<>();
        boolean isEnd;
    }

    private TrieNode root = new TrieNode();
    private ConcurrentHashMap<String, Integer> freqMap = new ConcurrentHashMap<>();

    public void addQuery(String query) {
        freqMap.merge(query, 1, Integer::sum);
        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEnd = true;
    }

    public List<String> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return typoSuggestions(prefix);
        }

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> freqMap.get(a) - freqMap.get(b)
        );

        dfs(node, new StringBuilder(prefix), pq);

        List<String> res = new ArrayList<>();
        while (!pq.isEmpty()) res.add(0, pq.poll());
        return res;
    }

    private void dfs(TrieNode node, StringBuilder sb, PriorityQueue<String> pq) {
        if (node.isEnd) {
            pq.offer(sb.toString());
            if (pq.size() > 10) pq.poll();
        }
        for (Map.Entry<Character, TrieNode> e : node.children.entrySet()) {
            sb.append(e.getKey());
            dfs(e.getValue(), sb, pq);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    private List<String> typoSuggestions(String word) {
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> freqMap.get(a) - freqMap.get(b)
        );

        for (String q : freqMap.keySet()) {
            if (editDistance(q, word) <= 1) {
                pq.offer(q);
                if (pq.size() > 10) pq.poll();
            }
        }

        List<String> res = new ArrayList<>();
        while (!pq.isEmpty()) res.add(0, pq.poll());
        return res;
    }

    private int editDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        }
        return dp[a.length()][b.length()];
    }

    public static void main(String[] args) {

        Problem7 ac = new Problem7();

        ac.addQuery("java tutorial");
        ac.addQuery("javascript");
        ac.addQuery("java download");
        ac.addQuery("java tutorial");
        ac.addQuery("java tutorial");
        ac.addQuery("java 21 features");

        System.out.println(ac.search("jav"));
    }
}