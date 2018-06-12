package sunucuProje;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageThread implements Runnable {
	public Images i;
	public void run() {

		try {
			while ((i = (Images) main.queueScale.take()) != null) {
				synchronized (i) {
					
					
					i.setImage(createResizedCopy(i.getImage(),i.getImage().getWidth(),i.getImage().getHeight()));
					
					if (i.getColor() != null)
						i.setImage(makeGray(i.getImage()));
					
					i.ready=true;
					i.notify();
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage makeGray(BufferedImage img)
	{
	    for (int x = 0; x < img.getWidth(); ++x)
	    for (int y = 0; y < img.getHeight(); ++y)
	    {
	        int rgb = img.getRGB(x, y);
	        int r = (rgb >> 16) & 0xFF;
	        int g = (rgb >> 8) & 0xFF;
	        int b = (rgb & 0xFF);

	        int grayLevel = (r + g + b) / 3;
	        int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel; 
	        img.setRGB(x, y, gray);
	    }
	    return img;
	}
	
	public static BufferedImage createResizedCopy(BufferedImage originalImage, int scaledWidth, int scaledHeight) {
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaledBI.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }
	
	

}
