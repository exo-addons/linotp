package org.exoplatform.addons.linotp.web;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.addons.linotp.utils.LinotpUtils;
import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.web.filter.Filter;

/**
 * created on 17/11/2016
 * 
 * @author azaoui@exoplatform.com
 */
public class LinOTPFilter implements Filter {
  private static final String LINOTP_SERVLET_CTX = "/linotp-extension";

  private static final String ACCOUNT_SETUP_SERVLET       = "/linotpCheck";

  private static final String USER_OTP_TOKEN              = "userOTPTOKEN";

  private static final String USER_SPACE_INITIAL_URI      = "intialURI";

  private static final Log    LOG                         = ExoLogger.getLogger(LinOTPFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    SettingService settingService = (SettingService) PortalContainer.getInstance()
                                                                    .getComponentInstanceOfType(SettingService.class);
    boolean secondFactorAuthDone = false;
    ConversationState state = getStateCurrentLoginUser();
    String otp = getServiceSettings(state.getIdentity().getUserId(), settingService);
    if (otp != null) {
      secondFactorAuthDone = true;
    }
    if ((!secondFactorAuthDone)&&LinotpUtils.isSecuredSpaceUri(((HttpServletRequest)request).getRequestURI())) {
      updateInitialUriServiceSettings(state.getIdentity().getUserId(),
                                      ((HttpServletRequest) request).getRequestURL().toString(),
                                      settingService);
      ServletContext platformExtensionContext = httpServletRequest.getSession()
                                                                  .getServletContext()
                                                                  .getContext(LINOTP_SERVLET_CTX);
      platformExtensionContext.getRequestDispatcher(ACCOUNT_SETUP_SERVLET).forward(httpServletRequest, httpServletResponse);
      return;
    }
    chain.doFilter(request, response);
  }

  public static ConversationState getStateCurrentLoginUser() {
    return ConversationState.getCurrent();
  }

  private String getServiceSettings(String userId, SettingService settingService) {
    SettingValue<String> value = null;
    value = (SettingValue<String>) settingService.get(Context.USER.id(userId), Scope.GLOBAL, USER_OTP_TOKEN);
    if (value != null)
      return value.getValue();
    return null;
  }

  private void updateInitialUriServiceSettings(String userId, String value, SettingService settingService) {
    settingService.set(Context.USER.id(userId), Scope.GLOBAL, USER_SPACE_INITIAL_URI, SettingValue.create(value));
  }

}
