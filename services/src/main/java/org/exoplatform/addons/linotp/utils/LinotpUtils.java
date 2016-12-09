package org.exoplatform.addons.linotp.utils;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.social.core.activity.model.ExoSocialActivity;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.ActivityManager;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

/**
 * created on 17/11/2016
 * 
 * @author azaoui@exoplatform.com
 */

public class LinotpUtils {

  static ActivityManager activityManager = (ActivityManager) ExoContainerContext.getCurrentContainer()
                                                                                .getComponentInstanceOfType(ActivityManager.class);

  static SpaceService    spaceService    = (SpaceService) ExoContainerContext.getCurrentContainer()
                                                                             .getComponentInstanceOfType(SpaceService.class);

  static IdentityManager identityManager = (IdentityManager) ExoContainerContext.getCurrentContainer()
                                                                                .getComponentInstanceOfType(IdentityManager.class);

  public static boolean isSecuredSpaceActivity(String activityid) {
    ExoSocialActivity activity = activityManager.getActivity(activityid);
    String spacePrettyId = activity.getActivityStream().getPrettyId();
    Space space = spaceService.getSpaceByPrettyName(spacePrettyId);
    if (space != null) {
    Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
    Profile spaceProfile = spaceIdentity.getProfile();
    if (spaceProfile.getProperty("secure") != null && Boolean.parseBoolean(spaceProfile.getProperty("secure").toString()))
      return true;
    }
    return false;

  }

  public static boolean isSecuredSpaceUri(String spaceRequestURI) {

    Space space = spaceService.getSpaceByUrl(spaceRequestURI.substring(spaceRequestURI.lastIndexOf("/") + 1));
    if (space != null) {
      Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
      Profile spaceProfile = spaceIdentity.getProfile();
      if (spaceProfile.getProperty("secure") != null && Boolean.parseBoolean(spaceProfile.getProperty("secure").toString()))
        return true;
    }

    return false;

  }

  public static boolean isSecuredSpaceIdentity(Identity spaceIdentity) {
    Profile spaceProfile = spaceIdentity.getProfile();
    if (spaceProfile.getProperty("secure") != null && Boolean.parseBoolean(spaceProfile.getProperty("secure").toString()))
      return true;

    return false;

  }
  
  public static boolean isSecuredSpace(Space space) {
    Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
    Profile spaceProfile = spaceIdentity.getProfile();
    if (spaceProfile.getProperty("secure") != null && Boolean.parseBoolean(spaceProfile.getProperty("secure").toString()))
      return true;

    return false;

  }

}
