import java.util.*;

class Problem9 {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time;

        Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    public List<List<Transaction>> findTwoSum(List<Transaction> txs, int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();
        List<List<Transaction>> res = new ArrayList<>();

        for (Transaction t : txs) {
            int comp = target - t.amount;
            if (map.containsKey(comp)) {
                res.add(Arrays.asList(map.get(comp), t));
            }
            map.put(t.amount, t);
        }
        return res;
    }

    public List<List<Transaction>> findTwoSumTimeWindow(List<Transaction> txs, int target) {
        List<List<Transaction>> res = new ArrayList<>();
        txs.sort(Comparator.comparingLong(a -> a.time));
        HashMap<Integer, List<Transaction>> map = new HashMap<>();

        int left = 0;
        for (int right = 0; right < txs.size(); right++) {

            while (txs.get(right).time - txs.get(left).time > 3600_000) {
                List<Transaction> list = map.get(txs.get(left).amount);
                list.remove(txs.get(left));
                if (list.isEmpty()) map.remove(txs.get(left).amount);
                left++;
            }

            Transaction cur = txs.get(right);
            int comp = target - cur.amount;

            if (map.containsKey(comp)) {
                for (Transaction t : map.get(comp)) {
                    res.add(Arrays.asList(t, cur));
                }
            }

            map.computeIfAbsent(cur.amount, k -> new ArrayList<>()).add(cur);
        }
        return res;
    }

    public List<List<Transaction>> findKSum(List<Transaction> txs, int k, int target) {
        List<List<Transaction>> res = new ArrayList<>();
        backtrack(txs, k, target, 0, new ArrayList<>(), res);
        return res;
    }

    private void backtrack(List<Transaction> txs, int k, int target, int index,
                           List<Transaction> path, List<List<Transaction>> res) {

        if (k == 0 && target == 0) {
            res.add(new ArrayList<>(path));
            return;
        }

        if (k == 0 || index >= txs.size()) return;

        for (int i = index; i < txs.size(); i++) {
            path.add(txs.get(i));
            backtrack(txs, k - 1, target - txs.get(i).amount, i + 1, path, res);
            path.remove(path.size() - 1);
        }
    }

    public Map<String, List<Transaction>> detectDuplicates(List<Transaction> txs) {
        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : txs) {
            String key = t.amount + "_" + t.merchant;
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
        }

        Map<String, List<Transaction>> res = new HashMap<>();
        for (Map.Entry<String, List<Transaction>> e : map.entrySet()) {
            Set<String> accounts = new HashSet<>();
            for (Transaction t : e.getValue()) accounts.add(t.account);
            if (accounts.size() > 1) res.put(e.getKey(), e.getValue());
        }
        return res;
    }

    public static void main(String[] args) {

        Problem9 p = new Problem9();

        List<Transaction> txs = new ArrayList<>();
        long base = System.currentTimeMillis();

        txs.add(new Transaction(1, 500, "StoreA", "acc1", base));
        txs.add(new Transaction(2, 300, "StoreB", "acc2", base + 1000));
        txs.add(new Transaction(3, 200, "StoreC", "acc3", base + 2000));
        txs.add(new Transaction(4, 500, "StoreA", "acc4", base + 3000));

        System.out.println(p.findTwoSum(txs, 500));
        System.out.println(p.findTwoSumTimeWindow(txs, 500));
        System.out.println(p.findKSum(txs, 3, 1000));
        System.out.println(p.detectDuplicates(txs));
    }
}