package Uility;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class ChampDetails{
    private String[] bsCHamps = {"MasterYi","Vayne","Fiora"};

    public String getChampDetails(String champName) throws IOException
    {
        String champNameCap = champName.substring(0, 1).toUpperCase() + champName.substring(1);
        if(champName.toLowerCase().contains("bullshit"))
        {
            champNameCap = bsCHamps[new Random().nextInt(bsCHamps.length)];
        }
        URL url = new URL("https://ddragon.leagueoflegends.com/cdn/11.1.1/data/en_US/champion/"+champNameCap+".json");
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
                return "No Data Found";
            }
            String passive = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getJSONObject("passive").get("description").toString(); // returns Passive
            List<Object> enemyTips = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getJSONArray("enemytips").toList(); // returns Get array of enemy tips
            List<Object>  allyTips = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getJSONArray("allytips").toList(); // returns Get array of ally tips
            JSONArray spells = jsonObject.getJSONObject("data").getJSONObject(champNameCap).getJSONArray("spells") ; // returns Spells
            StringBuilder stringBuilder = new StringBuilder("```");
            stringBuilder.append("Champion : ").append(champNameCap).append(System.getProperty("line.separator"));
            stringBuilder.append("Passive : ").append(passive).append(System.getProperty("line.separator"));
            stringBuilder.append("Enemy Tips : ");
            for (int i = 0; i < enemyTips.size();i++)
            {
                if (i!=0)
                {
                    stringBuilder.append("             ");
                }
                stringBuilder.append((String) enemyTips.get(i)).append(System.getProperty("line.separator"));
            }
            stringBuilder.append("Ally Tips : ");
            for (int i = 0; i < allyTips.size();i++)
            {
                if (i!=0)
                {
                    stringBuilder.append("             ");
                }
                stringBuilder.append((String) allyTips.get(i)).append(System.getProperty("line.separator"));
            }
            for (Object spell : spells  )
            {
                stringBuilder.append("Name: ").append(((JSONObject)spell).get("name")).append(System.getProperty("line.separator"));
                stringBuilder.append("Cooldown: ").append(((JSONObject)spell).get("cooldown")).append(System.getProperty("line.separator"));
                stringBuilder.append("Description: ").append(((JSONObject)spell).get("description")).append(System.getProperty("line.separator"));
            }
            stringBuilder.append("```");
            return stringBuilder.toString().replaceAll("(<br><br>)|(<br>)",System.getProperty("line.separator")).replaceAll("(<font color=)['#0-9A-f]*>","").replaceAll("(</font>)","");

        }
        else
        {
            return "Unable to find the champ";
        }
    }

}