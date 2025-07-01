package ru.chsu.is31.year2025.savelev.model;
import java.util.Date;
import java.util.List;

public class Hourly {
    private List<Date> time;
    private List<Double> temperature;
    private  String name;
    public Hourly(List<Date> time, List<Double> temperature, String hourly) {
        this.time = time;
        this.temperature = temperature;
        this.name = hourly;
    }
    public List<Date> getTime() {return time;}
    public List<Double> getTemperature() {return temperature;}
    public String getName() {return name;}
}
