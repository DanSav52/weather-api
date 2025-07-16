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
    private String start_date;
    private String end_date;
    private HourlyValues hourlyValues;
    private String error_message = null;

    public Weather(double latitude, double longitude, String hourly) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.hourlyValues = new HourlyValues(hourly);
    }

    public Weather(double latitude, double longitude, String hourly, String start_date, String end_date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.hourlyValues = new HourlyValues(hourly);
        this.start_date = start_date;
        this.end_date = end_date;
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

    public String getHourly() {return hourlyValues.getHourly();}

    public HourlyValues getHourlyValues() {return hourlyValues;}

    public void setElevation(double elevation) {this.elevation = elevation;}

    public void setTimezone(String timezone) {this.timezone = timezone;}

    public void setHourlyValues(HourlyValues hourlyValues) {this.hourlyValues = hourlyValues;}

    public void setTimezone_abbreviation(String timezone_abbreviation) {this.timezone_abbreviation = timezone_abbreviation;}

    public String getStart_date() {return start_date;}

    public String getEnd_date() {return end_date;}

    public String getError_message() {return error_message;}

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public boolean isErrorEmpty(){
        return this.error_message == null;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        JsonArrayBuilder valuesBuilder = Json.createArrayBuilder();
        for (Date d: hourlyValues.getTime()){
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
                        .add(hourlyValues.getHourly(), Json.createArrayBuilder(hourlyValues.getTemperature())))
                .build();
        return json.toString();
    }
}
