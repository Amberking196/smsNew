package com.server.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class GraphicHelper {

	    public static String create(final int width, final int height, final String imgType, OutputStream output) {  
	    StringBuffer sb = new StringBuffer();  
	    Random random = new Random();  
	  
	    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
	    Graphics graphic = image.getGraphics();  
	  
	    graphic.setColor(Color.getColor("F8F8F8"));  
	    graphic.fillRect(0, 0, width, height);  
	  
	    Color[] colors = new Color[] { Color.BLUE, Color.GRAY, Color.GREEN, Color.RED, Color.BLACK, Color.ORANGE,  
	        Color.CYAN };  
	    // 在 "画板"上生成干扰线条 ( 50 是线条个数)  
	    for (int i = 0; i < 50; i++) {  
	        graphic.setColor(colors[random.nextInt(colors.length)]);  
	        final int x = random.nextInt(width);  
	        final int y = random.nextInt(height);  
	        final int w = random.nextInt(20);  
	        final int h = random.nextInt(20);  
	        final int signA = random.nextBoolean() ? 1 : -1;  
	        final int signB = random.nextBoolean() ? 1 : -1;  
	        graphic.drawLine(x, y, x + w * signA, y + h * signB);  
	    }  
	  
	    // 在 "画板"上绘制字母  
	    graphic.setFont(new Font("Comic Sans MS", Font.BOLD, 30));  
	    for (int i = 0; i < 6; i++) {  
	        final int temp = random.nextInt(26) + 97;  
	        String s = String.valueOf((char) temp);  
	        sb.append(s);  
	        graphic.setColor(colors[random.nextInt(colors.length)]);  
	        graphic.drawString(s, i * (width / 6), height - (height / 3));  
	    }  
	    graphic.dispose();  
	    try {  
	        ImageIO.write(image, imgType, output);  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    return sb.toString();  
	    }  
}
