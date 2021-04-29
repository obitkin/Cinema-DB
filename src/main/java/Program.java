import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
    static String password = "After1901";

    static int Hall_typesINT = 30;
    static int HallsINT = 30;
    static int GenresINT = 30;
    static int NewsreelsINT = 30;
    static int StatusesINT = 30;
    static int filmINT = 1000;
    static int StatusINT = 30;

    static String[] Hall_typesString = new String[] {"3D", "4D", "5D", "IMAX", "KINO", "VIDEO"};
    static String[] HallsString = new String[] {"Hall №1", "Hall №2", "Hall №3","Hall №4","Hall №5","Hall №6","Hall VIP"};
    static String[] GenresString = new String[] {"Comedy","Drama","Shooter","War","Anime",
            "Horror","Slasher","Documental","Fantasy","Fantastic",};
    static String[] NewsreelsString = new String[] {"Newsreel 1","Newsreel 2","Newsreel 3",
            "Newsreel 4","Newsreel 5",};
    static String[] StatusesString = new String[] {"Free", "Bought online", "Bought offline",
            "Reserved", "Used", "Not used"};


    static void generateSlovary() {
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            Statement statement = conn.createStatement();
            statement.executeUpdate("SET NAMES 'utf8'");
            generate(statement, Hall_types, "description", Hall_typesString);
            generate(statement, Halls, "name", HallsString);
            generate(statement, Genres, "name", GenresString);
            generate(statement, Newsreels, "name", NewsreelsString);
            generate(statement, Statuses, "description", StatusesString);
            System.out.println("Словари загружены!");
        } catch(Exception ex){
            System.out.println("Словари упали...");
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {

        creation(creation);

        generateSlovary();

        films();

    }

    static String get(List<String> list, int maxSize) {
        StringBuilder res = new StringBuilder();
        String r = list.get(random.nextInt(list.size()));
        while (res.length() +  r.length() < maxSize) {
            res.append(" ").append(r);
            r = list.get(random.nextInt(list.size()));
        }
        return res.toString();
    }

    static void generate(Statement st, String table, String column, String[] sup) {
        StringBuilder x = new StringBuilder("INSERT " + table + "(" + column + ") VALUES (\"" + sup[0] + "\")");
        for (int i = 1; i < sup.length; i++) {
            String generated = "(\"" + sup[i] + "\")";
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

    static void films() {
        Path file = Path.of("src/main/resources/linuxwords.txt");
         List<String> stringList = null;
        try {
            stringList = Files.readAllLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            Statement statement = conn.createStatement();
            for (int i = 0; i < filmINT; i++) {
                statement.executeUpdate("INSERT " + Films + " (name, age_rating, year, country)" +
                        " VALUES (\"" + get(stringList, 50)+ "\","+ random.nextInt(22) +", " + 2000 + random.nextInt(20)+", \"Russia\")");
            }
            System.out.println("Словари загружены!");
        } catch(Exception ex){
            System.out.println("Словари упали...");
            System.out.println(ex);
        }
    }



}


/**
 * -classpath
 * /home/robert/IdeaProjects/DataBase-Kursach/libs/mysql-connector-java-8.0.16.jar;
 * /home/robert/IdeaProjects/DataBase-Kursach/target/classes
 */
//java -classpath c:\Java\mysql-connector-java-8.0.11.jar;c:\Java Program