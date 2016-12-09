
package org.exoplatform.addons.linotp.portlet.SecurityManagement;

import java.io.Writer;

import org.exoplatform.addons.linotp.portlet.SecurityManagement.component.UIListSpaces;
import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIContainer;

/**
 * created on 06/12/2016
 * 
 * @author azaoui@exoplatform.com
 */

@ComponentConfig()
@Serialized
public class UISpaceManagement extends UIContainer {

    public UISpaceManagement() throws Exception {
        addChild(UIListSpaces.class, null, null).setRendered(true);
    }

    public void processRender(WebuiRequestContext context) throws Exception {
        Writer w = context.getWriter();
        w.write("<div id=\"UISpaceManagement\" class=\"UISpaceManagement\">");
        renderChildren();
        w.write("</div>");
    }

}
