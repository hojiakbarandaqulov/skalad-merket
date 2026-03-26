/*
package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {

    @Value("${yandex.api.key}")
    private String yandexApiKey;

    @GetMapping
    @Operation(
            summary = "Manzil bo'yicha koordinata olish",
            description = "Berilgan manzilga (adresga) Yandex orqali koordinata (longitude, latitude) ni qaytaradi. Masalan, salon manzilidan joylashuv aniqlash uchun ishlatiladi."
    )
    public ResponseEntity<?> getCoordinates(@RequestParam String address) {
        try {
            String url = "https://geocode-maps.yandex.ru/1.x/?apikey=" + yandexApiKey +
                    "&format=json&geocode=" + URLEncoder.encode(address, StandardCharsets.UTF_8);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            JSONArray features = json.getJSONObject("response")
                    .getJSONObject("GeoObjectCollection")
                    .getJSONArray("featureMember");

            if (features.isNull(0)) {
                return ResponseEntity.notFound().build();
            }

            JSONObject point = features.getJSONObject(0)
                    .getJSONObject("GeoObject")
                    .getJSONObject("Point");

            String[] pos = point.getString("pos").split(" ");
            Map<String, Object> coords = new HashMap<>();
            coords.put("longitude", pos[0]);
            coords.put("latitude", pos[1]);

            return ResponseEntity.ok(coords);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("error: " + e.getMessage());
        }
    }
}*/
