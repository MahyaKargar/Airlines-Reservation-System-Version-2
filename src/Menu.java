import java.io.IOException;
import java.util.Scanner;

public class Menu {

    private final PassengersMenu passengersMenu = new PassengersMenu();
    private final AdminMenu adminMenu = new AdminMenu();

    /**
     * << print the menu >>
     *
     * @param input an instance of the scanner class to get input.
     * @return command that has chosen by users and admin to sing in or sing up
     */
    public String printStartMenu(Scanner input) {
        System.out.println("\n---------------------------------------------------------------------------------------------------");
        System.out.print("\t\t\t\t\t\t\tWELCOME TO AIRLINE RESERVATION SYSTEM\n");
        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println("------------------------------------<< MENU OPTIONS >>---------------------------------------------");
        System.out.println("\n\t\t\t\t<< 1-Sing in >> \t\t << 2-Sing up >>\t\t << 3-Exit >>");
        System.out.print(">>\t");

        return input.next();
    }

    /**
     * << get the command from the start menu >>
     * / sing in by entering command : 1
     * / sing up by entering command : 2
     * / Exit by entering command : 3
     *
     * @param input        an instance of the scanner class to get input.
     * @param dataBaseFile the class that includes instances of other classes.
     */

    public void mainMenu(Scanner input, DataBaseFile dataBaseFile) throws IOException {

        String command = printStartMenu(input);
        while (!command.equals("3")) {
            switch (command) {
                case "1" -> singIn(input, dataBaseFile);
                case "2" -> singUp(input, dataBaseFile);
                default -> System.out.println("<< Please try again >>");
            }
            command = printStartMenu(input);
        }

    }

    /**
     * << The user or admin can enter their information  including username and password, if they have already signed up >>
     *
     * @param input        an instance of the scanner class to get input.
     * @param dataBaseFile the class that includes instances of other classes.
     */
    public void singIn(Scanner input, DataBaseFile dataBaseFile) throws IOException {
        System.out.println("--------------------------------------<< Sing in >>-------------------------------------------------");

        System.out.print("This is username >>\t");
        String userName = input.next();

        System.out.print("This is password >>\t");
        String password = input.next();

        Passenger passenger = new Passenger(userName, password, "0");

        if (passenger.getUserName().equals(dataBaseFile.checkFormatFile.formatToRead(0, 20, dataBaseFile.adminsFile.getAdminsInfoFile())) && passenger.getPassword().equals(dataBaseFile.checkFormatFile.formatToRead(20, 20, dataBaseFile.adminsFile.getAdminsInfoFile())))
            adminMenu.adminMenu(dataBaseFile, input);

        else if (dataBaseFile.passengersFile.searchPassenger(dataBaseFile.checkFormatFile, 0, passenger.getUserName(), passenger.getPassword())[0] != null)
            passengersMenu.passengerMenu(passenger, dataBaseFile, input);

        else
            System.out.println("\n<< No account found with this information. >>");

    }

    /**
     * users enter password and username to be created account for themselves.
     *
     * @param input        an instance of the scanner class to get input.
     * @param dataBaseFile the class that includes instances of other classes.
     */
    public void singUp(Scanner input, DataBaseFile dataBaseFile) throws IOException {
        System.out.println("--------------------------------------<< Sing up >>-------------------------------------------------");

        System.out.print("This is username >>\t");
        String userName = input.next();

        while (dataBaseFile.passengersFile.searchPassenger(dataBaseFile.checkFormatFile, 0, userName)[0] != null || dataBaseFile.adminsFile.searchAdmins(dataBaseFile.checkFormatFile, 0, userName)[0] != null) {
            System.out.print("\n<< This username had used. Please enter new username >>\t");
            userName = input.next();
        }

        System.out.print("This is password >>\t");
        String password = input.next();

        dataBaseFile.passengersFile.writePassengersInfo(new Passenger(userName, password, "0"), dataBaseFile.checkFormatFile);
        System.out.println("\n<< Your information has been successfully registered. >>\n");

    }
}

