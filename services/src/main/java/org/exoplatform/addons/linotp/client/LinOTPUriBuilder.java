package org.exoplatform.addons.linotp.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

/**
 * @author azaoui@exoplatform.com
 */
public class LinOTPUriBuilder {

  /**
   * @param host
   * @param port
   * @return URI
   * @throws URISyntaxException
   */
  public static URI createUri(final String host, final int port) throws URISyntaxException {
    final URIBuilder uriBuilder = new URIBuilder();
    uriBuilder.setScheme("https");
    uriBuilder.setHost(host);
    uriBuilder.setPort(port);
    return uriBuilder.build();
  }

  /**
   * @param host
   * @param path
   * @param queryParameters
   * @return URI with param
   * @throws URISyntaxException
   */
  public static URI createUriWithParameters(final String host, String path, final List<NameValuePair> queryParameters) throws URISyntaxException {
    final URIBuilder uriBuilder = new URIBuilder();
    uriBuilder.setScheme("https");
    uriBuilder.setHost(host);
    uriBuilder.setPath(path);
    uriBuilder.setParameters(queryParameters);
    return uriBuilder.build();
  }

}
