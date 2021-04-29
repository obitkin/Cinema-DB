import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Program{

    String  Hall_types = "Hall_types";
    String  Halls = "Halls";
    String  Halls_info = "Halls_info";
    String  Places = "Places";
    String  Genres = "Genres";
    String  Films = "Films";
    String  Films_info = "Films_info";
    String  Advertising = "Advertising";
    String  Newsreels = "Newsreels";
    String  Order_ad = "Order_ad";
    String  Statuses = "Statuses";
    String  Sessions = "Sessions";
    String  Tickets = "Tickets";
    String  Logs = "Logs";

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

    static String url = "jdbc:mysql://localhost/cinema";
    static String username = "root";
    static String password = "";

    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(url, username, password)){
//            Statement statement = conn.createStatement();
//            int rows = statement.executeUpdate("INSERT products(ProductName, Price) VALUES ('iPhone X', 76000)," +
//                    "('Galaxy S9', 45000), ('Nokia 9', 36000)");
//            System.out.printf("Added %d rows", rows);
            Statement statement = conn.createStatement();
            for (String s : creation)
                statement.executeUpdate(s);
            System.out.println("Database has been created!");
        } catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }


    static void remove(Statement statement, String[] removing) {
        for (String s : removing) {
            try {
                statement.executeUpdate(s);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}


/**
 * -classpath
 * /home/robert/IdeaProjects/DataBase-Kursach/libs/mysql-connector-java-8.0.16.jar;
 * /home/robert/IdeaProjects/DataBase-Kursach/target/classes
 */
//java -classpath c:\Java\mysql-connector-java-8.0.11.jar;c:\Java Program