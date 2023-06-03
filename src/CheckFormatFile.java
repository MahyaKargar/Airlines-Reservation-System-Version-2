import java.io.IOException;
import java.io.RandomAccessFile;

public class CheckFormatFile {
    /**
     * << correct the desired format for writing to the file. >>
     *
     * @param data the desired field to write.
     * @param size the number of characters to fix the length of data.
     * @return data in the correct format.
     */
    public String formatToWrite(String data, long size) {

        StringBuilder nameBuilder = new StringBuilder(data);
        while (nameBuilder.length() < size)
            nameBuilder.append(" ");
        data = nameBuilder.toString();

        return data.substring(0, (int) size);
    }

    /**
     * reading the desired data from the file in the number of sizes and correcting its format. >>
     *
     * @param sizeSeek the initial byte to start reading from the file.
     * @param endSize  the number of characters to read.
     * @param file     desired file that includes information and data.
     * @return data in the correct format.
     */
    public String formatToRead(long sizeSeek, long endSize, RandomAccessFile file) throws IOException {
        file.seek(sizeSeek);
        StringBuilder str = new StringBuilder();
        for (long i = 0; i < endSize; i++) {
            str.append(file.readChar());
        }
        return str.toString().trim();
    }
}
