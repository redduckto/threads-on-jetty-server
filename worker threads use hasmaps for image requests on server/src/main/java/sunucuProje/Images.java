package sunucuProje;

import java.awt.image.BufferedImage;

public class Images {
	
	public String fileName;
	public String width;
	public String height;
	public String color;
	public BufferedImage Image;
	public volatile boolean ready = false;
	
	public Images(String[] params) {
		super();
		this.fileName = params[0];
		this.width = params[1];
		this.height = params[2];
		this.color = params[3];
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public BufferedImage getImage() {
		return Image;
	}

	public void setImage(BufferedImage image) {
		Image = image;
	}

	public boolean getReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	
	
	
}
