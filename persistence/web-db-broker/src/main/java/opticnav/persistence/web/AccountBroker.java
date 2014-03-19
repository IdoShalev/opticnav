package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import opticnav.persistence.web.exceptions.AccountBrokerException;
import opticnav.persistence.web.map.Anchor;
import opticnav.persistence.web.map.GetMap;
import opticnav.persistence.web.map.MapsListEntry;
import opticnav.persistence.web.map.Marker;
import opticnav.persistence.web.map.ModifyMap;

public class AccountBroker implements AutoCloseable {
    private java.sql.Connection conn;
    private int accountID;

    public AccountBroker(DataSource dataSource, int accountID)
            throws SQLException {
        this.conn = DBUtil.getConnectionFromDataSource(dataSource);
        this.accountID = accountID;
    }

    @Override
    public void close() throws AccountBrokerException {
        try {
            this.conn.close();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }

    public int getARD() throws AccountBrokerException {
        try (CallableStatement cs = conn.prepareCall("{? = call getARD(?)}")) {
            cs.setInt(2, accountID);
            cs.registerOutParameter(1, Types.INTEGER);

            cs.execute();
            int ard_id = cs.getInt(1);
            return ard_id;
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }

    }

    public void setARD(int ardID) throws AccountBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call setARD(?, ?)}")) {
            cs.setInt(1, accountID);
            cs.setInt(2, ardID);

            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }

    public void removeARD() throws AccountBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call deleteARD(?)}")) {
            cs.setInt(1, accountID);

            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }

    public int createMap(String name, int imageResource)
            throws AccountBrokerException {
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
            throw new AccountBrokerException(e);
        }
    }

    public void modifyMap(int id, ModifyMap map) throws AccountBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call deleteMarker(?)}")) {
            cs.setInt(1, id);
            cs.execute();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        try (CallableStatement cs = conn.prepareCall("{call deleteAnchor(?)}")) {
            cs.setInt(1, id);
            cs.execute();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        try (CallableStatement cs = conn
                .prepareCall("{call createMarker(?, ?, ?, ?, ?)}")) {
            List<Marker> ml = map.getMarkers();
            for (int i = 0; i < ml.size(); i++) {
                cs.setString(1, ml.get(i).getName());
                cs.setInt(2, id);
                cs.setInt(3, 0);
                cs.setInt(4, ml.get(i).getLat());
                cs.setInt(5, ml.get(i).getLng());

                cs.execute();
            }
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
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
            throw new AccountBrokerException(e);
        }
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }

    public void deleteMap(int id) throws AccountBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call deleteMap(?)}")) {
            cs.setInt(1, id);
            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }

    }

    public GetMap getMap(int id) throws AccountBrokerException {
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
            throw new AccountBrokerException(e);
        }
    }

    public List<MapsListEntry> getMapsList() throws AccountBrokerException {
        List<MapsListEntry> list = new LinkedList<>();
        MapsListEntry mapE;
        try {
            try (CallableStatement cs = conn
                    .prepareCall("{call getMapsList(?)}")) {
                cs.setInt(1, accountID);

                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        mapE = new MapsListEntry(rs.getString(3), rs.getInt(1));
                        list.add(mapE);
                    }
                }
            }
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        return list;
    }
}
