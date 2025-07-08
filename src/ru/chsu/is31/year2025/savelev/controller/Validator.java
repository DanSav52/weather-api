package ru.chsu.is31.year2025.savelev.controller;

public class Validator {
    public static double parseCoordinate(String value, String name) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Недопустимое значение для " + name + ": \"" + value + "\"");
        }
    }
    public static boolean isValidLatitude(double latitude){
        return latitude >= -90 && latitude <= 90;
    }
    public static boolean isValidLongitude( double longitude){
        return  longitude >= -180 && longitude <= 180;
    }
}
