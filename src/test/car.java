package test;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import com.pearsoneduc.ip.io.*;
import com.pearsoneduc.ip.gui.*;
import com.pearsoneduc.ip.op.OperationException;

public class car extends ImageSelector {
	
	static BufferedImage I0;
	static BufferedImage I1;

	  public car(String imageFile)
	   throws IOException, ImageDecoderException, OperationException {
	   super(imageFile);
	  }


	  // Checks that we have a greyscale image

	  public boolean imageOK() {
	    return getSourceImage().getType() == BufferedImage.TYPE_3BYTE_BGR;
	  }


	  //Subtracts the two images

	 public static BufferedImage scaling (BufferedImage image0, BufferedImage image1) {
	  int w, h;
	  w = image0.getWidth();
	  h = image0.getHeight();
	  BufferedImage scaledImage = new BufferedImage (w, h, image0.getType());
	  
	    int argb0, argb1, a0,r0,b0,g0, a1,r1,b1,g1, aDiff,rDiff, gDiff, bDiff;
	    int rgb, r, g, b, gray, grayLevel, rgb1,rgb2, Yellow, Yellow1;
	    
	    Vector<Integer> L = new Vector<Integer>();
	    Vector<Integer> H = new Vector<Integer>();
	   
	    //subtracting the two color images--------------------------------------------------
	    for (int i=0;i<h;i++) {
	        for (int j=0;j<w;j++) {
	        	    
		           argb0 = image0.getRGB(j, i);
		           argb1 = image1.getRGB(j, i);
		           
		           a0 = (argb0 >> 24) & 0xFF;
		           r0 = (argb0 >> 16) & 0xFF;
		           g0 = (argb0 >>  8) & 0xFF;
		           b0 = (argb0      ) & 0xFF;

		           a1 = (argb1 >> 24) & 0xFF;
		           r1 = (argb1 >> 16) & 0xFF;
		           g1 = (argb1 >>  8) & 0xFF;
		           b1 = (argb1      ) & 0xFF;

		           aDiff = a1;
		           rDiff = Math.abs(r1 - r0);
		           gDiff = Math.abs(g1 - g0);
		           bDiff = Math.abs(b1 - b0);

		           int diff = (aDiff << 24) | (rDiff << 16) | (gDiff << 8) | bDiff;
		           
	            scaledImage.setRGB(j, i, diff);
	        }
	    }
	    
	    // making the subtracted image black and white ----------------------------
	    for (int i=0;i<h;i++) {
	        for (int j=0;j<w;j++) { 
	    rgb = scaledImage.getRGB(j, i);
	    a0 = (rgb >> 24) & 0xFF;
        r = (rgb >> 16) & 0xFF;
        g = (rgb >> 8) & 0xFF;
        b = (rgb & 0xFF);

        grayLevel = (r + g + b) / 3;
        gray =(a0<<24)+ (grayLevel << 16) + (grayLevel << 8) + grayLevel; 
        
        scaledImage.setRGB(j, i, gray);
	        }
	    }
	    
	    //Detecting L1, L2------------------------------------------------------------
	    
	    for (int i=0;i<h;i++) {
	        for (int j=0;j<(w/2);j++) { 
	    rgb = scaledImage.getRGB(j, i);
	    a0 = (rgb >> 24) & 0xFF;
        r = (rgb >> 16) & 0xFF;
        g = (rgb >> 8) & 0xFF;
        b = (rgb & 0xFF);
        
        if(b>100)
        	L.add(j);
        
	        }
	    }
	    
	    Object L1 = Collections.max(L);
	    Object L2= Collections.min(L);
	    System.out.println(L1 + " " + L2);
	    int startf = (Integer) L1;
	    int startl = (Integer) L2;
	    int startLine = (int)((startf+startl)/2);
	    
	    
	    //Detecting L3, L4---------------------------------------------------------------
	    
	    for (int i=0;i<h;i++) {
	        for (int j=(w/2);j<w;j++) { 
	    rgb = scaledImage.getRGB(j, i);
	    a0 = (rgb >> 24) & 0xFF;
        r = (rgb >> 16) & 0xFF;
        g = (rgb >> 8) & 0xFF;
        b = (rgb & 0xFF);
        
        if(b>100)
        	H.add(j);
        
	        }
	    }
	    
	    Object L3 = Collections.max(H);
	    Object L4= Collections.min(H);
	    System.out.println(L3 + " " + L4);
	    int endf = (Integer) L3;
	    int endl = (Integer) L4;
	    int endLine = (int)((endf+endl)/2);
	    
	    //Highlighting the Start and End Lines---------------------------------------------------------
	    for (int i=0;i<h;i++) {
	        
	    	 rgb1 = scaledImage.getRGB(startLine, i);
	    	 rgb2 = scaledImage.getRGB(endLine, i);
	 	     a0 = (rgb1 >> 24) & 0xFF;
	 	     a1 = (rgb2 >> 24) & 0xFF;
	 	     
	         Yellow =(a0<<24)+ (255 << 16) + (255 << 8) + 0; 
	         Yellow1 =(a1<<24)+ (255 << 16) + (255 << 8) + 0;
	         
	         scaledImage.setRGB(startLine, i, Yellow);
	         scaledImage.setRGB(startLine+1, i, Yellow);
	         scaledImage.setRGB(startLine-1, i, Yellow);
	         
	         scaledImage.setRGB(endLine, i, Yellow1);
	         scaledImage.setRGB(endLine+1, i, Yellow1);
	         scaledImage.setRGB(endLine-1, i, Yellow1);
	    	
	        }
	    
	    //Calculating Speed of the car in mph and kph
	    float speed = (float) ((Math.abs(endLine-startLine) * 22.5 * 360000)/(9 * 5.4429 * 1920 * 1000));
	    System.out.println("Speed of the Car is " + speed * 0.621371 + " mph OR " + speed + " kph");
	        
	    return scaledImage;
	  
  }


	  // Creates versions of the image simulating quantisation with fewer bits

	  public Vector generateImages() {

	    Vector quantisation = new Vector();
	  
	    BufferedImage img0 = I0;
	    quantisation.addElement("0");
	    addImage("0", new ImageIcon(img0));
	    
	    BufferedImage img1 = I1;
	    quantisation.addElement("1");
	    addImage("1", new ImageIcon(img1));
	    
	    quantisation.addElement("diff");
	    addImage("diff", new ImageIcon(scaling(img0,img1)));
	    
	    return quantisation;

	  }


	  public static void main(String[] argv) {
		  
		  int width = 960;
		  int height = 545;
		  
		  File f1 = null;
		  File f2 = null;
		  
		  	  
	    if (argv.length > 0) {
	      try {
	    	    //Taking both images as command line inputs  	  
	    	  f1 = new File(argv[0]);
	    	  I0 = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	    	  I0 = ImageIO.read(f1);
	    	  System.out.println("Reading 0 complete");
	    	  f2 = new File(argv[1]);
	    	  I1 = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	    	  I1 = ImageIO.read(f2);
	    	  System.out.println("Reading 1 complete");
	    	  
	    	  JFrame frame = new car(argv[0]);
		      frame.pack();
		      frame.setVisible(true);
	        	        
	     
	      }
	      catch (Exception e) {
		System.err.println(e);
		System.exit(1);
	      }
	    }
	    else {
	      System.err.println("java car <imagefile>");
	      System.exit(1);
	    }
	    
	   	  
	    
	  }

}
