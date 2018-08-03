package com.ubddpd.utils;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.rest.RestMethod;
import com.eviware.soapui.impl.rest.RestRequest;
import com.eviware.soapui.impl.rest.RestResource;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.impl.wsdl.support.http.SoapUIMultiThreadedHttpConnectionManager;
import com.eviware.soapui.model.iface.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SoapUIClient {

    private WsdlProject project;
    private RestService restService;
    private RestResource restResource;
    private RestMethod restMethod;
    private RestRequest restRequest;

    public void openProject(String projectFile) throws Exception {
        project = new WsdlProject(projectFile);
    }

    public void setRestService(String serviceName){
        List children = project.getChildren();
        for(Object child : children) {
            if (child instanceof RestService) {
                RestService service = (RestService) child;
                String name = service.getName();
                if (name.equals("AssetAllocationDetail")) {
                    restService = service;
                    break;
                }
            }
        }
    }

    public void setRestResource(String resourceName) {
        List<RestResource> resources = restService.getResourceList();
        for (RestResource resource : resources) {
            if(resource.getName().equals(resourceName)){
                restResource = resource;
                break;
            }
        }
    }

    public void setRestMethod(String methodName){
        restMethod = restResource.getRestMethodByName(methodName);
    }

    public void setRestRequest(String requestName){
        restRequest = restMethod.getRequestByName("AssetAllocationDetail");
    }

    public String getResponse(Map<String, String > params) throws Exception {

        for(String key: params.keySet()){
            String value = params.get(key);
            restRequest.setPropertyValue(key, value);
        }

        WsdlSubmitContext wsdlSubmitContext = new WsdlSubmitContext(restRequest);
        WsdlSubmit<RestRequest> submit = restRequest.submit(wsdlSubmitContext, false);
        Response response = submit.getResponse();
        return response.getContentAsString();
    }

    public void closeProject(){
        // Need to shutdown all the threads invoked by each SoapUI TestSuite
        SoapUI.getThreadPool().shutdown();
        try {
            SoapUI.getThreadPool().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Now to shutdown the monitor thread setup by SoapUI
        Thread[] tarray = new Thread[Thread.activeCount()];
        Thread.enumerate(tarray);
        for (Thread t : tarray) {
            if (t instanceof SoapUIMultiThreadedHttpConnectionManager.IdleConnectionMonitorThread) {
                ((SoapUIMultiThreadedHttpConnectionManager.IdleConnectionMonitorThread) t)
                        .shutdown();
            }
        }

        // Finally Shutdown SoapUI itself.
        SoapUI.shutdown();
    }

    public static void main(String[] args) throws Exception {
        SoapUIClient client = new SoapUIClient();
        client.openProject("C:\\SoapUIScripts\\CIServiceAutomation-soapui-project.xml");
        client.setRestService("AssetAllocationDetail");
        client.setRestResource("AssetAllocationDetail");
        client.setRestMethod("AssetAllocationDetail");
        client.setRestRequest("AssetAllocationDetail");
        Map<String, String> params = new HashMap<>();
        params.put("planNumber", "004493");
        System.out.println("*** RESULT: " + client.getResponse(params));
        client.closeProject();
    }

}
