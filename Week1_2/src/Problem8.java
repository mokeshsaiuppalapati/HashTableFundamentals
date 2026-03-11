import java.util.*;

class Problem8 {

    class Spot {
        String plate;
        long entryTime;
        Status status;
    }

    enum Status {
        EMPTY, OCCUPIED, DELETED
    }

    private Spot[] table;
    private int capacity;
    private int size = 0;
    private long totalProbes = 0;
    private int operations = 0;
    private int peakHour = 0;
    private int[] hourlyCount = new int[24];

    public Problem8(int cap) {
        capacity = cap;
        table = new Spot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new Spot();
            table[i].status = Status.EMPTY;
        }
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    public String parkVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (table[index].status == Status.OCCUPIED) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].plate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = Status.OCCUPIED;

        size++;
        totalProbes += probes;
        operations++;

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        hourlyCount[hour]++;
        if (hourlyCount[hour] > hourlyCount[peakHour]) peakHour = hour;

        return "Assigned spot #" + index + " (" + probes + " probes)";
    }

    public String exitVehicle(String plate) {
        int index = hash(plate);

        while (table[index].status != Status.EMPTY) {
            if (table[index].status == Status.OCCUPIED &&
                    table[index].plate.equals(plate)) {

                long durationMs = System.currentTimeMillis() - table[index].entryTime;
                double hours = durationMs / 3600000.0;
                double fee = hours * 5;

                table[index].status = Status.DELETED;
                size--;

                return "Spot #" + index + " freed, Duration: " +
                        String.format("%.2f", hours) + "h, Fee: $" +
                        String.format("%.2f", fee);
            }
            index = (index + 1) % capacity;
        }

        return "Vehicle not found";
    }

    public String getStatistics() {
        double occupancy = (size * 100.0) / capacity;
        double avgProbes = operations == 0 ? 0 : totalProbes / operations;

        return "Occupancy: " + String.format("%.2f", occupancy) +
                "%, Avg Probes: " + String.format("%.2f", avgProbes) +
                ", Peak Hour: " + peakHour + "-" + (peakHour + 1);
    }

    public static void main(String[] args) throws Exception {

        Problem8 parking = new Problem8(500);

        System.out.println(parking.parkVehicle("ABC-1234"));
        System.out.println(parking.parkVehicle("ABC-1235"));
        System.out.println(parking.parkVehicle("XYZ-9999"));

        Thread.sleep(2000);

        System.out.println(parking.exitVehicle("ABC-1234"));
        System.out.println(parking.getStatistics());
    }
}