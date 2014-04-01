package com.cintel.frame.chart;

import java.awt.Color;

import com.cintel.frame.util.RandomUtils;

/**
 * 
 * @file    : ColorFactory.java
 * @author  : WangShuDa
 * @date    : 2008-10-15
 * @corp    : CINtel
 * @version : 1.0
 */
public class ColorFactory {

	/**
	 * Inner class to describe the color info.
	 * 
	 * @file    : ColorFactory.java
	 * @author  : WangShuDa
	 * @date    : 2008-10-15
	 * @corp    : CINtel
	 * @version : 1.0
	 */
	private static class ColorInfo {
		private String name;
		private Color color;
		
		public ColorInfo(String name, Color color) {
			this.name = name;
			this.color = color;
		}
		
		public String getName() {
			return name;
		}
		
		public Color getColor() {
			return color;
		}
	}
	
	private static ColorInfo[] colorInfoArr = new ColorInfo[]{
		new ColorInfo("black", Color.black)
		,new ColorInfo("blue", Color.blue)
		,new ColorInfo("red", Color.red)
		,new ColorInfo("green", Color.green)
		,new ColorInfo("yellow", Color.yellow)
		,new ColorInfo("cyan", Color.cyan)
		,new ColorInfo("darkGray", Color.darkGray)
		,new ColorInfo("gray", Color.gray)
		,new ColorInfo("lightGray", Color.lightGray)
		,new ColorInfo("magenta", Color.magenta)
		,new ColorInfo("orange", Color.orange)
		,new ColorInfo("pink", Color.pink)
	};

	private static int colorInfoArrLength = colorInfoArr.length;
	
	/**
	 * 
	 * @method: getRandomColor
	 * @return: Color
	 * @author: WangShuDa
	 * @return
	 */
	private static Color getRandomColor() {
		int redValue = RandomUtils.generateRandomInt(255);
		int greenValue = RandomUtils.generateRandomInt(255);
		int blueValue = RandomUtils.generateRandomInt(255);
		return new Color(redValue, greenValue, blueValue);
	}
	
	/**
	 * 
	 * @method: getColor
	 * @return: Color
	 * @author: WangShuDa
	 * @param colorIndex
	 * @return
	 */
	public static Color getColor(int colorIndex) {
		if(colorIndex < colorInfoArrLength) {
			return colorInfoArr[colorIndex].getColor();
		}
		else {
			return getRandomColor();
		}
	}
	
	/**
	 * 
	 * @method: getColor
	 * @return: Color
	 * @author: WangShuDa
	 * @param color
	 * @return
	 */
	public static Color getColor(String color) throws Exception {
		for(ColorInfo colorInfo:colorInfoArr) {
			if(colorInfo.getName().equals(color)) {
				return colorInfo.getColor();
			}
		}
		return getRgbColor(color);
	}
	
	
	/**
	 * 
	 * @method: getRgbColor
	 * @return: Color
	 * @author: WangShuDa
	 * @param rgbValue
	 * @return
	 * @throws Exception
	 */
	public static Color getRgbColor(String rgbValue) throws Exception {
		//
		int redValue = 0;
		int greenValue = 0;
		int blueValue = 0;
		String[] colorRgb = rgbValue.split(",");
		//
		redValue = Integer.parseInt(colorRgb[0].trim());
		greenValue = Integer.parseInt(colorRgb[1].trim());
		blueValue = Integer.parseInt(colorRgb[2].trim());
		//
		return new Color(redValue, greenValue, blueValue);
		
	}
}
