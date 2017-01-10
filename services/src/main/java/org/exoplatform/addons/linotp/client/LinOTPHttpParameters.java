package org.exoplatform.addons.linotp.client;

import org.apache.http.message.BasicNameValuePair;

public class LinOTPHttpParameters {

  private static final String SERIAL_PARAM_NAME   = "serial";

  private static final String USER_PARAM_NAME     = "user";

  private static final String OTP_PARAM_NAME      = "otp";

  private static final String REALM_PARAM_NAME    = "realm";

  private static final String ASSIGNED_PARAM_NAME = "assigned";

  private static final String PIN_PARAM_NAME      = "pin";

  private static final String TYPE_PARAM_NAME     = "type";

  private static final String PASSWORD_PARAM_NAME = "pass";

  /**
   * @param name
   * @param value
   * @return
   */
  public static BasicNameValuePair getBasicNameValuePair(String name, String value) {

    return new BasicNameValuePair(name, value);

  }

  /**
   * @param value
   * @return
   */
  public static BasicNameValuePair getSerialParameter(String value) {

    return new BasicNameValuePair(SERIAL_PARAM_NAME, value);

  }

  /**
   * @param value
   * @return
   */
  public static BasicNameValuePair getUserParameter(String value) {

    return new BasicNameValuePair(USER_PARAM_NAME, value);

  }

  /**
   * @param value
   * @return
   */
  public static BasicNameValuePair getRealmParameter(String value) {

    return new BasicNameValuePair(REALM_PARAM_NAME, value);

  }

  /**
   * @param value
   * @return
   */
  public static BasicNameValuePair getOtpParameter(String value) {

    return new BasicNameValuePair(OTP_PARAM_NAME, value);

  }

  /**
   * @param value
   * @return
   */
  public static BasicNameValuePair getPinParameter(String value) {

    return new BasicNameValuePair(PIN_PARAM_NAME, value);

  }

  /**
   * @param value
   * @return
   */
  public static BasicNameValuePair getAssignedParameter(String value) {

    return new BasicNameValuePair(ASSIGNED_PARAM_NAME, value);

  }

  /**
   * @param value
   * @return
   */
  public static BasicNameValuePair getTypeParameter(String value) {

    return new BasicNameValuePair(TYPE_PARAM_NAME, value);

  }

  /**
   * @param value
   * @return
   */
  public static BasicNameValuePair getPasswordParameter(String value) {

    return new BasicNameValuePair(PASSWORD_PARAM_NAME, value);

  }

  /**
   * @param name
   * @param value
   * @return
   */
  public static BasicNameValuePair getCustomParameter(String name, String value) {

    return new BasicNameValuePair(name, value);

  }

}
