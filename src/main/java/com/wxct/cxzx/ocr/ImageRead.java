package com.wxct.cxzx.ocr;

import java.awt.image.BufferedImage; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 

public class ImageRead { 

	public static String read(BufferedImage bi,String file){ 
		//ImageFilter imgFliter = new ImageFilter(bi);//进行图像处理，实际上不处理反而可以读出
		//BufferedImage ss = imgFliter.changeGrey();//进行图像处理，实际上不处理反而可以读出
		BufferedImage ss = bi; 		 
		File xx = ImageIOHelper.createImage(ss); 		 
		try{ 
		    FileInputStream input = new FileInputStream(xx);
		    File picFile=new File(file);
			FileOutputStream output = new FileOutputStream(picFile);// 把扩展名添加到原来文件的后面 
			int in = input.read(); 
			while (in != -1) { 
				output.write(in); 
				in = input.read(); 
			} 
			input.close(); 
			output.close(); 
			OCR ocr = new OCR(); 
			String rlt = ocr.recognizeText(xx, "tiff"); 
	        StringBuffer str = new StringBuffer(); 
	        for(int i=0;i<rlt.length();i++){ 
		        String s = rlt.substring(i,i+1); 
		        try{ 
		           int t = Integer.valueOf(s); 
		           str.append(t); 
		        }catch (Exception e) {
		        	e.printStackTrace();
		        } 
	        } 
	        return str.toString(); 
		} catch (Exception e) { 
		    e.printStackTrace(); 
		    return null; 
		} 
	} 
} 