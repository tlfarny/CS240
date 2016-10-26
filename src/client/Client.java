package client;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;

import clientCommunicatorClass.ClientCommunicator;;

public class Client {
	public static Login2 login;
	public static mainWindow Main;
	public static ClientCommunicator cc;  //will need to be set to take the host and port off of command line functions
	public static BatchState batch;
	
	public static void main(String[] args) {
		try {
			cc = new ClientCommunicator(args[0], args[1]);
			batch = new BatchState();
			login = new Login2();
			Main = new mainWindow();
			login.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			login.pack();
			login.setVisible(true);
			Main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Main.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loggedIn(){
		login.clearFields();
		login.setVisible(false);
		Main.setVisible(true);
//		Main.setSize(new Dimension(2, 10000));
//		Main.resetScreenBasedOnUser();
//		if(Client.cc.getUser().getBatchNumber() != -1){						//This will need to be uncommented. Commented for troubleshooting purposes.
//			Main.userAlreadyHasBatch(true);
//		}
	}
	
	public static void loggedOut(){
		Main.setVisible(false);
		login.setVisible(true);
		login.clearFields();
	}

}
