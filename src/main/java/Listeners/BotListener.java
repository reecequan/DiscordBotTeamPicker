package Listeners;

import Exceptions.ApiRejectedException;
import Riot.Api.Items;
import Riot.Api.RiotApi;
import Riot.ComparePlayers;
import Riot.LiveGame;
import Riot.Ranked;
import Riot.Storage.ChampData;
import Riot.Storage.GameHistory;
import Riot.Storage.Item;
import Riot.Storage.RankedData;
import Uility.ChampDetails;
import Uility.SaveProperties;
import Uility.TeamPicker;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BotListener extends ListenerAdapter {
    private Map<String, List<String>> namesMap = new HashMap<>();
    private Map<String, TeamPicker> teamStore = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        String prompt = e.getMessage().getContentRaw().split(" ")[0];

        String input = e.getMessage().getContentRaw().replace(prompt,"").trim();
        switch (prompt){
            case "!setReplyPath":
                setReply(e);
                break;
            case "!addName":
                addNames(input, e);
                break;
            case "!removeName":
                removeUser(input, e);
                break;
            case "!whosIn":
                showUsers(e);
                break;
            case "!teamCreate":
                createTeam(e);
                break;
            case "!teamNoRoles":
                createTeamNoRole(e);
                break;
            case "!clearTeam":
                getNames(e.getGuild().getId()).clear();
                reply(e,"All names have been cleared");
                break;
            case "!teamHelp":
                getHelp(e);
                break;
            case "!champ":
                getChampDetails(input,e);
                break;
            case "!history":
                getHistory(input,e, false);
                break;
            case "!historyFile":
                getHistory(input,e, true);
                break;
            case "!historyFileDate":
                getHistoryDate(input,e);
                break;
            case "!reroll":
                reroll(input, e);
                break;
            case "!move":
                move(e);
                break;
            case "!rank":
                getRankedData(input, e);
                break;
            case "!item":
                getItem(input, e);
                break;
            case "!rankList":
                reply(e,new ComparePlayers().getPlayers());
                break;
        }
    }

    private void getItem(String input, MessageReceivedEvent e)
    {
        Item item = new Items().getItem(input);
        reply(e, item.getMessage());
    }

    private void getRankedData(String input, MessageReceivedEvent e)
    {

        String[] inputParams = input.split(" ");
        RankedData rankedData;
        try {
             rankedData = new Ranked().getRankedData(inputParams[0]);
            //new LiveGame().getLiveGameData(inputParams[0]);
        } catch (ApiRejectedException apiRejectedException) {
            reply(e, apiRejectedException.getMessage());
            return;
        }
        reply(e, rankedData.getMessage());

    }

    private void reroll(String input, MessageReceivedEvent e)
    {
        if(teamStore.containsKey(e.getGuild().getId()))
        {
            reply(e, teamStore.get(e.getGuild().getId()).reRoll(input.split(" ")[0]));
        }
        else
        {
            createTeam(e);
        }
    }

    private void move(MessageReceivedEvent e)
    {
        Guild controller = e.getGuild();

        List<VoiceChannel> vc = e.getGuild().getVoiceChannels();
        VoiceChannel main = null;
        for(VoiceChannel channel : vc)
        {
            if(channel.getName().toLowerCase().contains("lol") || channel.getName().toLowerCase().contains("league"))
            {
               main = channel;
            }
        }

        for(VoiceChannel channel : vc)
        {
            if(channel.getName().toLowerCase().contains("team"))
            {
                List<Member> members = channel.getMembers();
                for(Member member : members)
                {
                    controller.moveVoiceMember(member,main).complete();
                }

            }
        }

        System.out.println(vc.get(0).getName());
    }


    private void getHistory(String input, MessageReceivedEvent e, boolean fileOnly)
    {
        String[] inputParams = input.split(" ");
        String champ;
        if(inputParams.length < 4)
        {
            champ = "";
        }
        else
        {
            champ = inputParams[3];
        }
        RiotApi riotApi = new RiotApi(inputParams[0],inputParams[1],Integer.parseInt(inputParams[2]), champ);
        GameHistory gameHistory = null;
        try {
            gameHistory = riotApi.getGameHistory();
        } catch (ApiRejectedException apiRejectedException) {
            reply(e, apiRejectedException.getMessage());
            return;
        }
        if(fileOnly)
        {
            final GameHistory gameHistoryTemp = gameHistory;
            e.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendFile(gameHistoryTemp.getCsv().write(inputParams[0]+" - Game History"))).queue();
            gameHistory.getCsv().removeFile();
            //e.getChannel().sendFile(gameHistory.getCsv().write(inputParams[0]+" - Game History")).queue();
        }
        else
        {
            reply(e,gameHistory.getOutput());
        }
    }

    private void getHistoryDate(String input, MessageReceivedEvent e)
    {
        String[] inputParams = input.split(" ");
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        RiotApi riotApi = null;
        try {
            riotApi = new RiotApi(inputParams[0],
                    inputParams[1],
                    format.parse(inputParams[2]),
                    format.parse(inputParams[3])
            );
        } catch (ParseException parseException) {
            e.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage("Unable to use date please use dd/MM/yyyy format")).queue();

        }
        GameHistory gameHistory = null;
        try {
            gameHistory = riotApi.getGameHistory();
        } catch (ApiRejectedException apiRejectedException) {
            reply(e, apiRejectedException.getMessage());
            return;
        }
        final GameHistory gameHistoryTemp = gameHistory;
        e.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendFile(gameHistoryTemp.getCsv().write(inputParams[0]+" - Game History"))).queue();
        gameHistory.getCsv().removeFile();
    }

    private void setReply(MessageReceivedEvent e)
    {
        SaveProperties.setReplyRoom(e.getGuild().getId(), e.getChannel().getName());
        reply(e, "Reply path has now been set to this room - If the server is restarted this will reset to the default");
    }

    private void reply(MessageReceivedEvent e, String message)
    {
        String secondMessage = null;
        if(message.length() > 2000)
        {
            int split = message.substring(0,1900).lastIndexOf(System.getProperty("line.separator"));
            secondMessage = "```".concat(message.substring(split));
            message = message.substring(0,split).concat("```");
        }
        String replyRoom = SaveProperties.getReplyRoom(e.getGuild().getId());
        if(replyRoom != null && !e.getGuild().getTextChannelsByName(replyRoom,true).isEmpty())
        {
            e.getGuild().getTextChannelsByName(replyRoom,true).get(0).sendMessage(message).queue();
        }
        else
        {
            e.getChannel().sendMessage(message).queue();
        }
        if(secondMessage != null)
        {
            reply(e,secondMessage);
        }
    }

    private void reply(MessageReceivedEvent e, MessageEmbed message)
    {
        String replyRoom = SaveProperties.getReplyRoom(e.getGuild().getId());
        if(replyRoom != null && !e.getGuild().getTextChannelsByName(replyRoom,true).isEmpty())
        {
            e.getGuild().getTextChannelsByName(replyRoom,true).get(0).sendMessage(message).queue();
        }
        else
        {
            e.getChannel().sendMessage(message).queue();
        }
    }

    private void reply(MessageReceivedEvent e, Message message)
    {
        String replyRoom = SaveProperties.getReplyRoom(e.getGuild().getId());
        if(replyRoom != null && !e.getGuild().getTextChannelsByName(replyRoom,true).isEmpty())
        {
            e.getGuild().getTextChannelsByName(replyRoom,true).get(0).sendMessage(message).queue();
        }
        else
        {
            e.getChannel().sendMessage(message).queue();
        }
    }

    private void splitChampDetailsDownIfNeeded(String champDetails,MessageReceivedEvent e)
    {
        if(champDetails.length()>= 2000)
        {
            int split = champDetails.substring(0,2000).lastIndexOf("Name: ");
            String secondMessage = "```".concat(champDetails.substring(split));
            String firstMessage = champDetails.substring(0,split).concat("```");
            reply(e,firstMessage);
            reply(e,secondMessage);
        }
        else
        {
            reply(e,champDetails);
        }
    }

    private void getChampDetails(String champ,MessageReceivedEvent e) {
        ChampData champDetails = new ChampData();
        try {
            champDetails = new ChampDetails().getChampDetails(champ);
        } catch (IOException ex)
        {
            //champDetails = "An Error has occoured";
        }

        reply(e, champDetails.getMessage());
    }

    private List<String> getNames(String guildId)
    {
        if(namesMap.containsKey(guildId))
        {
            return namesMap.get(guildId);
        }
        java.util.List<String> names = new ArrayList<>();
        namesMap.put(guildId, names);
        return names;
    }

    private void addNames(String input, MessageReceivedEvent e)
    {
        List<String> names = getNames(e.getGuild().getId());
        String inputStrings[] = input.split(" ");
        for(String name : inputStrings)
        {
            if(names.size() < 10){
                names.add(name.trim());
                reply(e,name.trim() + " has been added");
            } else
            {
                reply(e,"Unable to add " + name + " as list is at capacity");
            }
        }
    }

    private void createTeam(MessageReceivedEvent e)
    {
        List<String> names = getNames(e.getGuild().getId());
        if(names.size() >= 11)
        {
            reply(e,"There is not enough names in the list to create teams");
            return;
        }
        teamStore.put(e.getGuild().getId(), new TeamPicker());
        reply(e,teamStore.get(e.getGuild().getId()).splitTeamsAndAssignRoles(names));
    }

    private void createTeamNoRole(MessageReceivedEvent e)
    {
        List<String> names = getNames(e.getGuild().getId());
        if(names.size() != 10)
        {
            reply(e,"There is not enough names in the list to create teams");
            return;
        }
        teamStore.put(e.getGuild().getId(), new TeamPicker());
        reply(e,teamStore.get(e.getGuild().getId()).splitTeams(names));
    }

    private void getHelp(MessageReceivedEvent e)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("```");
        sb.append("!addName - adds names to a list to be able to create a team");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        sb.append("!removeName - removes names from the list");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        sb.append("!teamCreate - once the team list is at 10 you can create 2 random league teams");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        sb.append("!teamNoRoles - Splits the names into 2 teams");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        sb.append("!whosIn - Replys with the current players in the team list");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        sb.append("!champ - Gets the champ details from League");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        sb.append("!clearTeam - removes all names from the team list");
        sb.append(System.getProperty("line.separator"));

        sb.append("```");
        reply(e, sb.toString());
    }

    private void removeUser(String input, MessageReceivedEvent e)
    {
        List<String> names = getNames(e.getGuild().getId());
        String inputStrings[] = input.split(" ");
        for(String name : inputStrings)
        {
            if(names.remove(name)){
                reply(e, name.trim() + " has been removed");
            } else
            {
                reply(e,"Unable to remove " + name + " as not found in the list");
            }
        }
    }

    private void showUsers(MessageReceivedEvent e)
    {
        List<String> names = getNames(e.getGuild().getId());
        if(names.size() == 0 )
        {
            reply(e,"There are no names currently registered");
            return;
        }
        StringBuilder sb = new StringBuilder("```The following names are currently in the system").append(System.getProperty("line.separator"));
        ;
        for(String name : names)
        {
            sb.append(name);
            sb.append(System.getProperty("line.separator"));
        }
        sb.append("```");

        reply(e, sb.toString());
    }

}
