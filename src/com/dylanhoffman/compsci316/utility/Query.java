package src.com.dylanhoffman.compsci316.utility;

import src.com.dylanhoffman.compsci316.Constants;
import src.com.dylanhoffman.compsci316.logging.Log;

import java.sql.*;

/**
 * Class to hold a basic Query object
 * contains 6 private variables
 * String url to hold the database url
 * String dbName to hold the database name
 * String dbUser to hold the database username
 * String strQuery to hold the query to be executed
 * String dbPass to hold the password
 * Enum queryType can be INSERT,UPDATE,DELETE, OR SELECT
 */
public class Query {

    private String url;
    private String dbName;
    private String dbUser;
    private String strQuery;
    private String dbPass;
    //enum with types of select, update, insert, or delete
    private QueryType queryType;

    /**
     * overloaded method that does not include the url connection object passed as a parm, so the default is localhost
     *
     * @param dbName String dbName to hold the database name
     * @param dbUser String dbUser to hold the database username
     * @param dbPass String dbPass to hold the password
     * @param queryType Enum queryType can be INSERT,UPDATE,DELETE, OR SELECT
     * @param strQuery String strQuery to hold the query to be executed
     */
    public Query(String dbName, String dbUser, String dbPass, QueryType queryType, String strQuery){
        this(dbName,dbUser,dbPass,queryType,strQuery, "jdbc:mysql://localhost:3306/");
//        this.dbName=dbName;
//        this.dbUser=dbUser;
//        this.dbPass=dbPass;
//        this.queryType=queryType;
//        this.strQuery=strQuery.trim();
//        this.url = ("jdbc:mysql://localhost:3306/");
    }


    /**
     * overloaded method that includes the url for the db connection object
     * @param dbName String dbName to hold the database name
     * @param dbUser String dbUser to hold the database username
     * @param dbPass String dbPass to hold the password
     * @param queryType Enum queryType can be INSERT,UPDATE,DELETE, OR SELECT
     * @param strQuery String strQuery to hold the query to be executed
     * @param url url to connect to the db
     */

    public Query(String dbName, String dbUser, String dbPass, QueryType queryType, String strQuery, String url){
        this.dbName=dbName;
        this.dbUser=dbUser;
        this.dbPass=dbPass;
        this.queryType=queryType;
        this.strQuery=strQuery.trim();
        this.url = url;
    }


//    public void executeQuery() {
//
//        String query = (new StringBuilder().append(getQueryType().toString()).append(" ").append(getStrSelect()).toString());
//        int rowCount = 1;
//
//        try (
//                //create the connection object, then create the statement object
//                Connection connection = DriverManager.getConnection( getUrl() + getDbName() + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", getDbUser(), getDbPass());
//                Statement statement = connection.prepareStatement(query)
//            ) {
//
//            System.out.println("SQL Statement is " + query);
//            int result = statement.executeUpdate(query);
//            System.out.println("Total number of records = " + rowCount);
//        } catch(SQLIntegrityConstraintViolationException e) {
//            Log.writeToLog(Constants.getLogPath(), e.getMessage());
//            System.out.println("Duplicate primary key, cannot insert same value twice!");
//            e.printStackTrace();
//        }
//        catch (SQLException ex){
//            Log.writeToLog(Constants.getLogPath(), ex.getMessage());
//            ex.printStackTrace();
//        }
//    }

    /**
     * method to execute the query object
     * creates the connection to the Database
     * then uses a prepared statement consisting of the query + the queryType enum
     */
    public void executeQueryThrows() throws SQLException {

        String query = (new StringBuilder().append(getQueryType().toString()).append(" ").append(getStrSelect()).toString());
        int rowCount = 1;
                //create the connection object, then create the statement object
                Connection connection = DriverManager.getConnection( getUrl() + getDbName() + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", getDbUser(), getDbPass());
                Statement statement = connection.prepareStatement(query);

            System.out.println("SQL Statement is " + query);
            int result = statement.executeUpdate(query);
            System.out.println("Total number of records = " + rowCount);

            statement.close();
            connection.close();
    }

    /**
     * public getter for the url
     * @return returns the url as string
     */
    public String getUrl() {
        return url;
    }

    /**
     * publice setter for the url
     * @param url string to set the url to
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * public getter for the db name
     * @return returns the dbname as string
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * public setter for the db name
     * @param dbName string to set the db name to
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * public getter for the db user
     * @return returns the db user as string
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * public setter for the db user
     * @param dbUser string to set the db user
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    /**
     * public getter for the query
     * @return returns the query as string
     */
    public String getStrSelect() {
        return strQuery;
    }

    /**
     * public setter for the query
     * @param strQuery string to set the query to
     */
    public void setStrSelect(String strQuery) {
        this.strQuery = strQuery;
    }

    /**
     * public getter for the queryType
     * @return returns the queryTye\pe
     */
    public QueryType getQueryType() {
        return queryType;
    }

    /**
     * public setter for the queryType
     * @param queryType sets the querytype to the passed in querytype
     */
    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    /**
     * public getter for the db pass
     * @return returns the pass as string
     */
    public String getDbPass() {
        return dbPass;
    }

    /**
     * public setter for the db pass
     * @param dbPass string to set the pass to
     */
    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

}
