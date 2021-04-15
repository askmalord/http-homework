package ru.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    private static final String WEBSITE = "https://cat-fact.herokuapp.com/facts";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main( String[] args ) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        try {
            HttpGet request = new HttpGet(WEBSITE);

            CloseableHttpResponse response = httpClient.execute(request);
            try {
                List<Post> posts = mapper.readValue(
                        response.getEntity().getContent(),
                        new TypeReference<List<Post>>() {});
                posts.stream().filter(value -> value.getText().length() < 150).forEach(System.out::println);
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }


    }
}
