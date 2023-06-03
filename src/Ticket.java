public class Ticket {
    private final Passenger passenger;
    private final Flight flight;
    private final String ticketId;
    static final int SIZE = 20;
    static final int FIX_SIZE = Passenger.SIZE + Flight.FIX_SIZE + SIZE;

    public Passenger getPassenger() {
        return passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Ticket(Passenger passenger, Flight flight, String ticketId) {
        this.passenger = passenger;
        this.flight = flight;
        this.ticketId = ticketId;
    }

}
