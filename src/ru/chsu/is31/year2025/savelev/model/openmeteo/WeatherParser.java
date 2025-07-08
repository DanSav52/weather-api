package ru.chsu.is31.year2025.savelev.model.openmeteo;
import ru.chsu.is31.year2025.savelev.model.Hourly;
import ru.chsu.is31.year2025.savelev.model.Weather;
import ru.chsu.is31.year2025.savelev.model.WeatherException;

import javax.json.*;
import javax.json.stream.JsonParsingException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherParser  {
    private static final SimpleDateFormat OPENMETEO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public static Weather parse_info(InputStream iS, Weather weather, String error_message) throws WeatherException{
        if (iS == null) {
            error_message = "Пустой поток данных от сервера.";
            throw new WeatherException(error_message);
        }
        try(JsonReader reader = Json.createReader(iS)) {
            JsonObject jobj = reader.readObject();
            if(jobj.containsKey("error")){
                error_message = "Ошибка от сервера Open-Meteo: ";
                throw new WeatherException(error_message + jobj.getString("reason"));
            }

            Date date = new Date();
            List<Date> dates = new ArrayList<>();
            for (JsonValue element: jobj.getJsonObject("hourly").getJsonArray("time")){
                try {
                    dates.add(OPENMETEO_DATE_FORMAT.parse(((JsonString) element).getString()));
                } catch (ParseException e) {
                    error_message = "Ошибка разбора даты.";
                    throw new WeatherException(error_message, e);
                }
            }

            if (jobj.getJsonObject("hourly") == null || !jobj.getJsonObject("hourly").containsKey("time") || !jobj.getJsonObject("hourly").containsKey(weather.getHourly())) {
                error_message = "Ответ не содержит необходимых данных (hourly): ";
                throw new WeatherException(error_message + weather.getHourly());
            }

            return new Weather(
                    weather.getLatitude(),
                    weather.getLongitude(),
                    jobj.getString("timezone"),
                    jobj.getString("timezone_abbreviation"),
                    jobj.getJsonNumber("elevation").doubleValue(),
                    new Hourly(
                            dates,
                            jobj.getJsonObject("hourly").getJsonArray(weather.getHourly()).getValuesAs(JsonNumber.class)
                                    .stream()
                                    .map(JsonNumber::doubleValue)
                                    .collect(Collectors.toList()),
                            weather.getHourly()
                    )
            );
        } catch (JsonParsingException e){
            error_message = "Неправильно введены параметры запроса: " + e.getMessage();
            throw new WeatherException(error_message);
        }


    }
}
