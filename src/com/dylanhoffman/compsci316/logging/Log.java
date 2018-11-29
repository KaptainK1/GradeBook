package src.com.dylanhoffman.compsci316.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Class to handle application logging
 */
public class Log {

    /**
     * static method to write to the log
     * @param filePath specifies the file path of the log
     * @param message specifies the message that is to be written to the log
     */
    public static void writeToLog(String filePath,String message){
        Logger logger = Logger.getLogger("MainLog");

        try {
           FileHandler file = new FileHandler(filePath,100000,1,true);
            logger.addHandler(file);
            SimpleFormatter formatter = new SimpleFormatter();
            file.setFormatter(formatter);
            logger.warning(message);
            file.close();
        } catch (SecurityException | IOException ex){
            ex.printStackTrace();
            logger.warning(ex.getMessage());
        }
    }

}
