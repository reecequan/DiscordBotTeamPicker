package Riot.Storage;

import Riot.Ranked;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RankedData implements Comparable{
    private String leagueId;
    private String summonerId;
    private String summonerName;
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private boolean hotStreak;
    private boolean veteran;
    private boolean freshBlood;

    public RankedData(JSONObject jsonObject) {
        leagueId = jsonObject.getString("leagueId");
        summonerId = jsonObject.getString("summonerId");
        summonerName = jsonObject.getString("summonerName");
        queueType = jsonObject.getString("queueType");
        tier = jsonObject.getString("tier");
        rank = jsonObject.getString("rank");
        leaguePoints = jsonObject.getInt("leaguePoints");
        wins = jsonObject.getInt("wins");
        losses = jsonObject.getInt("losses");
        hotStreak = jsonObject.getBoolean("hotStreak");
        freshBlood = jsonObject.getBoolean("freshBlood");
        veteran = jsonObject.getBoolean("veteran");
    }

    public RankedData()
    {
    }

    public RankedData(String summonerName)
    {
        this.summonerName = summonerName;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public String getSummonerId() {
        return summonerId;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public String getQueueType() {
        return queueType;
    }

    public String getTier() {
        return tier;
    }

    public String getRank() {
        return rank;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public boolean isHotStreak() {
        return hotStreak;
    }

    public boolean isVeteran() {
        return veteran;
    }

    public boolean isFreshBlood() {
        return freshBlood;
    }

    private String getShortRank()
    {
        return tier.substring(0,1);
    }

    private String getIntTier()
    {
        switch (rank)
        {
            case "I":
                return "1";
            case "II":
                return "2";
            case "III":
                return "3";
            case "IV":
                return "4";
            case "V":
                return "5";
        }
        return "";
    }

    public String toString()
    {
        if(rank == null)
        {
            return "```Player is unranked```";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(summonerName);
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Rank: ").append(getTier()).append(" ").append(getRank()).append(" at ")
                .append(getLeaguePoints()).append("LP");
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("Wins - ").append(getWins()).append(System.getProperty("line.separator"))
                .append("Losses - ").append(getLosses());
        if(isHotStreak())
        {
            stringBuilder.append(System.getProperty("line.separator"));
            stringBuilder.append("Player is currently on a Hot Streak");
        }
        stringBuilder.append("```");
        return stringBuilder.toString();
    }

    public MessageEmbed getMessage()
    {
        EmbedBuilder eb = new EmbedBuilder();

        if(rank == null)
        {
            return eb.setDescription("Player is Unranked").build();
        }
        eb.setTitle(summonerName);
        eb.addField("Rank",getTier().concat(" ").concat(getRank()), true);
        eb.addBlankField(true);
        eb.addField("Points", String.valueOf(getLeaguePoints()), true );
        eb.addField("Wins", String.valueOf(getWins()), true );
        eb.addBlankField(true);
        eb.addField("Losses ", String.valueOf(getLosses()), true );
        if(isHotStreak())
        {
            eb.setDescription("Player is currently on a Hot Streak");
        }

        return eb.build();
    }

    public String getShort()
    {
        if(rank == null)
        {
            return getSummonerName() + " Unranked";
        }
        return getSummonerName() + " " + getShortRank() + getIntTier() +" at " + getLeaguePoints() + " LP" ;
    }

    @Override
    public int compareTo(Object anotherRank)
    {
        int rankCheck = comapreRank(tier, ((RankedData) anotherRank).getTier());
        if(rankCheck != 0)
        {
            return rankCheck;
        }

        int tierCheck = comapreTier(rank, ((RankedData) anotherRank).getRank());
        if(tierCheck != 0)
        {
            return tierCheck;
        }

        return ((RankedData) anotherRank).getLeaguePoints() - leaguePoints;
    }

    private int comapreRank(String rank, String anotherRank)
    {
        if(rank == null)
        {
            return 1;
        }

        if(anotherRank == null)
        {
            return -1;
        }
        Map<String, Integer> rankMap = new HashMap<>();
        rankMap.put("IRON", 1);
        rankMap.put("BRONZE", 2);
        rankMap.put("SILVER", 3);
        rankMap.put("GOLD", 4);
        rankMap.put("Platinum", 5);
        rankMap.put("Diamond", 6);
        rankMap.put("Master", 7);
        rankMap.put("GrandMaster", 8);
        rankMap.put("Challenger", 9);
        int rankOne = rankMap.get(rank.toUpperCase());
        int rankTwo = rankMap.get(anotherRank.toUpperCase());
        return  rankTwo - rankOne;
    }

    private int comapreTier(String rank, String anotherRank)
    {
        Map<String, Integer> tierMap = new HashMap<>();
        tierMap.put("IV", 1);
        tierMap.put("III", 2);
        tierMap.put("II", 3);
        tierMap.put("I", 4);
        int rankOne = tierMap.get(rank.toUpperCase());
        int rankTwo = tierMap.get(anotherRank.toUpperCase());
        return rankTwo - rankOne;
    }
}
