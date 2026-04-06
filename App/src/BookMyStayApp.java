import java.util.*;

public class BookMyStayApp {

    // Simple Map to track booking status: BookingID -> RoomID
    private static Map<String, String> confirmedBookings = new HashMap<>();

    // Inventory count for room types
    private static Map<String, Integer> inventory = new HashMap<>();

    // Stack to track recently released room IDs for rollback/audit purposes
    private static Stack<String> rollbackStack = new Stack<>();

    public static void main(String[] args) {
        // Initializing System State
        inventory.put("Deluxe", 5);

        // Simulating a confirmed booking
        String bookingId = "BK1001";
        String roomId = "ROOM_D101";
        confirmedBookings.put(bookingId, roomId);
        inventory.put("Deluxe", inventory.get("Deluxe") - 1);

        System.out.println("--- Initial State ---");
        System.out.println("Booking " + bookingId + " confirmed for " + roomId);
        System.out.println("Deluxe Inventory: " + inventory.get("Deluxe"));
        System.out.println("---------------------\n");

        // Process Cancellation
        cancelBooking(bookingId, "Deluxe");

        System.out.println("\n--- Final State ---");
        System.out.println("Deluxe Inventory: " + inventory.get("Deluxe"));
        if (!rollbackStack.isEmpty()) {
            System.out.println("Last Room Released (from Stack): " + rollbackStack.peek());
        }
    }

    /**
     * Goal: Enable safe cancellation and inventory rollback.
     */
    public static void cancelBooking(String bookingId, String roomType) {
        System.out.println("Initiating cancellation for: " + bookingId);

        // 1. Validate the reservation
        if (!confirmedBookings.containsKey(bookingId)) {
            System.out.println("Error: Booking ID not found or already cancelled.");
            return;
        }

        // 2. Identify the Room ID to be released
        String roomId = confirmedBookings.get(bookingId);

        // 3. Record Room ID in the rollback structure (Stack - LIFO)
        rollbackStack.push(roomId);

        // 4. Increment Inventory count
        inventory.put(roomType, inventory.get(roomType) + 1);

        // 5. Update Booking History (Remove from active bookings)
        confirmedBookings.remove(bookingId);

        System.out.println("Success: Booking " + bookingId + " cancelled.");
        System.out.println("Room " + roomId + " has been rolled back to available pool.");
    }
}