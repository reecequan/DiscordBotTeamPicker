package Riot;

import Exceptions.ApiRejectedException;
import Riot.Api.RiotApi;
import Riot.Storage.SummonerData;
import org.json.JSONObject;

import static Riot.Constants.RiotApiLinks.SUMMONER;

public class Summoner {

    public SummonerData getChampData(String summonerName) throws ApiRejectedException
    {
        String data =  RiotApi.callRiotAPI(SUMMONER, summonerName);
        JSONObject jsonObject = new JSONObject(data);
        SummonerData sumData = new SummonerData();
        sumData.setPuuid(jsonObject.getString("puuid"));
        sumData.setAccountId(jsonObject.getString("accountId"));
        sumData.setName(jsonObject.getString("name"));
        sumData.setSummonerLevel( String.valueOf(jsonObject.getInt("summonerLevel")));
        sumData.setId(jsonObject.getString("id"));
        return sumData;
    }

}
