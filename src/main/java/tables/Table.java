package tables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.List;

public class Table {

    public String tableName;
    public String[] columnsNames;

    static String url = "jdbc:mysql://localhost:3306/cinema";
    static String username = "root";
    static String password = "root";

    static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connections accepted!");
        } catch (SQLException throwables) {
            System.out.println("Connection failed...");
            System.out.println(throwables);
        }
    }

    private boolean isEmpty = true;
    private StringBuilder query = null;

    public Table(String tableName, String[] columnsNames) {
        this.tableName = tableName;
        this.columnsNames = columnsNames;
    }

    public Table INSERT(List<String> parameters) {
        if (parameters == null || parameters.size() == 0) {
            throw new IllegalArgumentException("parameters is NULL or EMPTY");
        }
        if (columnsNames.length != parameters.size()) {
            throw new IllegalArgumentException(columnsNames.length + "   !=   " + parameters.size());
        }
        if (query == null) {
            query = new StringBuilder("INSERT " + tableName + " (");
            query.append(columnsNames[0]);
            for (int i = 1; i < columnsNames.length; i++) {
                query.append(", ").append(columnsNames[i]);
            }
            query.append(")\n");
            query.append("VALUES");
        }
        if (isEmpty) {
            query.append("\n(");
            isEmpty = false;
        } else {
            query.append(",\n(");
        }
        query.append("'" + parameters.get(0) + "'");
        for (int i = 1; i < parameters.size(); i++) {
            query.append(", ").append("'" + parameters.get(i) + "'");
        }
        query.append(")");
        return this;
    }

    public String getQuery() {
        if (isEmpty) {
            throw new NoSuchElementException("Insert some data");
        }
        String tmp = query.append(";").toString();
        query = null;
        isEmpty = true;
        System.out.println(tmp);
        return tmp;
    }

    public void executeQuery() throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate(getQuery());
    }
}
