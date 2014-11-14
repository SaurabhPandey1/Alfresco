<import resource="classpath:/alfresco/site-webscripts/org/alfresco/components/documentlibrary/include/toolbar.lib.js">
 <import resource="classpath:/alfresco/site-webscripts/org/alfresco/components/upload/uploadable.lib.js">
 <import resource="classpath:/alfresco/site-webscripts/org/alfresco/components/documentlibrary/include/documentlist.lib.js">

doclibCommon();

function widgets()
{
  var useTitle = "true";
  var rootNode = callWS(user.name);
  var docLibConfig = config.scoped["DocumentLibrary"];
  if (docLibConfig != null)
  {
    var tmp = docLibConfig["use-title"];
    useTitle = tmp != null ? tmp : "true";
  }

  var docListToolbar = {
    id: "DocListToolbar",
    name: "Alfresco.RepositoryDocListToolbar",
    assignTo: "docListToolbar",
    options: {
      siteId: "",
      rootNode: rootNode,
      hideNavBar: Boolean(toolbar.preferences.hideNavBar),
      googleDocsEnabled: toolbar.googleDocsEnabled,
      repositoryBrowsing: toolbar.rootNode != null,
      useTitle: (useTitle == "true"),
      syncMode: toolbar.syncMode != null ? toolbar.syncMode : "",
      createContentByTemplateEnabled: model.createContentByTemplateEnabled,
      createContentActions: model.createContent
    }
  };

  var documentList = {
    id: "DocumentList",
    name: "Alfresco.DocumentList",
    options: {
      syncMode: model.syncMode != null ? model.syncMode : "",
      siteId: "",
      containerId: "documentLibrary",
      rootNode: rootNode,
      usePagination: (args.pagination == "true"),
      sortAscending: (model.preferences.sortAscending != null ? model.preferences.sortAscending : true),
      sortField: model.preferences.sortField != null ? model.preferences.sortField : "cm:name",
      showFolders: (model.preferences.showFolders != null ? model.preferences.showFolders : true),
      simpleView: model.preferences.simpleView != null ? model.preferences.simpleView : "null",
      viewRenderers: model.viewRenderers,
      viewRendererName: model.preferences.viewRendererName != null ? model.preferences.viewRendererName : "detailed",
      viewRendererNames: model.viewRendererNames != null ? model.viewRendererNames :
                         ["simple", "detailed", "gallery", "filmstrip"],
      highlightFile: page.url.args["file"] != null ? page.url.args["file"] : "",
      replicationUrlMapping: model.replicationUrlMapping != null ? model.replicationUrlMapping : "{}",
      repositoryBrowsing: model.rootNode != null,
      useTitle: (model.useTitle != null ? model.useTitle == "true" : true),
      userIsSiteManager: model.userIsSiteManager,
      associatedToolbar: { _alfValue: "docListToolbar", _alfType: "REFERENCE" }
    }
  };
  if (model.repositoryUrl != null)
  {
    documentList.options.repositoryUrl = model.repositoryUrl;
  }

  model.widgets = [docListToolbar, documentList];
}

widgets();

function callWS(wsParamValue)
{
  var url = "/api/mywebscript/getnoderef?param=" + wsParamValue;
  var result = remote.call(url);

  if (result.status == 200)
  {
    var obj = eval('(' + result + ')');
    return obj.nodeRef;
  }
  else
  {
    status.setCode(500, "There is no folder for user " + wsParamValue);

  }
}

widgets();
