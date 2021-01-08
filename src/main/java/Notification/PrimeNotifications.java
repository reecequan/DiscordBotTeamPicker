package Notification;

import net.dv8tion.jda.api.JDA;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrimeNotifications {
    private JDA jda;
    private final List<LocalDateTime> dates = new ArrayList<>();

    public PrimeNotifications(JDA jda) {
        this.jda = jda;
    }

    {
        dates.add(LocalDateTime.of(2021,1,25,17,0));
        dates.add(LocalDateTime.of(2021,2,4,17,0));
        dates.add(LocalDateTime.of(2021,2,15,17,0));
        dates.add(LocalDateTime.of(2021,2,25,17,0));
        dates.add(LocalDateTime.of(2021,3,8,17,0));
        dates.add(LocalDateTime.of(2021,3,18,17,0));
        dates.add(LocalDateTime.of(2021,3,29,17,0));
    }

    public void sendPrimeMessage()
    {
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (LocalDateTime  date: dates ) {
            Duration d = Duration.between(LocalDateTime.now(), date);
            System.out.println(d.toMinutes());
            if(d.isNegative())
            {
                break;
            }
            jda.getGuildById(698869917776216095L).getTextChannelsByName("lol-stuff", true).get(0)
                    .sendMessage("@League New prime skin available - https://gaming.amazon.com/loot/lol9?ref_=SM_LOLS10_P2_IGP")
                    .queueAfter(d.toMinutes(), TimeUnit.MINUTES);
        }
    }


}
