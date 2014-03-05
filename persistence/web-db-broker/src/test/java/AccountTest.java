import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.sql.SQLException;

import opticnav.persistence.web.WebDBBroker;
import opticnav.persistence.web.WebDBBrokerException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AccountTest {
    static private WebDBBroker broker;
    
    @BeforeClass
    static public void startUp() throws ClassNotFoundException, SQLException {
        
        //reset the database
        
        // mysql connection stuff
        java.sql.Connection conn;
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost/OpticNavDB?"
                    + "user=test&password=password");
        broker = new WebDBBroker(conn);
    }
    
    @AfterClass
    static public void tearDown() throws WebDBBrokerException {
        broker.close();
    }
    
    @Test
    public void testregisterAccount() throws WebDBBrokerException {
        assertEquals(true, broker.registerAccount("java", "password"));
        assertEquals(false, broker.registerAccount("java", "password"));
        assertEquals(false, broker.registerAccount("", ""));
        assertEquals(false, broker.registerAccount("", "empty"));
        assertEquals(false, broker.registerAccount("empty", ""));
    }
    
    @Test
    public void testverify() throws WebDBBrokerException {
        assertEquals(1, broker.verify("Kay", "kaypass"));
        assertEquals(2, broker.verify("Ido", "idopass"));
        assertEquals(3, broker.verify("Danny", "dannypass"));
        assertEquals(4, broker.verify("Jacky", "jackypass"));
        assertEquals(0, broker.verify("stranger", "kaypass"));
            //should the password really be unique
    }
}
