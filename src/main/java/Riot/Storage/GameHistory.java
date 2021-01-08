package Riot.Storage;

import Uility.Csv;

public class GameHistory {
    private String output;
    private Csv csv;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Csv getCsv() {
        return csv;
    }

    public void setCsv(Csv csv) {
        this.csv = csv;
    }
}
