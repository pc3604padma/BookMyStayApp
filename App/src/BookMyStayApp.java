import java.util.*;
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() { return serviceName; }
    public double getCost() { return cost; }

    @Override
    public String toString() {
        return serviceName + " ($" + cost + ")";
    }
}
class AddOnServiceManager {
    // Maps reservation ID to a list of selected services
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        this.reservationServices = new HashMap<>();
    }

    public void addServiceToReservation(String reservationId, AddOnService service) {
        reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
        System.out.println("Added " + service.getServiceName() + " to Reservation: " + reservationId);
    }


    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = reservationServices.get(reservationId);
        if (services == null) return 0.0;

        return services.stream()
                .mapToDouble(AddOnService::getCost)
                .sum();
    }

    public void displayReservationServices(String reservationId) {
        List<AddOnService> services = reservationServices.get(reservationId);
        System.out.println("\n--- Services for Reservation: " + reservationId + " ---");
        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
        } else {
            services.forEach(System.out::println);
            System.out.println("Total Add-on Cost: $" + calculateTotalServiceCost(reservationId));
        }
    }
}
public class BookMyStayApp {
    public static void main(String[] args) {
        AddOnServiceManager manager = new AddOnServiceManager();

        // Define available services
        AddOnService wifi = new AddOnService("Premium WiFi", 15.0);
        AddOnService breakfast = new AddOnService("Buffet Breakfast", 25.0);
        AddOnService spa = new AddOnService("Spa Treatment", 50.0);

        // Scenario: Guest with Reservation ID "RES1001" selects services
        String resId = "RES1001";
        System.out.println("Processing Add-ons for " + resId + "...");

        manager.addServiceToReservation(resId, wifi);
        manager.addServiceToReservation(resId, breakfast);
        manager.addServiceToReservation(resId, spa);

        // Display results
        manager.displayReservationServices(resId);

        // Scenario: Another guest with Reservation ID "RES1002"
        String resId2 = "RES1002";
        manager.addServiceToReservation(resId2, wifi);
        manager.displayReservationServices(resId2);
    }
}