/*
 * Copyright (C) 2012 eXo Platform SAS.
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

import java.lang.reflect.Method;
import java.util.List;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.identity.provider.SpaceIdentityProvider;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.util.ReflectionUtil;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPageIterator;

@ComponentConfig(template = "app:/groovy/social/portlet/spaceSecurity/UIGridSpaces.gtmpl")
@Serialized
public class UIGridSpaces extends UIComponent {
    /** The page iterator */
    protected UIPageIterator uiIterator_;

    /** The bean field that holds the id of this bean */
    protected String beanIdField_;

    /** An array of String representing the fields in each bean */
    protected String[] beanField_;

    /** An array of String representing the actions on each bean */
    protected String[] action_;

    protected String label_;

    protected boolean useAjax = true;

    protected int displayedChars_ = 30;

    public UIGridSpaces() throws Exception {
        uiIterator_ = createUIComponent(UIPageIterator.class, null, null);
        uiIterator_.setParent(this);
    }

    public UIPageIterator getUIPageIterator() {
        return uiIterator_;
    }

    public UIGridSpaces configure(String beanIdField, String[] beanField, String[] action) {
        this.beanIdField_ = beanIdField;
        this.beanField_ = beanField;
        this.action_ = action;
        return this;
    }

    public String getBeanIdField() {
        return beanIdField_;
    }

    public String[] getBeanFields() {
        return beanField_;
    }

    public String[] getBeanActions() {
        return action_;
    }

    public List<?> getBeans() throws Exception {
        return uiIterator_.getCurrentPageData();
    }

    public String getLabel() {
        return label_;
    }

    public void setLabel(String label) {
        label_ = label;
    }

    public Object getFieldValue(Object bean, String field) throws Exception {
        Method method = ReflectionUtil.getGetBindingMethod(bean, field);
       // if(field.equals("url"))
          //return getSpaceUrl((String) method.invoke(bean, ReflectionUtil.EMPTY_ARGS));
        return method.invoke(bean, ReflectionUtil.EMPTY_ARGS);
    }

    public String getBeanIdFor(Object bean) throws Exception {
        return getFieldValue(bean, beanIdField_).toString();
    }

    @SuppressWarnings("unchecked")
    public UIComponent findComponentById(String lookupId) {
        if (uiIterator_.getId().equals(lookupId)) {
            return uiIterator_;
        }
        return super.findComponentById(lookupId);
    }

    public boolean isUseAjax() {
        return useAjax;
    }

    public void setUseAjax(boolean value) {
        useAjax = value;
    }

    public int getDisplayedChars() {
        return displayedChars_;
    }

    public void setDisplayedChars(int displayedChars) {
        this.displayedChars_ = displayedChars;
    }
    public static boolean isSecureSpace(String spaceName) {
      ExoContainer container = PortalContainer.getInstance();
      IdentityManager identityManager = (IdentityManager) container.getComponentInstance(IdentityManager.class);
      Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, spaceName, false);
      Profile spaceProfile = spaceIdentity.getProfile();
      if (spaceProfile.getProperty("secure") != null && Boolean.parseBoolean(spaceProfile.getProperty("secure").toString()))
        return true;

      return false;

    }
    
    public static boolean isSecureSpaceUrl(String spaceUrl) {
      ExoContainer container = PortalContainer.getInstance();
      SpaceService spaceService = (SpaceService) container.getComponentInstance(SpaceService.class);
      Space space= spaceService.getSpaceByUrl(spaceUrl);
      IdentityManager identityManager = (IdentityManager) container.getComponentInstance(IdentityManager.class);
      Identity spaceIdentity = identityManager.getOrCreateIdentity(SpaceIdentityProvider.NAME, space.getPrettyName(), false);
      Profile spaceProfile = spaceIdentity.getProfile();
      if (spaceProfile.getProperty("secure") != null && Boolean.parseBoolean(spaceProfile.getProperty("secure").toString()))
        return true;

      return false;

    }
    public static String getSpaceUrl(String spaceName){
      ExoContainer container = PortalContainer.getInstance();
      SpaceService spaceService = (SpaceService) container.getComponentInstance(SpaceService.class);
      Space space=spaceService.getSpaceByUrl(spaceName);
      StringBuffer baseSpaceURL = null;
      baseSpaceURL = new StringBuffer();
       baseSpaceURL.append(PortalContainer.getCurrentPortalContainerName()+ "/g/:spaces:") ;
      String groupId = space.getGroupId();
      String permanentSpaceName = groupId.split("/")[2];
      if (permanentSpaceName.equals(space.getPrettyName())) {
        baseSpaceURL.append(permanentSpaceName);
        baseSpaceURL.append("/");
        baseSpaceURL.append(permanentSpaceName);
      } else {
        baseSpaceURL.append(space.getPrettyName());
        baseSpaceURL.append("/");
        baseSpaceURL.append(space.getPrettyName());
      }
      return baseSpaceURL.toString();
    }
    
    public static Space getSpace(String spaceName){
      ExoContainer container = PortalContainer.getInstance();
      SpaceService spaceService = (SpaceService) container.getComponentInstance(SpaceService.class);
      Space space=spaceService.getSpaceByUrl(spaceName);
      
      return space;
    }
}
