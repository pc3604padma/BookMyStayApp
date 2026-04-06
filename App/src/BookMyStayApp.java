import java.util.ArrayList;
import java.util.List;

/**
 * Represents a confirmed booking.
 */
class Reservation {
    private String bookingId;
    private String guestName;
    private String roomType;

    public Reservation(String bookingId, String guestName, String roomType) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Guest: %-10s | Room: %s", bookingId, guestName, roomType);
    }
}

/**
 * Maintains the record of confirmed reservations.
 */
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        // Return a copy to ensure data integrity during reporting
        return new ArrayList<>(history);
    }
}

/**
 * Generates reports from the history data.
 */
class BookingReportService {
    public void generateReport(BookingHistory history) {
        List<Reservation> records = history.getAllBookings();

        System.out.println("\n--- Administrative Booking Report ---");
        if (records.isEmpty()) {
            System.out.println("No confirmed bookings found.");
        } else {
            for (Reservation r : records) {
                System.out.println(r);
            }
            System.out.println("------------------------------------");
            System.out.println("Total Bookings: " + records.size());
        }
        System.out.println("--- End of Report ---\n");
    }
}

/**
 * Main Class - Entry Point
 */
public class BookMyStayApp {
    public static void main(String[] args) {
        // Initialize components
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirming bookings (Adding to history)
        history.addReservation(new Reservation("B001", "Alice", "Deluxe"));
        history.addReservation(new Reservation("B002", "Bob", "Suite"));
        history.addReservation(new Reservation("B003", "Charlie", "Standard"));

        // Generate the report
        System.out.println("System: Generating historical report...");
        reportService.generateReport(history);
    }
}