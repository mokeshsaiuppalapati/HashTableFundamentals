import java.util.*;

class Problem4 {

    private int n = 5;
    private HashMap<String, Set<String>> index = new HashMap<>();
    private HashMap<String, List<String>> docNgrams = new HashMap<>();

    public void addDocument(String docId, String text) {
        List<String> grams = generateNgrams(text);
        docNgrams.put(docId, grams);
        for (String g : grams) {
            index.computeIfAbsent(g, k -> new HashSet<>()).add(docId);
        }
    }

    public void analyzeDocument(String docId) {
        List<String> grams = docNgrams.get(docId);
        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String g : grams) {
            Set<String> docs = index.get(g);
            if (docs != null) {
                for (String d : docs) {
                    if (!d.equals(docId)) {
                        matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> e : matchCount.entrySet()) {
            String other = e.getKey();
            int matches = e.getValue();
            int total = grams.size();
            double similarity = (matches * 100.0) / total;
            System.out.println("Found " + matches + " matching n-grams with \"" + other + "\"");
            System.out.println("Similarity: " + similarity + "%");
        }
    }

    private List<String> generateNgrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();
        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]).append(" ");
            }
            grams.add(sb.toString().trim());
        }
        return grams;
    }

    public static void main(String[] args) {

        Problem4 pd = new Problem4();

        String essay1 = "data structures and algorithms are important for computer science students to learn hashing and trees";
        String essay2 = "students must learn data structures and algorithms because hashing and trees are fundamental topics";
        String essay3 = "machine learning and artificial intelligence are modern computing fields with many applications";

        pd.addDocument("essay_089.txt", essay1);
        pd.addDocument("essay_092.txt", essay2);
        pd.addDocument("essay_123.txt", essay3);

        pd.analyzeDocument("essay_123.txt");
    }
}