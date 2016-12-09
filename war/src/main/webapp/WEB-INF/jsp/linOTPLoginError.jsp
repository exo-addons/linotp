<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.exoplatform.portal.resource.SkinService"%>
<%@ page import="org.exoplatform.container.PortalContainer"%>
<%@ page import="org.exoplatform.container.PortalContainer"%>
<%@ page import="org.exoplatform.services.resources.ResourceBundleService"%>
<%@ page import="java.util.ResourceBundle" %>
<%
  String contextPath = request.getContextPath();

  String lang = request.getLocale().getLanguage();
  response.setCharacterEncoding("UTF-8");
  response.setContentType("text/html; charset=UTF-8");  

  SkinService skinService = (SkinService) PortalContainer.getCurrentInstance(session.getServletContext()).getComponentInstanceOfType(SkinService.class);
  String cssPath = skinService.getSkin("portal/SoftwareRegistration", "Default").getCSSPath();
    
  PortalContainer portalContainer = PortalContainer.getCurrentInstance(session.getServletContext());
  ResourceBundleService service = (ResourceBundleService) portalContainer.getComponentInstanceOfType(ResourceBundleService.class);
  ResourceBundle rb = service.getResourceBundle("locale.portal.webui", request.getLocale());
%>
<html>
<head>
  <title><%=rb.getString("LinOTPLogin.label.header1")%></title>
  <meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">
  <link href="<%=cssPath%>" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="/platform-extension/javascript/jquery-1.7.1.js"></script>


</head>
<body  class="modal-open">
  <div class="UIPopupWindow uiPopup UIDragObject popupDarkStyle">
    <div class="popupHeader ClearFix">
        <span class="popupTitle center"><%=rb.getString("LinOTPLogin.label.header1")%></span>
    </div> 
    <div class="popupContent">


         <%
        String notReachable = (String)session.getAttribute("notReachable");
         if("true".equals(notReachable)){%>
                 <div class="alert alert-error"><i class="uiIconError"></i><%=rb.getString("LinOTPLogin.label.connexion.error")%></div>
         <% session.removeAttribute("notReachable"); }%>



    </div>
  </div>
</body>
</html>