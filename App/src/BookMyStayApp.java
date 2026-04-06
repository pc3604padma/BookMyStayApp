import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

// Class representing a Booking Request
class BookingRequest {
    private String guestName;
    private String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// Main class for Concurrent Booking Simulation
public class BookMyStayApp {

    // Shared resource: Inventory (Thread-safe via AtomicInteger or synchronized access)
    private static final Map<String, AtomicInteger> inventory = new ConcurrentHashMap<>();

    // Shared resource: Booking Queue
    private static final Queue<BookingRequest> bookingQueue = new LinkedList<>();

    static {
        // Initialize inventory
        inventory.put("Deluxe", new AtomicInteger(2));
        inventory.put("Suite", new AtomicInteger(1));
    }

    public static void main(String[] args) {
        System.out.println("--- Hotel Booking: Concurrent Simulation Start ---");

        // 1. Multiple guests submit requests simultaneously
        bookingQueue.add(new BookingRequest("Alice", "Deluxe"));
        bookingQueue.add(new BookingRequest("Bob", "Deluxe"));
        bookingQueue.add(new BookingRequest("Charlie", "Deluxe")); // Should fail (only 2 Deluxe available)
        bookingQueue.add(new BookingRequest("Dave", "Suite"));

        // 2. Create a thread pool to simulate concurrent processing
        ExecutorService executor = Executors.newFixedThreadPool(3);

        while (!bookingQueue.isEmpty()) {
            BookingRequest request = bookingQueue.poll();
            executor.execute(() -> processBooking(request));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- Final Inventory State ---");
        inventory.forEach((type, count) -> System.out.println(type + ": " + count.get() + " available"));
    }

    // 3. Critical Section: Synchronized method to prevent double-booking
    private static void processBooking(BookingRequest request) {
        String name = request.getGuestName();
        String type = request.getRoomType();

        System.out.println("[Thread " + Thread.currentThread().getId() + "] Processing " + name + " for " + type);

        synchronized (inventory) {
            AtomicInteger count = inventory.get(type);
            if (count != null && count.get() > 0) {
                // Simulate processing time
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                count.decrementAndGet();
                System.out.println(">>> SUCCESS: Room allocated to " + name);
            } else {
                System.out.println(">>> FAILED: No " + type + " rooms left for " + name);
            }
        }
    }
}