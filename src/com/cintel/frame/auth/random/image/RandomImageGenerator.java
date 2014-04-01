package com.cintel.frame.auth.random.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @version: $Id: RandomImageGenerator.java 13449 2009-12-17 00:29:53Z wangshuda $
 */
public class RandomImageGenerator implements IRandomImageGenerator{

	private int characterCount = 4;
	private int imageWidth = 90;
	private int imageHeight = 26;
	
	private int characterType = 0;// 1: only num; 2: only character; others : number and character; 
	
	public String generateRandomStr() {
		String fullStr = "23456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijkmnpqrstuvxyz";
		if(characterType == 1) {
			fullStr = "0123456789";
		}
		else if(characterType == 2) {
			fullStr = "ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijkmnpqrstuvxyz";
		}
		//
		return RandomStringUtils.random(characterCount, fullStr);
	}
	
	public void render(String randomStr, OutputStream out) throws IOException {
		//
		BufferedImage bi = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) bi.getGraphics();
		java.util.Random random = new java.util.Random();
		g.setColor(Color.white);
		g.fillRect(0, 0, imageWidth, imageHeight);
		Font mFont = new Font("Times New Roman", Font.PLAIN, 18);
		g.setFont(mFont);
		g.setColor(Color.BLACK);
		String str1[] = new String[4];
		for (int i = 0; i < str1.length; i++) {
			str1[i] = randomStr.substring(i, i + 1);
			int w = 0;
			int x = (i + 1) % 3;

			if (x == random.nextInt(3)) {
				w = 19 - random.nextInt(7);
			} else {
				w = 19 + random.nextInt(7);
			}

			Color color1 = new Color(random.nextInt(180), random.nextInt(180),
					random.nextInt(180));
			g.setColor(color1);
			g.drawString(str1[i], 20 * i + 10, w);
		}

		for (int i = 0; i < 100; i++) {
			int x = random.nextInt(imageWidth);
			int y = random.nextInt(imageHeight);
			Color color1 = new Color(random.nextInt(255), random.nextInt(255),
					random.nextInt(255));
			g.setColor(color1);
			g.drawOval(x, y, 0, 0);
		}
		g.dispose();
		ImageIO.write(bi, "jpg", out);
	}

	public static void main(String[] args) throws IOException {
/*		String num = random();
		System.out.println(num);
		render(num, new FileOutputStream("D:\\test.jpg"));
		System.out.println("Image generated.");*/
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public void setCharacterCount(int characterCount) {
		this.characterCount = characterCount;
	}

	public void setCharacterType(int characterType) {
		this.characterType = characterType;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
}
