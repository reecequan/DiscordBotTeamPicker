package Uility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Keys {

    private Keys(){};

    public static String getKey(KEYS key)
    {
        File file = new File(System.getProperty("user.dir") + "/properties/key.properties");
        Properties properties = new Properties();
        try(InputStream myReader = new FileInputStream(file))
        {
            System.out.println("Properties loaded");
            properties.load(myReader);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.exit(1);
        }
        return properties.getProperty(key.toString());
    }


    public enum KEYS
    {
        DISCORD,
        RIOT
    }
}
