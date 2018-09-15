package com.blekione.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ReaderTest {

    URI resourceURI;

    @Before
    public void setUp() throws URISyntaxException {
    resourceURI = new URI(
        "https://us.wio.seeed.io/v1/node/GroveTempHumD0/temperature?access_token=24dc55c37fc032db8c99590d69554f4e");
    }

    @Test
    @Ignore // need online test node to work
    public void testIfResourcesReceived() throws Exception {
        Reader reader = new HTTPReader();
        System.out.println(reader.getResource(resourceURI));
    }
}
