package ru.chsu.is31.year2025.savelev.model.openmeteo;
import ru.chsu.is31.year2025.savelev.model.Hourly;
import ru.chsu.is31.year2025.savelev.model.Weather;
import javax.json.*;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherParser {
    private static final SimpleDateFormat OPENMETEO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public static Weather parse_info(InputStream iS, String hourly){
        try(JsonReader reader = Json.createReader(iS)) {
            JsonObject jobj = reader.readObject();
            Date date = new Date();
            List<Date> dates = new ArrayList<>();
            for (JsonValue element: jobj.getJsonObject("hourly").getJsonArray("time")){
                try {
                    dates.add(OPENMETEO_DATE_FORMAT.parse(((JsonString) element).getString()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            return new Weather(
                    jobj.getJsonNumber("latitude").doubleValue(),
                    jobj.getJsonNumber("longitude").doubleValue(),
                    jobj.getJsonNumber("generationtime_ms").doubleValue(),
                    jobj.getInt("utc_offset_seconds"),

                    jobj.getString("timezone"),
                    jobj.getString("timezone_abbreviation"),

                    jobj.getJsonNumber("elevation").doubleValue(),
                    new Hourly(
                            dates,
                            jobj.getJsonObject("hourly").getJsonArray(hourly).getValuesAs(JsonNumber.class)
                                    .stream()
                                    .map(JsonNumber::doubleValue)
                                    .collect(Collectors.toList()),
                            hourly
                    )
            );
        }

    }
}
