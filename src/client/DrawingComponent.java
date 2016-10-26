package client;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import modelClasses.FieldModels;

import java.util.*;
import java.io.*;
import java.net.URL;


@SuppressWarnings("serial")
public class DrawingComponent extends JComponent {

//	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	
	private int w_translateX;
	private int w_translateY;
	private double scale;
	
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartTranslateX;
	private int w_dragStartTranslateY;
	private AffineTransform dragTransform;

	private ArrayList<DrawingShape> shapes;
	
	public DrawingComponent() {
		w_translateX = 0;
		w_translateY = 0;
		scale = 1.0;
		
		initDrag();

		shapes = new ArrayList<DrawingShape>();
		
		this.setBackground(new Color(15, 15, 15));
		this.setPreferredSize(new Dimension(700, 700));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(1000, 1000));
		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addComponentListener(componentAdapter);
		this.addMouseWheelListener(mouseAdapter); 
	}
	
	public void drawRectangle(int x, int y, int width, int height){
		shapes.add(new DrawingRect(new Rectangle2D.Double(x, y, width, height), new Color(0, 247, 255, 192)));
		Client.batch.setHighlightX(x);
		Client.batch.setHighlightY(y);
		repaint();
	}
	
	public void deleteRectangle(){
		shapes.remove(shapes.size()-1);
		repaint();
	}
	
	public void clearAllShapes(){
		shapes.clear();
		repaint();
	}
	
	public void addShape(String string) throws IOException{
		URL url = new URL(string);
		Image image = ImageIO.read(url);
		shapes.add(new DrawingImage(image, new Rectangle2D.Double(0, 0, image.getWidth(null), image.getHeight(null))));
	}
	
	private void initDrag() {
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartTranslateX = 0;
		w_dragStartTranslateY = 0;
		dragTransform = null;
	}
	
//	private Image loadImage(String imageFile) {
//		try {
//			return ImageIO.read(new File(imageFile));
//		}
//		catch (IOException e) {
//			return NULL_IMAGE;
//		}
//	}
	
	public void setScale(double newScale) {
		if (newScale > 5) {
			newScale = 5;
		}
		else if (newScale < 0.25) {
			newScale = 0.25;
		}
		scale = newScale;
		this.repaint();
		// TODO Auto-generated method stub
		//Save scale to Batch state.
	}
	
	public void setTranslation(int w_newTranslateX, int w_newTranslateY) {
		w_translateX = w_newTranslateX;
		w_translateY = w_newTranslateY;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);
		
		g2.translate(this.getWidth()/2, this.getHeight()/2);
		g2.scale(scale, scale);
		g2.translate(-this.getWidth()/2, -this.getHeight()/2);
		g2.translate(w_translateX, w_translateY);

		drawShapes(g2);
	}
	
	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		for (DrawingShape shape : shapes) {
			shape.draw(g2);
		}
	}
	
	public void invert(){
		shapes.get(0).invert();
		Color background = getBackground();
		int red = 255 - background.getRed();
		int green = 255 - background.getGreen();
		int blue = 255 - background.getBlue();
		this.setBackground(new Color(red, green, blue));
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();		//these are the points within the drawing frame that are clicked
			int d_Y = e.getY();
			
			
			AffineTransform transform = new AffineTransform();
			transform.translate(getWidth()/2, getHeight()/2);
			transform.scale(scale, scale);
			transform.translate(-getWidth()/2, -getHeight()/2);
			transform.translate(w_translateX, w_translateY);
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();		//these are the points of the image that are clicked
			int w_Y = (int)w_Pt.getY();
			
			boolean hitShape = false;
			
			Graphics2D g2 = (Graphics2D)getGraphics();
			for (DrawingShape shape : shapes) {
				if (shape.contains(g2, w_X, w_Y)) {
					hitShape = true;
					break;
				}
			}
			if(hitShape == true && Client.Main.isToggled() == true){
				int xSpot = -1;
				int ySpot = -1;
				int column = 0;
				int row = 0;
				int firstYCoord = Client.cc.getResult().getProject().getFirstYCoordinate();
				int firstXCoord = Client.cc.getResult().getFields().get(0).getxCoord();
				int heightOfRow = Client.cc.getResult().getProject().getRecordHeight();
				int rows = Client.cc.getResult().getRecordsPerImage();
				ArrayList<FieldModels> fields = Client.cc.getResult().getFields();
				int runningWidth = firstXCoord;
				for(FieldModels field : fields){
					if(w_X < firstXCoord){
						//do nothing
					}
					else if(w_X > runningWidth + field.getWidth()){
						runningWidth += field.getWidth();
						column++;
					}
					else{
						xSpot = runningWidth;
						break;
					}
				}
				for (int i = 0; i < rows; i++) {
					if(w_Y > (rows * heightOfRow) + firstYCoord){
						ySpot = -1;
					}
					else if(w_Y > (i*heightOfRow)+firstYCoord){
						ySpot = (i*heightOfRow) + firstYCoord;
						row++;
					}
				}
				if(xSpot >= 0 && ySpot >= 0){
					deleteRectangle();
					drawRectangle(xSpot, ySpot, fields.get(column).getWidth(), heightOfRow);
					Client.Main.updateBatchHighlights(row-1, column, xSpot, ySpot, heightOfRow, fields.get(column).getWidth());
//					Client.batch.setSelectedColumn(column);
//					Client.batch.setSelectedRow(row-1);
//					Client.batch.setHighlightX(xSpot);
//					Client.batch.setHighlightY(ySpot);
					Client.Main.changeTableSelection(row-1, column);
					repaint();
				}
				System.out.println(row + " " + column);
				
			}
			
			dragging = true;		
			w_dragStartX = w_X;
			w_dragStartY = w_Y;		
			w_dragStartTranslateX = w_translateX;
			w_dragStartTranslateY = w_translateY;
			dragTransform = transform;
		}

		@Override
		public void mouseDragged(MouseEvent e) {		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					dragTransform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
				
				w_translateX = w_dragStartTranslateX + w_deltaX;
				w_translateY = w_dragStartTranslateY + w_deltaY;
				
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			setScale(scale + e.getWheelRotation()*0.05);
		}	
	};
	
	private ComponentAdapter componentAdapter = new ComponentAdapter() {

		@Override
		public void componentHidden(ComponentEvent e) {
			return;
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			return;
		}

		@Override
		public void componentShown(ComponentEvent e) {
			return;
		}	
	};

	
	///////////////////
	// Drawing Shape //
	///////////////////
	
	
	interface DrawingShape {
		boolean contains(Graphics2D g2, double x, double y);
		void draw(Graphics2D g2);
		Rectangle2D getBounds(Graphics2D g2);
		void invert();
	}


	class DrawingRect implements DrawingShape {

		private Rectangle2D rect;
		private Color color;
		
		public DrawingRect(Rectangle2D rect, Color color) {
			this.rect = rect;
			this.color = color;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(rect);
		}
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}

		@Override
		public void invert() {
			// TODO Auto-generated method stub
			
		}
	}


	class DrawingImage implements DrawingShape {

		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}
		
		public Image getImage(){
			return this.image;
		}
		
		public void setImage(Image image){
			this.image = image;
		}

		@Override
		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}

		@Override
		public void draw(Graphics2D g2) {
			g2.drawImage(image, (int)rect.getMinX(), (int)rect.getMinY(), (int)rect.getMaxX(), (int)rect.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}	
		
		@Override
		public Rectangle2D getBounds(Graphics2D g2) {
			return rect.getBounds2D();
		}

		@Override
		public void invert() {
			RescaleOp op = new RescaleOp(-1.0f, 255f, null);
			this.image = op.filter((BufferedImage)this.image, null);
		}
	}	
	
	public double getScale(){
		return scale;
	}
}



