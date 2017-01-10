package org.exoplatform.addons.linotp.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.conn.HttpHostConnectException;
import org.exoplatform.addons.linotp.client.LinOTPHttpsClient;
import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.json.JSONException;

/**
 * created on 17/11/2016
 * 
 * @author azaoui@exoplatform.com
 */
public class LinOTPLogin extends HttpServlet {

  /**
   * 
   */
  private static final long   serialVersionUID       = 1L;

  private static final Log    LOG                    = ExoLogger.getLogger(LinOTPLogin.class);

  public static Boolean       SETUP_SKIP             = false;

  private final static String OTP_JSP_RESOURCE       = "/WEB-INF/jsp/linOTPLogin.jsp";

  private final static String OTP_JSP_RESOURCE_ERROR = "/WEB-INF/jsp/linOTPLoginError.jsp";

  private final static String PIN                    = "pin";

  private final static String OTP                    = "otp";

  private static final String USER_OTP_TOKEN         = "userOTPTOKEN";

  private static final String USER_SPACE_INITIAL_URI = "intialURI";

  private static final String NOT_REACHABLE          = "notReachable";

  LinOTPHttpsClient           linOTPHttpsClient      = (LinOTPHttpsClient) PortalContainer.getInstance()
                                                                                          .getComponentInstanceOfType(LinOTPHttpsClient.class);

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    SettingService settingService = (SettingService) PortalContainer.getInstance()
                                                                    .getComponentInstanceOfType(SettingService.class);
    if (request.getRequestURI().contains("linOTPcheckAction")) {
      String pin = request.getParameter(PIN);
      String otp = request.getParameter(OTP);
      String otpUser = request.getQueryString();
      if (pin == null || otp == null) {
        response.sendRedirect(getInitialUriServiceSettings(otpUser, settingService));
        return;
      }
      String otpPass = pin.concat(otp);
      boolean verify = false;
      try {
        verify = linOTPHttpsClient.checkVerify(otpUser, pin, otp);
      } catch (UnknownHostException |KeyManagementException | NoSuchAlgorithmException | KeyStoreException | URISyntaxException | JSONException |HttpHostConnectException | MalformedURLException |SSLException  |NoRouteToHostException e ) {
        // TODO Auto-generated catch block
        //to do should add error code according to exception type (multi catch)
        LOG.error(e);
        request.getSession().setAttribute(NOT_REACHABLE, "true");
        getServletContext().getRequestDispatcher(OTP_JSP_RESOURCE_ERROR).forward(request, response);
        return;
      }

      if (verify) {
        updateServiceSettings(request.getQueryString(), otpPass, settingService);
        response.sendRedirect(getInitialUriServiceSettings(otpUser, settingService));
      } else

        response.sendRedirect(getInitialUriServiceSettings(otpUser, settingService));
      return;
    }

    getServletContext().getRequestDispatcher(OTP_JSP_RESOURCE).forward(request, response);

  }

  // }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  private String getOrCreateServiceSettings(String userId, SettingService settingService) {
    SettingValue<String> value = (SettingValue<String>) settingService.get(Context.USER.id(userId), Scope.GLOBAL, USER_OTP_TOKEN);
    if (value != null)
      return value.getValue();
    return null;
  }

  private void updateServiceSettings(String userId, String value, SettingService settingService) {
    settingService.set(Context.USER.id(userId), Scope.GLOBAL, USER_OTP_TOKEN, SettingValue.create(value));
  }

  private String getInitialUriServiceSettings(String userId, SettingService settingService) {
    SettingValue<String> value = (SettingValue<String>) settingService.get(Context.USER.id(userId),
                                                                           Scope.GLOBAL,
                                                                           USER_SPACE_INITIAL_URI);
    if (value != null)
      return value.getValue();
    return null;
  }

}
