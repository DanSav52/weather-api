package ru.chsu.is31.year2025.savelev.model;
import java.util.Date;
import java.util.List;
public class HourlyValues {
    private  String hourly;
    private List<Date> time;
    private List<Double> temperature;

    public HourlyValues(String hourly){
        this.hourly = hourly;
    }

    public void setTemperature(List<Date> time, List<Double> temperature) {
        this.time = time;
        this.temperature = temperature;
    }

    public List<Date> getTime() {return time;}

    public List<Double> getTemperature() {return temperature;}

    public String getHourly() {return hourly;}
}
