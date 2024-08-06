import java.sql.ResultSet;

public interface MetropolisTableOperations {
    void add(String metropolis, String continent, String population);

    ResultSet search(String metropolis, String continent, String population, boolean largerOrNot, boolean exact);
}