import java.io.IOException;

public class DataBaseFile {
    CheckFormatFile checkFormatFile = new CheckFormatFile();
    AdminsFile adminsFile = new AdminsFile(checkFormatFile);
    PassengersFile passengersFile = new PassengersFile();
    FlightsFile flightsFile = new FlightsFile(checkFormatFile);
    TicketsFile ticketsFile = new TicketsFile();
    CheckFormatData checkFormat = new CheckFormatData();

    public DataBaseFile() throws IOException {
    }
}
