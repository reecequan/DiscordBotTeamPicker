import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamPicker {
    StringBuilder sb = new StringBuilder();


    private List<String> getRoles()
    {
        List<String> roles = new ArrayList();
        roles.add("Top");
        roles.add("Jungle");
        roles.add("Mid");
        roles.add("ADC");
        roles.add("Support");
        return new ArrayList<>(roles);
    }

    private void assignTeam(List<String> names)
    {
        List<String> roles = getRoles();
        for (String name : names)
        {
            String role = roles.get(0);
            sb.append(name.trim() + " has been assigned " + role);
            sb.append(System.getProperty("line.separator"));
            roles.remove(role);
        }
    }

    protected String splitTeams(List<String> allNames)
    {
        List <String> teamOne = new ArrayList<>();
        List <String> teamTwo = new ArrayList<>();
        Collections.shuffle(allNames);
        for(int i=0; i < allNames.size(); i++)
        {
            if(i % 2 == 0)
            {
                teamOne.add(allNames.get(i));
            }
            else
            {
                teamTwo.add(allNames.get(i));
            }
        }
        sb.append("```");
        sb.append("--------------");
        sb.append("Team One");
        sb.append("--------------");
        sb.append(System.getProperty("line.separator"));
        for (String name: teamOne)
        {
            sb.append(name);
            sb.append(System.getProperty("line.separator"));
        }
        sb.append("--------------");
        sb.append("Team Two");
        sb.append("--------------");
        sb.append(System.getProperty("line.separator"));
        for (String name: teamTwo)
        {
            sb.append(name);
            sb.append(System.getProperty("line.separator"));
        }
        sb.append("```");
        return sb.toString();
    }

    protected String splitTeamsAndAssignRoles(List<String> allNames)
    {
        List <String> teamOne = new ArrayList<>();
        List <String> teamTwo = new ArrayList<>();
        Collections.shuffle(allNames);
        for(int i=0; i < allNames.size(); i++)
        {
            if(i % 2 == 0)
            {
                teamOne.add(allNames.get(i));
            }
            else
            {
                teamTwo.add(allNames.get(i));
            }
        }
        sb.append("```");
        sb.append("--------------");
        sb.append("Team One");
        sb.append("--------------");
        sb.append(System.getProperty("line.separator"));
        assignTeam(teamOne);
        sb.append("--------------");
        sb.append("Team Two");
        sb.append("--------------");
        sb.append(System.getProperty("line.separator"));
        assignTeam(teamTwo);
        sb.append("```");
        return sb.toString();
    }

}
