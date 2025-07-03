package ru.chsu.is31.year2025.savelev.model;
import javax.json.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather {
    private double latitude;
    private double longitude;
    @Deprecated
    private String timezone;
    @Deprecated
    private String timezone_abbreviation;
    private double elevation;
    private Hourly hourly;

    public Weather(double latitude, double longitude, String timezone, String timezone_abbreviation, double elevation, Hourly hourly) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.timezone_abbreviation = timezone_abbreviation;
        this.elevation = elevation;
        this.hourly = hourly;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getTimezone_abbreviation() {
        return timezone_abbreviation;
    }

    public double getElevation() {
        return elevation;
    }

    public Hourly getHourly() {return hourly;}

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        JsonArrayBuilder valuesBuilder = Json.createArrayBuilder();
        for (Date d: hourly.getTime()){
            valuesBuilder.add(dateFormat.format(d));
        }
        JsonObject json = Json.createObjectBuilder()
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("timezone", timezone)
                .add("timezone_abbreviation", timezone_abbreviation)
                .add("elevation", elevation)
                .add("hourly", Json.createObjectBuilder()
                        .add("time", valuesBuilder)
                        .add(hourly.getName(), Json.createArrayBuilder(hourly.getTemperature())))
                .build();
        return json.toString();
    }
}
