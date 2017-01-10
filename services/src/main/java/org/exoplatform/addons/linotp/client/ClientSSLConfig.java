package org.exoplatform.addons.linotp.client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author azaoui@exoplatform.com
 *
 */
public class ClientSSLConfig {

  /**
   * @return  httpClient object which can be used to make any HTTPS calls
   * @throws KeyStoreException 
   * @throws NoSuchAlgorithmException 
   * @throws KeyManagementException 
   */
  public HttpClient create() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
   // try {
      SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
      sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
      //in production it's recommended to import keystore
      SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
                                                                                             SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();

      return httpClient;

  }
}
