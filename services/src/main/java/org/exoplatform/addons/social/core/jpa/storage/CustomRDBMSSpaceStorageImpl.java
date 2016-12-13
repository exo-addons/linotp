package org.exoplatform.addons.social.core.jpa.storage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.api.persistence.ExoTransactional;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.jpa.rest.IdentityAvatarRestService;
import org.exoplatform.social.core.jpa.storage.RDBMSIdentityStorageImpl;
import org.exoplatform.social.core.jpa.storage.RDBMSSpaceStorageImpl;
import org.exoplatform.social.core.jpa.storage.dao.ActivityDAO;
import org.exoplatform.social.core.jpa.storage.dao.IdentityDAO;
import org.exoplatform.social.core.jpa.storage.dao.SpaceDAO;
import org.exoplatform.social.core.jpa.storage.dao.SpaceMemberDAO;
import org.exoplatform.social.core.jpa.storage.entity.SpaceEntity;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.storage.SpaceStorageException;

public class CustomRDBMSSpaceStorageImpl extends RDBMSSpaceStorageImpl{
  
  private SpaceDAO             myspaceDAO;
  private static final Log LOG = ExoLogger.getLogger(CustomRDBMSSpaceStorageImpl.class);

  public CustomRDBMSSpaceStorageImpl(SpaceDAO spaceDAO,
                                     SpaceMemberDAO spaceMemberDAO,
                                     RDBMSIdentityStorageImpl identityStorage,
                                     IdentityDAO identityDAO,
                                     ActivityDAO activityDAO) {
    super(spaceDAO, spaceMemberDAO, identityStorage, identityDAO, activityDAO);
    this.myspaceDAO = spaceDAO;
  }
  
  
  //SOC-5633 add the ExoTransactional annotation in order to fix Session is closed issue this override will be removed after fix SOC-5633 is applied
  @Override
  @ExoTransactional
  public Space getSpaceByUrl(String url) throws SpaceStorageException {
    SpaceEntity entity = myspaceDAO.getSpaceByURL(url);
    return fillSpaceFromEntity(entity);
  }
  
  private Space fillSpaceFromEntity(SpaceEntity entity) {
    if (entity == null) {
      return null;
    }
    Space space = new Space();
    fillSpaceSimpleFromEntity(entity, space);

    space.setPendingUsers(entity.getPendingMembersId());
    space.setInvitedUsers(entity.getInvitedMembersId());

    //
    String[] members = entity.getMembersId();
    String[] managers = entity.getManagerMembersId();

    //
    Set<String> membersList = new HashSet<String>();
    if (members != null)
      membersList.addAll(Arrays.asList(members));
    if (managers != null)
      membersList.addAll(Arrays.asList(managers));

    //
    space.setMembers(membersList.toArray(new String[] {}));
    space.setManagers(entity.getManagerMembersId());
    return space;
  }
  
  private Space fillSpaceSimpleFromEntity(SpaceEntity entity, Space space) {
    space.setApp(StringUtils.join(entity.getApp(), ","));
    space.setId(String.valueOf(entity.getId()));
    space.setDisplayName(entity.getDisplayName());
    space.setPrettyName(entity.getPrettyName());
    if (entity.getRegistration() != null) {
      space.setRegistration(entity.getRegistration().name().toLowerCase());
    }
    space.setDescription(entity.getDescription());
    space.setType(entity.getType());
    if (entity.getVisibility() != null) {
      space.setVisibility(entity.getVisibility().name().toLowerCase());
    }
    if (entity.getPriority() != null) {
      switch (entity.getPriority()) {
        case HIGH:
          space.setPriority(Space.HIGH_PRIORITY);
          break;
        case INTERMEDIATE:
          space.setPriority(Space.INTERMEDIATE_PRIORITY);
          break;
        case LOW:
          space.setPriority(Space.LOW_PRIORITY);
          break;
        default:
          space.setPriority(null);
      }
    }
    space.setGroupId(entity.getGroupId());
    space.setUrl(entity.getUrl());
    space.setCreatedTime(entity.getCreatedDate().getTime());

    if (entity.getAvatarLastUpdated() != null) {
      try {
        space.setAvatarUrl(IdentityAvatarRestService.buildAvatarURL(SpaceIdentityProvider.NAME, space.getPrettyName()));
      } catch (Exception e) {
        LOG.warn("Failed to build avatar url: " + e.getMessage());
      }
    }
    if (entity.getAvatarLastUpdated() != null) {
      space.setAvatarLastUpdated(entity.getAvatarLastUpdated().getTime());
    }
    return space;
  }
  }



