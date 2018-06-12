package sunucuProje;

import java.awt.image.BufferedImage;

import java.util.*;

public class Caching {

	private static Caching instance;
	private static Object monitor = new Object();
	private Map<String, BufferedImage> cache = Collections.synchronizedMap(new HashMap<String, BufferedImage>());

	public Caching() {
	}

	public void put(String cacheKey, BufferedImage value) {
		cache.put(cacheKey, value);
	}

	public BufferedImage get(String cacheKey) {
		return cache.get(cacheKey);
	}

	public void clear(String cacheKey) {
		cache.put(cacheKey, null);

	}

	public void clear() {
		cache.clear();
	}

	public static synchronized Caching getInstance() {
		if (instance == null) {
			synchronized (monitor) {
				if (instance == null) {
					instance = new Caching();
				}
			}
		}
		return instance;
	}

	public boolean contains(String cacheKey) {

		return cache.containsKey(cacheKey);

	}

}