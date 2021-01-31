package Application;

import Listeners.BotListener;
import Notification.PrimeNotifications;
import Uility.Keys;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import static Uility.Keys.getKey;

public class App
{
    public static void main( String[] args )
    {
        JDABuilder jdaBuilder = JDABuilder.createDefault(getKey(Keys.KEYS.DISCORD));
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

}
