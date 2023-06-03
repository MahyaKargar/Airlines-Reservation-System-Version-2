import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PassengersFile {
    private final RandomAccessFile passengersFile;

    public PassengersFile() throws FileNotFoundException {
        this.passengersFile = new RandomAccessFile("PassengersFile.dat", "rw");
    }

    /**
     * << write the passenger 's information to the file >>
     *
     * @param passenger       the passenger 's information.
     * @param checkFormatFile the class for checking format to read or write.
     */
    public void writePassengersInfo(Passenger passenger, CheckFormatFile checkFormatFile) throws IOException {
        passengersFile.seek(passengersFile.length());
        passengersFile.writeChars(checkFormatFile.formatToWrite(passenger.getUserName(), Passenger.SIZE));
        passengersFile.writeChars(checkFormatFile.formatToWrite(passenger.getPassword(), Passenger.SIZE));
        passengersFile.writeChars(checkFormatFile.formatToWrite(passenger.getCredit(), Passenger.SIZE));
    }

    /**
     * << read the passenger 's information from file >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     * @param index           the number of recorde in passengers' file.
     * @return one of the passenger 's recorde  that includes the passenger 's information.
     */
    public String readPassengersInfo(CheckFormatFile checkFormatFile, long index) throws IOException {
        return checkFormatFile.formatToRead(index * Passenger.FIX_SIZE * 2, Passenger.FIX_SIZE, passengersFile);
    }

    /**
     * << search the passenger with the desired information >>
     *
     * @param checkFormatFile   the class for checking format to read or write.
     * @param primarySizeSearch the initial location on record to search for the desired fields.
     * @param values            the passenger 's information.
     * @return passenger records found with desired information.
     */
    public String[] searchPassenger(CheckFormatFile checkFormatFile, long primarySizeSearch, String... values) throws IOException {

        String[] foundedPassengers = new String[100];
        int j = 0;
        int countFoundedValues = 0;

        for (long i = 0; i < passengersFile.length() / (Passenger.FIX_SIZE * 2); i++) {
            long size = i * Passenger.FIX_SIZE * 2;

            for (String value : values) {
                if (checkFormatFile.formatToRead(size + primarySizeSearch * 2, Passenger.SIZE, passengersFile).equals(value)) {
                    countFoundedValues++;
                    if (countFoundedValues == values.length)
                        foundedPassengers[j++] = readPassengersInfo(checkFormatFile, i);
                }
                size = size + 20;
            }
            countFoundedValues = 0;
        }
        return foundedPassengers;
    }

    /**
     * << update the passenger's information >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     * @param startSearchSize the initial location on record to search for the desired field.
     * @param startUpdateSize the initial location on record to update for the desired field.
     * @param searchData      the desired field to search in the file.
     * @param updateData      the desired field to update in the file.
     */
    public void updatePassenger(CheckFormatFile checkFormatFile, long startSearchSize, long startUpdateSize, String searchData, String updateData) throws IOException {
        if (searchPassenger(checkFormatFile, startSearchSize, searchData)[0] != null) {
            for (int i = 0; i < passengersFile.length() / (Passenger.FIX_SIZE * 2); i++) {
                if (readPassengersInfo(checkFormatFile, i).equals(searchPassenger(checkFormatFile, startSearchSize, searchData)[0])) {
                    passengersFile.seek((long) i * 2 * Passenger.FIX_SIZE + startUpdateSize * 2);
                    passengersFile.writeChars(checkFormatFile.formatToWrite(updateData, Passenger.SIZE));
                }
            }
        }
    }

    /**
     * << search a field on a record of passenger >>
     *
     * @param username        one of the passenger 's characteristics.
     * @param checkFormatFile the class for checking format to read or write.
     * @param startSizeField  the initial location if the field on the record.
     * @return the field was found.
     */
    public String findFieldsFromPassenger(String username, CheckFormatFile checkFormatFile, int startSizeField) throws IOException {
        StringBuilder searchField = new StringBuilder();
        for (int i = startSizeField; i < Passenger.SIZE + startSizeField && i < searchPassenger(checkFormatFile, 0, username)[0].length(); i++)
            searchField.append(searchPassenger(checkFormatFile, 0, username)[0].toCharArray()[i]);
        return searchField.substring(0).trim();
    }
}
