import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FlightsFile {
    private RandomAccessFile flightsFile;
    private final File file;

    public FlightsFile(CheckFormatFile checkFormatFile) throws IOException {

        this.file = new File("Flights File.dat");
        if (!file.exists()) {
            this.flightsFile = new RandomAccessFile("Flights File.dat", "rw");
            writeDefaultFlights(checkFormatFile);
        } else if (this.flightsFile == null)
            this.flightsFile = new RandomAccessFile("Flights File.dat", "rw");
    }


    public RandomAccessFile getFlightsFile() {
        return flightsFile;
    }

    /**
     * << write the information of flights to the file >>
     *
     * @param flight          the information of flight.
     * @param checkFormatFile the class for checking format to read or write.
     */
    public void writeFlightsInfo(Flight flight, CheckFormatFile checkFormatFile) throws IOException {
        flightsFile.seek(flightsFile.length());
        flightsFile.writeChars(checkFormatFile.formatToWrite(flight.getFlightId(), Flight.SIZE));
        flightsFile.writeChars(checkFormatFile.formatToWrite(flight.getOrigin(), Flight.SIZE));
        flightsFile.writeChars(checkFormatFile.formatToWrite(flight.getDestination(), Flight.SIZE));
        flightsFile.writeChars(checkFormatFile.formatToWrite(flight.getDate(), Flight.SIZE));
        flightsFile.writeChars(checkFormatFile.formatToWrite(flight.getTime(), Flight.SIZE));
        flightsFile.writeChars(checkFormatFile.formatToWrite(flight.getPrice(), Flight.SIZE));
        flightsFile.writeChars(checkFormatFile.formatToWrite(flight.getSeats(), Flight.SIZE));
    }

    /**
     * << read the information of flights from file >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     * @param index           the number of recorde in file of flight.
     * @return one of the recorde of flight that includes the information of flight.
     */
    public String readFlightInfo(CheckFormatFile checkFormatFile, long index) throws IOException {
        return checkFormatFile.formatToRead(index * Flight.FIX_SIZE * 2, Flight.FIX_SIZE, flightsFile);
    }

    /**
     * << update the information of flight >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     * @param startSearchSize the initial location on record to search for the desired field.
     * @param startUpdateSize the initial location on record to update for the desired field.
     * @param searchData      the desired field to search in the file.
     * @param updateData      the desired field to update in the file.
     */
    public void updateFlight(CheckFormatFile checkFormatFile, long startSearchSize, long startUpdateSize, String searchData, String updateData) throws IOException {
        if (searchFlights(checkFormatFile, startSearchSize, searchData) != null) {

            for (int i = 0; i < flightsFile.length() / (Flight.FIX_SIZE * 2); i++) {
                if (readFlightInfo(checkFormatFile, i).equals(searchFlights(checkFormatFile, startSearchSize, searchData)[0])) {
                    flightsFile.seek((long) i * 2 * Flight.FIX_SIZE + startUpdateSize * 2);

                    flightsFile.writeChars(checkFormatFile.formatToWrite(updateData, Flight.SIZE));
                }
            }
        }
    }

    /**
     * << search the flight with the desired information >>
     *
     * @param checkFormatFile   the class for checking format to read or write.
     * @param primarySizeSearch the initial location on record to search for the desired fields.
     * @param values            the information of flight.
     * @return flight records found with desired information.
     */

    public String[] searchFlights(CheckFormatFile checkFormatFile, long primarySizeSearch, String... values) throws IOException {

        String[] foundedFlights = new String[100];
        int j = 0;
        int countFoundedValues = 0;
        long temp = primarySizeSearch;

        for (long i = 0; i < flightsFile.length() / (Flight.FIX_SIZE * 2); i++) {
            long size = i * Flight.FIX_SIZE * 2;

            for (String value : values) {
                if (checkFormatFile.formatToRead(size + temp * 2, Flight.SIZE, flightsFile).equals(value)) {
                    countFoundedValues++;
                    if (countFoundedValues == values.length)
                        foundedFlights[j++] = readFlightInfo(checkFormatFile, i);
                }
                temp = temp + 20;
            }
            countFoundedValues = 0;
            temp = primarySizeSearch;
        }
        return foundedFlights;
    }

    /**
     * << remove the flight with data >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     * @param searchData      the desired data ti find the flight that will be deleted.
     */
    public void removeFlights(CheckFormatFile checkFormatFile, int primarySizeSearch, String searchData) throws IOException {
        long flightFileLength = flightsFile.length();

        if (searchFlights(checkFormatFile, primarySizeSearch, searchData)[0] != null) {
            for (int i = 0; i < flightFileLength / (Flight.FIX_SIZE * 2); i++) {

                if (readFlightInfo(checkFormatFile, i).equals(searchFlights(checkFormatFile, primarySizeSearch, searchData)[0])) {

                    String removedFlight = checkFormatFile.formatToWrite(checkFormatFile.formatToRead((long) (i + 1) * 2 * Flight.FIX_SIZE, (flightFileLength - (long) (i + 1) * Flight.FIX_SIZE * 2) / 2, flightsFile), (flightFileLength - (long) (i + 1) * Flight.FIX_SIZE * 2) / 2);
                    flightsFile.seek((long) i * Flight.FIX_SIZE * 2);
                    flightsFile.writeChars(removedFlight);

                    flightsFile.setLength(flightsFile.length() - 2 * Flight.FIX_SIZE);
                    break;
                }
            }
        }
    }

    /**
     * << search a field on a record of flight >>
     *
     * @param flightId        one of the characteristics of flight.
     * @param checkFormatFile the class for checking format to read or write.
     * @param startSizeField  the initial location if the field on the record.
     * @return the field was found.
     */
    public String findFieldsFromFlight(String flightId, CheckFormatFile checkFormatFile, int startSizeField) throws IOException {
        StringBuilder searchField = new StringBuilder();
        for (int i = startSizeField; i < Flight.SIZE + startSizeField && i < searchFlights(checkFormatFile, 0, flightId)[0].length(); i++)
            searchField.append(searchFlights(checkFormatFile, 0, flightId)[0].toCharArray()[i]);
        return searchField.substring(0).trim();
    }

    /**
     * << writing default flight information  >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     */
    public void writeDefaultFlights(CheckFormatFile checkFormatFile) throws IOException {
        writeFlightsInfo(new Flight("WX-12", "Yazd", "Tehran", "1402/02/12", "12:30", "700000", "51"), checkFormatFile);
        writeFlightsInfo(new Flight("WZ-15", "Yazd", "Tehran", "1402/02/12", "08:30", "900000", "220"), checkFormatFile);
        writeFlightsInfo(new Flight("BG-22", "Shiraz", "Tabriz", "1402/02/13", "22:30", "1100000", "86"), checkFormatFile);
        writeFlightsInfo(new Flight("WZ-17", "Mashhad", "Shiraz", "1402/02/13", "20:30", "900000", "70"), checkFormatFile);
        writeFlightsInfo(new Flight("BG-34", "Tehran", "Esfahan", "1402/04/31", "00:00", "1250000", "108"), checkFormatFile);
        writeFlightsInfo(new Flight("FH-90", "Gilan", "Kordestan", "1402/05/10", "20:40", "870000", "55"), checkFormatFile);
        writeFlightsInfo(new Flight("WX-20", "Yazd", "Kish", "1402/04/08", "05:05", "1800000", "190"), checkFormatFile);
        writeFlightsInfo(new Flight("DX-46", "Tabriz", "Arak", "1402/07/15", "15:40", "970000", "100"), checkFormatFile);
        writeFlightsInfo(new Flight("KJ-55", "Kerman", "Bandaabbas", "1402/10/30", "18:55", "1380000", "210"), checkFormatFile);
        writeFlightsInfo(new Flight("FH-88", "Sanandaj", "Mazandaran", "1402/12/17", "22:10", "1500000", "258"), checkFormatFile);
        writeFlightsInfo(new Flight("KL-65", "Shiraz", "Khoozestan", "1402/11/15", "01:50", "1680000", "168"), checkFormatFile);
        writeFlightsInfo(new Flight("BM-11", "Tehran", "Esfahan", "1403/01/05", "23:00", "1300000", "202"), checkFormatFile);
        writeFlightsInfo(new Flight("FH-10", "Yazd", "Kish", "1402/04/08", "21:15", "2200000", "250"), checkFormatFile);
        writeFlightsInfo(new Flight("WK-61", "Kerman", "Bandarabbas", "1402/10/30", "03:25", "1530000", "98"), checkFormatFile);
        writeFlightsInfo(new Flight("WZ-33", "Shiraz", "Tabriz", "1402/02/13", "14:15", "1175000", "114"), checkFormatFile);
        writeFlightsInfo(new Flight("BD-55", "Hormozgan", "Golestan", "1403/03/13", "00:35", "1900000", "140"), checkFormatFile);
        writeFlightsInfo(new Flight("FH-16", "Gilan", "Kordestan", "1402/05/10", "11:11", "800000", "110"), checkFormatFile);
        writeFlightsInfo(new Flight("BH-38", "Yazd", "Mashhad", "1404/04/04", "04:04", "1400000", "140"), checkFormatFile);
        writeFlightsInfo(new Flight("HN-12", "Tehran", "Esfahan", "1402/04/31", "17:45", "1250000", "100"), checkFormatFile);
        writeFlightsInfo(new Flight("FH-01", "Tabriz", "Arak", "1402/07/15", "03:00", "970000", "89"), checkFormatFile);

    }

}
