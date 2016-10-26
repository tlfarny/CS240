	package server;

import handlers.DownloadBatchHandler;
import handlers.DownloadFileHandler;
import handlers.GetFieldsHandler;
import handlers.GetProjectsHandler;
import handlers.GetSampleImageHandler;
import handlers.SearchHandler;
import handlers.SubmitBatchHandler;
import handlers.ValidateUserHandler;
import serverFacade.ServerFacade;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;

public class Server {
	
	public Server(){
		return;
	}
	
	public Server(String portNum){
		port = Integer.parseInt(portNum);
	}
	
	private static final int MAX_WAITING_CONNECTIONS = 10;
	private static int port = 40000;
	private HttpServer server;
	
	public static void main(String[] args){
		if(args.length > 0){
			new Server(args[0]).run();
		}
		else{
			new Server().run();
		}
		
	}
	
	public void run(){
		try {
			System.out.println("server\n");
			ServerFacade.initialize();
			server = HttpServer.create(new InetSocketAddress(port), MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) {
			e.printStackTrace();
			return;
		}

		server.setExecutor(null); // use the default executor
		
		server.createContext("/downloadBatch", downloadBatchHandler);
		server.createContext("/", downloadFileHandler);
		server.createContext("/getFields", getFieldsHandler);
		server.createContext("/getProjects", getProjectsHandler);
		server.createContext("/getSampleImage", getSampleImageHandler);
		server.createContext("/search", searchHandler);
		server.createContext("/submitBatch", submitBatchHandler);
		server.createContext("/validateUser", validateUserHandler);
		server.start();
	}
	
	private HttpHandler downloadBatchHandler = new DownloadBatchHandler();
	private HttpHandler downloadFileHandler = new DownloadFileHandler();
	private HttpHandler getFieldsHandler = new GetFieldsHandler();
	private HttpHandler getProjectsHandler = new GetProjectsHandler();
	private HttpHandler getSampleImageHandler = new GetSampleImageHandler();
	private HttpHandler searchHandler = new SearchHandler();
	private HttpHandler submitBatchHandler = new SubmitBatchHandler();
	private HttpHandler validateUserHandler = new ValidateUserHandler();
}
