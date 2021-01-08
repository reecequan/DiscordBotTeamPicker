package Riot.Api;

import Exceptions.ApiRejectedException;
import Riot.Constants.DDragonLinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {

    public static String getApiData(String link) throws IOException, ApiRejectedException
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
        else
        {
            throw new ApiRejectedException("Api call failed with reason code - " + responceCode);
        }
        return responce.toString();
    }

    public static String getVersion() throws IOException, ApiRejectedException
    {
        String data = getApiData(DDragonLinks.VERSION.getValue());
        return data.replaceAll("\"","").replaceAll("\\[","").replaceAll("\\]","").split(",")[0];
    }
}
