package org.elasticsearch.plugin.zazzle;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.rest.*;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.NOT_FOUND;
import static org.elasticsearch.rest.RestStatus.OK;


import org.elasticsearch.rest.BaseRestHandler;

public class HelloWorldAction extends BaseRestHandler{

    @Inject public HelloWorldAction(Settings settings, Client client, RestController controller) {
        super(settings, client);

        // Define REST endpoints
        controller.registerHandler(GET, "/_hello/", this);
        controller.registerHandler(GET, "/_hello/{name}", this);
    }

    public void handleRequest(final RestRequest request, final RestChannel channel) {
        Refresher rf = new Refresher();
        
   /*     final GetRequest getRequest = new GetRequest(INDEX, TYPE, "test");
        getRequest.listenerThreaded(false);
        getRequest.operationThreaded(true);
        
        client.get(getRequest);
        XContentBuilder builder;
		try {
		//	builder = restContentBuilder(request);
		//	builder.startObject().field(new XContentBuilderString("hello"), "test").endObject();
		//	channel.sendResponse(new BytesRestResponse(OK, builder));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     */  
    }
}
