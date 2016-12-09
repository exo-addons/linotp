package org.exoplatform.addons.linotp.listener;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.security.ConversationRegistry;
import org.exoplatform.services.security.ConversationState;

/**
 * created on 18/11/2016
 * 
 * @author azaoui@exoplatform.com
 */

public class LinOTPLoginListener extends Listener<ConversationRegistry, ConversationState> {

  private final SettingService settingService;

  private static final String USER_OTP_TOKEN         = "userOTPTOKEN";

  private static final String USER_SPACE_INITIAL_URI = "intialURI";

  public LinOTPLoginListener(SettingService settingService) throws Exception {
    this.settingService = settingService;
  }

  @Override
  public void onEvent(Event<ConversationRegistry, ConversationState> event) throws Exception {
 
    settingService.remove(Context.USER.id(event.getData().getIdentity().getUserId()), Scope.GLOBAL, USER_OTP_TOKEN);
    settingService.remove(Context.USER.id(event.getData().getIdentity().getUserId()), Scope.GLOBAL, USER_SPACE_INITIAL_URI);

  }

}
