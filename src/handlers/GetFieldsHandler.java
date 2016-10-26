package handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.rmi.ServerException;

import serverFacade.ServerFacade;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import communicationsClasses.GetFieldsParam;
import communicationsClasses.GetFieldsResult;

public class GetFieldsHandler implements HttpHandler{
	
	private XStream xmlStream = new XStream(new DomDriver());

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerFacade sf = new ServerFacade();
		GetFieldsParam param = (GetFieldsParam)xmlStream.fromXML(exchange.getRequestBody());
		GetFieldsResult result = null;
		try{
			if (param.getProjectID() != -1) {
				result = sf.getFields(param.getProjectID());
			}
			else{
				result = sf.getFieldsFromNull();
			}
//		if (param.getProjectID() != -1) {
//			result = sf.getFields(param.getProjectID());
//		}
//		else{
//			
//		}
//		try {
//			fields = ServerFacade.getFields(projectID);
		} catch (ServerException e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(result, exchange.getResponseBody());
		exchange.getResponseBody().close();
	}

}
