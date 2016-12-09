
package org.exoplatform.addons.linotp.portlet.SecurityManagement.component;


import org.exoplatform.addons.linotp.spaces.ProfileSpaceListAccess;
import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.commons.utils.PageListAccess;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.social.core.space.SpaceFilter;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;



/**
 * created on 06/12/2016
 * 
 * @author azaoui@exoplatform.com
 */

public class FindSpacesPageList extends PageListAccess<Space, SpaceQuery> {

  private static final long serialVersionUID = 1L;

  private final SpaceStatus  status;

  public FindSpacesPageList(SpaceQuery state, int pageSize, SpaceStatus status) {
    super(state, pageSize);
    this.status = status;
  }

  protected ListAccess<Space> create(SpaceQuery state) throws Exception {
    ExoContainer container = PortalContainer.getInstance();
    SpaceService service = (SpaceService) container.getComponentInstance(SpaceService.class);
 if (state.getSpaceName() != null) {
      SpaceFilter filter = new SpaceFilter();
      filter.setSpaceNameSearchCondition(state.getSpaceName());
      return service.getAllSpacesByFilter(filter);
    } else if (status.name().equals("SECURE")) {
      ListAccess<Space> allSpaceListAccess = service.getAllSpacesWithListAccess();
      Space[] spaceList = allSpaceListAccess.load(0, allSpaceListAccess.getSize());
      return new ProfileSpaceListAccess(spaceList, true);

    } else if (status.name().equals("INSECURE")) {
      ListAccess<Space> allSpaceListAccess = service.getAllSpacesWithListAccess();
      Space[] spaceList = allSpaceListAccess.load(0, allSpaceListAccess.getSize());
      return new ProfileSpaceListAccess(spaceList, false);

    }
    return service.getAllSpacesWithListAccess();
  }
}
