package Riot;

import Exceptions.ApiRejectedException;
import Riot.Api.RiotApi;
import Riot.Constants.RiotApiLinks;
import Riot.Storage.RankedData;
import Riot.Storage.SummonerData;
import org.json.JSONArray;
import org.json.JSONObject;

public class Ranked {

    public RankedData getRankedData(String summonerName) throws ApiRejectedException
    {
        SummonerData summonerData = new Summoner().getChampData(summonerName);
        String data = RiotApi.callRiotAPI(RiotApiLinks.LEAGUE_BY_SUMMONER, summonerData.getId());
        if(new JSONArray(data).length()==0)
        {
            //Unranked
            return new RankedData(summonerName);
        }
        JSONObject jsonObject = new JSONArray(data).getJSONObject(0);
        return new RankedData(jsonObject);
    }
}
