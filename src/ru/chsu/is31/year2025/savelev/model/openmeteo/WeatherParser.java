package ru.chsu.is31.year2025.savelev.model.openmeteo;
import ru.chsu.is31.year2025.savelev.model.HourlyValues;
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

    public static Weather parse_info(InputStream iS, Weather weather) throws WeatherException{
        if (iS == null) {
            throw new WeatherException(weather.getError_message() + "Пустой поток данных от сервера.");
        }
        try(JsonReader reader = Json.createReader(iS)) {
            JsonObject jobj = reader.readObject();
            if(jobj.containsKey("error")){
                throw new WeatherException(weather.getError_message() + jobj.getString("reason"));
            }

            Date date = new Date();
            List<Date> dates = new ArrayList<>();
            for (JsonValue element: jobj.getJsonObject("hourly").getJsonArray("time")){
                try {
                    dates.add(OPENMETEO_DATE_FORMAT.parse(((JsonString) element).getString()));
                } catch (ParseException e) {
                    throw new WeatherException(weather.getError_message() + "Ошибка разбора даты.", e);
                }
            }

            if (jobj.getJsonObject("hourly") == null
                    || !jobj.getJsonObject("hourly").containsKey("time")
                    || !jobj.getJsonObject("hourly").containsKey(weather.getHourly())) {
                throw new WeatherException(weather.getError_message() + "Запрос не содержит необходимых данных (hourly): " + weather.getHourly());
            }

            weather.setElevation(jobj.getJsonNumber("elevation").doubleValue());
            weather.setTimezone(jobj.getString("timezone"));
            weather.setTimezone_abbreviation(jobj.getString("timezone_abbreviation"));
            weather.getHourlyValues().setTemperature(
                    dates,
                    jobj.getJsonObject("hourly").getJsonArray(weather.getHourly()).getValuesAs(JsonNumber.class)
                            .stream()
                            .map(JsonNumber::doubleValue)
                            .collect(Collectors.toList())
            );

            return weather;

        } catch (JsonParsingException e){
            throw new WeatherException(weather.getError_message() + "Неправильно введены параметры запроса: " + e.getMessage());
        }

    }
}
