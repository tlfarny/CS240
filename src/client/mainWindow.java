package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import communicationsClasses.SubmitBatchParam;
import modelClasses.FieldModels;

import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class mainWindow extends JFrame {

	private JPanel contentPane;
	private JSplitPane mainSplitPane;
	private JPanel top;
	private JSplitPane bottom;
	private boolean highlightsON = true;
	public static DrawingComponent drawing;
	private boolean imagePresent = true;
	
	private JScrollPane scrollPaneTable;
	
	private JTabbedPane left;
	private JTabbedPane right;
	
	private JMenuItem downloadBatch;
	private JMenuItem logout;
	private JMenuItem exit;
	
	private JButton zoomIn;
	private JButton zoomOut;
	private JButton invertImage;
	private JButton toggleHighlights;
	private JButton save;
	private JButton submit;
	
	private DefaultTableModel tm;
	private JTable table;
	
	private JPanel formEntry;
	private JScrollPane formEntryScrollPane;
	private JScrollPane jListScrollPane;
	private JPanel formEntryPanel;
	private JList<String> jl;
	private ArrayList<JTextField> textList;
	
	private JEditorPane fieldHelp;
	private JScrollPane scrollPaneFieldHelp;
	private JPanel imageNavigation;

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public mainWindow() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(0, 0, 3000, 200);
		setPreferredSize(new Dimension(1000, 1000));
		setResizable(true);
		setLocation(225, 0);  //Will need to be changed to per user login data
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		drawing = new DrawingComponent();
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		downloadBatch = new JMenuItem("Download Batch");
		downloadBatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					DownloadBatchPane dlOptions = new DownloadBatchPane();
					dlOptions.setModal(true);
					dlOptions.setVisible(true);
					dlOptions.setResizable(false);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		logout = new JMenuItem("Logout");
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client.loggedOut();
			}
		});
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		file.add(downloadBatch);
		file.add(logout);
		file.add(exit);
		menuBar.add(file);
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		top = new JPanel();
		top.setSize(new Dimension(1000, 250));
		top.setLayout(new BorderLayout(0, 0));
		zoomIn = new JButton("Zoom In");
		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(imagePresent == true){
					drawing.setScale(drawing.getScale() + (1 * 0.5));
				}
			}
		});
		zoomOut = new JButton("Zoom Out");
		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(imagePresent == true){
					drawing.setScale(drawing.getScale() - (1 * 0.5));
				}
			}
		});
		invertImage = new JButton("Invert Image");
		invertImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawing.invert();
			}
		});
		toggleHighlights = new JButton("Toggle Highlights");
		toggleHighlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(highlightsON == true){
					highlightsON = false;
					drawing.deleteRectangle();
					drawing.setScale(drawing.getScale());
				}
				else{
					highlightsON = true;
					if(Client.batch.getSelectedRow() == -1 || Client.batch.getSelectedColumn() == -1){
						drawing.drawRectangle(Client.cc.getResult().getFields().get(0).getxCoord(), Client.cc.getResult().getProject().getFirstYCoordinate(), Client.cc.getResult().getFields().get(0).getWidth(), Client.cc.getResult().getProject().getRecordHeight());
						drawing.setScale(drawing.getScale());
						updateBatchHighlights(1, 1, Client.cc.getResult().getFields().get(0).getxCoord(), Client.cc.getResult().getProject().getFirstYCoordinate(), Client.cc.getResult().getProject().getRecordHeight(), Client.cc.getResult().getFields().get(0).getWidth());
						Client.Main.changeTableSelection(0, 0);
						try {
							updateFieldHelp(0);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					else{
						drawing.drawRectangle(Client.batch.getHighlightX(),Client.batch.getHighlightY(),Client.cc.getResult().getFields().get(Client.batch.getSelectedColumn()).getWidth(), Client.cc.getResult().getProject().getRecordHeight());  
						drawing.setScale(drawing.getScale());
						try {
							updateFieldHelp(Client.batch.getSelectedColumn());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Client.cc.submitBatch(new SubmitBatchParam(Client.cc.getUsername(), Client.cc.getPassword(), Client.cc.getResult().getBatch().getBatchID(), getTableValues()));
					drawing.clearAllShapes();
					toolbarButtonsSwitch(false);
					userAlreadyHasBatch(false);
					left.removeAll();
					right.removeAll();
					initializeTabs();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		JToolBar imageOptions = new JToolBar();
		toolbarButtonsSwitch(false);
		imageOptions.add(zoomIn);
		imageOptions.add(zoomOut);
		imageOptions.add(invertImage);
		imageOptions.add(toggleHighlights);
		imageOptions.add(save);
		imageOptions.add(submit);
		
		top.add(imageOptions, BorderLayout.NORTH);
		top.add(drawing, BorderLayout.CENTER);
		bottom = new JSplitPane();
		initializeTabs();
		
		mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
		mainSplitPane.setDividerLocation(500);
		contentPane.add(mainSplitPane, BorderLayout.CENTER);
		
		setResizable(true);
		pack();
		
	}
	
public void initializeTabs(){
	
	scrollPaneTable = new JScrollPane();
	tm = null;
	table = new JTable(tm);
	table.addMouseListener(mouseAdapter);
	scrollPaneTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	formEntry = new JPanel(new BorderLayout());
	String[] empty = new String[1];
	empty[0] = new String("             ");
	formEntryScrollPane = new JScrollPane();
	formEntryPanel = new JPanel();
	formEntryPanel.setLayout(new BorderLayout(0,0));
	jl = new JList<>(empty);
	textList = new ArrayList<>();
	jl.addMouseListener(mouseListener);
	jListScrollPane = new JScrollPane(jl);
	formEntryScrollPane = new JScrollPane(formEntry);
	formEntryPanel.add(jListScrollPane, BorderLayout.WEST);
	formEntryPanel.add(formEntryScrollPane, BorderLayout.CENTER);
	
	fieldHelp = new JEditorPane();  
	fieldHelp.setEditable(false);
	fieldHelp.setContentType("text/html");
	scrollPaneFieldHelp = new JScrollPane(fieldHelp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	imageNavigation = new JPanel();
	
	left = new JTabbedPane();
	left.addTab("Table Entry", scrollPaneTable);
	left.addTab("Form Entry", formEntry);
	
	right = new JTabbedPane();
	right.addTab("Field Help", scrollPaneFieldHelp);
	right.addTab("Image Navigation", imageNavigation);
	
	bottom.setRightComponent(right);
	bottom.setLeftComponent(left);
	bottom.setDividerLocation(0.5);
}

public DefaultTableModel setupTables(){
		String[] columns = new String[Client.cc.getResult().getFields().size() + 1];
		columns[0] = "Record Number";
		ArrayList<FieldModels> fields = Client.cc.getResult().getFields();
		int fieldCounter = 1;
		for(FieldModels field : fields){
			columns[fieldCounter] = field.getTitle();
			fieldCounter++;
		}
		Object[][] data = new Object[Client.cc.getResult().getProject().getRecordsPerImage()][Client.cc.getResult().getFields().size()];
		for (int i = 0; i < Client.cc.getResult().getProject().getRecordsPerImage(); i++) {
			for (int j = 0; j < Client.cc.getResult().getFields().size(); j++) {
				if(j == 0){
					data[i][j] = Integer.toString(i+1);
				}  
//				data[i][j] = Client.batch.getCellData(i, j);
				else{
					data[i][j] = "";
				}
			}
		}
		return new DefaultTableModel(data, columns) {
			@Override
			public boolean isCellEditable(int row, int column){
				if(column == 0){
					return false;
				}
				else{
					return true;	
				}
			}
		};
	}
	
public void resetScreenBasedOnUser(){
	setSize(new Dimension(3000,100));
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	setBounds(0, 0, 2000, 1000);
	setResizable(true);
//	setPreferredSize(new Dimension(1000, 1000));
	setLocation(225, 0); 
	bottom.setDividerLocation(0.5);
	mainSplitPane.setDividerLocation(0.5);
	}

public void userAlreadyHasBatch(boolean Switch){
	downloadBatch.setEnabled(!Switch);
}

public void toolbarButtonsSwitch(boolean bool){
	zoomIn.setEnabled(bool);
	zoomOut.setEnabled(bool);
	invertImage.setEnabled(bool);
	toggleHighlights.setEnabled(bool);
	save.setEnabled(bool);
	submit.setEnabled(bool);
}

public boolean isToggled(){
	return highlightsON;
}

public void addTable(){
	left.removeAll();
	tm = setupTables();
	tm.addTableModelListener(tmListener);
	table = new JTable(tm);
	table.getTableHeader().setReorderingAllowed(false);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	table.setCellSelectionEnabled(true);
	table.addMouseListener(mouseAdapter);
	table.addKeyListener(keyListener);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	
	formEntry = new JPanel();
	formEntry.setLayout(new BoxLayout(formEntry, BoxLayout.Y_AXIS));
	textList = new ArrayList<>();
	ArrayList<FieldModels> fields = Client.cc.getResult().getFields();
	for (FieldModels field : fields) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(RIGHT_ALIGNMENT);
		JLabel label = new JLabel(field.getTitle());
		JTextField textfield = new JTextField();
		textfield.addFocusListener(textListFocusListener);
		textList.add(textfield);
		textfield.setMaximumSize(new Dimension(150, textfield.getPreferredSize().height));
		box.add(label);
		box.add(Box.createRigidArea(new Dimension(30, 0)));
		box.add(textfield);
		formEntry.add(box);
		formEntry.add(Box.createRigidArea(new Dimension(0, 5)));
	}
	String[] list = new String[Client.cc.getResult().getProject().getRecordsPerImage()];
	for (int i = 0; i < Client.cc.getResult().getRecordsPerImage(); i++) {
		list[i] = new String(i+1 + "             ");
	}
	jl = new JList<>(list);
	jl.addMouseListener(mouseListener);
	jListScrollPane = new JScrollPane(jl);
	formEntryPanel = new JPanel(new BorderLayout());
	formEntryScrollPane = new JScrollPane(formEntry);
	jListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	formEntryPanel.add(jListScrollPane, BorderLayout.WEST);
	formEntryPanel.add(formEntryScrollPane, BorderLayout.CENTER);
	
	scrollPaneTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	left.addTab("Table Entry", scrollPaneTable);
	left.addTab("Form Entry", formEntryPanel);
	left.addChangeListener(new ChangeListener() {
		
		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			textList.get(Client.batch.getSelectedColumn()).requestFocus();
			int row = Client.batch.getSelectedRow();
			for (int i = 0; i < textList.size(); i++) {
				String t = (String) table.getValueAt(row, i+1);
				textList.get(i).setText(t);
			}
			
		}
	});
	}

public void changeTableSelection(int row, int column){
	table.changeSelection(row, column+1, false, false);
	jl.setSelectedIndex(row);
	textList.get(column).requestFocus();
}

private TableModelListener tmListener = new TableModelListener() {
	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		int column = e.getColumn();
		int row = e.getLastRow();
		textList.get(column-1).setText((String) table.getValueAt(row, column));
		System.out.println(table.getValueAt(row, column));
		
	}
};

private FocusListener textListFocusListener = new FocusListener() {
	
	@Override
	public void focusLost(FocusEvent e) {
		// TODO update table with values from text field
		int row = jl.getSelectedIndex();
		int column = textList.indexOf(e.getSource());
		table.setValueAt(textList.get(column).getText(), row, column+1);
		
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		// TODO change image and table spots
		int row = jl.getSelectedIndex();
		int column = textList.indexOf(e.getSource());
		int x = Client.cc.getResult().getFields().get(column).getxCoord();
		int y = Client.cc.getResult().getProject().getFirstYCoordinate() + (Client.cc.getResult().getProject().getRecordHeight() * row);
		int width = Client.cc.getResult().getFields().get(column).getWidth();
		int height = Client.cc.getResult().getProject().getRecordHeight();
		changeTableSelection(row, column);
		if(highlightsON == true){
			drawing.deleteRectangle();
			drawing.drawRectangle(x, y, width, height);
		}
		updateBatchHighlights(row, column, x, y, height, width);
		try {
			updateFieldHelp(column);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		Component c = e.getComponent();  //correct component
		
	}
};

private KeyListener keyListener = new KeyListener() {
	
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT){
			int row  = table.getSelectedRow();
			int column = table.getSelectedColumn();
			if (row >= 0 && column >=1) {
				jl.setSelectedIndex(row);
				int ySpot = Client.cc.getResult().getProject().getFirstYCoordinate() + (Client.cc.getResult().getProject().getRecordHeight() * row);
				int xSpot = Client.cc.getResult().getFields().get(0).getxCoord();
				for (int i = 0; i < column-1; i++) {
					xSpot += Client.cc.getResult().getFields().get(i).getWidth();
				}
				if (highlightsON == true) {
					drawing.deleteRectangle();
					drawing.drawRectangle(xSpot, ySpot, Client.cc.getResult().getFields().get(column-1).getWidth(), Client.cc.getResult().getProject().getRecordHeight());  
					drawing.setScale(drawing.getScale());
				}
				updateBatchHighlights(row, column-1, xSpot, ySpot, Client.cc.getResult().getProject().getRecordHeight(), Client.cc.getResult().getFields().get(column-1).getWidth());
				try {
					updateFieldHelp(column-1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
};

private MouseListener mouseListener = new MouseAdapter() {
	@Override
	public void mousePressed(MouseEvent e){
		int row = jl.getSelectedIndex();
		int column = 0;
		for (JTextField current: textList){
			if (current.hasFocus()) {
				break;
			}
			else{
				column++;
			}
		}
		
		for (int i = 0; i < textList.size(); i++) {
			String t = (String) table.getValueAt(row, i+1);
			textList.get(i).setText(t);
		}
//		for(JTextField current : textList){
////			current.setText(table.getValueAt(row, column).toString());
//			System.out.println(table.getValueAt(row, column));
//		}
		if(column >= textList.size()){
			column = 0;
		}
		changeTableSelection(row, column);
		int ySpot = Client.cc.getResult().getProject().getFirstYCoordinate() + (Client.cc.getResult().getProject().getRecordHeight() * row);
		int xSpot = Client.cc.getResult().getFields().get(0).getxCoord();
		for (int i = 0; i < column; i++) {
			xSpot += Client.cc.getResult().getFields().get(i).getWidth();
		}
		if(highlightsON == true){
			drawing.deleteRectangle();
			drawing.drawRectangle(xSpot, ySpot, Client.cc.getResult().getFields().get(column).getWidth(), Client.cc.getResult().getProject().getRecordHeight());
		}
		updateBatchHighlights(row, column, xSpot, ySpot, Client.cc.getResult().getProject().getRecordHeight(), Client.cc.getResult().getFields().get(column-1).getWidth());
	}
};

private MouseAdapter mouseAdapter = new MouseAdapter() {
	
	@Override
	public void mousePressed(MouseEvent e){
		int row = table.rowAtPoint(e.getPoint());
		int column = table.columnAtPoint(e.getPoint());
		if (row >= 0 && column >=1) {
			jl.setSelectedIndex(row);
			int ySpot = Client.cc.getResult().getProject().getFirstYCoordinate() + (Client.cc.getResult().getProject().getRecordHeight() * row);
			int xSpot = Client.cc.getResult().getFields().get(0).getxCoord();
			for (int i = 0; i < column-1; i++) {
				xSpot += Client.cc.getResult().getFields().get(i).getWidth();
			}
			if (highlightsON==true) {
				drawing.deleteRectangle();
				drawing.drawRectangle(xSpot, ySpot, Client.cc.getResult().getFields().get(column-1).getWidth(), Client.cc.getResult().getProject().getRecordHeight());  
				drawing.setScale(drawing.getScale());
			}
			updateBatchHighlights(row, column-1, xSpot, ySpot, Client.cc.getResult().getProject().getRecordHeight(), Client.cc.getResult().getFields().get(column-1).getWidth());
			try {
				updateFieldHelp(column-1);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
};

public void updateBatchHighlights(int row, int column, int xSpot, int ySpot, int height, int width){
	Client.batch.setSelectedRow(row);
	Client.batch.setSelectedColumn(column);
	Client.batch.setHighlightX(xSpot);
	Client.batch.setHighlightY(ySpot);
	Client.batch.setHighlightHeight(height);
	Client.batch.setHighlightWidth(width);
}


public void updateFieldHelp(int column) throws IOException{
	right.removeAll();
	fieldHelp = new JEditorPane();
	fieldHelp.setEditable(false);
	fieldHelp.setContentType("text/html");
	fieldHelp.setPage("http://" + Client.cc.getHost() + ":" + Client.cc.getPortString() + "/" + Client.cc.getResult().getFields().get(column).getHelpHTML());
	scrollPaneFieldHelp = new JScrollPane(fieldHelp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	right.addTab("Field Help", scrollPaneFieldHelp);
	right.addTab("Image Navigation", imageNavigation);
}


public ArrayList<ArrayList<String>> getTableValues(){
	ArrayList<ArrayList<String>> tableToReturn = new ArrayList<>();
	for (int i = 0; i < table.getRowCount(); i++) {
		ArrayList<String> row = new ArrayList<>();
		for (int j = 0; j < table.getColumnCount(); j++) {
			if(j==0){
				
			}
			else{
				String string = (String) table.getValueAt(i, j);
				if (string==null) {
					string = "";
				}
				row.add(string);
			}
		}
		tableToReturn.add(row);
	}
	return tableToReturn;
}

}