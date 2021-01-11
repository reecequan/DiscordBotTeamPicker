package Listeners;

import RiotApi.RiotApi;
import Uility.ChampDetails;
import Uility.SaveProperties;
import Uility.TeamPicker;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotListener extends ListenerAdapter {
    private Map<String, List<String>> namesMap = new HashMap<>();

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
                getHistory(input,e);
                break;
        }
    }

    private void getHistory(String input, MessageReceivedEvent e)
    {
        String[] inputParams = input.split(" ");
        RiotApi riotApi = new RiotApi(inputParams[0],inputParams[1],Integer.parseInt(inputParams[2]));
        String output = riotApi.getGameHistory();
        reply(e,output);
    }

    private void setReply(MessageReceivedEvent e)
    {
        SaveProperties.setReplyRoom(e.getGuild().getId(), e.getChannel().getName());
        reply(e, "Reply path has now been set to this room - If the server is restarted this will reset to the default");
    }

    private void reply(MessageReceivedEvent e, String message)
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
        String champDetails;
        try {
            champDetails = new ChampDetails().getChampDetails(champ);
        } catch (IOException ex)
        {
            champDetails = "An Error has occoured";
        }
        if(champDetails.isEmpty())
        {
            reply(e,"No Data has been returned");
        }
        splitChampDetailsDownIfNeeded(champDetails,e);
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
        if(names.size() != 10)
        {
            reply(e,"There is not enough names in the list to create teams");
            return;
        }
        reply(e,new TeamPicker().splitTeamsAndAssignRoles(names));
    }

    private void createTeamNoRole(MessageReceivedEvent e)
    {
        List<String> names = getNames(e.getGuild().getId());
        if(names.size() != 10)
        {
            reply(e,"There is not enough names in the list to create teams");
            return;
        }
        reply(e,new TeamPicker().splitTeams(names));
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
