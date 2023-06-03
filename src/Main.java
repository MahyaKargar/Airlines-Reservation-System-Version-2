import java.io.IOException;
import java.util.Scanner;

public class Main {
    /**
     * <h1><span style = "font-family:arial; color:#3F88D1" > Airlines Reservation System </h1>
     *
     * @author <h2><span stule = "font-style:italic; color:#8A2BE2" > Mahya Kargar </h2></b></span><hr>
     * <h3><span style = "font-family:bradly; color:#7EC736" >  E-mail : </span>
     * </h2><span style = "font-style:italic; color:#09736C7 " > kargar.mahya.82@gmail.com </span>
     * @since <h4><span style = "font-style:italic; color:43BFC7 "> 21 April 2023 </span>
     */
    public static void main(String[] args) throws IOException {
        Menu menu = new Menu();
        Scanner input = new Scanner(System.in);
        DataBaseFile dataBaseFile = new DataBaseFile();

        menu.mainMenu(input, dataBaseFile);

    }
}