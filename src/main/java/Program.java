import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.function.Supplier;

public class Program{

    /**
     * USE cinema; SHOW TABLES; DROP DATABASE cinema; CREATE DATABASE cinema;
     */

    static Random random = new Random();

    static String  Hall_types = "Hall_types";
    static String  Halls = "Halls";
    static String  Halls_info = "Halls_info";
    static String  Places = "Places";
    static String  Genres = "Genres";
    static String  Films = "Films";
    static String  Films_info = "Films_info";
    static String  Advertising = "Advertising";
    static String  Newsreels = "Newsreels";
    static String  Order_ad = "Order_ad";
    static String  Statuses = "Statuses";
    static String  Sessions = "Sessions";
    static String  Tickets = "Tickets";
    static String  Logs = "Logs";

    static String[] creation = new String[] {
            "CREATE TABLE Hall_types (hall_type_id INT PRIMARY KEY AUTO_INCREMENT, description VARCHAR(20))",
            "CREATE TABLE Halls (hall_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(40))",
            "CREATE TABLE Halls_info (" +
                    "info_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "hall_id INT, " +
                    "hall_type_id INT," +
                    "FOREIGN KEY (hall_id)  REFERENCES Halls (hall_id)," +
                    "FOREIGN KEY (hall_type_id)  REFERENCES Hall_types (hall_type_id))",
            "CREATE TABLE Places (" +
                    "place_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "hall_id INT, " +
                    "row SMALLINT," +
                    "seat SMALLINT," +
                    "FOREIGN KEY (hall_id)  REFERENCES Halls (hall_id))",
            "CREATE TABLE Genres (" +
                    "genre_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50))",
            "CREATE TABLE Films (" +
                    "film_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(200), " +
                    "age_rating TINYINT, " +
                    "description TEXT, " +
                    "year YEAR, " +
                    "country VARCHAR(100), " +
                    "duration TIME, " +
                    "poster TEXT, " +
                    "video TEXT)",
            "CREATE TABLE Films_info (" +
                    "info_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "film_id INT, " +
                    "genre_id INT," +
                    "FOREIGN KEY (film_id)  REFERENCES Films(film_id)," +
                    "FOREIGN KEY (genre_id)  REFERENCES Genres(genre_id))",
            "CREATE TABLE Advertising (" +
                    "ad_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50), " +
                    "age_rating TINYINT," +
                    "video TEXT)",
            "CREATE TABLE Newsreels (" +
                    "newsreel_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50))",
            "CREATE TABLE Order_ad (" +
                    "order_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "newsreel_id INT, " +
                    "ad_id INT," +
                    "ordering INT," +
                    "FOREIGN KEY (newsreel_id) REFERENCES Newsreels (newsreel_id)," +
                    "FOREIGN KEY (ad_id) REFERENCES Advertising(ad_id))",
            "CREATE TABLE Statuses (" +
                    "status_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "description VARCHAR(40))",
            "CREATE TABLE Sessions (" +
                    "session_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "hall_id INT, " +
                    "film_id INT," +
                    "newsreel_id INT, " +
                    "time DATETIME, " +
                    "FOREIGN KEY (hall_id)  REFERENCES Halls(hall_id)," +
                    "FOREIGN KEY (film_id)  REFERENCES Films(film_id)," +
                    "FOREIGN KEY (newsreel_id)  REFERENCES Newsreels(newsreel_id))",
            "CREATE TABLE Tickets (" +
                    "ticket_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "place_id INT, " +
                    "status_id INT," +
                    "session_id INT, " +
                    "booking_code VARCHAR(5), " +
                    "FOREIGN KEY (place_id)  REFERENCES Places(place_id)," +
                    "FOREIGN KEY (status_id)  REFERENCES Statuses(status_id)," +
                    "FOREIGN KEY (session_id)  REFERENCES Sessions(session_id))",
            "CREATE TABLE Logs (" +
                    "log_id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "ticket_id INT, " +
                    "status_id INT," +
                    "change_time DATETIME, " +
                    "FOREIGN KEY (ticket_id)  REFERENCES Tickets(ticket_id)," +
                    "FOREIGN KEY (status_id)  REFERENCES Statuses(status_id))"
    };

    static void creation(String[] tables) {
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            Statement statement = conn.createStatement();
            for (String s : tables) {
                statement.executeUpdate(s);
            }
            System.out.println("Создание таблиц успешно!");
        } catch(Exception ex){
            System.out.println("Таблицы упали...");
            System.out.println(ex);
        }
    }

    static String url = "jdbc:mysql://localhost/cinema";
    static String username = "root";
    static String password = "";

    static int Hall_typesINT = 30;
    static int HallsINT = 30;
    static int GenresINT = 30;
    static int NewsreelsINT = 30;
    static int StatusesINT = 30;

    static String[] Hall_typesString = new String[] {"3D", "IMAX", "KINO", "VIDEO", "4D", "5D"};
    static String[] HallsString = new String[] {"Зал №1", "Зал №2", "Зал №3","Зал №4","Зал №5","Зал №6","Зал VIP"};
    static String[] GenresString = new String[] {"Comedy","Drama","Shooter","War","Anime",
            "Horror","Slasher","Documential","Fantasy","Fantastic",};
    static String[] NewsreelsString = new String[] {"Newsreel 1","Newsreel 2","Newsreel 3",
            "Newsreel 4","Newsreel 5",};
    static String[] StatusesString = new String[] {"Free", "Bought online", "Bought offline",
            "Reserved", "Used", "Not used"};


    static void generateSlovary() {
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            Statement statement = conn.createStatement();
            statement.executeUpdate("SET NAMES 'utf8'");
            generate(statement, Hall_types, "description", new SupplierMy(Hall_typesString), Hall_typesINT);
            generate(statement, Halls, "name", new SupplierMy(HallsString), HallsINT);
            generate(statement, Genres, "name", new SupplierMy(GenresString), GenresINT);
            generate(statement, Newsreels, "name", new SupplierMy(NewsreelsString), NewsreelsINT);
            generate(statement, Statuses, "description", new SupplierMy(StatusesString), StatusesINT);
            System.out.println("Словари загружены!");
        } catch(Exception ex){
            System.out.println("Словари упали...");
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {

        creation(creation);

        generateSlovary();

    }

    static void generate(Statement st, String table, String column, Supplier<String> sup, int times) {
        StringBuilder x = new StringBuilder("INSERT " + table + "(" + column + ") VALUES (\"" + sup.get() + "\")");
        for (int i = 0; i < times - 1; i++) {
            String generated = "(\"" + sup.get() + "\")";
            x.append("," + generated);
        }
        x.append(";");
        try {
            st.executeUpdate(x.toString());
            System.out.println("Table " + table + " generated");
        } catch (SQLException throwables) {
            System.out.println("Table " + table + " fall");
            throwables.printStackTrace();
        }
    }



}


/**
 * -classpath
 * /home/robert/IdeaProjects/DataBase-Kursach/libs/mysql-connector-java-8.0.16.jar;
 * /home/robert/IdeaProjects/DataBase-Kursach/target/classes
 */
//java -classpath c:\Java\mysql-connector-java-8.0.11.jar;c:\Java Program