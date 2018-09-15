package com.blekione.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * IMplements {@link Reader} inerface for HTTP readers.
 * @author mkruglik
 *
 */
public class HTTPReader implements Reader {

    // move URI creation stuff from IOTNode here
    // or provide 2 APis one to return resource with URI and one twith schema

    @Override
    public String getResource(URI resourceURI) throws IOException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(resourceURI);
        try (
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            InputStream is = httpResponse.getEntity().getContent()) {

            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode json = mapper.readTree(is);
            return json.toString();
        }
    }

}
