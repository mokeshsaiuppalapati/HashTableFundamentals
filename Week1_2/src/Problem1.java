import java.util.*;
import java.util.concurrent.*;

class Problem1 {

    private ConcurrentHashMap<String, Integer> usernameMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> attemptFrequency = new ConcurrentHashMap<>();

    public Problem1() {
        usernameMap.put("john_doe", 1);
        usernameMap.put("admin", 2);
        usernameMap.put("player1", 3);
    }

    public boolean checkAvailability(String username) {
        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);
        return !usernameMap.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        int i = 1;
        while (suggestions.size() < 3) {
            String s1 = username + i;
            if (!usernameMap.containsKey(s1)) suggestions.add(s1);
            i++;
        }
        String modified = username.replace('_', '.');
        if (!usernameMap.containsKey(modified)) suggestions.add(modified);
        return suggestions;
    }

    public String getMostAttempted() {
        String result = null;
        int max = 0;
        for (Map.Entry<String, Integer> e : attemptFrequency.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                result = e.getKey();
            }
        }
        return result;
    }

    public void registerUser(String username, int userId) {
        usernameMap.put(username, userId);
    }

    public static void main(String[] args) {
        Problem1 uc = new Problem1();

        System.out.println(uc.checkAvailability("john_doe"));
        System.out.println(uc.checkAvailability("jane_smith"));
        System.out.println(uc.suggestAlternatives("john_doe"));
        System.out.println(uc.getMostAttempted());
    }
}