package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class APIPuller {
    private String apiKey = "76314534956745e89ea174317241507";
    private String baseUrl = "http://api.weatherapi.com/v1";
    private static APIPuller instance;

    // Singleton pattern
    private APIPuller() {}

    public static APIPuller getInstance() {
        if (instance == null) {
            instance = new APIPuller();
        }
        return instance;
    }

    public ArrayList<Integer> getCurrentTemperature(String location) {
        ArrayList<Integer> temperatures = new ArrayList<>();
        String urlString = baseUrl + "/current.json?key=" + apiKey + "&q=" + location;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            conn.disconnect();

            JSONObject json = new JSONObject(sb.toString());
            int tempCelsius = json.getJSONObject("current").getInt("temp_c");
            int tempFahrenheit = json.getJSONObject("current").getInt("temp_f");

            temperatures.add(tempCelsius);
            temperatures.add(tempFahrenheit);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return temperatures;
    }

    public ArrayList<Integer> getForecastTemperature(String location, int days, boolean celcius) {
        if (days < 1 || days > 14) {
            throw new IllegalArgumentException("Number of days must be between 1 and 3");
        }
        ArrayList<Integer> temperatures = new ArrayList<>();
        String urlString = baseUrl + "/forecast.json?key=" + apiKey + "&q=" + location + "&days=" + days;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            conn.disconnect();

            JSONObject json = new JSONObject(sb.toString());
            JSONArray forecastArray = json.getJSONObject("forecast").getJSONArray("forecastday");
            for (int i = 0; i < days; i++) {
                if (celcius) {
                    int tempCelsius = forecastArray.getJSONObject(i).getJSONObject("day").getInt("avgtemp_c");
                    temperatures.add(tempCelsius);
                } else {
                    int tempFahrenheit = forecastArray.getJSONObject(i).getJSONObject("day").getInt("avgtemp_f");
                    temperatures.add(tempFahrenheit);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return temperatures;
    }
}
