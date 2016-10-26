package client;

import javax.swing.JButton;
import javax.swing.JDialog;

import communicationsClasses.ValidateUser_Params;
import communicationsClasses.ValidateUser_Result;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class Login2 extends JDialog {
	Connection connection = null;
	private JTextField usernameJTF;
	private JPasswordField passwordJPF;

	public Login2() {
		//------------------------------------------------------------------------
		// Login Window Construction
		//------------------------------------------------------------------------
		
		setTitle("Login to Indexer");
		setSize(460,133);
		setResizable(false);
		setModal(true);
		setLocation(450,300);
		
		//------------------------------------------------------------------------
		// Login Window Components
		//------------------------------------------------------------------------
		JLabel lblUsername = new JLabel("Username");
		JLabel lblPassword = new JLabel("Password");
		JButton btnLogin = new JButton("Login");
		JButton btnCancel = new JButton("Cancel");
		usernameJTF = new JTextField();
		passwordJPF = new JPasswordField();
		
		//------------------------------------------------------------------------
		// Grid Bag Layout for Login Window Components
		//------------------------------------------------------------------------
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{68, 69, 77, 77, 117, 0};
		gridBagLayout.rowHeights = new int[]{40, 34, 32, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 1;
		gbc_lblUsername.gridy = 0;
		getContentPane().add(lblUsername, gbc_lblUsername);
		
		GridBagConstraints gbc_usernameJTF = new GridBagConstraints();
		gbc_usernameJTF.anchor = GridBagConstraints.SOUTH;
		gbc_usernameJTF.gridwidth = 2;
		gbc_usernameJTF.insets = new Insets(0, 0, 5, 5);
		gbc_usernameJTF.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameJTF.gridx = 2;
		gbc_usernameJTF.gridy = 0;
		getContentPane().add(usernameJTF, gbc_usernameJTF);
		usernameJTF.setColumns(10);
		
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 1;
		gbc_lblPassword.gridy = 1;
		getContentPane().add(lblPassword, gbc_lblPassword);
		
		GridBagConstraints gbc_passwordJPF_1 = new GridBagConstraints();
		gbc_passwordJPF_1.gridwidth = 2;
		gbc_passwordJPF_1.insets = new Insets(0, 0, 5, 5);
		gbc_passwordJPF_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordJPF_1.gridx = 2;
		gbc_passwordJPF_1.gridy = 1;
		getContentPane().add(passwordJPF, gbc_passwordJPF_1);
		
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.anchor = GridBagConstraints.NORTH;
		gbc_btnLogin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogin.insets = new Insets(0, 0, 0, 5);
		gbc_btnLogin.gridx = 2;
		gbc_btnLogin.gridy = 2;
		getContentPane().add(btnLogin, gbc_btnLogin);
		
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.anchor = GridBagConstraints.NORTH;
		gbc_btnCancel.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCancel.gridx = 3;
		gbc_btnCancel.gridy = 2;
		getContentPane().add(btnCancel, gbc_btnCancel);
		
		
		//------------------------------------------------------------------------
		// Action Listeners for Login Window Components
		//------------------------------------------------------------------------
		
		passwordJPF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER){
					SwingUtilities.getRootPane(btnLogin).setDefaultButton(btnLogin);
				}
			}
		});
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					char[] pass = passwordJPF.getPassword();
					String passString = new String(pass);
					ValidateUser_Params params = new ValidateUser_Params(usernameJTF.getText(), passString);
					ValidateUser_Result loginResult = Client.cc.validateUser(params);
					if(loginResult == null){
						JOptionPane.showMessageDialog(null, "Invalid username and/or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
					}
					else{
						JOptionPane.showMessageDialog(null, "Welcome, " + loginResult.getFirstName() + " " + loginResult.getLastName() + ".\nYou have indexed " + loginResult.getNumRecords() + " records.", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
						Client.cc.setUsername(usernameJTF.getText());
						Client.cc.setPassword(passString);
						Client.cc.setUser(loginResult.getUser());
						Client.loggedIn();
					}			
				}
				catch(Exception er){
					er.printStackTrace();
					JOptionPane.showMessageDialog(null, "Server not found or not running.\nPlease check your connnection and try again.", "Server Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	
	
	public void clearFields(){
		usernameJTF.setText("");
		passwordJPF.setText("");
	}
}
