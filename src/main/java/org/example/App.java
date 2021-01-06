package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class App
{
    public static void main( String[] args )
    {
        JDABuilder jdaBuilder = JDABuilder.createDefault("NzkwNjgxOTUwNjU4NjI1NTc3.X-EJ8w.Xs7Q23N86GFi1NLffVgjbvZjGQo");
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
}
