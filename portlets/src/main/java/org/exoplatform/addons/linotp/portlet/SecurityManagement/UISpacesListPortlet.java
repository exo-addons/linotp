package org.exoplatform.addons.linotp.portlet.SecurityManagement;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.portal.webui.portal.UIPortalComponentActionListener.ViewChildActionListener;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

/**
 * created on 06/12/2016
 * 
 * @author azaoui@exoplatform.com
 */

@ComponentConfig(lifecycle = UIApplicationLifecycle.class, template = "app:/groovy/social/portlet/spaceSecurity/UISpacesListPortlet.gtmpl", events = {
        @EventConfig(listeners = ViewChildActionListener.class) }

)
@Serialized
public class UISpacesListPortlet extends UIPortletApplication {

    public UISpacesListPortlet() throws Exception {

        addChild(UISpaceManagement.class, null, null);

    }


}
