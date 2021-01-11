package RiotApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {

    public static String getApiData(String link) throws IOException
    {
        URL url = new URL(link);
        String lineRead = null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("userId","dwrf2341");
        int responceCode = connection.getResponseCode();
        StringBuffer responce = new StringBuffer();
        if (responceCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((lineRead = in.readLine()) != null) {
                responce.append(lineRead);
            }
            in.close();
        }
        return responce.toString();
    }

    public static String getVersion() throws IOException
    {
        String data = getApiData("https://ddragon.leagueoflegends.com/api/versions.json");
        return data.replaceAll("\"","").replaceAll("\\[","").replaceAll("\\]","").split(",")[0];
    }
}
