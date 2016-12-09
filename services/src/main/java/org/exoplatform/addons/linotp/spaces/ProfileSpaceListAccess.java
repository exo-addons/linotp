package org.exoplatform.addons.linotp.spaces;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;

/**
 * created on 06/12/2016
 * 
 * @author azaoui@exoplatform.com
 */

public class ProfileSpaceListAccess implements ListAccess<Space> {

  private Space[] spaces;

  ExoContainer    container       = PortalContainer.getInstance();

  IdentityManager identityManager = (IdentityManager) container.getComponentInstance(IdentityManager.class);

  List<Space>     secureSpaces    = null;

  List<Space>     notSecureSpaces = null;

  boolean         secure          = false;

  public ProfileSpaceListAccess(Space[] spaceList, boolean secure) {
    // TODO Auto-generated constructor stub
    this.spaces = spaceList;
    this.secure = secure;

  }

  @Override
  public Space[] load(int index, int length) throws Exception, IllegalArgumentException {
    secureSpaces = new ArrayList<Space>();
    notSecureSpaces = new ArrayList<Space>();
    for (Space space : spaces) {

      Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
      Profile spaceProfile = spaceIdentity.getProfile();
      if (spaceProfile.getProperty("secure") != null && Boolean.parseBoolean(spaceProfile.getProperty("secure").toString())) {
        secureSpaces.add(space);

      } else {
        notSecureSpaces.add(space);
      }

    }
    if (secure)
      return secureSpaces.toArray(new Space[secureSpaces.size()]);
    return notSecureSpaces.toArray(new Space[notSecureSpaces.size()]);

  }

  @Override
  public int getSize() throws Exception {
    if (secure) {
      if (secureSpaces == null) {
        this.load(0, spaces.length);

      }
      return secureSpaces.size();
    }
    if (notSecureSpaces == null) {
      this.load(0, spaces.length);
    }

    return notSecureSpaces.size();
  }

}
