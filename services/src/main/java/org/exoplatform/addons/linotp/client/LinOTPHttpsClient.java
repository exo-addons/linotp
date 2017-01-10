package org.exoplatform.addons.linotp.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.exoplatform.addons.linotp.service.LinOTPServiceConfig;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author azaoui@exoplatform.com
 */
public class LinOTPHttpsClient {

  private String              host;
  private String realm;

  private ClientSSLConfig     clientSSLConfig;

  private static final String VALIDATION_MODULE = "/validate/check";

  public LinOTPHttpsClient(LinOTPServiceConfig linOTPServiceConfig, ClientSSLConfig clientSSLConfig) {
    this.host = linOTPServiceConfig.getLinotpHost();
    this.clientSSLConfig = clientSSLConfig;
    this.realm=linOTPServiceConfig.getRealm();

  }

  private HttpResponse execute(HttpGet httpGet) throws URISyntaxException,
                                               ClientProtocolException,
                                               IOException,
                                               KeyManagementException,
                                               NoSuchAlgorithmException,
                                               KeyStoreException {

    HttpClient client = clientSSLConfig.create();
    HttpResponse httpResponse = client.execute(httpGet);
    return httpResponse;

  }

  public boolean checkVerify(String user, String pin, String otp) throws URISyntaxException,
                                                                               ClientProtocolException,
                                                                               IOException,
                                                                               JSONException,
                                                                               KeyManagementException,
                                                                               NoSuchAlgorithmException,
                                                                               KeyStoreException {
    
    String pass = pin.concat(otp); 
    List<NameValuePair> queryParameters = LinOTPAdminParametersBuilder.getQueryParameters(realm, user, pass);
    URI uri = LinOTPUriBuilder.createUriWithParameters(host, VALIDATION_MODULE, queryParameters);
    boolean status = false;
    boolean value = false;
    HttpGet httpGet = new HttpGet(uri);
    HttpResponse reponse = execute(httpGet);
    HttpEntity entity = reponse.getEntity();
    String jsonContent = EntityUtils.toString(entity);
    JSONObject otpJSONObject = null;

    otpJSONObject = new JSONObject(jsonContent);
    JSONObject result = otpJSONObject.getJSONObject("result");

    status = result.getBoolean("status");
    value = result.getBoolean("value");

    return status && value;

  }

}
