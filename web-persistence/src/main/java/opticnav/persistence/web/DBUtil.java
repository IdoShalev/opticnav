package opticnav.persistence.web;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Utility class to get a connection to the database
 * 
 * @author Danny Spencer
 */
public class DBUtil {
    /**
     * Creates a connection to the database from a Data Source 
     * 
     * @param ds Data source containing parameters needed to connect to the database
     * @return A connection to the database
     * @throws SQLException
     */
    public static Connection getConnectionFromDataSource(DataSource ds)
            throws SQLException {
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false);
        
        return conn;
    }
}
