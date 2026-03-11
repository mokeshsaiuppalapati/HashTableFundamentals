import java.util.*;
import java.util.concurrent.*;

class Problem2 {

    private ConcurrentHashMap<String, Integer> stockMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, LinkedHashMap<Integer, Integer>> waitingList = new ConcurrentHashMap<>();

    public Problem2() {
        stockMap.put("IPHONE15_256GB", 100);
        waitingList.put("IPHONE15_256GB", new LinkedHashMap<>());
    }

    public int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    public synchronized String purchaseItem(String productId, int userId) {
        int stock = stockMap.getOrDefault(productId, 0);
        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        } else {
            LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);
            int position = queue.size() + 1;
            queue.put(userId, position);
            return "Added to waiting list, position #" + position;
        }
    }

    public static void main(String[] args) {
        Problem2 manager = new Problem2();

        System.out.println(manager.checkStock("IPHONE15_256GB"));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", 10000 + i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}