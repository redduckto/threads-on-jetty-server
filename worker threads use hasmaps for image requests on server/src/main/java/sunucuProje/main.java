package sunucuProje;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;

public class main extends AbstractHandler {

	public String[] params = new String[4];
	public static BlockingQueue queueIo = new ArrayBlockingQueue(1024);
	public static BlockingQueue queueScale = new ArrayBlockingQueue(1024);

	public main() {

		final ThreadPoolExecutor executor1 = new ThreadPoolExecutor(25, 50, 100, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
		final ThreadPoolExecutor executor2 = new ThreadPoolExecutor(25, 50, 100, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());

		for (int i = 0; i < 20; i++) {
			executor1.execute(new ReadImageThread());
		}

		for (int i = 0; i < 20; i++) {
			executor2.execute(new ImageThread());
		}
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ContextHandler context = new ContextHandler();
		context.setContextPath("/img");
		context.setHandler(new main());
		server.setHandler(context);
		server.start();
		server.join();
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		params[0] = request.getPathInfo();
		params[1] = request.getParameter("width");
		params[2] = request.getParameter("height");
		params[3] = request.getParameter("color");

		Images i = new Images(params);

		synchronized (i) {
			try {
				queueIo.put(i);
				if (!i.ready)
					i.wait();

				
				response.setHeader("Content-Type", "image/png");
				ImageIO.write(scale(i.getImage(), Integer.parseInt(params[1]), Integer.parseInt(params[2])), "png", response.getOutputStream());
				baseRequest.setHandled(true);
				i.notify();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	public static BufferedImage scale(BufferedImage src, int w, int h)
    {
        BufferedImage img = 
                new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        int[] ys = new int[h];
        for (y = 0; y < h; y++)
            ys[y] = y * hh / h;
        for (x = 0; x < w; x++) {
            int newX = x * ww / w;
            for (y = 0; y < h; y++) {
                int col = src.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }

}
