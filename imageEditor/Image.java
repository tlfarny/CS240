package imageEditor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Image {
	Pixel[][] pixels;
	int height, width;
	
	public Image(String input){
		try {
			Scanner s = new Scanner(new BufferedInputStream(new FileInputStream(input)));
			s.useDelimiter("(\\s+|#[^\\n]*\\n)+");
			s.next();								//removes "P3"
			width = s.nextInt();
			height = s.nextInt();
			s.next();								//We know max color value to be 255 already
			pixels = new Pixel[width][height];
			int row = 0, col = 0;
			int red = 0, green = 0, blue = 0;
			while(s.hasNext()){
				red = s.nextInt();
				green = s.nextInt();
				blue = s.nextInt();
				pixels[col][row] = new Pixel(red, green, blue);
				col++;
				if(col >= width){
					col = 0;
					row++;
				}
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void invert(){
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				pixels[j][i].setRed(255-pixels[j][i].getRed());
				pixels[j][i].setGreen(255-pixels[j][i].getGreen());
				pixels[j][i].setBlue(255-pixels[j][i].getBlue());
			}
		}
	}
	
	public void grayscale(){
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int colorsTotal = 0;
				colorsTotal += pixels[j][i].getRed();
				colorsTotal += pixels[j][i].getGreen();
				colorsTotal += pixels[j][i].getBlue();
				pixels[j][i].setRed(colorsTotal/3);
				pixels[j][i].setGreen(colorsTotal/3);
				pixels[j][i].setBlue(colorsTotal/3);
			}
		}
	}
	
	public void emboss(){
		int redDiff, greenDiff, blueDiff, maxDiff;
		for (int i = height-1; i > 0; i--) {
			
		}
		
	}
	
	public void motionBlur(int length){
		
	}
}
