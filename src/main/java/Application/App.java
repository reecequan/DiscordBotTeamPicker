package Application;

import Listeners.BotListener;
import Notification.PrimeNotifications;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App
{
    public static void main( String[] args )
    {
        JDABuilder jdaBuilder = JDABuilder.createDefault(getKey());
        JDA jda;
        BotListener botListener = new BotListener();
        jdaBuilder.addEventListeners(botListener).setActivity(Activity.playing("Type !teamHelp"));
        try {
            jda = jdaBuilder.build();
            new PrimeNotifications(jda).sendPrimeMessage();
        }catch (LoginException e)
        {
            e.getStackTrace();
        }
    }

    private static String getKey()
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
        return properties.getProperty("key");
    }

}
