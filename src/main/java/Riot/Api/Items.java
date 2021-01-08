package Riot.Api;

import Exceptions.ApiRejectedException;
import Riot.Storage.Item;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Items {
    private static final String ITEM_LINK = "http://ddragon.leagueoflegends.com/cdn/%s/data/en_US/item.json";
    private static JSONObject jsonObject;
    private static String version;
    private final static Map<String, Item> items = new HashMap<>();

    private static void setVersion() {
        try {
            Items.version = ApiHelper.getVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkVersion()
    {
        String newVersion = "";
        try {
            newVersion = ApiHelper.getVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version.equals(newVersion);
    }

    public Item getItem(String item)
    {
        if(items.isEmpty() || checkVersion())
        {
            getItems();
        }
        item = item.replaceAll("[^A-z]","").toLowerCase();
        if(items.containsKey(item))
        {
            return items.get(item);
        }
        return new Item();
    }

    public String getItemName(String itemNumber)
    {
        if(jsonObject == null)
        {
            getItems();
        }
        return jsonObject.getJSONObject(itemNumber).getString("name");
    }

    public String getItemCost(String itemNumber)
    {
        if(jsonObject == null)
        {
            getItems();
        }
        return String.valueOf(jsonObject.getJSONObject(itemNumber).getJSONObject("gold").get("base"));
    }

    public void getItems()
    {
        String results = "";
        try {
            setVersion();
            results = ApiHelper.getApiData(String.format(ITEM_LINK, Items.version));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiRejectedException e) {
            System.out.println("unable to get version");
        }

        jsonObject = new JSONObject(results).getJSONObject("data");
        Iterator<String> jsonObject1 = jsonObject.keys();
        while (jsonObject1.hasNext())
        {
            String key =jsonObject1.next();
            JSONObject o = jsonObject.getJSONObject(key);
            Item item = new Item();
            item.setCost(String.valueOf(o.getJSONObject("gold").get("total")));
            item.setUpgradeCost(String.valueOf(o.getJSONObject("gold").get("base")));
            item.setName(o.getString("name"));
            item.setObject(o);
            item.setImageNumber(o.getJSONObject("image").getString("full"));
            item.setDescription(o.getString("description"));
            items.put(item.getSearchName(), item);
        }


    }




}
