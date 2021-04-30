import data.CreationQuery;
import data.Data;
import tables.Table;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main implements CreationQuery, Data {

    static void insert(Table table, List<String[]> corteges) {
        for (String[] cortege : corteges) {
            table.INSERT(cortege);
        }
        try {
            table.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(table.tableName + " can't insert " + corteges.size() + " corteges");
        }
    }

    public static void main(String[] args) {

        createDB();

    }




    static void createDB() {
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            Statement statement = conn.createStatement();
            for (String creator : creation) {
                statement.executeUpdate(creator);
            }
            System.out.println("Database has been created!");
        } catch(Exception ex){
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }
}