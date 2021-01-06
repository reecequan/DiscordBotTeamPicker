import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App
{
    public static void main( String[] args ) throws IOException
    {
        JDABuilder jdaBuilder = JDABuilder.createDefault(new App().getKey());
        JDA jda;
        BotListener botListener = new BotListener();
        jdaBuilder.addEventListeners(botListener);
        try {
            jda = jdaBuilder.build();
        }catch (LoginException e)
        {
            e.getStackTrace();
        }
    }

    private String getKey() throws IOException
    {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("key.properties");

        if(inputStream != null)
            properties.load(inputStream);
        else
        {
            throw new FileNotFoundException();
        }
        return properties.getProperty("key");
    }

}
