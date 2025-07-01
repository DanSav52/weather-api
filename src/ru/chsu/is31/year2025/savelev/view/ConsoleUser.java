package ru.chsu.is31.year2025.savelev.view;

import ru.chsu.is31.year2025.savelev.model.Weather;
import ru.chsu.is31.year2025.savelev.model.openmeteo.OpenMeteo;
import ru.chsu.is31.year2025.savelev.model.openmeteo.WeatherParser;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class ConsoleUser {
    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        OpenMeteo connect = new OpenMeteo("https", "api.open-meteo.com");
        Weather w1 = WeatherParser.parse_info(connect.getForecast(4423, 25.74, "temperature_2m"), "temperature_2m");
        System.out.println(w1.toString());
        Weather w2 = WeatherParser.parse_info(connect.getForecast(22.3, 15.2, "relative_humidity_2m"), "relative_humidity_2m");
        System.out.println(w2.toString());
        //todo как обрабатывать при передачи ошибочных долготы и широты (строковый литерал)
    }
}

