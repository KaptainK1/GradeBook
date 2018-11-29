package src.com.dylanhoffman.compsci316;

//make the class non-extendable by adding final
public final class Constants {
    //Hide the constructor
    private Constants(){}

    private static final String LOG_PATH = "src/com/dylanhoffman/compsci316/log.txt";
    private static final String CSS_PATH = "src/com/dylanhoffman/compsci316/UI/stylesheet.css";

    public static String getLogPath(){
        return LOG_PATH;
    }

    public static String getCssPath(){
        return CSS_PATH;
    }
}