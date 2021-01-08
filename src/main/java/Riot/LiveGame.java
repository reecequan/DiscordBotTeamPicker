package Riot;

import Exceptions.ApiRejectedException;
import Riot.Api.RiotApi;
import Riot.Constants.RiotApiLinks;
import Riot.Storage.SummonerData;
import org.json.JSONObject;

public class LiveGame {

    public void getLiveGameData(String summonerName) throws ApiRejectedException
    {
        SummonerData summonerData = new Summoner().getChampData(summonerName);
        String data;
        try
        {
             data = RiotApi.callRiotAPI(RiotApiLinks.LIVE_GAME, summonerData.getId());
        }
        catch (ApiRejectedException e)
        {
            if(e.getMessage().contains("404"))
            {
                throw new ApiRejectedException("No Live Game Data Found");
            }
            else
            {
                throw e;
            }
        }
        JSONObject object = new JSONObject(data);
        System.out.println(object);

    }
}
