package Riot.Storage;

import Riot.Constants.DDragonLinks;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static Riot.Api.ApiHelper.getVersion;
import static Riot.Constants.StringConstants.NEW_LINE;
import static Riot.Constants.StringConstants.REMOVE_HTML_TAGS;

public class ChampData {
    private String champName;
    private String passive;
    private String enemyTips = "";
    private String allyTips = "";
    private String ability1;
    private String ability2;
    private String ability3;
    private String ult;
    private String image;
    private String title;

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title.trim().replaceAll(REMOVE_HTML_TAGS, "");
    }

    public void setChampName(String champName) {
        this.champName = champName.trim().replaceAll(REMOVE_HTML_TAGS, "");
    }

    public void setPassive(String passive) {
        this.passive = passive.trim().replaceAll(REMOVE_HTML_TAGS, "");
    }

    public void setEnemyTips(List<Object> enemyTips) {

        for (int i = 0; i < enemyTips.size();i++)
        {
            this.enemyTips += ((String) enemyTips.get(i)).trim().replaceAll(REMOVE_HTML_TAGS, "").concat(System.getProperty(NEW_LINE).concat(System.getProperty(NEW_LINE)));
        }
    }

    public void setAllyTips(List<Object> enemyTips) {
        for (int i = 0; i < enemyTips.size();i++)
        {
            this.allyTips += ((String) enemyTips.get(i)).trim().replaceAll(REMOVE_HTML_TAGS, "").concat(System.getProperty(NEW_LINE).concat(System.getProperty(NEW_LINE)));
        }
    }

    public void setAbility1(JSONObject spell) {

        this.ability1 = getAbilityDetails(spell);
    }

    private String getAbilityDetails(JSONObject spell)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**Name:** ").append(spell.get("name")).append(System.getProperty(NEW_LINE));
        stringBuilder.append("**Cooldown: **");
        JSONArray cooldowns = spell.getJSONArray("cooldown");
        for(int i =0; i < cooldowns.length(); i++)
        {
            if(i != cooldowns.length() -1)
            {
                stringBuilder.append(cooldowns.getInt(i)).append("/");
            }
            else {
                stringBuilder.append(cooldowns.getInt(i));
            }
        }
        stringBuilder.append(System.getProperty(NEW_LINE));
        stringBuilder.append("**Description: **").append(spell.getString("description").trim().replaceAll(REMOVE_HTML_TAGS, "")).append(System.getProperty(NEW_LINE));
        return stringBuilder.toString();
    }

    public void setAbility2(JSONObject spell) {
        this.ability2 =  getAbilityDetails(spell);;
    }

    public void setAbility3(JSONObject spell) {
        this.ability3 =  getAbilityDetails(spell);;
    }

    public void setUlt(JSONObject spell) {
        this.ult =  getAbilityDetails(spell);;
    }

    public MessageEmbed getMessage()
    {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(champName.concat(" - ").concat(title));
        eb.addField("Passive", passive, true);
        eb.addField("Enemy Tips", enemyTips, false);
        eb.addField("Ally Tips", allyTips, false);
        eb.addField("Ability 1", ability1, false);
        eb.addField("Ability 2", ability2, false);
        eb.addField("Ability 3", ability3, false);
        eb.addField("Ultimate", ult, false);
        try {
            eb.setThumbnail(String.format(DDragonLinks.CHAMP_IMAGE.getValue(),getVersion(),image));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eb.build();

    }

}
