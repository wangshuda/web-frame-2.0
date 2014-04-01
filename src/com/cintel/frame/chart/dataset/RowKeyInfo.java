package com.cintel.frame.chart.dataset;

public class RowKeyInfo {
	
	String property;

	String title;

	String color;
	
	public RowKeyInfo(String property, String title, String color) {
		setProperty(property);
		setTitle(title);
		setColor(color);
	}
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
