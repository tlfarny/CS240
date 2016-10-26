package handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.rmi.ServerException;

import serverFacade.ServerFacade;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import communicationsClasses.SubmitBatchParam;
import communicationsClasses.SubmitBatchResult;

public class SubmitBatchHandler implements HttpHandler {
	
	private XStream xmlStream = new XStream(new DomDriver());

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServerFacade sf = new ServerFacade();
		SubmitBatchParam params = (SubmitBatchParam)xmlStream.fromXML(exchange.getRequestBody());
		SubmitBatchResult result = null;
		 try {
			result = sf.submitBatch(params);
		} catch (ServerException e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		 
		 exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		
	}

}
