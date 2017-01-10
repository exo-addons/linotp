package org.exoplatform.addons.linotp.client;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;

/**
 * @author azaoui@exoplatform.com
 *
 */

public class LinOTPAdminParametersBuilder {
  
  
  
  
  
  
  
  
  
  
  
  public static List<NameValuePair> getQueryParameters(String realm, String user, String pass) {

    List<NameValuePair> queryParameters = new ArrayList<>();
    if (realm != null) {
      queryParameters.add(LinOTPHttpParameters.getRealmParameter(realm));
    }

    if (user != null) {
      queryParameters.add(LinOTPHttpParameters.getUserParameter(user));
    }

    if (pass != null) {
      queryParameters.add(LinOTPHttpParameters.getPasswordParameter(pass));
    }


    return queryParameters;

  }
  

  /**
   * @param realm
   * @param user
   * @param pin
   * @param otp
   * @return
   */
  public static List<NameValuePair> getQueryParameters(String realm, String user, String pin, String otp) {

    List<NameValuePair> queryParameters = new ArrayList<>();
    if (realm != null) {
      queryParameters.add(LinOTPHttpParameters.getRealmParameter(realm));
    }
    if (otp != null) {
      queryParameters.add(LinOTPHttpParameters.getOtpParameter(otp));
    }
    if (user != null) {
      queryParameters.add(LinOTPHttpParameters.getUserParameter(user));
    }

    if (pin != null) {
      queryParameters.add(LinOTPHttpParameters.getPinParameter(pin));
    }
    if (otp != null) {
      queryParameters.add(LinOTPHttpParameters.getPinParameter(pin));
    }

    return queryParameters;

  }
  

  /**
   * @param name
   * @param value
   * @return
   */
  public static List<NameValuePair> getCustomQueryParameters(String name, String value) {

    List<NameValuePair> queryParameters = new ArrayList<>();
    if ((name != null) && (value != null)) {
      queryParameters.add(LinOTPHttpParameters.getCustomParameter(name, value));
    }

    return queryParameters;

  }

}
