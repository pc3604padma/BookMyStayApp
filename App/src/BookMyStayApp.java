import java.io.*;
import java.util.*;

// Essential for saving object state to a file
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;
    Map<String, Integer> inventory;
    List<String> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<String> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

public class BookMyStayApp {
    private static final String STORAGE_FILE = "system_state.ser";
    private Map<String, Integer> inventory = new HashMap<>();
    private List<String> bookingHistory = new ArrayList<>();

    public UseCase12DataPersistenceRecovery() {
        // Initialize default inventory if no saved state exists
        inventory.put("Standard Room", 10);
        inventory.put("Deluxe Suite", 5);
    }

    // --- Persistence Logic ---

    public void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            SystemState state = new SystemState(inventory, bookingHistory);
            oos.writeObject(state);
            System.out.println("[System] State successfully persisted to " + STORAGE_FILE);
        } catch (IOException e) {
            System.err.println("[Error] Failed to save system state: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadState() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) {
            System.out.println("[System] No previous state found. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            SystemState state = (SystemState) ois.readObject();
            this.inventory = state.inventory;
            this.bookingHistory = state.bookingHistory;
            System.out.println("[System] State successfully recovered from storage.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] Recovery failed. File may be corrupted. " + e.getMessage());
        }
    }

    // --- Business Logic ---

    public void createBooking(String roomType, String guestName) {
        if (inventory.getOrDefault(roomType, 0) > 0) {
            inventory.put(roomType, inventory.get(roomType) - 1);
            bookingHistory.add("Guest: " + guestName + " | Room: " + roomType);
            System.out.println("Booking confirmed for " + guestName);
        } else {
            System.out.println("Booking failed: " + roomType + " is full.");
        }
    }

    public void displayStatus() {
        System.out.println("\n--- Current System Status ---");
        System.out.println("Inventory: " + inventory);
        System.out.println("Total Bookings: " + bookingHistory.size());
        System.out.println("-----------------------------\n");
    }

    public static void main(String[] args) {
        UseCase12DataPersistenceRecovery app = new UseCase12DataPersistenceRecovery();

        // 1. Attempt to recover data from previous run
        app.loadState();
        app.displayStatus();

        // 2. Perform some operations
        System.out.println("Processing new bookings...");
        app.createBooking("Standard Room", "Alice");
        app.createBooking("Deluxe Suite", "Bob");

        app.displayStatus();

        // 3. Persist state before "shutting down"
        app.saveState();
        System.out.println("System shutting down. Run the program again to see recovered data.");
    }
}