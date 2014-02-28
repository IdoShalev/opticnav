import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.sql.SQLException;

import opticnav.persistence.web.WebDBBroker;
import opticnav.persistence.web.WebDBBrokerException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AccountTest {
    static private java.sql.Connection conn;
    static private WebDBBroker broker;
    
    @BeforeClass
    static public void startUp() throws ClassNotFoundException, SQLException {
        
        //reset the database
        
        // mysql connection stuff
        Class.forName("com.mysql.jdbc.Driver");        
        conn = DriverManager.getConnection("jdbc:mysql://localhost/OpticNavDB?"
                    + "user=test&password=password");        
        broker = new WebDBBroker(conn);
    }
    
    @AfterClass
    static public void tearDown() throws SQLException {
        conn.close();
    }
    
    /*
    @Test
    public void testregisterAccount() throws WebDBBrokerException {
        assertEquals(true, broker.registerAccount("java", "password"));
    }
    */
    
    @Test
    public void testverify() throws WebDBBrokerException {
        assertEquals(1, broker.verify("java", "password"));
    }
}
