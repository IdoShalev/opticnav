import static org.junit.Assert.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import opticnav.persistence.web.WebAccountDAO;
import opticnav.persistence.web.DBUtil;
import opticnav.persistence.web.WebAccountPublicDAO;
import opticnav.persistence.web.WebResourceDAO;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class DatabaseTest {
    static private WebAccountPublicDAO publicBroker;
    static private WebAccountDAO accountBroker1;
    static private WebAccountDAO accountBroker2;
    static private WebAccountDAO accountBroker3;
    static private WebResourceDAO resourceBroker;
    static private MysqlDataSource ds, ds2;
    static private Connection conn;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ds = new MysqlDataSource();
        ds.setUser("root");
        ds.setPassword("password");
        ds.setServerName("localhost");
        ds.setDatabaseName("testDB");
        
        ds2 = new MysqlDataSource();
        ds2.setUser("root");
        ds2.setPassword("password");
        ds2.setServerName("localhost");
        ds2.setDatabaseName("testDB");
        
        publicBroker = new WebAccountPublicDAO(ds);
        //resourceBroker = new ResourceBroker(path, ds);
        conn = DBUtil.getConnectionFromDataSource(ds);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        publicBroker.close();
        accountBroker1.close();
        accountBroker2.close();
        accountBroker3.close();
        resourceBroker.close();
        conn.close();
        
        //TODO
        //clean up the database
    }

    @Test
    public void test() throws Exception{
        //User registration
        
        assertEquals(true, publicBroker.registerAccount("user1", "password"));
        assertEquals(true, publicBroker.registerAccount("user2", "password"));
        assertEquals(true, publicBroker.registerAccount("user3", "password"));
        assertEquals(false, publicBroker.registerAccount("user3", "password"));
        assertEquals(false, publicBroker.registerAccount("", ""));
        assertEquals(false, publicBroker.registerAccount("", "password"));
        assertEquals(false, publicBroker.registerAccount("user9", ""));
        
        /*
        //username and password validation
        assertEquals(1, publicBroker.verify("user1", "password"));
        assertEquals(2, publicBroker.verify("user2", "password"));
        assertEquals(3, publicBroker.verify("user3", "password"));
        assertEquals(0, publicBroker.verify("stranger", "pass"));
        assertEquals(0, publicBroker.verify("user1", "fail"));
        assertEquals(0, publicBroker.verify("user9", ""));
        assertEquals(0, publicBroker.verify("", "password"));
        assertEquals(0, publicBroker.verify("", ""));
        
        //getting user IDs by name
        assertEquals(1, publicBroker.findName("user1"));
        assertEquals(2, publicBroker.findName("user2"));
        assertEquals(3, publicBroker.findName("user3"));
        assertEquals(0, publicBroker.findName("stranger"));
        assertEquals(0, publicBroker.findName(""));
        */
        
        accountBroker1 = new WebAccountDAO(ds2, 1);
        accountBroker2 = new WebAccountDAO(ds, 2);
        accountBroker3 = new WebAccountDAO(ds, 3);
        
        /*
        //set the ard id in the account table
        accountBroker1.setARD(1234);
        accountBroker2.setARD(5678);
        
        //getting the ard
        assertEquals(1234, accountBroker1.getARD());
        assertEquals(5678, accountBroker2.getARD());
        assertEquals(0, accountBroker3.getARD());
        
        //ard removal
        accountBroker1.removeARD();
        accountBroker2.removeARD();
        accountBroker3.removeARD();        
        assertEquals(0, accountBroker1.getARD());
        assertEquals(0, accountBroker2.getARD());
        assertEquals(0, accountBroker3.getARD());
        */
        
        //resource creation
        try (CallableStatement cs = conn.prepareCall("{? = call addResource(?)}")){
            cs.setString(2, "type1");
            cs.registerOutParameter(1, Types.INTEGER);
            
            cs.execute();
            conn.commit();
            assertEquals(1, cs.getInt(1));
                        
            cs.setString(2, "type2");
            cs.registerOutParameter(1, Types.INTEGER);
            
            cs.execute();
            conn.commit();
            assertEquals(2, cs.getInt(1));            
            
            
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        /*
        //getting the resource type
        try (CallableStatement cs = conn.prepareCall("{? = call getResourceType(?)}")){
            cs.setInt(2, 1);
            cs.registerOutParameter(1, Types.VARCHAR);
            
            cs.execute();
            assertEquals("type1", cs.getString(1));
            
            cs.setInt(2, 2);
            cs.registerOutParameter(1, Types.VARCHAR);
            
            cs.execute();
            assertEquals("type2", cs.getString(1));
            
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */
        
        //creating maps
        assertEquals(1, accountBroker1.createMap("map1", 1));
        assertEquals(2, accountBroker1.createMap("map2", 2));
        assertEquals(3, accountBroker1.createMap("map3", 2));
        assertEquals(4, accountBroker2.createMap("map4", 1));
        assertEquals(0, accountBroker3.createMap("map9", 99));
        assertEquals(0, accountBroker3.createMap("", 1));
        /*
        //void modifyMap(int mapid, ModifyMap map)
        ModifyMap mMap = new ModifyMap();
        for(int i = 1; i < 4; i++){
            Anchor a = new Anchor(i*100, i*100, i*200, i*200);
            Marker m = new Marker(i*100, i*100, "marker" + i, 0);
            mMap.addAnchor(a);
            mMap.addMarker(m);
        }
        accountBroker1.modifyMap(1, mMap);
        accountBroker2.modifyMap(2, mMap);  //user does not own that map        
        
        /*
        //GetMap getMap(int id)
        GetMap gMap = new GetMap("map1", 1);
        for(int i = 1; i < 4; i++){
            Anchor a = new Anchor(i*100, i*100, i*200, i*200);
            Marker m = new Marker(i*100, i*100, "marker" + i, 0);
            gMap.addAnchor(a);
            gMap.addMarker(m);
        }
        GetMap dbMap = accountBroker1.getMap(1);
        assertEquals(true, gMap.getName().equals(dbMap.getName()));
        assertEquals(1, dbMap.getImageResource());
        List<Marker> ml = dbMap.getMarkers();
        List<Anchor> al = dbMap.getAnchors();
        assertEquals(100, ml.get(0).getLat());
        assertEquals(100, al.get(0).getLat());
        
        //List<MapsListEntry> getMapsList()
        List<MapsListEntry> mle = accountBroker1.getMapsList();
        assertEquals("map1", mle.get(0).getName());
        assertEquals(1, mle.get(0).getId());
        */
        
        //void deleteMap(int id)
        accountBroker1.deleteMap(1);
        accountBroker1.deleteMap(2);
        accountBroker1.deleteMap(3);
        accountBroker1.deleteMap(4); //does not belong to this account
        
        //TODO
        //check if there are still maps there
    }

}
