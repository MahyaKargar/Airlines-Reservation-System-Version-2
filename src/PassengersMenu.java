import java.io.IOException;
import java.util.Scanner;

public class PassengersMenu {

    /**
     * << print the passengers' menu >>
     *
     * @param input an instance of the scanner class to get input.
     * @return command that has chosen by passenger to do his activity.
     */
    public String printPassengerMenu(Scanner input) {

        System.out.println("\n----------------------------------<< PASSENGER MENU OPTIONS >>------------------------------------------");
        System.out.println("<< 1-Change password >>\n<< 2-Search flight tickets >>\n<< 3-Booking tickets >>\n<< 4-Ticket cancellation >>");
        System.out.println("<< 5-Booked tickets >>\n<< 6-Add charge >>\n<< 0-sing out >>");
        System.out.print(">>\t");

        return input.next();
    }

    /**
     * << get the command from the passengers' menu >>
     * / Change password by entering command : 1
     * / Search flight tickets by entering command : 2
     * / Booking tickets by entering command : 3
     * / Ticket cancellation by entering command : 4
     * / Booked tickets by entering command : 5
     * / Add charge by entering command : 6
     *
     * @param input        an instance of the scanner class to get input.
     * @param dataBaseFile the class that includes instances of other classes.
     * @param passenger    the passenger 's information.
     */
    public void passengerMenu(Passenger passenger, DataBaseFile dataBaseFile, Scanner input) throws IOException {
        String command = printPassengerMenu(input);

        while (!command.equals("0")) {
            switch (command) {
                case "1" -> changePasswordInfoFile(input, passenger, dataBaseFile);
                case "2" -> searchFlightsInfoFile(dataBaseFile, input);
                case "3" -> bookingTicketInfo(dataBaseFile, input, passenger);
                case "4" -> cancellationTicketInfoFile(dataBaseFile, input, passenger);
                case "5" -> bookedTicketsFile(dataBaseFile, passenger);
                case "6" -> addChargeInfoFile(dataBaseFile, input, passenger);
                default -> System.out.println("<< Please try again >>");
            }
            command = printPassengerMenu(input);
        }
    }

    /**
     * << change the passenger 's password >>
     *
     * @param input        an instance of the scanner class to get input.
     * @param passenger    the passenger 's information.
     * @param dataBaseFile the class that includes instances of other classes.
     */
    public void changePasswordInfoFile(Scanner input, Passenger passenger, DataBaseFile dataBaseFile) throws IOException {
        System.out.println("----------------------------------<< Change password >>------------------------------------------");

        System.out.print("\n<< Please enter old password. >>\t");
        String previousPassword = input.next();

        if (dataBaseFile.passengersFile.searchPassenger(dataBaseFile.checkFormatFile, 0, passenger.getUserName(), previousPassword)[0] != null) {
            System.out.print("\n" + "=".repeat(100) + "\n<< << Please enter the new password. >> >>\t");
            dataBaseFile.passengersFile.updatePassenger(dataBaseFile.checkFormatFile, 20, 20, previousPassword, input.next());
            System.out.print("\n" + "=".repeat(100) + "\n<< Your password has been changed successfully. >>");
        } else
            System.out.print("\n" + "=".repeat(100) + "\n<< << These passwords aren't same >> >>");

    }

    /**
     * << to search flights, get the information of flight from the passenger >>
     *
     * @param dataBaseFile the class that includes instances of other classes.
     * @param input        an instance of the scanner class to get input.
     */
    public void searchFlightsInfoFile(DataBaseFile dataBaseFile, Scanner input) throws IOException {
        System.out.println("----------------------------------<< Search flight tickets >>------------------------------------------");

        System.out.print("<< Please enter origin >>\t");
        String origin = input.next();

        System.out.print("\n<< Please enter destination >>\t");
        String destination = input.next();

        System.out.print("\n<< Please enter date >>\t");
        String date = input.next();

        System.out.println("\n\t-----------------------------------------------------<< Flight schedules >>-----------------------------------------------------------------\n");
        System.out.printf("\t\t%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "FlightId", "origin", "Destination", "Date", "Time", "Prise", "Seats");
        System.out.println("\n\t" + "_".repeat(140));

        int i = 0;
        if (dataBaseFile.flightsFile.searchFlights(dataBaseFile.checkFormatFile, 20, origin, destination, date)[0] != null) {
            while (dataBaseFile.flightsFile.searchFlights(dataBaseFile.checkFormatFile, 20, origin, destination, date)[i] != null) {
                System.out.println("\t\t" + dataBaseFile.flightsFile.searchFlights(dataBaseFile.checkFormatFile, 20, origin, destination, date)[i]);
                System.out.println("\t" + "_".repeat(140));
                i++;
            }
        } else
            System.out.println("\n" + "=".repeat(100) + "\n<< There isn't any flight with these features. >>");
    }

    /**
     * << to booking ticket, get the information of flight from the passenger >>
     *
     * @param dataBaseFile the class that includes instances of other classes.
     * @param input        an instance of the scanner class to get input.
     * @param passenger    the passenger 's information.
     */
    public void bookingTicketInfo(DataBaseFile dataBaseFile, Scanner input, Passenger passenger) throws IOException {
        System.out.println("----------------------------------<< Booking tickets >>------------------------------------------");

        System.out.print("\n<< Please enter flightId >>\t");
        String flightId = input.next();

        if (dataBaseFile.flightsFile.searchFlights(dataBaseFile.checkFormatFile, 0, flightId)[0] != null && (Integer.parseInt(dataBaseFile.passengersFile.findFieldsFromPassenger(passenger.getUserName(), dataBaseFile.checkFormatFile, 40)) >= Integer.parseInt(dataBaseFile.flightsFile.findFieldsFromFlight(flightId, dataBaseFile.checkFormatFile, 100))) && Integer.parseInt(dataBaseFile.flightsFile.findFieldsFromFlight(flightId, dataBaseFile.checkFormatFile, 120)) > 0) {
            long updateCharge = Long.parseLong(dataBaseFile.passengersFile.findFieldsFromPassenger(passenger.getUserName(), dataBaseFile.checkFormatFile, 40)) - Integer.parseInt(dataBaseFile.flightsFile.findFieldsFromFlight(flightId, dataBaseFile.checkFormatFile, 100));
            dataBaseFile.passengersFile.updatePassenger(dataBaseFile.checkFormatFile, 0, 40, passenger.getUserName(), String.valueOf(updateCharge));

            int updateSeat = Integer.parseInt(dataBaseFile.flightsFile.findFieldsFromFlight(flightId, dataBaseFile.checkFormatFile, 120)) - 1;
            dataBaseFile.flightsFile.updateFlight(dataBaseFile.checkFormatFile, 0, 120, flightId, String.valueOf(updateSeat));

            String ticketId = dataBaseFile.ticketsFile.writeTicketInfoIno(passenger, flightId, dataBaseFile);
            System.out.println("=".repeat(100));
            System.out.println("<< Reserve was done successfully >>\n" + "=".repeat(100) + "\n<< Your ticketId:  " + ticketId + " >>");
            System.out.println("<< Your charge :  " + updateCharge + " >>");

        } else
            System.out.println("=".repeat(100) + "\n<< You can't reserve flight >>");

    }

    /**
     * << to cancel ticket, get the information of flight from the passenger >>
     *
     * @param dataBaseFile the class that includes instances of other classes.
     * @param input        an instance of the scanner class to get input.
     * @param passenger    the passenger 's information.
     */

    public void cancellationTicketInfoFile(DataBaseFile dataBaseFile, Scanner input, Passenger passenger) throws IOException {
        System.out.println("----------------------------------<< Ticket cancellation >>------------------------------------------");

        System.out.print("\n<< Please enter your ticketId >>\t");
        String ticketId = input.next();
        if (dataBaseFile.ticketsFile.searchTicket(dataBaseFile.checkFormatFile, 20, ticketId)[0] != null) {

            long updatedCharge = Long.parseLong(dataBaseFile.passengersFile.findFieldsFromPassenger(passenger.getUserName(), dataBaseFile.checkFormatFile, 40)) + Integer.parseInt(dataBaseFile.ticketsFile.findFieldsTicket(ticketId, dataBaseFile.checkFormatFile, 140));
            dataBaseFile.passengersFile.updatePassenger(dataBaseFile.checkFormatFile, 0, 40, passenger.getUserName(), String.valueOf(updatedCharge));

            int updatedSeat = Integer.parseInt(dataBaseFile.ticketsFile.findFieldsTicket(ticketId, dataBaseFile.checkFormatFile, 160)) + 1;
            dataBaseFile.flightsFile.updateFlight(dataBaseFile.checkFormatFile, 0, 120, dataBaseFile.ticketsFile.findFieldsTicket(ticketId, dataBaseFile.checkFormatFile, 40), String.valueOf(updatedSeat));

            dataBaseFile.ticketsFile.removeTickets(dataBaseFile.checkFormatFile, 20, ticketId);
            System.out.println("=".repeat(100) + "\n<< Remove was done successfully >>");
        } else
            System.out.println("=".repeat(100) + "\n<< There isn't any flights with this ticketId >>");
    }

    /**
     * << to add charge , get the information if flight from the passenger >>
     *
     * @param dataBaseFile the class that includes instances of other classes.
     * @param input        an instance of the scanner class to get input.
     * @param passenger    the passenger 's information.
     */

    public void addChargeInfoFile(DataBaseFile dataBaseFile, Scanner input, Passenger passenger) throws IOException {
        System.out.println("----------------------------------<< Add charge >>------------------------------------------");

        System.out.print("\n<< How much do you want to add to your credit? >>\t");
        String addedCharge = input.next();
        String previousCharge = dataBaseFile.passengersFile.findFieldsFromPassenger(passenger.getUserName(), dataBaseFile.checkFormatFile, 40);

        String updatedCharge = null;
        try {
            updatedCharge = String.valueOf(Long.parseLong(previousCharge) + Long.parseLong(addedCharge));
        } catch (Exception e) {
            System.out.println("<< Try again >>");
            addedCharge = input.next();
        }

        while (addedCharge.length() > 10 || !dataBaseFile.checkFormat.checkNumber(addedCharge)) {
            System.out.println("<< Try again >>");
            System.out.print("\n<< How much do you want to add to your credit? >>\t");
            addedCharge = input.next();
        }


        dataBaseFile.passengersFile.updatePassenger(dataBaseFile.checkFormatFile, 0, 40, passenger.getUserName(), updatedCharge);
        System.out.print("\nCurrently, your credit is -->>\t" + updatedCharge);
    }

    /**
     * << Show the tickets booked by the passenger >>
     *
     * @param dataBaseFile the class that includes instances of other classes.
     * @param passenger    the passenger 's information.
     */

    public void bookedTicketsFile(DataBaseFile dataBaseFile, Passenger passenger) throws IOException {
        System.out.println("\t------------------------------------------------------------------------------<< Booked tickets >>-------------------------------------------------------------------------------\n");

        System.out.printf("\t\t%-20s%-20s%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "Username", "TicketId", "FlightId", "Origin", "Destination", "Date", "Time", "Prise", "Seats");
        System.out.println("\n\t" + "_".repeat(180));

        int i = 0;
        while (dataBaseFile.ticketsFile.searchTicket(dataBaseFile.checkFormatFile, 0, passenger.getUserName())[i] != null) {
            System.out.println("\t\t" + dataBaseFile.ticketsFile.searchTicket(dataBaseFile.checkFormatFile, 0, passenger.getUserName())[i]);
            System.out.println("\t" + "_".repeat(180));
            i++;
        }
    }

}
