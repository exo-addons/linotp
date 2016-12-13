package org.exoplatform.addons.social.core.jpa.storage;

import java.util.Iterator;
import java.util.List;

import org.exoplatform.addons.linotp.utils.LinotpUtils;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.jpa.storage.RDBMSActivityStorageImpl;
import org.exoplatform.social.core.jpa.storage.dao.ActivityDAO;
import org.exoplatform.social.core.jpa.storage.dao.ConnectionDAO;
import org.exoplatform.social.core.jpa.storage.dao.StreamItemDAO;
import org.exoplatform.social.core.storage.api.IdentityStorage;
import org.exoplatform.social.core.storage.api.RelationshipStorage;
import org.exoplatform.social.core.storage.api.SpaceStorage;

/**
 * created on 13/12/2016
 * 
 * @author azaoui@exoplatform.com
 */

public class CustomRDBMSActivityStorageImpl extends RDBMSActivityStorageImpl {

  public CustomRDBMSActivityStorageImpl(RelationshipStorage relationshipStorage,
                                        IdentityStorage identityStorage,
                                        SpaceStorage spaceStorage,
                                        ActivityDAO activityDAO,
                                        ConnectionDAO connectionDAO,
                                        StreamItemDAO streamItemDAO) {
    super(relationshipStorage, identityStorage, spaceStorage, activityDAO, connectionDAO, streamItemDAO);
    // TODO Auto-generated constructor stub
  }

  // in order to remove secure activities from home page an override is needed
  // for getUserSpacesActivityIds (see call hierarchy) USER_SPACE_ACTIVITIES
  // secure space activities are only shown in the context of their spaces
  @Override
  public List<String> getUserSpacesActivityIds(Identity ownerIdentity, int offset, int limit) {
    List<String> allSpaceActivities = super.getUserSpacesActivityIds(ownerIdentity, offset, limit);
    Iterator<String> iter = allSpaceActivities.iterator();
    while (iter.hasNext()) {
      String str = iter.next();
      if (LinotpUtils.isSecuredSpaceActivity(str)) {
        iter.remove();// just remove the secure activities id before return
      }

    }
    return allSpaceActivities;
  }

  // in order to remove secure activities from home page an override is needed
  // for getActivityIdsFeed (see call hierarchy) ACTIVITY_FEED
  // secure space activities are only shown in the context of their spaces
  @Override
  public List<String> getActivityIdsFeed(Identity ownerIdentity, int offset, int limit) {
    List<String> allSpaceActivities = super.getActivityIdsFeed(ownerIdentity, offset, limit);
    Iterator<String> iter = allSpaceActivities.iterator();
    while (iter.hasNext()) {
      String str = iter.next();
      if (LinotpUtils.isSecuredSpaceActivity(str)) {
        iter.remove();// just remove the secure feed id before return
      }

    }
    return allSpaceActivities;
  }

}
