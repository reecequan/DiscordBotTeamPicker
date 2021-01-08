package Riot;

import Exceptions.ApiRejectedException;
import Riot.Storage.RankedData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.collections4.list.TreeList;
import java.util.*;

public class ComparePlayers {
    private List<String> players = new ArrayList<>();
    private List<RankedData> data = new ArrayList<>();

    {
        players.add("aogchain");
        players.add("pebblebrick");
        players.add("phoenixegg");
        players.add("Souleth");
        players.add("themurd0k");
        players.add("yukoto");
        players.add("far0n");
        players.add("katyaaa");
        players.add("Minigreenpanda");
        players.add("notevenmad103");
        players.add("Ph3l4n");
    }

    public MessageEmbed getPlayers()
    {
        for (String player : players)
        {
            try {
                RankedData rankedData = new Ranked().getRankedData(player);
                data.add(rankedData);
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EmbedBuilder messageBuilder = new EmbedBuilder();
        messageBuilder.setTitle("Player Ranks");
        Collections.sort(data);
        for (RankedData play : data)
        {
            messageBuilder.appendDescription(play.getShort()).appendDescription(System.getProperty("line.separator"));
        }
        return messageBuilder.build();
    }
}
