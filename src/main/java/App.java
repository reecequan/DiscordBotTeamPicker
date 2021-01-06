import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.w3c.dom.CDATASection;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Properties;
import java.util.Scanner;

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
        String key = "";
        try {
            File myObj = new File("properties/key.properties");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                key = data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return key;

    }

}
