package RiotApi;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GameData {
    private int wins;
    private int losses;
    private int kills ;
    private int deaths;
    private int assists;
    private int visionScore ;
    private int damage;
    private int goldEarned;
    private int visionWards;
    private double wardPer;
    private int numOfGames;


    public GameData() {
    }

    public void addData(boolean win, int kills, int deaths, int assists,int visionScore, int damage, int goldEarned,int visionWards, double wardPer)
    {
        if(win)
        {
            wins++;
        }
        else
        {
            losses++;
        }
        this.kills+=kills;
        this.deaths += deaths;
        this.assists += assists;
        this.visionScore += visionScore;
        this.damage += damage;
        this.goldEarned += goldEarned;
        this.visionWards += visionWards;
        this.wardPer += wardPer;
        numOfGames++;
    }

    public String getAverage() {
        String output = String.format("Past %s Games Wins:%s loss: %s, Kills %s, Deaths: %s, Assists: %s,KDA: %s, Vision Score: %s, Vision Per: %s, Damage To Champs: %s, Gold: %s, Vision Wards: %s",
                numOfGames,
                wins,
                losses,
                kills/numOfGames,
                deaths/numOfGames,
                assists/numOfGames,
                round((kills + assists) / ((double) deaths + numOfGames)),
                visionScore/numOfGames,
                round(wardPer/numOfGames),
                damage/numOfGames,
                goldEarned/numOfGames,
                visionWards/numOfGames);
        return output;
    }

    private double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
