import java.util.HashMap;
import java.util.Map;

// Custom Exception for domain-specific error handling
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Manages room counts and prevents negative inventory
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("DELUXE", 5);
        rooms.put("STANDARD", 10);
    }

    public boolean hasRoom(String roomType) {
        return rooms.containsKey(roomType.toUpperCase());
    }

    public int getAvailableRooms(String roomType) {
        return rooms.getOrDefault(roomType.toUpperCase(), 0);
    }

    public void reduceInventory(String roomType) throws InvalidBookingException {
        String type = roomType.toUpperCase();
        int currentCount = getAvailableRooms(type);

        if (currentCount <= 0) {
            throw new InvalidBookingException("Inventory Error: No " + type + " rooms left to book.");
        }
        rooms.put(type, currentCount - 1);
    }
}

// Validator class for "Fail-Fast" logic
class InvalidBookingValidator {
    public void validate(String guestName, String roomType, RoomInventory inventory) throws InvalidBookingException {
        // Rule 1: Validate Guest Name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Validation Failed: Guest name cannot be empty.");
        }

        // Rule 2: Validate Room Type existence
        if (!inventory.hasRoom(roomType)) {
            throw new InvalidBookingException("Validation Failed: Room type '" + roomType + "' does not exist.");
        }

        // Rule 3: Check availability before processing
        if (inventory.getAvailableRooms(roomType) <= 0) {
            throw new InvalidBookingException("Validation Failed: Requested room type is sold out.");
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        InvalidBookingValidator validator = new InvalidBookingValidator();

        System.out.println("--- Book My Stay: Error Handling & Validation ---");

        // Test Scenario 1: Valid Booking
        processBooking("Alice", "DELUXE", inventory, validator);

        // Test Scenario 2: Invalid Room Type (Should fail)
        processBooking("Bob", "PENTHOUSE", inventory, validator);

        // Test Scenario 3: Empty Guest Name (Should fail)
        processBooking("", "STANDARD", inventory, validator);

        System.out.println("\nSystem remains stable after handling errors.");
    }

    private static void processBooking(String name, String type, RoomInventory inv, InvalidBookingValidator val) {
        try {
            System.out.println("\nAttempting booking for: " + (name.isEmpty() ? "[Empty]" : name) + " (" + type + ")");

            // Step 1: Validate (Fail-Fast)
            val.validate(name, type, inv);

            // Step 2: Update Inventory
            inv.reduceInventory(type);

            System.out.println("SUCCESS: Booking confirmed for " + name);
        } catch (InvalidBookingException e) {
            // Step 3: Graceful Failure Handling
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}