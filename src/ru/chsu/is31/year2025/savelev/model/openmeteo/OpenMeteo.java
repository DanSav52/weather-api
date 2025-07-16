package ru.chsu.is31.year2025.savelev.model.openmeteo;
import ru.chsu.is31.year2025.savelev.model.Weather;
import java.io.IOException;
import java.net.*;
import java.text.ParseException;
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

    public Weather getForecast(Weather weather) throws MalformedURLException, URISyntaxException, ParseException {
        delParams();
        setParam(LATITUDE, String.valueOf(weather.getLatitude()));
        setParam(LONGITUDE, String.valueOf(weather.getLongitude()));
        setParam(HOURLY, weather.getHourly());
        URL url = getUrl("/v1/forecast");
        try {
            HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300){
                weather.setHourlyValues(WeatherParser.parse_info_pro(connection.getInputStream(), weather));
            }
            else{
                weather.setError_message("Ошибка от сервера Open-Meteo: " + WeatherParser.parse_error(connection.getErrorStream(), weather));
            }
            return weather;
        } catch (MalformedURLException e) {
            throw new MalformedURLException("Переданная строка не является URL: " + url.toString());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка соединения с сервером: " + e.getMessage());
        } catch (ParseException e) {
            throw new ParseException( e.getMessage(), 0);
        }
    };

    public Weather getArchive(Weather weather) throws MalformedURLException, URISyntaxException, ParseException {
        delParams();
        setParam(LATITUDE, String.valueOf(weather.getLatitude()));
        setParam(LONGITUDE, String.valueOf(weather.getLongitude()));
        setParam(HOURLY, String.valueOf(weather.getHourly()));
        setParam(START_DATE, weather.getStart_date());
        setParam(END_DATE, weather.getEnd_date());
        URL url = getUrl("/v1/archive");
        try {
            HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300){
                weather.setHourlyValues(WeatherParser.parse_info_pro(connection.getInputStream(), weather));
            }
            else{
                weather.setError_message("Ошибка от сервера Open-Meteo: " + WeatherParser.parse_error(connection.getErrorStream(), weather));
            }
            return weather;
        } catch (MalformedURLException e) {
            throw new MalformedURLException("Переданная строка не является URL: " + url.toString());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка соединения с сервером: " + e.getMessage());
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), 0);
        }
    }

    public void delParams(){
        this.params.clear();
    }

    public void setParam(String key, String value) {
        this.params.put(key, value);
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
