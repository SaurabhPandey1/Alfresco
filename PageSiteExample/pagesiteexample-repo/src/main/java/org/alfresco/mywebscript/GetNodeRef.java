package org.alfresco.mywebscript;

import org.activiti.engine.impl.util.json.JSONObject;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.site.SiteService;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.security.AlgorithmConstraints;

/**
 * Created by pmakhov on 25.10.14.
 */
public class GetNodeRef extends AbstractWebScript
{
  private FileFolderService fileFolderService;
  private SiteService siteService;

  /**
   * Return the nodeRef of the folder which name is the same as the authenticated user
   * @param req
   * @param res
   * @return nodeRef to the folder
   */
  @Override
  public void execute(WebScriptRequest req, WebScriptResponse res) {
    JSONObject json = new JSONObject();
    String userName = req.getParameter("param");
    NodeRef documentLibrary = siteService.getContainer("somesite", "documentlibrary");

    try  {
      if ("admin".equals(userName))
        json.put("nodeRef", documentLibrary.toString());
      else  {
        NodeRef userNodeRef = fileFolderService.searchSimple(documentLibrary, userName);
        if (userNodeRef == null)
          throw new Exception("no such folder");
        else
          json.put("nodeRef", userNodeRef.toString());
      }
      res.getWriter().write(json.toString());
    }
    catch (Exception e) {
      throw new WebScriptException("Unexpected exception", e);
    }
  }

  public void setServiceRegistry(ServiceRegistry serviceRegistry)
  {
    this.fileFolderService = serviceRegistry.getFileFolderService();
    this.siteService = serviceRegistry.getSiteService();
  }
}