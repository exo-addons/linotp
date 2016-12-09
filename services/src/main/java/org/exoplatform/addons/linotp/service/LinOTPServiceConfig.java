package org.exoplatform.addons.linotp.service;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.space.spi.SpaceService;

/**
 * created on 17/11/2016
 * 
 * @author azaoui@exoplatform.com
 */

public class LinOTPServiceConfig extends BaseComponentPlugin {
  private static final Log    log                    = ExoLogger.getLogger(LinOTPServiceConfig.class);

  private String              LinotpHost;

  private String              validationPath;

  private static final String LINOTP_HOST            = "exo.linotp.linotp.host";

  private static final String LINOTP_VALIDATION_PATH = "exo.linotp.validationPath";

  public LinOTPServiceConfig(InitParams initParams, SpaceService spaceService) {

    LinotpHost = StringUtils.isNotBlank(PropertyManager.getProperty(LINOTP_HOST)) ? PropertyManager.getProperty(LINOTP_HOST)
                                                                                 : initParams.getValueParam("linotpHost")
                                                                                             .getValue();

    validationPath = StringUtils.isNotBlank(PropertyManager.getProperty(LINOTP_VALIDATION_PATH)) ? PropertyManager.getProperty(LINOTP_VALIDATION_PATH)
                                                                                                : initParams.getValueParam("linotp-validationPath")
                                                                                                            .getValue();

    if (LinotpHost == null || LinotpHost.length() == 0 || LinotpHost.trim().equals("<<to be replaced>>")) {
      log.warn("Property 'LinotpHost' needs to be provided. The value should be " + "the host of your linOTP server");
      return;
    }

    if (validationPath == null || validationPath.length() == 0 || validationPath.trim().equals("<<to be replaced>>")) {
      log.warn("Property 'validationPath' needs to be provided. The value should be " + "validationPath of your linOTP server");
      return;
    }

  }

  public String getLinotpHost() {
    return LinotpHost;
  }

  public String getValidationPath() {
    return validationPath;
  }

}
