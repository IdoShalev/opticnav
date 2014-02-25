import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.sql.SQLException;

import opticnav.persistence.web.WebDBBroker;
import opticnav.persistence.web.WebDBBrokerException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AccountTest {
    private java.sql.Connection conn;
    private WebDBBroker broker;
    
    @BeforeClass
    public void startUp() throws ClassNotFoundException, SQLException {
        // mysql connection stuff lol
        Class.forName("com.mysql.jdbc.Driver");
        
        this.conn = DriverManager.getConnection("jdbc:mysql://localhost/OpticNavDB?"
                    + "user=test&password=password");
        
        broker = new WebDBBroker(conn);
    }
    
    @AfterClass
    public void tearDown() throws SQLException {
        conn.close();
    }
    
    @Test
    public void testregisterAccount() throws WebDBBrokerException {
        assertEquals(true, broker.registerAccount("kay", "password"));
    }
    
    @Test
    public void testverify() throws WebDBBrokerException {
        assertEquals(1, broker.verify("kay", "password"));
    }
}
