package ru.chsu.is31.year2025.savelev.view;

import ru.chsu.is31.year2025.savelev.controller.Validator;
import ru.chsu.is31.year2025.savelev.model.Weather;
import ru.chsu.is31.year2025.savelev.model.WeatherException;
import ru.chsu.is31.year2025.savelev.model.openmeteo.OpenMeteo;
import ru.chsu.is31.year2025.savelev.model.openmeteo.WeatherParser;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleUser {
    public static double readLatitude(Scanner scanner){
        double latitude;
        while (true){
            System.out.println("Введите широту: ");
            try {
                latitude = scanner.nextDouble();
                if (Validator.isValidLatitude(latitude)) {
                    return latitude;
                }
                else {
                    System.out.println("Ошибка: широта должна быть в диапазоне от -90 до 90. Повторите ввод.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите числовое значение широты.");
                scanner.nextLine();
            }
        }
    }
    public static double readLongitude(Scanner scanner){
        double longitude;
        while (true) {
            System.out.println("Введите долготу: ");
            try {
                longitude = scanner.nextDouble();
                if (Validator.isValidLongitude(longitude)){
                    return longitude;
                }
                else {
                    System.out.println("Ошибка: долгота должна быть в диапазоне от -180 до 180. Повторите ввод.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите числовое значение широты.");
                scanner.nextLine();
            }
        }
    }

    public static void main(String[] args)  {
        double latitude, longitude;
        String hourly;
        int choice;
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);
        while (true) {
            System.out.println("Какой прогноз погоды Вас интересует?");
            System.out.println("1 - Прогноз погоды на 16 дней");
            System.out.println("2 - История погоды");
            System.out.print("Ваш выбор: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            }catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число 1 или 2.");
                scanner.nextLine();
                continue;
            }
            switch (choice){
                case 1:
                    latitude = readLatitude(scanner);
                    longitude = readLongitude(scanner);
                    scanner.nextLine();
                    System.out.println("Введите параметры hourly (например: temperature_2m): ");
                    hourly = scanner.nextLine();
                    try {
                        Weather weather = new OpenMeteo(
                                    "https",
                                    "api.open-meteo.com").getForecast(
                                            new Weather(
                                                    longitude,
                                                    latitude,
                                                    hourly)
                                    );
                        System.out.println("Результат:");
                        System.out.println(weather.toString());
                    }catch (WeatherException e){
                        System.out.println(e.getMessage());
                    }catch (MalformedURLException e) {
                        System.out.println(e.getMessage());
                    }catch (URISyntaxException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    latitude = readLatitude(scanner);
                    longitude = readLongitude(scanner);
                    scanner.nextLine();
                    System.out.println("Введите параметры hourly (например: temperature_2m): ");
                    hourly = scanner.nextLine();
                    System.out.println("Введите дату начала (YYYY-MM-DD): ");
                    String startDate = scanner.nextLine();
                    System.out.println("Введите дату конца (YYYY-MM-DD): ");
                    String endDate = scanner.nextLine();
                    try {
                        Weather weather = new OpenMeteo("https",
                                    "archive-api.open-meteo.com").getArchive(
                                            new Weather(
                                                    latitude,
                                                    longitude,
                                                    hourly,
                                                    startDate,
                                                    endDate)
                                    );
                        System.out.println("Результат:");
                        System.out.println(weather.toString());

                    }catch (WeatherException e){
                        System.out.println(e.getMessage());
                    }catch (MalformedURLException e) {
                        System.out.println(e.getMessage());
                    }catch (URISyntaxException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Неверный выбор. Введите 1 или 2");
            }
            while (true) {
                System.out.println("Желаете продолжить работу с приложением?");
                System.out.println("1 - Да");
                System.out.println("2 - Нет");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Ошибка: введите число 1 или 2.");
                    scanner.nextLine();
                }
            }
            if (choice == 2)
                break;
        }
    }
}

