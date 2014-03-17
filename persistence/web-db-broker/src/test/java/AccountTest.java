import static org.junit.Assert.*;
import opticnav.persistence.web.AccountBroker;
import opticnav.persistence.web.PublicBroker;
import opticnav.persistence.web.exceptions.PublicBrokerException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class AccountTest {
    static private PublicBroker publicBroker;
    static private MysqlDataSource ds;
    
    @BeforeClass
    static public void startUp() throws Exception {
        
        //reset the database
        
        ds = new MysqlDataSource();
        ds.setUser("root");
        ds.setPassword("password");
        ds.setServerName("localhost");
        ds.setDatabaseName("OpticNavDB");
        
        publicBroker = new PublicBroker(ds);
    }
    
    @AfterClass
    static public void tearDown() throws Exception {
        publicBroker.close();
    }
    
    @Test
    public void testregisterAccount() throws PublicBrokerException {
        assertEquals(true, publicBroker.registerAccount("java", "password"));
        assertEquals(false, publicBroker.registerAccount("java", "password"));
        assertEquals(true, publicBroker.registerAccount("doublepass", "password"));
        assertEquals(false, publicBroker.registerAccount("", ""));
        assertEquals(false, publicBroker.registerAccount("", "empty"));
        assertEquals(false, publicBroker.registerAccount("empty", ""));
    }
    
    @Test
    public void testverify() throws PublicBrokerException {
        assertEquals(1, publicBroker.verify("Kay", "kaypass"));
        assertEquals(2, publicBroker.verify("Ido", "idopass"));
        assertEquals(3, publicBroker.verify("Danny", "dannypass"));
        assertEquals(4, publicBroker.verify("Jacky", "jackypass"));
        assertEquals(0, publicBroker.verify("stranger", "kaypass"));
        assertEquals(0, publicBroker.verify("Kay", "fail"));
        assertEquals(0, publicBroker.verify("Ido", ""));
    }
    
    @Test
    public void testfindName() throws PublicBrokerException {
        assertEquals(1, publicBroker.findName("Kay"));
        assertEquals(2, publicBroker.findName("Ido"));
        assertEquals(3, publicBroker.findName("Danny"));
        assertEquals(4, publicBroker.findName("Jacky"));
        assertEquals(0, publicBroker.findName("stranger"));
        assertEquals(0, publicBroker.findName(""));
    }
    
    @Test
    public void testSetRemoveGetARD() throws Exception {
        AccountBroker ab1 = new AccountBroker(ds, 1);
        AccountBroker ab2 = new AccountBroker(ds, 2);
        AccountBroker ab3 = new AccountBroker(ds, 3);
        AccountBroker ab4 = new AccountBroker(ds, 4);
        
        try {
            ab1.setARD(1234);
            ab2.setARD(5678);
            ab3.setARD(9012);
            
            assertEquals(1234, ab1.getARD());
            assertEquals(5678, ab2.getARD());
            assertEquals(9012, ab3.getARD());
            assertEquals(0, ab4.getARD());
            
            ab1.removeARD();
            ab2.removeARD();
            ab3.removeARD();
            
            assertEquals(0, ab1.getARD());
            assertEquals(0, ab2.getARD());
            assertEquals(0, ab3.getARD());
        } finally {
            ab4.close();
            ab3.close();
            ab2.close();
            ab1.close();
        }
    }
}
