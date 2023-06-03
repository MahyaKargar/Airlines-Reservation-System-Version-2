import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AdminsFile {
    private RandomAccessFile adminsInfoFile;
    private final File file;

    public AdminsFile(CheckFormatFile checkFormatFile) throws IOException {

        this.file = new File("AdminsFile.dat");
        if (!file.exists()) {
            this.adminsInfoFile = new RandomAccessFile("AdminsFile.dat", "rw");
            writeDefaultAdmins(checkFormatFile);
        }
        else if (this.adminsInfoFile == null)
            this.adminsInfoFile = new RandomAccessFile("AdminsFile.dat", "rw");
    }

    public RandomAccessFile getAdminsInfoFile() {
        return adminsInfoFile;
    }

    /**
     * << write the admin 's information to the file >>
     *
     * @param admin           the admin 's information.
     * @param checkFormatFile the class for checking format to read or write.
     */
    public void writeAdminsInfo(Admin admin, CheckFormatFile checkFormatFile) throws IOException {
        adminsInfoFile.writeChars(checkFormatFile.formatToWrite(admin.getUserName(), Admin.SIZE));
        adminsInfoFile.writeChars(checkFormatFile.formatToWrite(admin.getPassword(), Admin.SIZE));
    }

    /**
     * << read the admin 's information from file >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     * @param index           number of recorde in admin 's file.
     * @return one of the recorde of admin that includes  admin 's information.
     */
    public String readAdminsInfo(CheckFormatFile checkFormatFile, int index) throws IOException {
        return checkFormatFile.formatToRead((long) index * Admin.FIX_SIZE * 2, Admin.FIX_SIZE, adminsInfoFile);
    }

    /**
     * << write default admin information >>
     *
     * @param checkFormatFile the class for checking format to read or write.
     */
    public void writeDefaultAdmins(CheckFormatFile checkFormatFile) throws IOException {
        writeAdminsInfo(new Admin("FirstAdmin", "14021"), checkFormatFile);
    }

    /**
     * << search the admin with the desired information >>
     *
     * @param checkFormatFile   the class for checking format to read or write.
     * @param primarySizeSearch start size of field for search.
     * @param values            admin 's information.
     * @return array of admin 's information
     */
    public String[] searchAdmins(CheckFormatFile checkFormatFile, long primarySizeSearch, String... values) throws IOException {
        String[] foundedAdmins = new String[100];
        int j = 0;
        int countFoundedValues = 0;
        long temp = primarySizeSearch;

        for (long i = 0; i < adminsInfoFile.length() / (Admin.FIX_SIZE * 2); i++) {
            long size = i * Flight.FIX_SIZE * 2;

            for (String value : values) {
                if (checkFormatFile.formatToRead(size + temp * 2, Admin.SIZE, adminsInfoFile).equals(value)) {
                    countFoundedValues++;
                    if (countFoundedValues == values.length)
                        foundedAdmins[j++] = readAdminsInfo(checkFormatFile, (int) i);
                }
                temp = temp + 20;
            }
            countFoundedValues = 0;
            temp = primarySizeSearch;
        }
        return foundedAdmins;
    }
}
