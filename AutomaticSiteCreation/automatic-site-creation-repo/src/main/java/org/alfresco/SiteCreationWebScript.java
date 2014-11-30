package org.alfresco;

import org.alfresco.repo.site.script.ScriptSiteService;
import org.alfresco.service.cmr.site.SiteService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;

public class SiteCreationWebScript extends AbstractWebScript
{
  private static final String LOGIN_URL = "http://localhost:8081/share/page/dologin";
  private static final String CREATE_SITE_URL = "http://localhost:8081/share/service/modules/create-site";

  private static final String CONTENT_TYPE_JSON = "application/json";
  private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
  private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

  @Autowired SiteService siteService;
  @Autowired ScriptSiteService siteService1;

  @Override
  public void execute(WebScriptRequest req, WebScriptResponse res) throws IOException
  {
    try
    {
      HttpClient client = new HttpClient();

      JSONObject someSite = new JSONObject();
      someSite.put("visibility", "PUBLIC");
      someSite.put("title", "somenewsite");
      someSite.put("shortName", "somenewsite");
      someSite.put("description", "somenewsite");
      someSite.put("sitePreset", "site-dashboard");

      JSONObject anotherSite = new JSONObject();
      anotherSite.put("visibility", "PUBLIC");
      anotherSite.put("title", "Another Site");
      anotherSite.put("shortName", "anotherSite");
      anotherSite.put("description", "Autmatically created Site");
      anotherSite.put("sitePreset", "site-dashboard");

      makePostCall(client, LOGIN_URL, "username=admin&password=admin", CONTENT_TYPE_FORM, "Login to Alfresco Share");

      /**
       * For some reason first post call fails to be executed. I couldn't find any trace of it.
       * For the moment I'll keep it here.
       */
      makePostCall(client, CREATE_SITE_URL, someSite.toString(), CONTENT_TYPE_JSON, "Login");
      makePostCall(client, CREATE_SITE_URL, anotherSite.toString(), CONTENT_TYPE_JSON, "Create 1st site");
      makePostCall(client, CREATE_SITE_URL, anotherSite.toString(), CONTENT_TYPE_JSON, "Create 2nd site");

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void makePostCall(HttpClient client, String url, String data, String dataType, String callName)
  {
    PostMethod postMethod = new PostMethod(url);
    try
    {
      postMethod.setRequestHeader("Content-Type", dataType);
      postMethod.setRequestEntity(new StringRequestEntity(data, CONTENT_TYPE_TEXT_PLAIN, "UTF-8"));
      client.executeMethod(postMethod);
    }
    catch (IOException ioe)
    {
      System.out.println("Failed to " + callName);
    }
    finally
    {
      postMethod.releaseConnection();
    }
  }
}