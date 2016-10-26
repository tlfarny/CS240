package handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import serverFacade.ServerFacade;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import communicationsClasses.DownloadBatchParam;
import communicationsClasses.DownloadBatchResult;

public class DownloadBatchHandler implements HttpHandler{

	private XStream xmlStream = new XStream(new DomDriver());
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("Download batch handler");
		ServerFacade sf = new ServerFacade();
		DownloadBatchParam params = (DownloadBatchParam)xmlStream.fromXML(exchange.getRequestBody());
		DownloadBatchResult result = null;
		try {
			result = sf.downloadBatch(params);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
	}

}
