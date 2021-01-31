package Riot.Api;

import Exceptions.ApiRejectedException;
import Uility.Csv;
import Riot.Storage.GameData;
import Riot.Storage.GameHistory;
import Riot.Constants.RiotApiLinks;
import Uility.Keys;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static Riot.Api.ApiHelper.getApiData;
import static Riot.Api.ApiHelper.getVersion;

public class RiotApi {
    public static final String KEY = Keys.getKey(Keys.KEYS.RIOT);
    public static JSONObject champData;
    private String role;
    private String date;
    private int numOfGames;
    private final GameData gameData = new GameData();
    private static final Map<String, Integer> queueType = new HashMap<>();
    private String summonorName;
    private String queue;
    private HashMap<String, String> sumData;
    private StringBuilder stringBuilder = new StringBuilder("```");
    private Csv csv = new Csv();
    private final GameHistory gameHistory = new GameHistory();
    private long startTime;
    private long endTime;
    private String champ = "";

    static{
        queueType.put("aram",450);
        queueType.put("ranked",420);
        queueType.put("flex",440);
        queueType.put("normal",400);
    }

    public RiotApi(String summonorName, String queue, int numOfGames, String champ)
    {
        this.summonorName = summonorName;
        this.numOfGames = numOfGames;
        if(queueType.containsKey(queue.toLowerCase()))
        {
            this.queue = String.valueOf(queueType.get(queue.toLowerCase()));
        }
        this.champ = champ;
    }

    public RiotApi(String summonorName, String queue, Date start, Date end)
    {
        this.summonorName = summonorName;
        if(queueType.containsKey(queue.toLowerCase()))
        {
            this.queue = String.valueOf(queueType.get(queue.toLowerCase()));
        }
        startTime = start.getTime();
        endTime = end.getTime();
    }
    public static String callRiotAPI(RiotApiLinks link, String... args) throws ApiRejectedException
    {
        String data = "";
        try {
            data = getApiData(String.format(link.getValue(), (Object[]) args).concat("api_key=").concat(KEY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void getChampData() throws ApiRejectedException
    {
        String data = "";
        try {
            data = getApiData("https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+summonorName +"?api_key="+KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(data);
        sumData = new HashMap<>();
        sumData.put("puuid", jsonObject.getString("puuid"));
        sumData.put("accountId", jsonObject.getString("accountId"));
        sumData.put("name", jsonObject.getString("name"));
        sumData.put("summonerLevel", String.valueOf(jsonObject.getInt("summonerLevel")));
        sumData.put("id", jsonObject.getString("id"));
    }

    private void getRole(JSONObject jsonObject)
    {
        String lane = jsonObject.getJSONArray("matches").getJSONObject(0).getString("lane");
        if(lane.equals("BOTTOM"))
        {
            String duoRol = jsonObject.getJSONArray("matches").getJSONObject(0).getString("role");
            if(duoRol.contains("SUPPORT"))
            {
                role = "SUPPORT";
            }
            else
            {
                role="BOTTOM";
            }
            return;
        }
        role = lane;
    }

    public GameHistory getGameHistory() throws ApiRejectedException
    {
        getChampData();
        String data = "";
        String champNum = "";
        System.out.println(sumData.get("id"));
        System.out.println(startTime);
        try {
            data = getApiData("https://euw1.api.riotgames.com/lol/match/v4/matchlists/by-account/"+sumData.get("accountId")+"?champion="+getChampNum(champ)+"&queue="+queue+"&beginTime="+startTime+"&="+endTime+"&api_key="+KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(data);
        if (numOfGames == 0)
        {
            numOfGames = jsonObject.getJSONArray("matches").length();
        }
        if(jsonObject.getJSONArray("matches").length() < numOfGames)
        {
            numOfGames = jsonObject.getJSONArray("matches").length();
        }
        setUpCsv();


        for(int i = 0; i < numOfGames; i++) {
            date = getDate(jsonObject.getJSONArray("matches").getJSONObject(i).getLong("timestamp"));
            String gameId = String.valueOf(jsonObject.getJSONArray("matches").getJSONObject(i).getBigInteger("gameId"));
            String champId = String.valueOf(jsonObject.getJSONArray("matches").getJSONObject(i).getBigInteger("champion"));
            getRole(jsonObject);
            getMatchData(gameId, champId);
        }
        stringBuilder.append(gameData.getAverage());
        gameHistory.setOutput(stringBuilder.append("```").toString());
        gameHistory.setCsv(csv);
        return gameHistory;

    }

    private void setUpCsv()
    {
        csv.add("Date").add("Win Or Loss").add("Champ").add("Kills").add("Deaths").add("Assists").add("KDA").add("Vision Score").add("Vision Per Min").add("Damage To Champs").add("Gold").add("Vision Wards").add("Role").addBreak();
    }

    public void getMatchData(String gameId, String champId) throws ApiRejectedException
    {
        String data = "";
        try {
            data = getApiData("https://euw1.api.riotgames.com/lol/match/v4/matches/"+gameId+"?api_key="+KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(data);
        int gameDuration = jsonObject.getInt("gameDuration");
        JSONArray partData = jsonObject.getJSONArray("participants");
        String champName = getChampName(champId);
        for(Object part : partData)
        {
            JSONObject d = (JSONObject) part;
            String partChampId = String.valueOf(d.get("championId"));
            if((champId).equals(partChampId))
            {
                JSONObject pd = d.getJSONObject("stats");
                boolean win = pd.getBoolean("win");
                int kills = pd.getInt("kills");
                int deaths = pd.getInt("deaths");
                int assists = pd.getInt("assists");
                int visionScore =pd.getInt("visionScore");
                int damage = pd.getInt("totalDamageDealtToChampions");
                int goldEarned = pd.getInt("goldEarned");
                int visionWards = pd.getInt("visionWardsBoughtInGame");
                double wardPer = visionScore/( (double) gameDuration/60);
                double kda =  round((kills + assists) / ((double) deaths + 1), 2);
                gameData.addData(win,kills,deaths,assists,visionScore,damage,goldEarned,visionWards,wardPer);
                csv.add(date).add((win) ? "Win":"Loss").add(champName).add(kills).add(deaths).add(assists).add(kda).add(visionScore).add(wardPer).add(damage).add(goldEarned).add(visionWards).add(role).addBreak();
                String output = String.format("Date: %s, Win/loss: %s,Champ: %s, Kills %s, Deaths: %s, Assists: %s,KDA: %s, Vision Score: %s, Vision Per: %s, Damage To Champs: %s, Gold: %s, Vision Wards: %s, Role: %s",
                        date,
                        (win) ? "Win":"Loss",
                        champName,
                        kills,
                        deaths,
                        assists,
                        round((kills + assists) / ((double) deaths + 1), 2),
                        visionScore,
                        round(wardPer,2),
                        damage,
                        goldEarned,
                        visionWards,
                        role);
                stringBuilder.append(output);
                stringBuilder.append(System.getProperty("line.separator"));
            }
        }
    }

    private String getChampName(String num) throws ApiRejectedException
    {
        if(champData == null) {
            String data = "";
            try {
                String version = getVersion();
                data = getApiData("https://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_GB/champion.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            champData = new JSONObject(data).getJSONObject("data");
        }
        Iterator<String> i = champData.keys();

        while (i.hasNext())
        {
            String key = i.next();
            JSONObject s = champData.getJSONObject(key);
            if(s.get("key").equals(num))
            {
                return s.getString("name");
            }
        }

        return "Unknown";
    }

    private String getChampNum(String num) throws ApiRejectedException
    {
        if(champData == null) {
            String data = "";
            try {
                String version = getVersion();
                data = getApiData("https://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_GB/champion.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            champData = new JSONObject(data).getJSONObject("data");
        }
        Iterator<String> i = champData.keys();

        while (i.hasNext())
        {
            String key = i.next();
            JSONObject s = champData.getJSONObject(key);
            if(s.get("id").toString().toLowerCase().equals(num.toLowerCase()))
            {
                return s.getString("key");
            }
        }

        return "";
    }

    ////UTILITY Methods

    private double round(double value, int places)
    {
        if (places < 0)
        {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private String getDate(long timestamp)
    {
        Date date = new Date(timestamp);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }

}
