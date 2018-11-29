package src.com.dylanhoffman.compsci316;

//make the class non-extendable by adding final
public final class Constants {
    //Hide the constructor
    private Constants(){}

    private static final String LOG_PATH = "src/com/dylanhoffman/compsci316/log.txt";
    private static final String CSS_PATH = "src/com/dylanhoffman/compsci316/UI/stylesheet.css";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Winter I_S Coming!";
    private static final String DB_NAME = "GradeBook_Application";

    public static String getLogPath(){
        return LOG_PATH;
    }

    public static String getCssPath(){
        return CSS_PATH;
    }

    public static String getDbUsername() {
        return DB_USERNAME;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }

    public static String getDbName() {
        return DB_NAME;
    }
}