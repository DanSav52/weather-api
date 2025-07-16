package ru.chsu.is31.year2025.savelev.model.openmeteo;
import ru.chsu.is31.year2025.savelev.model.HourlyValues;
import ru.chsu.is31.year2025.savelev.model.Weather;

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

    public static HourlyValues parse_info_pro(InputStream iS, Weather weather) throws ParseException {
        try(JsonReader reader = Json.createReader(iS)){
            JsonObject jobj = reader.readObject();
            Date date = new Date();
            List<Date> dates = new ArrayList<>();
            for (JsonValue element: jobj.getJsonObject("hourly").getJsonArray("time")){
                try {
                    dates.add(OPENMETEO_DATE_FORMAT.parse(((JsonString) element).getString()));
                } catch (ParseException e){
                    throw new ParseException(e.getMessage(), 0);
                }
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

        }
        return weather.getHourlyValues();
    }

    public static String parse_error(InputStream iS, Weather weather) throws ParseException {
        try(JsonReader reader = Json.createReader(iS)) {
            JsonObject jobj = reader.readObject();
            return jobj.getString("reason");
        }catch (JsonParsingException e){
            throw new ParseException("Ошибка ввода данных: " + e.getMessage(), 0);
        }
    }
}
