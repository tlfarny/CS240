package handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import serverFacade.ServerFacade;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import communicationsClasses.ValidateUser_Params;
import communicationsClasses.ValidateUser_Result;

public class ValidateUserHandler implements HttpHandler{
	
	private XStream xmlStream = new XStream(new DomDriver());

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("validate user handler");
		ServerFacade sf = new ServerFacade();
		ValidateUser_Params params = (ValidateUser_Params)xmlStream.fromXML(exchange.getRequestBody());
		String username = params.getUsername();
		String password = params.getPassword();
		ValidateUser_Result result = null;
		try {
			result = sf.validateUser(username, password);
		} catch (Exception e) {
			e.printStackTrace();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			exchange.getResponseBody().close();
			return;
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(result, exchange.getResponseBody());
		exchange.getResponseBody().close();
		
	}

}
