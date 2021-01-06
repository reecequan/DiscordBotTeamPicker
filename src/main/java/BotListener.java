import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;

public class BotListener extends ListenerAdapter {
    private java.util.List<String> names = new ArrayList<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        String prompt = e.getMessage().getContentRaw().split(" ")[0];

        String input = e.getMessage().getContentRaw().replace(prompt,"").trim();
        switch (prompt){
            case "!addName":
                addNames(input, e);
                break;
            case "!removeName":
                removeUser(input, e);
                break;
            case "!showUsers":
                e.getChannel().sendMessage(names.toString()).queue();
                break;
            case "!teamCreate":
                createTeam(e);
                break;
            case "!teamNoRoles":
                createTeamNoRole(e);
                break;
            case "!clearTeam":
                names.clear();
                e.getChannel().sendMessage("All names have been cleared").queue();
                break;
            case "!teamHelp":
                getHelp(e);
                break;
        }
    }

    private void addNames(String input, MessageReceivedEvent e)
    {
        String inputStrings[] = input.split(" ");
        for(String name : inputStrings)
        {
            if(names.size() < 10){
                names.add(name.trim());
                e.getChannel().sendMessage(name.trim() + " has been added").queue();
            } else
            {
                e.getChannel().sendMessage("Unable to add " + name + " as list is at capacity").queue();
            }
        }
    }

    private void createTeam(MessageReceivedEvent e)
    {
        if(names.size() != 10)
        {
            e.getChannel().sendMessage("There is not enough names in the list to create teams").queue();
            return;
        }
        e.getChannel().sendMessage(new TeamPicker().splitTeamsAndAssignRoles(names)).queue();
    }

    private void createTeamNoRole(MessageReceivedEvent e)
    {
        if(names.size() != 10)
        {
            e.getChannel().sendMessage("There is not enough names in the list to create teams").queue();
            return;
        }
        e.getChannel().sendMessage(new TeamPicker().splitTeams(names)).queue();
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

        sb.append("!clearTeam - removes all names from the team list");
        sb.append(System.getProperty("line.separator"));

        sb.append("```");
        e.getChannel().sendMessage(sb.toString()).queue();
    }

    private void removeUser(String input, MessageReceivedEvent e)
    {
        String inputStrings[] = input.split(" ");
        for(String name : inputStrings)
        {
            if(names.remove(name)){
                e.getChannel().sendMessage(name.trim() + " has been removed").queue();
            } else
            {
                e.getChannel().sendMessage("Unable to remove " + name + " as not found in the list").queue();
            }
        }
    }


}
