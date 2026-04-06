import java.util.*;

public class BookMyStayApp {

    // Inventory: Maps Room Type to available count
    private static Map<String, Integer> inventory = new HashMap<>();

    // Allocation Tracking: Maps Room Type to a Set of unique assigned Room IDs
    private static Map<String, Set<String>> allocatedRooms = new HashMap<>();

    // FIFO Queue for Booking Requests
    private static Queue<BookingRequest> requestQueue = new LinkedList<>();

    static class BookingRequest {
        String guestName;
        String roomType;

        public BookingRequest(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    public static void main(String[] args) {
        // 1. Initialize Inventory
        inventory.put("Deluxe", 2);
        inventory.put("Standard", 1);

        allocatedRooms.put("Deluxe", new HashSet<>());
        allocatedRooms.put("Standard", new HashSet<>());

        // 2. Add Requests to Queue (FIFO)
        requestQueue.add(new BookingRequest("Alice", "Deluxe"));
        requestQueue.add(new BookingRequest("Bob", "Deluxe"));
        requestQueue.add(new BookingRequest("Charlie", "Deluxe")); // Should fail (no inventory)
        requestQueue.add(new BookingRequest("David", "Standard"));

        System.out.println("--- Processing Room Allocations ---");
        processAllocations();
    }

    public static void processAllocations() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();
            String type = request.roomType;

            // Check inventory consistency
            if (inventory.containsKey(type) && inventory.get(type) > 0) {
                // Generate a Unique Room ID (e.g., DELUXE-101)
                String roomID = type.toUpperCase() + "-" + (100 + allocatedRooms.get(type).size() + 1);

                // Uniqueness Enforcement using Set
                if (!allocatedRooms.get(type).contains(roomID)) {
                    allocatedRooms.get(type).add(roomID);

                    // Atomic-like update: Decrement inventory immediately
                    inventory.put(type, inventory.get(type) - 1);

                    System.out.println("SUCCESS: " + request.guestName + " assigned to " + roomID);
                }
            } else {
                System.out.println("FAILED: No " + type + " rooms available for " + request.guestName);
            }
        }

        displayFinalState();
    }

    private static void displayFinalState() {
        System.out.println("\n--- Final System State ---");
        System.out.println("Remaining Inventory: " + inventory);
        System.out.println("Allocated Rooms: " + allocatedRooms);
    }
}