package imageEditor;

import java.io.IOException;

public class ImageEditor {
	static Image image;
	
	public ImageEditor(String input){
		image = new Image(input);
	}
	
	public void commandChooser(String command, Image image, String motionBlur){
		if(command.equals("invert")){
			image.invert();
		}
		else if (command.equals("grayscale")) {
			image.grayscale();
		}
		else if (command.equals("emboss")) {
			image.grayscale();
		}
		else if (command.equals("motionblur")) {
			int blurLength = Integer.parseInt(motionBlur);
			image.motionBlur(blurLength);
		}
	}
	
	public void outputImage(){
		
	}
	
	public static void main(String[] args) throws IOException{
		String input = args[0];
		String output = args[1];
		String command = args[2];
		String motionBlur = "";
		if(args.length==4){
			motionBlur = args[3];
		}
		ImageEditor ie  = new ImageEditor(input);
		ie.commandChooser(command, image, motionBlur);
	}
}
