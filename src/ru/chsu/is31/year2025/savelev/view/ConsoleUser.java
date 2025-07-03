package ru.chsu.is31.year2025.savelev.view;

import ru.chsu.is31.year2025.savelev.model.Weather;
import ru.chsu.is31.year2025.savelev.model.openmeteo.OpenMeteo;
import ru.chsu.is31.year2025.savelev.model.openmeteo.WeatherParser;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class ConsoleUser {
    private static double parseCoordinate(String value, String name) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Недопустимое значение для " + name + ": \"" + value + "\"");
        }
    }

    private  static boolean IsValidCoordinate(double latitude, double longtude){
        return latitude >= -90 && latitude <= 90 && longtude >= -180 && longtude <= 180;
    }

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        OpenMeteo connect_forecast = new OpenMeteo("https", "api.open-meteo.com");
        OpenMeteo connect_archive = new OpenMeteo("https", "archive-api.open-meteo.com");
        String latStr1 = "44.22";
        String lonStr1 = "25.74";
        String latStr2 = "22.3";
        String lonStr2 = "15.2";
        try{
            double lat1 = parseCoordinate(latStr1, "широта");
            double lon1 = parseCoordinate(lonStr1, "долгота");
            if (IsValidCoordinate(lat1, lon1)) {
                Weather w1 = WeatherParser.parse_info(connect_forecast.getForecast(lat1, lon1, "temperature_2m"), "temperature_2m");
                System.out.println(w1);
            }
            else {
                System.err.println("Ошибка координат (w1): координаты вне допустимого диапазона");
            }
        }catch (NumberFormatException e){
            System.err.println("Ошибка координат (w1): " + e.getMessage());
        }
        try {
            double lat2 = parseCoordinate(latStr2, "широта");
            double lon2 = parseCoordinate(lonStr2, "долгота");
            if (IsValidCoordinate(lat2, lon2)){
                Weather w2 = WeatherParser.parse_info(connect_archive.getArchive(lat2, lon2, "relative_humidity_2m", "2025-06-17", "2025-07-01"), "relative_humidity_2m");
                System.out.println(w2);
            }
            else{
                System.err.println("Ошибка координат (w2): координаты вне допустимого диапазона");
            }
        } catch (NumberFormatException e) {
            System.err.println("Ошибка координат (w2): " + e.getMessage());
        }
    }
}

