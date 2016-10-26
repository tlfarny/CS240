package client;

import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import communicationsClasses.DownloadBatchParam;
import communicationsClasses.DownloadBatchResult;
import communicationsClasses.GetProjectsParam;
import communicationsClasses.GetProjectsResult;
import communicationsClasses.GetSampleImageParam;
import communicationsClasses.GetSampleImageResult;
import modelClasses.Project;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.net.URL;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DownloadBatchPane extends JDialog {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DownloadBatchPane() throws Exception {
		//------------------------------------------------------------------------
		// Window Construction
		//------------------------------------------------------------------------
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Download Batch");
		setBounds(100, 100, 450, 136);
		setSize(new Dimension(400, 135));
		setResizable(false);
		
		//------------------------------------------------------------------------
		// Window Components
		//------------------------------------------------------------------------
		JLabel lblProject = new JLabel("Project: ");
		
		JComboBox comboBox = new JComboBox();
		GetProjectsResult optionsResults = Client.cc.getProjects(new GetProjectsParam(Client.cc.getUser()));
		for(Project project : optionsResults.getProjects()){
			comboBox.addItem(project.getTitle());
		}
		
		JButton btnViewSample = new JButton("View Sample");
		JButton btnDownload = new JButton("Download");
		JButton btnCancel = new JButton("Cancel");
		
		btnViewSample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int result = getProjectNumberFromSelection((String) comboBox.getSelectedItem());
					GetSampleImageResult sample = Client.cc.getSampleImage(new GetSampleImageParam(Client.cc.getUsername(), Client.cc.getPassword(), result));
					final ImageIcon icon = new ImageIcon(new URL(sample.toString(Client.cc.getHost(), Client.cc.getPortString())));
			        JOptionPane.showMessageDialog(null, "", "Sample Image", JOptionPane.INFORMATION_MESSAGE, icon);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int result = getProjectNumberFromSelection((String) comboBox.getSelectedItem());
					DownloadBatchResult batchDownload = Client.cc.downloadBatch(new DownloadBatchParam(Client.cc.getUsername(), Client.cc.getPassword(), result));
					Client.cc.setResult(batchDownload);
					mainWindow.drawing.addShape("http://" + Client.cc.getHost() + ":" + Client.cc.getPortString() + "/" + batchDownload.getBatch().getFilePath());
					mainWindow.drawing.setScale(mainWindow.drawing.getScale());
					Client.Main.userAlreadyHasBatch(true);
					Client.Main.toolbarButtonsSwitch(true);
					mainWindow.drawing.drawRectangle(Client.cc.getResult().getFields().get(0).getxCoord(), Client.cc.getResult().getProject().getFirstYCoordinate(), Client.cc.getResult().getFields().get(0).getWidth(), Client.cc.getResult().getProject().getRecordHeight());
			 		Client.Main.addTable();
			 		Client.Main.changeTableSelection(0, 0);
			 		Client.Main.updateFieldHelp(0);
					setVisible(false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		//------------------------------------------------------------------------
		// Grid Bag Layout for Window Components
		//------------------------------------------------------------------------
				
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 110, 92, 130, 0};
		gridBagLayout.rowHeights = new int[]{38, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		GridBagConstraints gbc_lblProject = new GridBagConstraints();
		gbc_lblProject.insets = new Insets(0, 0, 5, 5);
		gbc_lblProject.anchor = GridBagConstraints.EAST;
		gbc_lblProject.gridx = 1;
		gbc_lblProject.gridy = 1;
		getContentPane().add(lblProject, gbc_lblProject);
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 1;
		getContentPane().add(comboBox, gbc_comboBox);
		
		GridBagConstraints gbc_btnViewSample = new GridBagConstraints();
		gbc_btnViewSample.insets = new Insets(0, 0, 5, 0);
		gbc_btnViewSample.gridx = 4;
		gbc_btnViewSample.gridy = 1;
		getContentPane().add(btnViewSample, gbc_btnViewSample);
		
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.insets = new Insets(0, 0, 0, 5);
		gbc_btnDownload.gridx = 2;
		gbc_btnDownload.gridy = 2;
		getContentPane().add(btnDownload, gbc_btnDownload);
		
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.gridx = 3;
		gbc_btnCancel.gridy = 2;
		getContentPane().add(btnCancel, gbc_btnCancel);
	}
	
	private int getProjectNumberFromSelection(String projectToGet) throws Exception{
		int result = 1;
		GetProjectsResult optionsResults = Client.cc.getProjects(new GetProjectsParam(Client.cc.getUser()));
		for(Project project : optionsResults.getProjects()){
			if(projectToGet.equals(project.getTitle())){
				result = project.getID(); 
			}
		}
		return result;
	}

}
