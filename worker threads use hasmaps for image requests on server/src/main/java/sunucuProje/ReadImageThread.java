package sunucuProje;

import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ReadImageThread implements Runnable {
	public Caching cache = new Caching();
	public Images i;
	public URL url;

	public void run() {
		try {
			while ((i = (Images) main.queueIo.take()) != null) {
				if (cache.contains(i.getFileName()) == false) {
					url = new URL("http://bihap.com/img" + i.getFileName());
					cache.put(i.getFileName(), i.getImage());
				} else
					i.setImage(cache.get(i.getFileName()));

				i.setImage(ImageIO.read(url));
				main.queueScale.put(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
