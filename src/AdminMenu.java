import java.io.IOException;
import java.util.Scanner;

public class AdminMenu {

    /**
     * << print the menu of admin >>
     *
     * @param input an instance of the scanner class to get input.
     * @return command that has chosen by admin to do his activity.
     */
    public String printAdminMenu(Scanner input) {

        System.out.println("----------------------------------<< ADMIN MENU OPTIONS >>------------------------------------------");
        System.out.println("<< 1-Add >>\n<< 2-Update >>\n<< 3-Remove >>\n<< 4-Flight schedules >>\n<< 0-Sing out >>");
        System.out.print(">>\t");

        return input.next();
    }

    /**
     * get the command from the admin menu >>
     * / add flights by entering command : 1
     * / update flights by entering command : 2
     * / remove Flights by entering command : 3
     * / Flight schedules by entering command : 4
     *
     * @param input        an instance of the scanner class to get input.
     * @param dataBaseFile the class that includes instances of other classes.
     */
    public void adminMenu(DataBaseFile dataBaseFile, Scanner input) throws IOException {
        String command = printAdminMenu(input);

        while (!command.equals("0")) {
            switch (command) {
                case "1" -> addFlightInFile(dataBaseFile, input);
                case "2" -> updateFlightFromFile(dataBaseFile, input);
                case "3" -> removeFlightFromFile(dataBaseFile, input);
                case "4" -> flightSchedulesFile(dataBaseFile);
                default -> System.out.println("<< Please try again >>");
            }
            command = printAdminMenu(input);
        }
    }

    /**
     * << to add each flight, get the information if flight from the admin and checking the format of data >>
     *
     * @param dataBaseFile the class that includes instances of other classes.
     * @param input        an instance of the scanner class to get input.
     */
    public void addFlightInFile(DataBaseFile dataBaseFile, Scanner input) throws IOException {
        System.out.println("-----------------------------------------<< Add Flights >>-------------------------------------------\n");

        System.out.print("<< How many flights do you one to add? >>\t");
        String countAddFlight = input.next();
        countAddFlight = checkData("8", countAddFlight, dataBaseFile, input);

        for (int i = 0; i < Integer.parseInt(countAddFlight); i++) {
            System.out.println("\n<< Add Flights >>");

            System.out.print("\n<< Please enter flightId to add >>\t");
            String flightId = input.next();
            flightId = checkData("1", flightId, dataBaseFile, input);

            System.out.print("\n" + "=".repeat(100) + "\n<< Please enter origin to add >>   (( Please enter the first letter in uppercase )) \t");
            String origin = input.next();
            origin = checkData("2", origin, dataBaseFile, input);

            System.out.print("\n" + "=".repeat(100) + "\n<< Please enter destination to add >>    (( Please enter the first letter in uppercase )) \t");
            String destination = input.next();

            destination = checkData("3", destination, dataBaseFile, input);
            while (destination.equals(origin)) {
                System.out.print("\n<< Please try again >>\t");
                destination = input.next();
            }

            System.out.print("\n" + "=".repeat(100) + "\n<< Please enter date to add >> (( Please enter date like: --/--/-- ))\t");
            String date = input.next();
            date = checkData("4", date, dataBaseFile, input);

            System.out.print("\n" + "=".repeat(100) + "\n<< Please enter time to add >>  (( Please enter time like: --:-- ))\t");
            String time = input.next();
            time = checkData("5", time, dataBaseFile, input);

            System.out.print("\n" + "=".repeat(100) + "\n<< Please enter price to add >>\t");
            String price = input.next();
            price = checkData("6", price, dataBaseFile, input);

            System.out.print("\n" + "=".repeat(100) + "\n<< Please enter seats to add >>\t");
            String seats = input.next();
            seats = checkData("7", seats, dataBaseFile, input);

            dataBaseFile.flightsFile.writeFlightsInfo(new Flight(flightId, origin, destination, date, time, price, seats), dataBaseFile.checkFormatFile);
            System.out.println("\n<< The desired flight was registered successfully. >>\n");
        }
    }

    /**
     * << to update each flight, get the information of flight  from the admin and checking the format of data >>
     *
     * @param dataBaseFile the class that includes instances of other classes.
     * @param input        an instance of the scanner class to get input.
     */

    public void updateFlightFromFile(DataBaseFile dataBaseFile, Scanner input) throws IOException {
        System.out.println("------------------------------------<< Update Flights >>----------------------------------------------\n");

        System.out.print("<< How many flights do you one to update? >>\t");
        String countUpdateFlight = input.next();
        countUpdateFlight = checkData("8", countUpdateFlight, dataBaseFile, input);

        for (int k = 0; k < Integer.parseInt(countUpdateFlight); k++) {
            System.out.print("\n" + "=".repeat(120) + "\n\tUpdate flight ---->>\n");

            System.out.print("\n<< Please enter FlightId >>\t");
            String flightId = input.next();
            if (dataBaseFile.flightsFile.searchFlights(dataBaseFile.checkFormatFile, 0, flightId)[0] == null) {
                countUpdateFlight = String.valueOf(Integer.parseInt(countUpdateFlight) + 1);
                System.out.println("=".repeat(120) + "\n<< There is no flight with this flightId >>");
                continue;

            } else if (dataBaseFile.ticketsFile.searchTicket(dataBaseFile.checkFormatFile, 40, flightId)[0] != null) {
                System.out.println("=".repeat(120) + "\n<< This flight is already booked; You can't update it >>");
                continue;
            }

            String command = selectFieldUpdateFlight(input, dataBaseFile);
            if (command == null)
                break;

            String data;
            switch (command) {
                case "1" -> {
                    System.out.print("\n<< Please enter flightId to update >>\t");
                    data = input.next();
                    dataBaseFile.flightsFile.updateFlight(dataBaseFile.checkFormatFile, 0, 0, flightId, checkData("1", data, dataBaseFile, input));
                }
                case "2" -> {
                    System.out.print("\n<< Please enter origin to update >>  (( Please enter the first letter in uppercase ))\t");
                    data = input.next();
                    data = checkData("2", data, dataBaseFile, input);
                    while (data.equals(dataBaseFile.flightsFile.findFieldsFromFlight(flightId, dataBaseFile.checkFormatFile, 40))) {
                        System.out.print("\n<< Please try again >>\t");
                        data = input.next();
                    }

                    dataBaseFile.flightsFile.updateFlight(dataBaseFile.checkFormatFile, 0, 20, flightId, checkData("2", data, dataBaseFile, input));
                }
                case "3" -> {
                    System.out.print("\n<< Please enter destination to update >>  (( Please enter the first letter in uppercase ))\t");
                    data = input.next();
                    data = checkData("3", data, dataBaseFile, input);
                    while (data.equals(dataBaseFile.flightsFile.findFieldsFromFlight(flightId, dataBaseFile.checkFormatFile, 20))) {
                        System.out.print("\n<< Please try again >>\t");
                        data = input.next();
                    }

                    dataBaseFile.flightsFile.updateFlight(dataBaseFile.checkFormatFile, 0, 40, flightId, checkData("3", data, dataBaseFile, input));
                }
                case "4" -> {
                    System.out.print("\n<< Please enter date to update >>  (( Please enter date like: --/--/-- ))\t");
                    data = input.next();
                    dataBaseFile.flightsFile.updateFlight(dataBaseFile.checkFormatFile, 0, 60, flightId, checkData("4", data, dataBaseFile, input));
                }
                case "5" -> {
                    System.out.print("\n<< Please enter time to update >>  (( Please enter date like: --:-- ))\t");
                    data = input.next();
                    dataBaseFile.flightsFile.updateFlight(dataBaseFile.checkFormatFile, 0, 80, flightId, checkData("5", data, dataBaseFile, input));
                }
                case "6" -> {
                    System.out.print("\n<< Please enter price to update >>\t");
                    data = input.next();
                    dataBaseFile.flightsFile.updateFlight(dataBaseFile.checkFormatFile, 0, 100, flightId, checkData("6", data, dataBaseFile, input));
                }
                case "7" -> {
                    System.out.print("\n<< Please enter seats to update >>\t");
                    data = input.next();
                    dataBaseFile.flightsFile.updateFlight(dataBaseFile.checkFormatFile, 0, 120, flightId, checkData("8", data, dataBaseFile, input));
                }
            }
        }
    }

    /**
     * << to remove each flight, get the information of flight  from the admin  >>
     *
     * @param dataBaseFile the class that includes instances of other classes.
     * @param input        an instance of the scanner class to get input.
     */

    public void removeFlightFromFile(DataBaseFile dataBaseFile, Scanner input) throws IOException {
        System.out.println("------------------------------------<< Remove Flights >>----------------------------------------------\n");
        System.out.print("<< How many flights do you one to remove? >>\t");
        String countRemoveFlight = input.next();
        countRemoveFlight = checkData("8", countRemoveFlight, dataBaseFile, input);

        for (int i = 0; i < Integer.parseInt(countRemoveFlight); i++) {
            System.out.println("=".repeat(150) + "\n<< Remove Flights >>\n" + "=".repeat(150));
            System.out.print("<< Please enter flightId >>\t");
            String flightId = input.next();

            if (dataBaseFile.flightsFile.searchFlights(dataBaseFile.checkFormatFile, 0, flightId)[0] == null) {
                countRemoveFlight = String.valueOf(Integer.parseInt(countRemoveFlight) + 1);
                System.out.println("=".repeat(120) + "\n<< There is no flight with this flightId >>");
                continue;
            }
            if (dataBaseFile.ticketsFile.searchTicket(dataBaseFile.checkFormatFile, 40, flightId)[0] != null) {
               dataBaseFile.ticketsFile.removeTickets(dataBaseFile.checkFormatFile, 40, flightId);
            }
            dataBaseFile.flightsFile.removeFlights(dataBaseFile.checkFormatFile, 0, flightId);
        }
    }

    /**
     * << show flights for admin >>
     *
     * @param dataBaseFile the class that includes instances of other classes.
     */
    public void flightSchedulesFile(DataBaseFile dataBaseFile) throws IOException {
        System.out.println("\n\t-----------------------------------------------------<< Flight schedules >>-----------------------------------------------------------------\n");
        System.out.printf("\t\t%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "FlightId", "origin", "Destination", "Date", "Time", "Prise", "Seats");
        System.out.println("\n\t" + "_".repeat(140));

        for (long i = 0; i < dataBaseFile.flightsFile.getFlightsFile().length() / 280; i++) {
            System.out.println("\t\t" + dataBaseFile.flightsFile.readFlightInfo(dataBaseFile.checkFormatFile, i));
            System.out.println("\t" + "_".repeat(140));
        }
        System.out.println();
    }

    /**
     * << check the format of the information of flight >>
     *
     * @param command      choose which format to check.
     * @param data         information of flight.
     * @param dataBaseFile the class that includes instances of other classes.
     * @param input        an instance of the scanner class to get input.
     * @return correct data format.
     */


    public String checkData(String command, String data, DataBaseFile dataBaseFile, Scanner input) throws IOException {
        switch (command) {
            case "1" -> {
                while (dataBaseFile.flightsFile.searchFlights(dataBaseFile.checkFormatFile, 0, data)[0] != null) {
                    System.out.print("\n<< Please try again >>\t");
                    data = input.next();
                }
                return data;
            }
            case "2", "3" -> {
                while (!dataBaseFile.checkFormat.checkIsLetter(data)) {
                    System.out.print("\n<< Please try again >>\t");
                    data = input.next();
                }
                return data;
            }
            case "4" -> {
                while (!dataBaseFile.checkFormat.checkDate(data)) {
                    System.out.print("\n<< Please try again >>\t");
                    data = input.next();
                }
                return data;
            }
            case "5" -> {
                while (!dataBaseFile.checkFormat.checkTime(data)) {
                    System.out.print("\n<< Please try again >>\t");
                    data = input.next();
                }
                return data;
            }
            case "6", "7" -> {
                while (!dataBaseFile.checkFormat.checkNumber(data) || Integer.parseInt(data) == 0) {
                    System.out.print("\n<< Please try again >>\t");
                    data = input.next();
                }
            }
            case "8" -> {
                while (!dataBaseFile.checkFormat.checkNumber(data) || Integer.parseInt(data) < 0) {
                    System.out.print("\n<< Please try again >>\t");
                    data = input.next();
                }
                return data;
            }
        }
        return data;
    }

    /**
     * << give the number of  field from admin for update >>
     *
     * @param input        an instance of the scanner class to get input.
     * @param dataBaseFile the class that includes instances of other classes.
     * @return selected field of flight for update.
     */
    public String selectFieldUpdateFlight(Scanner input, DataBaseFile dataBaseFile) throws IOException {
        System.out.println("\n\t\t-->> Which of the following fields do you want to change? -->>");
        System.out.println("____________________________________________________________________________________");
        System.out.println("\n<< 1-FlightId >>\n<< 2-Origin >>\n<< 3-Destination >>\n<< 4-Date >>\n<< 5-Time >>\n<< 6-Price >>\n<< 7-Seats >>");
        System.out.print(">>\t");

        String command = input.next();
        command = checkData("8", command, dataBaseFile, input);
        if (Integer.parseInt(command) > 7)
            System.out.println("=".repeat(120) + "\n<< This field wasn't found >>");
        else
            return command;

        return null;
    }
}




