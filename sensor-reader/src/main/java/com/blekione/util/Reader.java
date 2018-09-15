package com.blekione.util;

import java.io.IOException;
import java.net.URI;

/**
 *
 * @author mkruglik
 *
 */
public interface Reader {

    /**
     *
     * @param resourceURI
     * @return
     * @throws IOException
     */
    String getResource(URI resourceURI) throws IOException;

}
