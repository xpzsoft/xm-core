package org.xm.api.com.imagecode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 
 * @author xpzsoft
 * @version 1.2.0
 */
public class ImageCode {
	// 图片的宽度。
    private int width = 100;
    // 图片的高度。
    private int height = 40;
    // 验证码字符个数
    private int codeCount = 4;
    // 验证码干扰线数
    private int lineCount = 20;
    
    //随机对象
    private Random random = new Random();
    
    /**
     * @author xpzsoft
     * @Description 默认构造函数，生成ImageCode默认实例
     * @param 
     * @return ImageCode对象
     * @throws
     */
    public ImageCode(){}
    
    /**
     * @author xpzsoft
     * @Description 默认构造函数，生成ImageCode实例
     * @param {width:[图片的宽度], height:[图片的高度]}
     * @return ImageCode对象
     * @throws
     */
    public ImageCode(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    /**
     * @author xpzsoft
     * @Description 默认构造函数，生成ImageCode实例
     * @param {width:[图片的宽度], height:[图片的高度], codeCount:[验证码个数]}
     * @return ImageCode对象
     * @throws
     */
    public ImageCode(int width, int height, int codeCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
    }
    
    /**
     * @author xpzsoft
     * @Description 默认构造函数，生成ImageCode实例
     * @param {width:[图片的宽度], height:[图片的高度], codeCount:[验证码个数], lineCount:[混淆线条数]}
     * @return ImageCode对象
     * @throws
     */
    public ImageCode(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
    }
    
    /**
     * @author xpzsoft
     * @Description 获取ImageCodeItem实例
     * @param 
     * @return ImageCodeItem示例
     * @throws
     */
    public ImageCodeItem getBuffImg() {
        return creatImage();
    }

    /**
     * @author xpzsoft
     * @Description 创建ImageCodeItem实例
     * @param 
     * @return ImageCodeItem示例
     * @throws
     */
    private ImageCodeItem creatImage() {
        int fontWidth = width / codeCount;// 字体的宽度
        int fontHeight = height - 5;// 字体的高度
        int codeY = height - 8;

        // 图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();
        //Graphics2D g = buffImg.createGraphics();
        // 设置背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        
        
        
        // 设置字体
        //Font font1 = getFont(fontHeight);
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        g.setFont(font);

        // 设置干扰线
        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            g.setColor(getRandColor(1, 255));
            g.drawLine(xs, ys, xe, ye);
        }

        // 添加噪点
        float yawpRate = 0.01f;// 噪声率
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            buffImg.setRGB(x, y, random.nextInt(255));
        }


        String code = randomStr(codeCount);// 得到随机字符
        for (int i = 0; i < codeCount; i++) {
            String strRand = code.substring(i, i + 1);
            g.setColor(getRandColor(1, 255));
            // g.drawString(a,x,y);
            // a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处
            
            g.drawString(strRand, i*fontWidth+3, codeY);
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(buffImg, "png", baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
        byte[] bytes = baos.toByteArray();
        
        //将图片转换成base64格式，并与验证码值一起存入ImageCodeItem对象
        return new ImageCodeItem(code.toLowerCase(), "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes).trim());
    }

    /**
     * @author xpzsoft
     * @Description 生成随机字符串
     * @param {n:[字符串长度]}
     * @return 返回字符串
     * @throws
     */
    private String randomStr(int n) {
        String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String str2 = "";
        int len = str1.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r = (Math.random()) * len;
            str2 = str2 + str1.charAt((int) r);
        }
        return str2;
    }

    /**
     * @author xpzsoft
     * @Description 随机生成颜色
     * @param  {fc:[最小值], bc:[最大值]}
     * @return 生成的Color对象
     * @throws
     */
    private Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
    
    /**
     * @author xpzsoft
     * @Description 随机生成生成字体
     * @param  {size:[字体大小]}
     * @return 生成的Font对象
     * @throws
     */
    @SuppressWarnings("unused")
	private Font getFont(int size) {
        Random random = new Random();
        Font font[] = new Font[5];
        font[0] = new Font("Ravie", Font.PLAIN, size);
        font[1] = new Font("Antique Olive Compact", Font.PLAIN, size);
        font[2] = new Font("Fixedsys", Font.PLAIN, size);
        font[3] = new Font("Wide Latin", Font.PLAIN, size);
        font[4] = new Font("Gill Sans Ultra Bold", Font.PLAIN, size);
        return font[random.nextInt(5)];
    }
}
