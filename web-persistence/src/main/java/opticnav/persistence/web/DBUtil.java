package opticnav.persistence.web;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DBUtil {
    public static Connection getConnectionFromDataSource(DataSource ds)
            throws SQLException {
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false);
        
        return conn;
    }
}
