package com.blekione.iot;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.blekione.util.HTTPReader;
import com.blekione.util.Reader;

public class IOTNodeTest {

    private static final String ACCESS_TOKEN = "24dc55c37f";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private double lat = 1.0;
    private double lng = -1.0;

    private IOTNode iotNode;

    @Mock
    private Reader reader;

    @Before
    public void setUp() {
        iotNode = new IOTNode("", "", ACCESS_TOKEN, lat, lng, reader);
    }

    @After
    public void cleanUp() {
        verifyNoMoreInteractions(reader);
    }

    @Test
    public void testIfResourceURIFormatIsAsExpected() throws Exception {
        final String expectedURI =
            "https://us.wio.seeed.io/v1/node/GroveTempHumD0/humidity?access_token=24dc55c37f";
        assertThat(
            iotNode.getResourceURI("GroveTempHumD0/humidity").toString(),
            equalTo(expectedURI));
    }

    @Test
    public void testIfResourceHasBeenAdded() {
        iotNode.addResource("GroveTempHumD0/humidity");

        assertThat(iotNode.getResourceNames().iterator().next(), equalTo("GroveTempHumD0/humidity"));

    }

    @Test
    public void testIfTwoResourcesAreAdded() {

        iotNode.addResource("GroveTempHumD0/humidity");
        iotNode.addResource("GroveTempHumD0/temperature");
        Collection<String> sensors = new HashSet<>();

        sensors.add("GroveTempHumD0/temperature");
        sensors.add("GroveTempHumD0/humidity");

        assertThat(iotNode.getResourceNames(), equalTo(sensors));
    }

    @Test
    public void testThatResourceNeedToBeUnique() {
        Collection<String> sensors = new HashSet<>();
        sensors.add("GroveTempHumD0/humidity");

        iotNode.addResource("GroveTempHumD0/humidity");
        iotNode.addResource("GroveTempHumD0/humidity");

        assertThat(iotNode.getResourceNames(), equalTo(sensors));
    }

    @Test
    public void testIfNodeGeolocationIsSet() {
        assertThat(iotNode.getLat(), equalTo(lat));
        assertThat(iotNode.getLng(), equalTo(lng));
    }

    @Test
    public void testIfInstanceCreatedFromConfig() throws Exception {
        final String board = "Wio Node v1.0";
        final String sn = "1b2edc28abe197b7f443618d";
        double lat = 51.4123113;
        double lng = -0.1689868;
        final String configJSON =
            "{" +
               "\"board\": \"" + board + "\"," +
               "\"sn\": \"" + sn + "\"," +
               "\"token\": \"" + ACCESS_TOKEN + "\"," +
               "\"lat\": " + lat + "," +
               "\"lng\": " + lng + "," +
               "\"reader:\": \"HTTPReader\"" +
             "}";

        final IOTNode node = IOTNode.create(configJSON);
        assertThat(node.getLat(), equalTo(lat));
        assertThat(node.getLng(), equalTo(lng));
        assertThat(node.getType(), equalTo(board));
        assertThat(node.getSerialNumber(), equalTo(sn));
        assertThat(node.getToken(), equalTo(ACCESS_TOKEN));
        assertThat(node.getReader(), instanceOf(HTTPReader.class));
    }

    @Test
    public void testIfResourceValueIsReturned() throws Exception {
        final String resource = "GroveTempHumD0/humidity";
        final double resourceValue = 22.01;
        final URI resourceURI = iotNode.getResourceURI(resource);
        when(reader.getResource(resourceURI))
            .thenReturn("{\"" + resource + "\": " + resourceValue + "}");
        assertThat(iotNode.getResourceValue(resource), equalTo(resourceValue));
        verify(reader).getResource(resourceURI);
    }

    @Test
    public void testIfDefaultReaderIsAssignedWhenConfigValueForReaderIsNull() throws Exception {
        final String configJSON =
            "{" +
               "\"board\": \"\"," +
               "\"sn\": \"\"," +
               "\"token\": \"\"," +
               "\"lat\": 0.0," +
               "\"lng\": 0.0" +
             "}";

        final IOTNode node = IOTNode.create(configJSON);
        assertThat(node.getReader(), instanceOf(HTTPReader.class));
    }
}
