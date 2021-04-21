package ru.netology;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.fasterxml.jackson.core.JsonParser.*;

public class NasaApiResponser {
    private static final String NASA_API_WEBSITE
            = "https://api.nasa.gov/planetary/apod?api_key=vN9IDDeIcOblU7Dyh3XQFYTEc6LfWCFMAZXT3MfT";
    private static final ObjectMapper mapper = new ObjectMapper().configure(Feature.AUTO_CLOSE_SOURCE, true);
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom().
                        setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        try {
            HttpGet request = new HttpGet(NASA_API_WEBSITE);

            CloseableHttpResponse response = httpClient.execute(request);
            try {
                NasaApi nasaApi = mapper.readValue(response.getEntity().getContent().readAllBytes(), NasaApi.class);
                System.out.println(nasaApi);
                String url = nasaApi.getUrl();
                response = httpClient.execute(new HttpGet(url));
                String bodyOfUrlResponse = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                String[] urlStringArray = url.split("/");
                String pictureName = urlStringArray[urlStringArray.length - 1];
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pictureName)));
                writer.write(bodyOfUrlResponse);
                writer.close();
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }
}
