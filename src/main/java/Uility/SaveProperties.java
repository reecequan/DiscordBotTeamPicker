package Uility;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaveProperties {
    private static final Properties properties = new Properties();
    private static boolean HAS_SET = false;

    private static void saveProperties(String server, String room)
    {
        File myObj = new File(System.getProperty("user.dir") + "/properties/defaultReplys.properties");

        try (OutputStream outputStream = new FileOutputStream(myObj))
        {
            properties.setProperty(server, room);
            properties.store(outputStream, null);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void readProperties()
    {
        File file = new File(System.getProperty("user.dir") + "/properties/defaultReplys.properties");
        if(!file.exists())
        {
            saveProperties("dummy","dummy" );
        }

        if(file.exists() && file.canRead())
        {
            try(InputStream myReader = new FileInputStream(file))
            {
                System.out.println("Properties loaded");
                properties.load(myReader);
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    public static void setReplyRoom(String server, String room)
    {
        if(!HAS_SET)
        {
            readProperties();
            HAS_SET = true;
        }
        saveProperties(server, room);
    }

    public static String getReplyRoom(String server)
    {
        if(!HAS_SET)
        {
            readProperties();
            HAS_SET = true;
        }
        System.out.println("Server checked and found room "+ properties.getProperty(server));

        return properties.getProperty(server);
    }
}
