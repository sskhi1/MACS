import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class MetropolisesModel extends AbstractTableModel implements MetropolisTableOperations{
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData resultSetMetaData;
    String Query;

    public MetropolisesModel(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Metropolises_DB",
                    "root", "password");
            statement = connection.createStatement();
            String show = "SELECT * FROM metropolises;";
            Query = show;
            resultSet = statement.executeQuery(show);
            resultSetMetaData = resultSet.getMetaData();

            /*
            while(resultSet.next()){
                System.out.println(resultSet.getObject(1));
            }

            for(int i = 0; i < getRowCount(); i++){
                for(int j = 0; j < getColumnCount(); j++){
                    System.out.println(getValueAt(i, j));
                    System.out.println(getRowCount());
                }
            }
             */

            fireTableDataChanged();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Adds metropolis, continent and population data to the table.
     * @param metropolis string - city
     * @param continent string - continent
     * @param population integer - city population
     */
    @Override
    public void add(String metropolis, String continent, String population) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Metropolises_DB",
                    "root", "password");
            statement = connection.createStatement();
            String query = "INSERT INTO metropolises VALUES (\"" + metropolis + "\", \"" + continent + "\"" +
                    ", \"" + population + "\");";
            statement.executeUpdate(query);
            String show = "SELECT * FROM metropolises WHERE metropolis = \"" + metropolis +"\" AND continent = \"" +
                    continent + "\" AND population = " + population + ";";
            Query = show;
            // System.out.println(show);
            resultSet = statement.executeQuery(show);
            resultSetMetaData = resultSet.getMetaData();
            fireTableStructureChanged();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds metropolis, continent and population data from the table if there exists (partial and exact matches,
     * population more than or less than x).
     * @param metropolis string - city
     * @param continent string - continent
     * @param population string - city population
     * @param largerOrNot boolean - '[population] larger than' selected in combobox (if not it's smaller than or equal to)
     * @param exact boolean - 'exact match' selected in combobox (if not it's partial)
     */
    @Override
    public ResultSet search(String metropolis, String continent, String population, boolean largerOrNot, boolean exact) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Metropolises_DB",
                    "root", "password");
            statement = connection.createStatement();
            String query = "SELECT * FROM metropolises";
            if(!metropolis.isEmpty() || !continent.isEmpty() || !population.isEmpty()){
                query += " WHERE ";
                if(!metropolis.isEmpty()){
                    if(exact){
                        query += " metropolis = \"" + metropolis + "\"";
                    }else{
                        query += " metropolis LIKE \"%" + metropolis + "%\"";
                    }
                }

                if(!continent.isEmpty()){
                    if(!metropolis.isEmpty()){
                        query += " AND ";
                    }
                    if(exact){
                        query += " continent = \"" + continent + "\"";
                    }else{
                        query += " continent LIKE \"%" + continent + "%\"";
                    }
                }

                if(!population.isEmpty()){
                    if(!metropolis.isEmpty() || !continent.isEmpty()){
                        query += " AND ";
                    }
                    if(largerOrNot){
                        query += " population > " + population;
                    }else{
                        query += " population <= " + population;
                    }
                }
            }

            query += ";";
            Query = query;
            // System.out.println(query);
            resultSet = statement.executeQuery(query);
            resultSetMetaData = resultSet.getMetaData();
            fireTableDataChanged();
            return resultSet;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getRowCount() {
        int result = 0;
        try {
            String show = Query;
            ResultSet res = statement.executeQuery(show);
            while(res.next()) result++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        try {
            String show = Query;
            ResultSet res = statement.executeQuery(show);
            for(int i = 0; i < rowIndex; i++){
                res.next();
            }
            res.next();
            result = res.getObject(columnIndex + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getColumnName(int col){
        String result = "";
        if(resultSetMetaData != null){
            try {
                result = resultSetMetaData.getColumnName(col + 1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}