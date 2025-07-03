package ru.chsu.is31.year2025.savelev.model.openmeteo;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
public class OpenMeteo {
    private final String  protocol;
    private final String server_name;
    private Map<String, String> params = new HashMap<>();
    public static String LATITUDE = "latitude";
    public  static final String LONGITUDE = "longitude";
    public  static final String HOURLY = "hourly";
    public  static final String START_DATE = "start_date";
    public  static final String END_DATE = "end_date";
    public OpenMeteo(String protocol, String server_name) throws MalformedURLException {
        this.protocol = protocol;
        this.server_name = server_name;
    }

    public InputStream getForecast(double latitude, double longitude, String hourly) throws MalformedURLException, URISyntaxException {
        setParams_forecast(String.valueOf(latitude), String.valueOf(longitude), hourly);
        return get_json(getUrl("/v1/forecast"));
    };

    public InputStream getArchive(double latitude, double longitude, String hourly, String start_date, String end_date) throws MalformedURLException, URISyntaxException {
        setParams_archive(String.valueOf(latitude), String.valueOf(longitude), hourly, start_date, end_date);
        return get_json(getUrl("/v1/archive"));
    }

    @Deprecated
    public InputStream get_json(URL url){
        try {
            HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()){
                return connection.getInputStream();
            }
            else {
                return null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setParams_forecast(String latitude, String longitude, String hourly) {
        this.params.put(LATITUDE, latitude);
        this.params.put(LONGITUDE, longitude);
        this.params.put(HOURLY, hourly);
    }

    public void setParams_archive(String latitude, String longitude, String hourly, String start_date, String end_date) {
        this.params.put(LATITUDE, latitude);
        this.params.put(LONGITUDE, longitude);
        this.params.put(START_DATE, start_date);
        this.params.put(END_DATE, end_date);
        this.params.put(HOURLY, hourly);
    }

    public URL getUrl(String path) throws MalformedURLException, URISyntaxException {
        return new URI(protocol,
                server_name,
                path,
                String.join("&",params.entrySet().stream().map(tp -> tp.getKey()+"="+tp.getValue()).toList()), null)
                .toURL();
    }

    public String getHourly() {
        return params.get("hourly");
    }
}
