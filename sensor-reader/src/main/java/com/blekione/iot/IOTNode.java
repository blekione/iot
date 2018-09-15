package com.blekione.iot;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.utils.URIBuilder;

import com.blekione.util.HTTPReader;
import com.blekione.util.Reader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a WIO Node, providing an access to its sensors resources
 * and identifiers.
 *
 * @author mkruglik
 *
 */
public class IOTNode {
    private final String authority = "us.wio.seeed.io";
    private final String scheme = "https";
    private final String path = "v1/node/";
    private final Set<String> resources = new HashSet<>();
    private final String accessTokenQuery = "access_token";
    private final String accesToken;
    private final Geolocation location;
    private final String type;
    private final String serialNumber;
    private final Reader reader;

    /**
     * Constructor.
     *
     * @param type the type of the WIO node
     * @param serialNumber the serial number identifying the node
     * @param accessToken the access token to the node API
     * @param lat the node geolocation latitude
     * @param lng the node geolocation longitude
     * @param reader
     */
    public IOTNode(String type, String serialNumber, String accessToken, double lat,
        double lng, Reader reader) {
            this.type = type;
            this.serialNumber = serialNumber;
            this.reader = reader;
            this.location = new Geolocation(lat, lng);
            this.accesToken = accessToken;
    }

    /**
     * Creates an instance of {@link IOTNode} from JSON configuration.
     *
     * @param config the configuration JSON string
     * @return the instance of {@link IOTNode}
     * @throws IOException if a config string has invalid JSON format
     */
    public static IOTNode create(String config) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode configJSON = mapper.readTree(config);
        final Reader reader = createReader(configJSON.get("reader"));
        return new IOTNode(
            configJSON.get("board").asText(""),
            configJSON.get("sn").asText(""),
            configJSON.get("token").asText(),
            configJSON.get("lat").asDouble(),
            configJSON.get("lng").asDouble(),
            reader);
    }

    private static Reader createReader(JsonNode readerClass) {
        if (readerClass == null) {
            return new HTTPReader();
        }
        else if ("HTTPReader".equals(readerClass.asText())) {
            return new HTTPReader();
        }
        return null;
    }

    /**
     * Returns URI to the node's sensor resource.
     *
     * @param resource the node sensor resource for which URI is requested.
     * @return the URI to the node's sensor resource.
     * @throws URISyntaxException if a URI exception occured
     */
    public URI getResourceURI(String resource) throws URISyntaxException {
            return new URIBuilder()
                .setScheme(scheme)
                .setHost(authority)
                .setPath(path + resource)
                .addParameter(accessTokenQuery, accesToken)
                .build();
    }

    /**
     * Adds the sensor resource.
     *
     * @param resource the name of the resource. The name has to be a valid name
     * for a resource of one of WIO Node sensors.
     */
    public void addResource(String resource) {
        this.resources.add(resource);
    }

    /**
     * Gets node resources.
     *
     * @return the node resources
     */
    public Collection<String> getResourceNames() {
        return this.resources;
    }

    /**
     * Gets nodes latitude.
     *
     * @return the latitude
     */
    public double getLat() {
        return location.getLat();
    }

    /**
     * Gets node longitude.
     *
     * @return the longitude
     */
    public double getLng() {
        return location.getLng();
    }

    /**
     * Gets node type.
     *
     * @return the node type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Gets node serial number.
     *
     * @return the node serial number
     */
    public String getSerialNumber() {
        return this.serialNumber;
    }

    /**
     * Get nodes API access token.
     *
     * @return the access token
     */
    public Object getToken() {
        return this.accesToken;
    }

    /**
     * Returns current value of the node sensor resource.
     * @param resource the name of the node sensor resource
     * @return the value of node sensor resource
     * @throws URISyntaxException
     * @throws IOException
     */
    public double  getResourceValue(String resource) throws URISyntaxException, IOException {
        final String resourceValueString = reader.getResource(getResourceURI(resource));
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(resourceValueString).elements().next().asDouble();
    }

    /* test visibility */ Reader getReader() {
        return this.reader;
    }

    private final class Geolocation {

        private final double lat;
        private final double lng;

        Geolocation(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        double getLat() {
            return this.lat;
        }

        double getLng() {
            return this.lng;
        }

    }

}
