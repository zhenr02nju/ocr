package com.wxct.cxzx.ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.util.OS;

import com.wxct.cxzx.utils.PropertiesHandle;

public class OCR {
    
	@SuppressWarnings("unused")
	private final String LANG_OPTION = "-l"; // 英文字母小写l，并非数字1
    private final String EOL = System.getProperty("line.separator");
    private String tessPath;//Tesseract安装路径
    
    public OCR() {
    	//tessPath=OCR.class.getResource("/").toString().replace("file:/", "")+"Tesseract-OCR";
    	
    	PropertiesHandle propertiesHandle=new PropertiesHandle();    	    	
    	try {
			tessPath=propertiesHandle.readProperties("tess_path","tesseract_path.properties",false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }

    public String recognizeText(File imageFile, String imageFormat) throws InterruptedException, IOException {
        File tempImage = ImageIOHelper.createImage(imageFile, imageFormat);
        File outputFile = new File(imageFile.getParentFile(), "output");
        StringBuffer strB = new StringBuffer();
        List<String> cmd = new ArrayList<String>();
        if (OS.isWindowsXP()) {
            cmd.add(tessPath + "//tesseract");
        } else if (OS.isLinux()) {
            cmd.add("tesseract");
        } else {
            cmd.add(tessPath + "//tesseract");
        }
        cmd.add("");
        cmd.add(outputFile.getName());

        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(imageFile.getParentFile());

        cmd.set(1, tempImage.getName());
        pb.command(cmd);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        int w = process.waitFor();

        // 删除临时正在工作文件
        tempImage.delete();

        if (w == 0) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(outputFile.getAbsolutePath() + ".txt"),
                    "UTF-8"));

            String str;
            while ((str = in.readLine()) != null) {
                strB.append(str).append(EOL);
            }
            in.close();
        } else {
            
			@SuppressWarnings("unused")
			String msg;
            switch (w) {
            case 1:
                msg = "Errors accessing files.There may be spaces in your image's filename.";
                break;
            case 29:
                msg = "Cannot recongnize the image or its selected region.";
                break;
            case 31:
                msg = "Unsupported image format.";
                break;
            default:
                msg = "Errors occurred.";
            }
            tempImage.delete();
        }
        new File(outputFile.getAbsolutePath() + ".txt").delete();
        return strB.toString();
    }
}
