package handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.rmi.ServerException;

import serverFacade.ServerFacade;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import communicationsClasses.GetSampleImageParam;
import communicationsClasses.GetSampleImageResult;

public class GetSampleImageHandler implements HttpHandler{
	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("get sample image handler");
		ServerFacade sf = new ServerFacade();
		GetSampleImageParam param = (GetSampleImageParam) xmlStream.fromXML(exchange.getRequestBody());
		GetSampleImageResult result = null;
		try {
			result = sf.getSampleImage(param);
		} catch (ServerException e) {
			e.printStackTrace();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(result, exchange.getResponseBody());
		exchange.getResponseBody().close();
	}

}
