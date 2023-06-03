public class Flight {
    private final String flightId;
    private final String origin;
    private final String destination;
    private final String date;
    private final String time;
    private final String price;
    private final String seats;
    static final int SIZE = 20;
    static final int FIX_SIZE = 140;

    public String getFlightId() {
        return flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getSeats() {
        return seats;
    }

    public Flight(String flightId, String origin, String destination, String date, String time, String price, String seats) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.price = price;
        this.seats = seats;
    }
}
