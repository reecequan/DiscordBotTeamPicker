package Riot.Storage;

import Riot.Api.Items;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.StringJoiner;

public class Item {
    private String name;
    private String searchName;
    private String totalCost;
    private String upgradeCost;
    private JSONObject object;
    private String description;
    private String imageNumber;
    private String better;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.searchName = name.toLowerCase().replaceAll("[^A-z]","");
    }

    public String getSearchName() {
        return searchName;
    }

    public String getCost() {
        return totalCost;
    }

    public void setCost(String cost) {
        this.totalCost = cost;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        setBetterDescription(description);
        description = description.replaceAll("<br>", System.getProperty("line.separator"));
        description = description.replaceAll("<.*?>", "");
        this.description = description;
    }

    private void setBetterDescription(String description)
    {
        this.better = description;
    }

    private void getBetterDescription(EmbedBuilder embedBuilder)
    {
        String[] descSplit = better.split("<\\/stats>");
        String stats = descSplit[0];
        String[] statsSplit = stats.split("<br>");
        for(String stat : statsSplit)
        {
            stat = stat.trim().replaceAll("<.*?>", "");
            String[] split = stat.trim().split(" ");
            embedBuilder.addField(stat.replace(split[0],"").trim(), split[0], true);
        }

        String desc = descSplit[1];
        desc = desc.replace("<br>","").replaceAll("<br>", System.getProperty("line.separator"));
        desc = desc.replaceAll("<.*?>", "");
        if(desc.isEmpty())
        {
            return;
        }
        embedBuilder.addField("Description",desc, false);


    }

    public String getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(String imageNumber) {
        this.imageNumber = imageNumber;
    }

    private String getBuildsInto()
    {
        StringJoiner stringJoiner = new StringJoiner(",");
        if(object.keySet().contains("into"))
        {
            JSONArray intoArray = object.getJSONArray("into");
            for(Object item : intoArray)
            {
                stringJoiner.add(new Items().getItemName((String) item) + " (" + new Items().getItemCost((String) item) + ")");
            }
            return stringJoiner.toString();
        }
        return "";
    }

    private String getBuildsFrom()
    {
        StringJoiner stringJoiner = new StringJoiner(", ");
        if(object.keySet().contains("from"))
        {
            JSONArray intoArray = object.getJSONArray("from");
            for(Object item : intoArray)
            {
                stringJoiner.add(new Items().getItemName((String) item) + " (" + new Items().getItemCost((String) item) + ")");
            }
            return stringJoiner.toString();
        }
        return "";
    }

    public void setUpgradeCost(String upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

    public MessageEmbed getMessage()
    {
        EmbedBuilder eb = new EmbedBuilder();
        if(name == null)
        {
            eb.setDescription("Unable to find Item");
            return eb.build();
        }
        eb.setTitle(name);
        eb.setThumbnail("http://ddragon.leagueoflegends.com/cdn/11.1.1/img/item/"+imageNumber);
        eb.addField("Total Cost",totalCost, true);
        eb.addField("Upgrade Cost",upgradeCost, true);
        eb.addBlankField(true);
        getBetterDescription(eb);
        String from = getBuildsFrom();
        if(!from.isEmpty())
        {
            eb.addField("Builds From", from, false);
        }
        String to = getBuildsInto();
        if(!to.isEmpty())
        {
            eb.addField("Builds Into", to, false);
        }
        return eb.build();

    }

}
