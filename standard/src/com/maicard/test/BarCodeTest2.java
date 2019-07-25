package com.maicard.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;


/**
 * 使用barcode4j
 * 条形码测试
 *
 *
 * @author NetSnake
 * @date 2015年10月25日
 *
 */



public class BarCodeTest2 {

	public static void main(String[] argv){
		try {
            //Create the barcode bean
            EAN13Bean bean = new EAN13Bean();
            final int dpi = 300;
            // bean.setModuleWidth(1);
            //Configure the barcode generator
           // bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
           // bean.se                                //width exactly one pixel
          // bean.setW(3);
          // bean.doQuietZone(false);
             
            //Open output file
            File outputFile = new File("d:\\out.png");
            OutputStream out = new FileOutputStream(outputFile);
            try {
                //Set up the canvas provider for monochrome JPEG output 
                BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                        out, "image/png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
             
                //Generate the barcode
                bean.generateBarcode(canvas, "779630187522");
             
                //Signal end of generation
                canvas.finish();
            } finally {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
}