package tables;

public class Table {

    public static Table of(String tableName, String[] columnsNames) {
        return new Table(tableName, columnsNames);
    }

    private String[] columnsNames;
    private String tableName;

    private StringBuilder query = null;

    private Table(String tableName, String[] columnsNames) {
        this.columnsNames = columnsNames;
        this.tableName = tableName;
    }

    public void process(String[] parameters) {
        if (columnsNames.length != parameters.length) {
            throw new RuntimeException(columnsNames.length + "   !=   " + parameters.length);
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
        query.append("\n(");
        query.append("'" + parameters[0] + "'");
        for (int i = 1; i < parameters.length; i++) {
            query.append(", ").append("'" + parameters[i] + "'");
        }
        query.append(")");
    }

    public String getQuery() {
        System.out.println(query.append(";").toString());
        return query.toString();
    }

}
