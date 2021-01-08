package Riot.Constants;

import Riot.LiveGame;

public enum RiotApiLinks {
    LEAGUE_BY_SUMMONER("https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/%s?"),
    SUMMONER("https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/%s?"),
    LIVE_GAME("https://euw1.api.riotgames.com/lol/spectator/v4/active-games/by-summoner/%s?");
    private final String value;


    private RiotApiLinks(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }
}
