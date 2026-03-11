import java.util.*;

class Problem10 {

    class VideoData {
        String id;
        String content;
        VideoData(String id, String content) {
            this.id = id;
            this.content = content;
        }
    }

    private LinkedHashMap<String, VideoData> L1;
    private LinkedHashMap<String, VideoData> L2;
    private HashMap<String, VideoData> L3 = new HashMap<>();
    private HashMap<String, Integer> accessCount = new HashMap<>();

    private int L1_CAP = 10000;
    private int L2_CAP = 100000;
    private int promoteThreshold = 3;

    private long l1Hits = 0, l2Hits = 0, l3Hits = 0, totalReq = 0;

    public Problem10() {

        L1 = new LinkedHashMap<>(L1_CAP, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> e) {
                return size() > L1_CAP;
            }
        };

        L2 = new LinkedHashMap<>(L2_CAP, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> e) {
                return size() > L2_CAP;
            }
        };

        for (int i = 1; i <= 200000; i++) {
            L3.put("video_" + i, new VideoData("video_" + i, "content_" + i));
        }
    }

    public String getVideo(String id) {

        totalReq++;

        if (L1.containsKey(id)) {
            l1Hits++;
            accessCount.merge(id, 1, Integer::sum);
            return "L1 HIT";
        }

        if (L2.containsKey(id)) {
            l2Hits++;
            accessCount.merge(id, 1, Integer::sum);
            promoteToL1(id);
            return "L2 HIT → promoted to L1";
        }

        if (L3.containsKey(id)) {
            l3Hits++;
            accessCount.put(id, 1);
            L2.put(id, L3.get(id));
            return "L3 HIT → added to L2";
        }

        return "Video not found";
    }

    private void promoteToL1(String id) {
        int count = accessCount.getOrDefault(id, 0);
        if (count >= promoteThreshold) {
            L1.put(id, L2.get(id));
        }
    }

    public void invalidate(String id) {
        L1.remove(id);
        L2.remove(id);
        L3.remove(id);
        accessCount.remove(id);
    }

    public String getStatistics() {

        double l1Rate = totalReq == 0 ? 0 : (l1Hits * 100.0) / totalReq;
        double l2Rate = totalReq == 0 ? 0 : (l2Hits * 100.0) / totalReq;
        double l3Rate = totalReq == 0 ? 0 : (l3Hits * 100.0) / totalReq;

        return "L1 Hit Rate: " + String.format("%.2f", l1Rate) +
                "%, L2 Hit Rate: " + String.format("%.2f", l2Rate) +
                "%, L3 Hit Rate: " + String.format("%.2f", l3Rate);
    }

    public static void main(String[] args) {

        Problem10 cache = new Problem10();

        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_999"));
        System.out.println(cache.getStatistics());
    }
}