package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import opticnav.persistence.web.exceptions.WebAccountDAOException;
import opticnav.persistence.web.exceptions.WebAccountPublicDAOException;
import opticnav.persistence.web.map.Anchor;
import opticnav.persistence.web.map.GetMap;
import opticnav.persistence.web.map.MapsListEntry;
import opticnav.persistence.web.map.Marker;
import opticnav.persistence.web.map.ModifyMap;

public class WebAccountDAO implements AutoCloseable {
    private java.sql.Connection conn;
    private int accountID;

    public WebAccountDAO(DataSource dataSource, int accountID)
            throws SQLException, WebAccountDAOException {
        this.conn = DBUtil.getConnectionFromDataSource(dataSource);
        this.accountID = accountID;
        validateUserID();
    }
    
    private void validateUserID() throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{? = call validateUserID(?)}")) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, this.accountID);
            cs.execute();
            if(!cs.getBoolean(1))
            {
                throw new WebAccountDAOException("User id does not exist");
            }
        }catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    @Override
    public void close() throws WebAccountDAOException {
        try {
            this.conn.close();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    public int getARD() throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{? = call getARD(?)}")) {
            cs.setInt(2, accountID);
            cs.registerOutParameter(1, Types.INTEGER);

            cs.execute();
            int ard_id = cs.getInt(1);
            return ard_id;
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }

    }

    public boolean hasARD() throws WebAccountDAOException {
        return getARD() != 0;
    }

    public void setARD(int ardID) throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{call setARD(?, ?)}")) {
            cs.setInt(1, accountID);
            cs.setInt(2, ardID);

            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    public void removeARD() throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{call deleteARD(?)}")) {
            cs.setInt(1, accountID);

            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    public int createMap(String name, int imageResource)
            throws WebAccountDAOException {
        try (CallableStatement cs = conn
                .prepareCall("{? = call createMap(?, ?, ?)}")) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, name);
            cs.setInt(3, imageResource);
            cs.setInt(4, this.accountID);

            cs.execute();
            int id = cs.getInt(1);
            conn.commit();

            return id;
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    public void modifyMap(int id, ModifyMap map) throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{call deleteMarker(?)}")) {
            cs.setInt(1, id);
            cs.execute();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        try (CallableStatement cs = conn.prepareCall("{call deleteAnchor(?)}")) {
            cs.setInt(1, id);
            cs.execute();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        try (CallableStatement cs = conn
                .prepareCall("{call createMarker(?, ?, ?, ?, ?)}")) {
            List<Marker> ml = map.getMarkers();
            for (int i = 0; i < ml.size(); i++) {
                cs.setString(1, ml.get(i).getName());
                cs.setInt(2, id);
                cs.setInt(3, 0);    //no resource
                cs.setInt(4, ml.get(i).getLat());
                cs.setInt(5, ml.get(i).getLng());

                cs.execute();
            }
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        try (CallableStatement cs = conn
                .prepareCall("{call createAnchor(?, ?, ?, ?, ?)}")) {
            List<Anchor> al = map.getAnchors();
            for (int i = 0; i < al.size(); i++) {
                cs.setInt(1, id);
                cs.setInt(2, al.get(i).getLocalX());
                cs.setInt(3, al.get(i).getLocalY());
                cs.setInt(4, al.get(i).getLat());
                cs.setInt(5, al.get(i).getLng());

                cs.execute();
            }
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    public void deleteMap(int id) throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{call deleteMap(?)}")) {
            cs.setInt(1, id);
            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }

    }

    public GetMap getMap(int id) throws WebAccountDAOException {
        String name;
        int imageResource;
        try {
            try (CallableStatement cs = conn
                    .prepareCall("{? = call getMapName(?)}")) {
                cs.registerOutParameter(1, Types.VARCHAR);
                cs.setInt(2, id);

                cs.execute();
                name = cs.getString(1);
            }
            try (CallableStatement cs = conn
                    .prepareCall("{? = call getMapResource(?)}")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setInt(2, id);

                cs.execute();
                imageResource = cs.getInt(1);
            }
            GetMap map = new GetMap(name, imageResource);
            try (CallableStatement cs = conn
                    .prepareCall("{call getMapMarkers(?)}")) {
                cs.setInt(1, id);

                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        Marker marker;
                        marker = new Marker(rs.getInt(5), rs.getInt(4),
                                rs.getString(1), rs.getInt(3));
                        map.addMarker(marker);
                    }
                }
            }
            try (CallableStatement cs = conn
                    .prepareCall("{call getMapAnchors(?)}")) {
                cs.setInt(1, id);

                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        Anchor anchor;
                        anchor = new Anchor(rs.getInt(5), rs.getInt(4),
                                rs.getInt(2), rs.getInt(3));
                        map.addAnchor(anchor);
                    }
                }
            }
            return map;
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    public List<MapsListEntry> getMapsList() throws WebAccountDAOException {
        List<MapsListEntry> list = new LinkedList<>();
        try {
            try (CallableStatement cs = conn
                    .prepareCall("{call getMapsList(?)}")) {
                cs.setInt(1, accountID);

                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        MapsListEntry mapE;
                        mapE = new MapsListEntry(rs.getString(3), rs.getInt(1));
                        list.add(mapE);
                    }
                }
            }
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        return list;
    }
    
    public String getUsername() throws WebAccountPublicDAOException {
        try (CallableStatement cs = conn.prepareCall("{? = call getUsernameByID(?)}")) {
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setInt(2, this.accountID);
            cs.execute();
            return cs.getString(1);
        } catch (SQLException e) {
            throw new WebAccountPublicDAOException(e);
        }
    }
}
