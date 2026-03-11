import java.util.*;
import java.util.concurrent.*;

class Problem3 {

    class DNSEntry {
        String domain;
        String ip;
        long expiryTime;

        DNSEntry(String d, String i, long ttl) {
            domain = d;
            ip = i;
            expiryTime = System.currentTimeMillis() + ttl * 1000;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private int capacity;
    private Map<String, DNSEntry> cache;
    private long hits = 0;
    private long misses = 0;

    public Problem3(int cap) {
        capacity = cap;
        cache = new LinkedHashMap<>(cap, 0.75f, true);
        startCleaner();
    }

    public synchronized String resolve(String domain) {
        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT → " + entry.ip;
        }

        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
        }

        misses++;
        String ip = queryUpstream(domain);
        putEntry(domain, ip, 5);
        return "Cache MISS → Query upstream → " + ip;
    }

    private void putEntry(String domain, String ip, long ttl) {
        if (cache.size() >= capacity) {
            String lruKey = cache.keySet().iterator().next();
            cache.remove(lruKey);
        }
        cache.put(domain, new DNSEntry(domain, ip, ttl));
    }

    private String queryUpstream(String domain) {
        return "172.217.14." + new Random().nextInt(255);
    }

    public synchronized String getCacheStats() {
        long total = hits + misses;
        double rate = total == 0 ? 0 : (hits * 100.0) / total;
        return "Hit Rate: " + rate + "%";
    }

    private void startCleaner() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                synchronized (this) {
                    Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
                    while (it.hasNext()) {
                        if (it.next().getValue().isExpired()) {
                            it.remove();
                        }
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static void main(String[] args) throws Exception {
        Problem3 dns = new Problem3(3);

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        Thread.sleep(6000);

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.getCacheStats());
    }
}