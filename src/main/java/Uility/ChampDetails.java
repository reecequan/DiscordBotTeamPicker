package Uility;
import Riot.Storage.ChampData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

import static Riot.Api.ApiHelper.getVersion;

public class ChampDetails{
    private String[] bsCHamps = {"MasterYi","Vayne","Fiora"};

    public ChampData getChampDetails(String champName) throws IOException
    {
        String champNameCap = champName.substring(0, 1).toUpperCase() + champName.substring(1);
        if(champName.toLowerCase().contains("bullshit"))
        {
            champNameCap = bsCHamps[new Random().nextInt(bsCHamps.length)];
        }
        String ver = "11.1.1";
        try {
            ver = getVersion();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        URL url = new URL("https://ddragon.leagueoflegends.com/cdn/" + ver + "/data/en_US/champion/" + champNameCap + ".json");
        String lineRead = null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("userId","dwrf2341");
        int responceCode = connection.getResponseCode();
        if (responceCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer responce = new StringBuffer();
            while((lineRead = in.readLine()) != null)
            {
                responce.append(lineRead);
            }
            in.close();
            JSONObject jsonObject = new JSONObject(responce.toString());
            if(jsonObject.getJSONObject("data").isEmpty())
            {
                //return "No Data Found";
            }
            ChampData champData = new ChampData();

            String passive = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getJSONObject("passive").get("description").toString(); // returns Passive
            String title = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getString("title");
            champData.setTitle(title);
            String image = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getJSONObject("image").get("full").toString(); // returns Passive
            champData.setImage(image);
            champData.setChampName(champNameCap);
            champData.setPassive(passive);
            List<Object> enemyTips = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getJSONArray("enemytips").toList(); // returns Get array of enemy tips
            champData.setEnemyTips(enemyTips);
            List<Object>  allyTips = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getJSONArray("allytips").toList(); // returns Get array of ally tips
            champData.setAllyTips(allyTips);

            JSONArray spells = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getJSONArray("spells") ; // returns Spells
            champData.setAbility1(spells.getJSONObject(0));
            champData.setAbility2(spells.getJSONObject(1));
            champData.setAbility3(spells.getJSONObject(2));
            champData.setUlt(spells.getJSONObject(3));

            return champData;
        }
        else
        {
            //return "Unable to find the champ";
            return new ChampData();
        }
    }

}