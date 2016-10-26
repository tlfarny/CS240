package client;

//import java.awt.Image;

import communicationsClasses.DownloadBatchResult;

public class BatchState {
	
	private int zoomLevel;
	private int scrollPosition;
	private boolean highlightsOn;
	private boolean imageInverted;
	private int windowSizeHeight;
	private int windowSizeWidth;
	private int windowPositionX;
	private int windowPositionY;
	private int horizontalSplitPosition;
	private int verticalSplitPosition;
//	private Image batchImage;  //Will need to be edited for persistent state
	private int selectedRow = -1;
	private int selectedColumn = -1;
	private int highlightX = -1;
	private int highlightY = -1;
	private int highlightWidth;
	private int highlightHeight;
	private DownloadBatchResult downloadBatchResult;  //This may need to be chopped down to parts
	private String[][] table;
	
	public int getHighlightHeight() {
		return highlightHeight;
	}

	public void setHighlightHeight(int highlightHeight) {
		this.highlightHeight = highlightHeight;
	}

	public int getHighlightWidth() {
		return highlightWidth;
	}

	public void setHighlightWidth(int highlightWidth) {
		this.highlightWidth = highlightWidth;
	}
	
	public String[][] getTable() {
		return table;
	}

	public void setTable(String[][] table) {
		this.table = table;
	}

	public int getHighlightX() {
		return highlightX;
	}

	public void setHighlightX(int highlightX) {
		this.highlightX = highlightX;
	}

	public int getHighlightY() {
		return highlightY;
	}

	public void setHighlightY(int highlightY) {
		this.highlightY = highlightY;
	}

	public int getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	public int getSelectedColumn() {
		return selectedColumn;
	}

	public void setSelectedColumn(int selectedColumn) {
		this.selectedColumn = selectedColumn;
	}
	
	public String getCellData(int row, int column){
		return table[row][column];
	}
	
	public int getZoomLevel() {
		return zoomLevel;
	}
	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	public int getScrollPosition() {
		return scrollPosition;
	}
	public void setScrollPosition(int scrollPosition) {
		this.scrollPosition = scrollPosition;
	}
	public boolean isHighlightsOn() {
		return highlightsOn;
	}
	public void setHighlightsOn(boolean highlightsOn) {
		this.highlightsOn = highlightsOn;
	}
	public boolean isImageInverted() {
		return imageInverted;
	}
	public void setImageInverted(boolean imageInverted) {
		this.imageInverted = imageInverted;
	}
	public int getWindowSizeHeight() {
		return windowSizeHeight;
	}
	public void setWindowSizeHeight(int windowSizeHeight) {
		this.windowSizeHeight = windowSizeHeight;
	}
	public int getWindowSizeWidth() {
		return windowSizeWidth;
	}
	public void setWindowSizeWidth(int windowSizeWidth) {
		this.windowSizeWidth = windowSizeWidth;
	}
	public int getWindowPositionX() {
		return windowPositionX;
	}
	public void setWindowPositionX(int windowPositionX) {
		this.windowPositionX = windowPositionX;
	}
	public int getWindowPositionY() {
		return windowPositionY;
	}
	public void setWindowPositionY(int windowPositionY) {
		this.windowPositionY = windowPositionY;
	}
	public int getHorizontalSplitPosition() {
		return horizontalSplitPosition;
	}
	public void setHorizontalSplitPosition(int horizontalSplitPosition) {
		this.horizontalSplitPosition = horizontalSplitPosition;
	}
	public int getVerticalSplitPosition() {
		return verticalSplitPosition;
	}
	public void setVerticalSplitPosition(int verticalSplitPosition) {
		this.verticalSplitPosition = verticalSplitPosition;
	}
//	public Image getBatchImage() {
//		return batchImage;
//	}
//	public void setBatchImage(Image batchImage) {
//		this.batchImage = batchImage;
//	}
	public DownloadBatchResult getDownloadBatchResult() {
		return downloadBatchResult;
	}
	public void setDownloadBatchResult(DownloadBatchResult downloadBatchResult) {
		this.downloadBatchResult = downloadBatchResult;
	}

}
