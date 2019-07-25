package com.maicard.captcha.service.impl.generator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.patchca.background.BackgroundFactory;
import org.patchca.background.SingleColorBackgroundFactory;
import org.patchca.color.ColorFactory;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.FilterFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.text.renderer.BestFitTextRenderer;
import org.patchca.text.renderer.TextRenderer;
import org.patchca.word.AdaptiveRandomWordFactory;
import org.springframework.stereotype.Service;

import com.maicard.captcha.criteria.CaptchaCriteria;
import com.maicard.captcha.domain.Captcha;
import com.maicard.captcha.service.Generator;
import com.maicard.common.base.BaseService;

/**
 * 使用org.patchca生成验证码
 * 
 * 2017.10.6
 * centos 7.3，nginx-1.10.2-1.el7.x86_64工作正常（与fontconfig-2.10.95-10.el7.x86_64配合）
 * 但是nginx-1.10.2-2.el7.x86_64会与fontconfig-2.10.95-11.el7.x86_64配合，而这个版本的fontconfig会安装一个lyx-fonts，而安装了lyx-fonts后，patcha无法正确的渲染字体，而是变成了类似象形图画，比如Windows下的某些Webding或symbol的字体显示。
 * 强行给RandomFontFactory指定字体也不行，解决方法是强行将lyx-fonts移除并重启tomcat。
 * 
 * @author NetSnake
 *
 */
@Service
public class PatchcaOrgGeneratorImpl extends BaseService implements Generator {

	private final String defaultCharacters = "ABEFHKPRSWXY34689";
	private final int defaultMaxLength = 8;
	private final int defaultMinLength = 6;
	private final int defaultImageWidth = 180;
	private final int defaultImageHeight = 80;
	private final int defaultForeColor = new Color(0,0,0).getRGB();
	
	private final String DEFAULT_IMG_TYPE = "png";

	@Override
	public Captcha generate(CaptchaCriteria captchaCriteria) {
		if(captchaCriteria == null){
			captchaCriteria = new CaptchaCriteria();
		}
		correctCriteriaValue(captchaCriteria);
		BufferedImage bufImage = new BufferedImage(captchaCriteria.getImageWidth(), captchaCriteria.getImageHeight(), BufferedImage.TYPE_INT_ARGB);
		/*Graphics2D g = bufImage.createGraphics();
		bufImage = g.getDeviceConfiguration().createCompatibleImage(bufImage.getWidth(), bufImage.getHeight(), Transparency.TRANSLUCENT);
		g.dispose();*/

		if(captchaCriteria.getBackColor() != 0){
			//设置背景色，否则默认是透明背景
			new SingleColorBackgroundFactory(new Color(captchaCriteria.getBackColor())).fillBackground(bufImage);
		}
		
		if(captchaCriteria.getIdentify() != null && captchaCriteria.getIdentify().equalsIgnoreCase("pgw")){
			MyCustomBackgroundFactory backgroundFactory = new MyCustomBackgroundFactory();  
			backgroundFactory.fillBackground(bufImage);
		}
		ColorFactory colorFactory = new SingleColorFactory(new Color(captchaCriteria.getForeColor()));
		FilterFactory filterFactory = new CurvesRippleFilterFactory(colorFactory);
		RandomFontFactory fontFactory = new RandomFontFactory();
		/*List<String> families = new ArrayList<String>();
		families.add("serif");
		fontFactory.setFamilies(families);*/
		TextRenderer textRenderer = new BestFitTextRenderer();
		textRenderer.setLeftMargin(10);
		textRenderer.setRightMargin(10);	

		String word = null;
		if(captchaCriteria.getWord() == null){
			AdaptiveRandomWordFactory wordFactory = new AdaptiveRandomWordFactory();
			wordFactory.setCharacters(defaultCharacters);
			wordFactory.setMinLength(captchaCriteria.getMinLength());
			wordFactory.setMaxLength(captchaCriteria.getMaxLength());
			word = wordFactory.getNextWord();
		} else {
			word = captchaCriteria.getWord();
		}


		textRenderer.draw(word, bufImage, fontFactory, colorFactory);
		if (captchaCriteria.getMode().equals("1")){
			bufImage = filterFactory.applyFilters(bufImage); 
		}
		Captcha captcha = new Captcha();
		captcha.setWord(word);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufImage, DEFAULT_IMG_TYPE, bos);
			captcha.setImage(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("生成的验证码:" + word + "图片大小为" + captcha.getImage().length);


		return captcha;


	}



	private void correctCriteriaValue(CaptchaCriteria captchaCriteria) {


		if(captchaCriteria.getForeColor() == 0){
			captchaCriteria.setForeColor(defaultForeColor);
		}
		if(captchaCriteria.getCharacters() == null || captchaCriteria.getCharacters().equals("")){
			captchaCriteria.setCharacters(defaultCharacters);
		}
		if(captchaCriteria.getImageFormat() ==null ){
			captchaCriteria.setImageFormat(DEFAULT_IMG_TYPE);
		}
		if(captchaCriteria.getMaxLength() == 0){
			captchaCriteria.setMaxLength(defaultMaxLength);
		}
		if(captchaCriteria.getMinLength() == 0){
			captchaCriteria.setMinLength(defaultMinLength);
		}
		if(captchaCriteria.getImageHeight() == 0){
			captchaCriteria.setImageHeight(defaultImageHeight);
		}
		if(captchaCriteria.getImageWidth() == 0){
			captchaCriteria.setImageWidth(defaultImageWidth);
		}
		if (captchaCriteria.getMode()==null ||captchaCriteria.getMode().equals(""))
			captchaCriteria.setMode("1");
		return;
	}

}

final class MyCustomBackgroundFactory implements BackgroundFactory {  
    private Random random = new Random();  

    public void fillBackground(BufferedImage image) {  
        Graphics graphics = image.getGraphics();  
          
        // 验证码图片的宽高  
        int imgWidth = image.getWidth();  
        int imgHeight = image.getHeight();  
          
        // 填充为白色背景  
        graphics.setColor(Color.WHITE);  
        graphics.fillRect(0, 0, imgWidth, imgHeight);  
          
        // 画100个噪点(颜色及位置随机)  
        for(int i = 0; i < 100; i++) {  
            // 随机颜色  
            int rInt = random.nextInt(255);  
            int gInt = random.nextInt(255);  
            int bInt = random.nextInt(255);  
              
            graphics.setColor(new Color(rInt, gInt, bInt));  
              
            // 随机位置  
            int xInt = random.nextInt(imgWidth - 3);  
            int yInt = random.nextInt(imgHeight - 2);  
              
            // 随机旋转角度  
            int sAngleInt = random.nextInt(360);  
            int eAngleInt = random.nextInt(360);  
              
            // 随机大小  
            int wInt = random.nextInt(6);  
            int hInt = random.nextInt(6);  
              
            graphics.fillArc(xInt, yInt, wInt, hInt, sAngleInt, eAngleInt);  
              
            // 画5条干扰线  
            if (i % 20 == 0) {  
                int xInt2 = random.nextInt(imgWidth);  
                int yInt2 = random.nextInt(imgHeight);  
                graphics.drawLine(xInt, yInt, xInt2, yInt2);  
            }  
        }  
    }  
}  
