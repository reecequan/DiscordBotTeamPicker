package Riot.Constants;

public enum DDragonLinks {
    CHAMP_IMAGE("http://ddragon.leagueoflegends.com/cdn/%s/img/champion/%s"),
    VERSION("https://ddragon.leagueoflegends.com/api/versions.json");

    private final String value;


    private DDragonLinks(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }
}
