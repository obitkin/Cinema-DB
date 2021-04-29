import tables.Table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main implements Data{

    public static void executeQuery(String tableName, String[] columnNames, List<String> data) {
        Table table = Table.of(tableName, columnNames);

        try (Connection conn = DriverManager.getConnection(url, username, password)){
            Statement statement = conn.createStatement();
            for (String param : data) {
                table.process(param.split(" "));
            }
            statement.executeUpdate(table.getQuery());
            System.out.println(tableName + " was created!");
        } catch(Exception ex){
            System.out.println(tableName + " was failed!");
            System.out.println(ex);
        }
    }

    static void createDB() {
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            Statement statement = conn.createStatement();
            createDB(statement, creation);
            System.out.println("Database has been created!");
        } catch(Exception ex){
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    static void createDB(Statement statement, String[] creators) throws SQLException {
        for (String creator : creators) {
            statement.executeUpdate(creator);
        }
    }

    public static void main(String[] args) {

        //createDB();

        try {
            executeQuery(
                    Hall_types,
                    new String[] {"description"},
                    Files.readAllLines(Path.of("src/main/java/data/Hall_types")));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}