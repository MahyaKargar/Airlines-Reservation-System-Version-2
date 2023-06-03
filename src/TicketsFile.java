import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class TicketsFile {
    private final RandomAccessFile ticketsFile;

    public TicketsFile() throws FileNotFoundException {
        this.ticketsFile = new RandomAccessFile("TicketsFile.dat", "rw");
    }

    /**
     * << write the information of tickets to the file >>
     *
     * @param passenger    the passenger 's information.
     * @param flightId     one of the characteristics of flight.
     * @param dataBaseFile the class that includes instances of other classes.
     * @return ticketId.
     */
    public String writeTicketInfoIno(Passenger passenger, String flightId, DataBaseFile dataBaseFile) throws IOException {
        String ticketId = selectTicketId(dataBaseFile.checkFormatFile);

        ticketsFile.seek(ticketsFile.length());

        ticketsFile.writeChars(dataBaseFile.checkFormatFile.formatToWrite(passenger.getUserName(), Ticket.SIZE));
        ticketsFile.writeChars(dataBaseFile.checkFormatFile.formatToWrite(ticketId, Ticket.SIZE));
        ticketsFile.writeChars(dataBaseFile.checkFormatFile.formatToWrite(dataBaseFile.flightsFile.searchFlights(dataBaseFile.checkFormatFile, 0, flightId)[0], Flight.FIX_SIZE));

        return ticketId;
    }

    /**
     * << read the information of tickets from file >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     * @param index           the number of record in file of ticket.
     * @return one of the recorde of ticket that includes the information of ticket.
     */

    public String readTicketFile(CheckFormatFile checkFormatFile, long index) throws IOException {
        return checkFormatFile.formatToRead(index * Ticket.FIX_SIZE * 2, Ticket.FIX_SIZE, ticketsFile);
    }

    /**
     * << search the ticket with the desired information >>
     *
     * @param checkFormatFile   the class for checking format to read or write.
     * @param primarySizeSearch the initial location on record to search for the desired fields.
     * @param values            the information of ticket.
     * @return ticket records that found with desired information.
     */

    public String[] searchTicket(CheckFormatFile checkFormatFile, long primarySizeSearch, String... values) throws IOException {
        String[] foundedTickets = new String[100];
        int j = 0;
        int countFoundedValues = 0;
        long temp = primarySizeSearch;

        for (long i = 0; i < ticketsFile.length() / (Ticket.FIX_SIZE * 2); i++) {
            long size = i * Ticket.FIX_SIZE * 2;

            for (String value : values) {
                if (checkFormatFile.formatToRead(size + temp * 2, Ticket.SIZE, ticketsFile).equals(value)) {
                    countFoundedValues++;
                    if (countFoundedValues == values.length)
                        foundedTickets[j++] = readTicketFile(checkFormatFile, i);
                }
                temp = temp + 20;
            }
            countFoundedValues = 0;
            temp = primarySizeSearch;
        }
        return foundedTickets;
    }

    /**
     * << remove the ticket with data >>
     *
     * @param checkFormatFile   the class for checking format to read or write.
     * @param primarySizeSearch the initial location on record to search for the desired fields.
     * @param searchData        the desired data ti find the ticket that will be deleted.
     */
    public void removeTickets(CheckFormatFile checkFormatFile, int primarySizeSearch, String searchData) throws IOException {
        long ticketsFileLength = ticketsFile.length();

        if (searchTicket(checkFormatFile, primarySizeSearch, searchData)[0] != null) {
            for (int i = 0; i < ticketsFileLength / (Ticket.FIX_SIZE * 2); i++) {
                if (readTicketFile(checkFormatFile, i).equals(searchTicket(checkFormatFile, primarySizeSearch, searchData)[0])) {

                    String removedTicket = checkFormatFile.formatToWrite(checkFormatFile.formatToRead((long) (i + 1) * 2 * Ticket.FIX_SIZE, (ticketsFileLength - (long) (i + 1) * Ticket.FIX_SIZE * 2) / 2, ticketsFile), (ticketsFileLength - (long) (i + 1) * Ticket.FIX_SIZE * 2) / 2);
                    ticketsFile.seek((long) i * Ticket.FIX_SIZE * 2);
                    ticketsFile.writeChars(removedTicket);

                    ticketsFile.setLength(ticketsFile.length() - 2 * Ticket.FIX_SIZE);
                    break;
                }
            }
        }
    }

    /**
     * << select the ticketId to the reservation ticket >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     * @return ticketId.
     */

    public String selectTicketId(CheckFormatFile checkFormatFile) throws IOException {
        long ticketId = 10234511;
        while (searchTicket(checkFormatFile, 20, String.valueOf(ticketId))[0] != null) {
            ticketId += 100;
        }
        return String.valueOf(ticketId);
    }

    /**
     * << search a field on a record of ticket >>
     *
     * @param ticketId        one of the characteristics of ticketId.
     * @param checkFormatFile the class for checking format to read or write.
     * @param startSizeField  the initial location if the field on the record.
     * @return the field was found.
     */
    public String findFieldsTicket(String ticketId, CheckFormatFile checkFormatFile, int startSizeField) throws IOException {
        StringBuilder str = new StringBuilder();
        for (int i = startSizeField; i < Ticket.SIZE + startSizeField && i < searchTicket(checkFormatFile, 20, ticketId)[0].toCharArray().length; i++) {
            str.append(searchTicket(checkFormatFile, 20, ticketId)[0].toCharArray()[i]);
        }
        return str.substring(0).trim();
    }


}
