package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import opticnav.persistence.web.exceptions.WebAccountPublicDAOException;

/**
 * Handles account registration and verification
 * 
 * @author Danny Spencer, Kay Bernhardt
 */
public class WebAccountPublicDAO implements AutoCloseable {
    public static final int ACCOUNT_ID_NONE = 0;
    
    private java.sql.Connection conn;

    /**
     * Constructor
     * Establishes a connection to the database
     * 
     * @param dataSource The parameters that are needed to connect to the database
     * @throws SQLException Thrown if there was a problem with the sql connection
     */
    public WebAccountPublicDAO(DataSource dataSource) throws SQLException {
        this.conn = DBUtil.getConnectionFromDataSource(dataSource);
    }

    @Override
    public void close() throws WebAccountPublicDAOException {
        try {
            this.conn.close();
        } catch (SQLException e) {
            throw new WebAccountPublicDAOException(e);
        }
    }

    /**
     * Registers a new account with the provided username and password
     * 
     * @param username The username identifying the user
     * @param password The password belonging to the username 
     * @return True if registration was successful, false if unsuccessful
     * @throws WebAccountPublicDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public boolean registerAccount(String username, String password)
            throws WebAccountPublicDAOException {
        if (!checkUsername(username) || !checkPassword(password)) {
            return false;
        } else {
            try (CallableStatement cs = conn
                    .prepareCall("{? = call registerAccount(?, ?)}")) {
                cs.setString(2, username);
                cs.setString(3, password);
                cs.registerOutParameter(1, Types.BOOLEAN);

                cs.execute();
                conn.commit();
                return cs.getBoolean(1);
            } catch (SQLException e) {
                throw new WebAccountPublicDAOException(e);
            }
        }
    }

    /**
     * Checks to see if the provided username and password identify a valid user
     * 
     * @param username The username to be verified
     * @param password The password to be verified
     * @return The account id of the user if the username and password pair identify a valid user, 0 otherwise
     * @throws WebAccountPublicDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public int verify(String username, String password)
            throws WebAccountPublicDAOException {
        if (!checkUsername(username) || !checkPassword(password)) {
            return 0;
        } else {
            try (CallableStatement cs = conn
                    .prepareCall("{? = call validateUser(?, ?)}")) {
                cs.setString(2, username);
                cs.setString(3, password);
                cs.registerOutParameter(1, Types.INTEGER);

                cs.execute();
                int id = cs.getInt(1);
                return id;
            } catch (SQLException e) {
                throw new WebAccountPublicDAOException(e);
            }
        }
    }

    /**
     * Finds the account ID with an exact match of the provided username
     * 
     * @param username The username to be query
     * @return A valid account ID, or 0 if none was found
     * @throws WebAccountPublicDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public int findName(String username) throws WebAccountPublicDAOException {
        try (CallableStatement cs = conn
                .prepareCall("{? = call findAccount(?)}")) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, username);

            cs.execute();
            int id = cs.getInt(1);

            return id;
        } catch (SQLException e) {
            throw new WebAccountPublicDAOException(e);
        }
    }

    /**
     * Gets a list of users and their ARD IDs if set
     * 
     * @return A List of username and ARD ID pairs
     * @throws WebAccountPublicDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public List<UserARDListEntry> getUserList() throws WebAccountPublicDAOException {
        List<UserARDListEntry> list = new LinkedList<>();
        try {
            try (CallableStatement cs = conn.prepareCall("{call getUserList()}")) {
                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        UserARDListEntry ue = new UserARDListEntry(rs.getString(2), rs.getInt(3));                        
                        list.add(ue);
                    }
                }
            }
        } catch (SQLException e) {
            throw new WebAccountPublicDAOException(e);
        }
        return list;
    }
    
    /**
     * Checks if the username is an empty String or NULL
     * 
     * @param username The username to be checked
     * @return True if the username is not empty, false if it is empty or null
     */
    private boolean checkUsername(String username) {
        boolean flag = true;
        if (username == null || username.equals("")) {
            flag = false;
        }
        return flag;
    }

    /**
     * Checks if the password is an empty String or NULL
     * 
     * @param password to be checked
     * @return True if the password is not empty, false if it is empty or null
     */
    private boolean checkPassword(String password) {
        boolean flag = true;
        if (password == null || password.equals("")) {
            flag = false;
        }
        return flag;
    }
}
