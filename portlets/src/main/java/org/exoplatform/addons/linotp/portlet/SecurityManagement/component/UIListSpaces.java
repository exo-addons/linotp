/**
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.addons.linotp.portlet.SecurityManagement.component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.SpaceUtils;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UISearch;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;


@ComponentConfig(lifecycle = UIContainerLifecycle.class, events = {
        @EventConfig(listeners = UIListSpaces.InsecureSecureSpaceActionListener.class) })
@Serialized
public class UIListSpaces extends UISearch {

    public static final String SPACE_NAME = "displayName";

    public static final String URL = "url";
    public static final String SPACE_STATUS_FILTER = "spaceStatusFilter";

    private static final String[] SPACE_BEAN_FIELD = { SPACE_NAME, URL};

    private static final String[] SPACE_ACTION = { "ViewUserInfo", "DeleteUser" };

    private static final List<SelectItemOption<String>> OPTIONS_ = Collections.unmodifiableList(Arrays.asList(
            new SelectItemOption<String>(SPACE_NAME)
           ));

    private static final List<SelectItemOption<String>> SPACE_STATUS_OPTIONS = Collections.unmodifiableList(Arrays.asList(
                new SelectItemOption<String>("Enabled", SpaceStatus.SECURE.name()),
                new SelectItemOption<String>("Disabled", SpaceStatus.INSECURE.name()),
                new SelectItemOption<String>("Any", SpaceStatus.ANY.name())
            ));

    private SpaceQuery lastQuery_;
    private SpaceStatus statusFilter = SpaceStatus.SECURE;

    private String spaceSelected_;

    private UIGridSpaces grid_;
    
    boolean insecureSpaceActived = true;

    public UIListSpaces() throws Exception {
        super(OPTIONS_);

        UIFormInputSet inputSet = getUISearchForm().getQuickSearchInputSet();


            UIFormSelectBox selectBox = new UIFormSelectBox("UIListSpaces-" + SPACE_STATUS_FILTER, null, SPACE_STATUS_OPTIONS);
            selectBox.setValue(SpaceStatus.SECURE.name());
            selectBox.setLabel("status");
            selectBox.setId("UIListSpaces-" + SPACE_STATUS_FILTER);
            inputSet.addChild(selectBox);


        grid_ = addChild(UIGridSpaces.class, null, "UIListSpacesGird");
        grid_.configure(SPACE_NAME, SPACE_BEAN_FIELD, SPACE_ACTION);
        grid_.getUIPageIterator().setId("UIListSpacesIterator");
        grid_.getUIPageIterator().setParent(this);
    }

    /**
     * @see org.exoplatform.webui.core.UIComponent#processRender(org.exoplatform.webui.application.WebuiRequestContext)
     */
    @Override
    public void processRender(WebuiRequestContext context) throws Exception {
        int curPage = grid_.getUIPageIterator().getCurrentPage();
        if (lastQuery_ == null)
            lastQuery_ = new SpaceQuery();
        search(lastQuery_);
        grid_.getUIPageIterator().setCurrentPage(curPage);
        grid_.getUIPageIterator().getCurrentPageData();
        super.processRender(context);
    }

    public void setSpaceSelected(String spaceName) {
        spaceSelected_ = spaceName;
    }

    public String getSpaceSelected() {
        return spaceSelected_;
    }

    public void search(SpaceQuery query) {
        lastQuery_ = query;
        grid_.getUIPageIterator().setPageList(new FindSpacesPageList(query, 10, statusFilter));
    }

    public void quickSearch(UIFormInputSet quickSearchInput) throws Exception {
        SpaceQuery query = new SpaceQuery();
        UIFormStringInput input = (UIFormStringInput) quickSearchInput.getChild(0);
        UIFormSelectBox select = (UIFormSelectBox) quickSearchInput.getChild(1);
        String name = input.getValue();
        if (name != null && !(name = name.trim()).equals("")) {
            if (name.indexOf("*") < 0) {
                if (name.charAt(0) != '*')
                    name = "*" + name;
                if (name.charAt(name.length() - 1) != '*')
                    name += "*";
            }
            name = name.replace('?', '_');
            String selectBoxValue = select.getValue();
            if (selectBoxValue.equals(SPACE_NAME))
                query.setSpaceName(name);


        }

        //Fetch space status
        UIFormSelectBox selectBox = getUISearchForm().getQuickSearchInputSet()
                .getChildById("UIListSpaces-" + SPACE_STATUS_FILTER);
        if(selectBox != null) {
            String status = selectBox.getValue();
            statusFilter = SpaceStatus.valueOf(status);
        } else {
            statusFilter = SpaceStatus.ANY;
        }

        search(query);

        if (getChild(UIGridSpaces.class).getUIPageIterator().getAvailable() == 0) {
            UIApplication uiApp = Util.getPortalRequestContext().getUIApplication();
            uiApp.addMessage(new ApplicationMessage("UISearchForm.msg.empty", null));
        }
    }

    @SuppressWarnings("unused")
    public void advancedSearch(UIFormInputSet advancedSearchInput) {
    }







  public static class InsecureSecureSpaceActionListener extends EventListener<UIListSpaces> {
    @Override
    public void execute(Event<UIListSpaces> event) throws Exception {
      UIListSpaces uiListSpace = event.getSource();
      SpaceService spaceService = uiListSpace.getApplicationComponent(SpaceService.class);
      IdentityManager identityManager = uiListSpace.getApplicationComponent(IdentityManager.class);
      String spaceName = event.getRequestContext().getRequestParameter(OBJECTID);

      ApplicationMessage warning = null;

      Space space = spaceService.getSpaceByPrettyName((SpaceUtils.cleanString(spaceName)));

      if (space == null) {
        warning = new ApplicationMessage("UIListSpaces.msg.space-is-deleted", null, ApplicationMessage.WARNING);
      } else {
        Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
        Profile spaceProfile = spaceIdentity.getProfile();
        if (spaceProfile.getProperty("secure") != null && Boolean.parseBoolean(spaceProfile.getProperty("secure").toString())) {
          spaceProfile.setProperty("secure", "false");
        } else {
          spaceProfile.setProperty("secure", "true");
        }
        identityManager.updateProfile(spaceProfile);
      }

      if (warning != null) {
        UIApplication uiApp = event.getRequestContext().getUIApplication();
        uiApp.addMessage(warning);
        Util.getPortalRequestContext().ignoreAJAXUpdateOnPortlets(true);
      }
    }
  }
    

}
