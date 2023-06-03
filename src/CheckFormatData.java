import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckFormatData {

    /**
     * << Checking the written state of the desired data that all characters are letters. >>
     *
     * @param data information of flight.
     * @return true if the format was correct otherwise false.
     */
    public boolean checkIsLetter(String data) {
        if (!(data.toCharArray()[0] >= 'A' && data.toCharArray()[0] <= 'Z'))
            return false;

        for (int i = 1; i < data.length(); i++) {
            if (!(data.toCharArray()[i] >= 'a' && data.toCharArray()[i] <= 'z'))
                return false;
        }
        return true;
    }

    /**
     * << Checking the written state of the desired data that all characters are numbers. >>
     *
     * @param data information of flight.
     * @return true if the format was correct otherwise false.
     */
    public boolean checkNumber(String data) {
        for (int i = 0; i < data.toCharArray().length; i++)
            if (!(data.toCharArray()[i] >= '0' && data.toCharArray()[i] <= '9'))
                return false;
        return true;
    }

    /**
     * << check the valid format of date >>
     *
     * @param data the date of flight.
     * @return true if the format was correct otherwise false
     */
    public boolean checkDate(String data) {

        Matcher matcher = Pattern.compile("\\d{4}/\\d{2}/\\d{2}").matcher(data);

        if (matcher.find()) {
            String[] digits = data.split("/");
            int[] convertedDigits = new int[digits.length];

            for (int i = 0; i < digits.length; i++)
                convertedDigits[i] = Integer.parseInt(digits[i]);

            boolean yearCheck = convertedDigits[0] > 1401 && convertedDigits[0] < 1410;
            boolean monthCheck = convertedDigits[1] > 0 && convertedDigits[1] < 13;
            boolean dayCheck = convertedDigits[2] > 0 && convertedDigits[2] < 31;

            if ((convertedDigits[1] > 0 && convertedDigits[1] < 7))
                dayCheck = convertedDigits[2] > 0 && convertedDigits[2] < 32;

            return monthCheck && dayCheck && yearCheck;
        }
        return false;

    }

    /**
     * << check the valid format of time >>
     *
     * @param data the time of flight.
     * @return true if the format was correct otherwise false.
     */

    public boolean checkTime(String data) {
        Matcher matcher = Pattern.compile("\\d{2}:\\d{2}").matcher(data);
        if (matcher.find()) {
            String[] digits = data.split(":");
            int[] convertedDigits = new int[digits.length];

            for (int i = 0; i < digits.length; i++)
                convertedDigits[i] = Integer.parseInt(digits[i]);

            boolean hourCheck = convertedDigits[0] >= 0 && convertedDigits[0] < 24;
            boolean minCheck = convertedDigits[1] >= 0 && convertedDigits[1] < 60;

            return hourCheck && minCheck;
        }
        return false;
    }
}
