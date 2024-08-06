import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MetropolisesModelTest extends TestCase {
    MetropolisesModel metropolisModel;
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData resultSetMetaData;

    protected void setUp() throws Exception {
        metropolisModel = new MetropolisesModel();
        JTable mainTable = new JTable();
        mainTable.setModel(metropolisModel);
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Metropolises_DB",
                "root", "password");
        statement = connection.createStatement();
        String clearStatement = "DELETE FROM metropolises;";
        statement.executeUpdate(clearStatement);
    }

    public void testEmpty() throws SQLException {
        String empty = "SELECT * FROM metropolises;";
        resultSet = statement.executeQuery(empty);
        resultSetMetaData = resultSet.getMetaData();
        int rows = 0;
        while(resultSet.next()) rows++;
        assertEquals(rows, 0);
    }

    public void testColumns() throws SQLException {
        String empty = "SELECT * FROM metropolises;";
        resultSet = statement.executeQuery(empty);
        resultSetMetaData = resultSet.getMetaData();
        assertEquals(resultSetMetaData.getColumnName(1), "metropolis");
        assertEquals(resultSetMetaData.getColumnName(2), "continent");
        assertEquals(resultSetMetaData.getColumnName(3), "population");
    }

    public void testAdd() throws SQLException {
        metropolisModel.add("Tbilisi", "Europe", "1000000");
        String query = "SELECT * FROM metropolises;";
        resultSet = statement.executeQuery(query);
        int rows = 0;
        while(resultSet.next()) rows++;
        assertEquals(rows, 1);
        metropolisModel.add("Kutaisi", "Europe", "200000");
        resultSet = statement.executeQuery(query);
        rows = 0;
        while(resultSet.next()) rows++;
        assertEquals(rows, 2);
    }

    public void testSearchExact() throws SQLException {
        metropolisModel.add("Tbilisi", "Europe", "1000000");
        metropolisModel.add("Kutaisi", "Europe", "200000");
        resultSet = metropolisModel.search("Tbilisi", "",
                "", true, true);
        int res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 1);

        resultSet = metropolisModel.search("", "Europe",
                "", true, true);
        res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 2);

        resultSet = metropolisModel.search("Kutaisi", "Europe",
                "", true, true);
        res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 1);
    }

    public void testSearchPartial() throws SQLException {
        metropolisModel.add("Tbilisi", "Europe", "1000000");
        metropolisModel.add("Kutaisi", "Europe", "200000");
        resultSet = metropolisModel.search("i", "",
                "", true, false);
        int res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 2);

        resultSet = metropolisModel.search("", "Eu",
                "", true, false);
        res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 2);

        resultSet = metropolisModel.search("i", "kkk",
                "", true, true);
        res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 0);
    }

    public void testPopulation() throws SQLException {
        metropolisModel.add("Tbilisi", "Europe", "1000000");
        metropolisModel.add("Kutaisi", "Europe", "200000");
        resultSet = metropolisModel.search("", "",
                "100", true, true);
        int res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 2);

        resultSet = metropolisModel.search("", "",
                "500000", true, true);
        res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 1);

        resultSet = metropolisModel.search("", "",
                "300000", false, true);
        res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 1);

        resultSet = metropolisModel.search("", "r",
                "2000000", false, false);
        res = 0;
        while(resultSet.next()) res++;
        assertEquals(res, 2);
    }
}
