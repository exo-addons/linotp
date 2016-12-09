<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.exoplatform.container.PortalContainer"%>
<%@ page import="org.exoplatform.services.security.ConversationState"%>
<%@ page import="org.exoplatform.services.resources.ResourceBundleService"%>


<%
    /**
     * Copyright ( C ) 2012 eXo Platform SAS.
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
%>
<%@ page language="java" %>
<%
    String contextPath = request.getContextPath() ;
    String lang = request.getLocale().getLanguage();
    response.setCharacterEncoding("iso-8859-1");
    response.setContentType("text/html; charset=iso-8859-1");

  PortalContainer portalContainer = PortalContainer.getCurrentInstance(session.getServletContext());
  ResourceBundleService service = (ResourceBundleService) portalContainer.getComponentInstanceOfType(ResourceBundleService.class);
  ResourceBundle rb = service.getResourceBundle(service.getSharedResourceBundleNames(), request.getLocale());
   ConversationState conversationState = (ConversationState) portalContainer.getComponentInstanceOfType(ConversationState.class);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="<%=lang%>">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link href="/platform-extension/css/welcome-screens/jquery.qtip.min.css" rel="stylesheet" type="text/css" />
    <link href="/eXoSkin/skin/css/platform/portlets/extensions/welcome-screens.css" rel="stylesheet" type="text/css" />

   

<head>
<div class="backLight"></div>
<div class="uiWelcomeBox" id="AccountSetup1"  >
    <div class="header"><%=rb.getString("LinOTPLogin.label.header1")%></div>
    <div class="content form-horizontal" id="AccountSetup">
        <h5><%=rb.getString("LinOTPLogin.label.check_account")%></h5>
        <p class="desc"><%=rb.getString("LinOTPLogin.label.primary_use")%></p>
        <%
        if(ConversationState.getCurrent()==null||ConversationState.getCurrent().getIdentity().getUserId()=="__anonim"){
        response.sendRedirect("/portal");
        }
        %>

        <form name="tcForm" action="<%= contextPath + "/linOTPcheckAction"+"?"+ConversationState.getCurrent().getIdentity().getUserId()%>" method="post">
            <div class="control-group" id ="pin">
                <label class="control-label"><%=rb.getString("LinOTPLogin.label.pin")%>:</label>
                <div class="controls">
                    <input type=password name="pin" id="pinAccount" placeholder="<%=rb.getString("LinOTPLogin.label.pin")%>" class="inputFieldLarge" required/>
                    <input type="checkbox" onchange="document.getElementById('pinAccount').type = this.checked ? 'text' : 'password'"><%=rb.getString("LinOTPLogin.label.showPIN")%>
                </div>
            </div>
             <div class="control-group" id ="otp">
                <label class="control-label"><%=rb.getString("LinOTPLogin.label.otp")%>:</label>
                <div class="controls">
                    <input type=password name="otp" id="otpAccount" placeholder="<%=rb.getString("LinOTPLogin.label.otp")%>" class="inputFieldLarge" required/>
                    <input type="checkbox" onchange="document.getElementById('otpAccount').type = this.checked ? 'text' : 'password'"><%=rb.getString("LinOTPLogin.label.showOTP")%>
                </div>
            </div>
    <div style="text-align: center; padding: 100px 0;">
        <button class="btn" name="submitbutton" value="submitbutton"><%=rb.getString("LinOTPLogin.label.submit")%></button>
    </div>

    </div>
    <!-- Please do not make it Button it may cause blocker problem -->

</div>
</div>
<div>

</div>
</form>

</html>
