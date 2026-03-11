import java.util.*;
import java.util.concurrent.*;

class Problem5 {

    private ConcurrentHashMap<String, Integer> pageViews = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Set<String>> uniqueVisitors = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> sourceCounts = new ConcurrentHashMap<>();

    public void processEvent(String url, String userId, String source) {
        pageViews.merge(url, 1, Integer::sum);
        uniqueVisitors.computeIfAbsent(url, k -> ConcurrentHashMap.newKeySet()).add(userId);
        sourceCounts.merge(source, 1, Integer::sum);
    }

    public List<String> getTopPages() {
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<String, Integer> e : pageViews.entrySet()) {
            pq.offer(e);
            if (pq.size() > 10) pq.poll();
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            Map.Entry<String, Integer> e = pq.poll();
            int unique = uniqueVisitors.getOrDefault(e.getKey(), new HashSet<>()).size();
            result.add(0, e.getKey() + " - " + e.getValue() + " views (" + unique + " unique)");
        }
        return result;
    }

    public Map<String, Integer> getSourceStats() {
        return new HashMap<>(sourceCounts);
    }

    public void startDashboard() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                }
                System.out.println("Top Pages:");
                for (String s : getTopPages()) {
                    System.out.println(s);
                }
                System.out.println("Traffic Sources: " + getSourceStats());
                System.out.println("----------------------");
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static void main(String[] args) throws Exception {

        Problem5 analytics = new Problem5();
        analytics.startDashboard();

        analytics.processEvent("/article/breaking-news", "user_123", "google");
        analytics.processEvent("/article/breaking-news", "user_456", "facebook");
        analytics.processEvent("/sports/championship", "user_999", "direct");
        analytics.processEvent("/article/breaking-news", "user_123", "google");

        Thread.sleep(15000);
    }
}