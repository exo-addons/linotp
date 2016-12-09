package org.exoplatform.addons.linotp.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.exoplatform.addons.linotp.service.LinOTPServiceConfig;
import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.json.JSONException;
import org.json.JSONObject;

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

  LinOTPServiceConfig         linOTPServiceConfig    = (LinOTPServiceConfig) PortalContainer.getInstance()
                                                                                            .getComponentInstanceOfType(LinOTPServiceConfig.class);

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
      Boolean status = false;
      Boolean value = false;
      getOrCreateServiceSettings(request.getQueryString(), settingService);

      URI uri = null;

      try {
        uri = new URIBuilder().setScheme("https")
                              .setPath(linOTPServiceConfig.getValidationPath())
                              .setHost(linOTPServiceConfig.getLinotpHost())
                              .setParameter("user", otpUser)
                              .setParameter("pass", otpPass)
                              .build();

        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = HttpClients.createDefault();
        HttpResponse httpResponse = client.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        String jsonContent = EntityUtils.toString(entity);
        JSONObject otpJSONObject = null;

        otpJSONObject = new JSONObject(jsonContent);
        JSONObject result = otpJSONObject.getJSONObject("result");

        status = result.getBoolean("status");
        value = result.getBoolean("value");
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        LOG.error(e);
        request.getSession().setAttribute(NOT_REACHABLE, "true");
        getServletContext().getRequestDispatcher(OTP_JSP_RESOURCE_ERROR).forward(request, response);
        return;
      } catch (URISyntaxException e) {
        // TODO Auto-generated catch block
        request.getSession().setAttribute(NOT_REACHABLE, "true");
        LOG.error(e);
        getServletContext().getRequestDispatcher(OTP_JSP_RESOURCE_ERROR).forward(request, response);
        return;
      } catch (UnknownHostException e) {
        request.getSession().setAttribute(NOT_REACHABLE, "true");
        LOG.error(e);
        getServletContext().getRequestDispatcher(OTP_JSP_RESOURCE_ERROR).forward(request, response);
        return;
      } catch (SSLException e) {
        request.getSession().setAttribute(NOT_REACHABLE, "true");
        LOG.error(e);
        getServletContext().getRequestDispatcher(OTP_JSP_RESOURCE_ERROR).forward(request, response);
        return;
      } catch (MalformedURLException e) {
        LOG.error(" Error creating HTTPs connection to the linOTP server : " + uri.getHost());
        request.getSession().setAttribute(NOT_REACHABLE, "true");
        getServletContext().getRequestDispatcher(OTP_JSP_RESOURCE_ERROR).forward(request, response);
      } catch (NoRouteToHostException e) {
        LOG.error(" Error creating HTTPs connection to the linOTP server : " + uri.getHost());
        request.getSession().setAttribute(NOT_REACHABLE, "true");
        getServletContext().getRequestDispatcher(OTP_JSP_RESOURCE_ERROR).forward(request, response);
        return;
      }

      if (status && value) {
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
