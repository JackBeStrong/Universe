package jack.net;

/*
 * Main sim class
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Sim extends Canvas implements Runnable {

	public static int width = 1300;
	public static int height = width / 16 * 9;
	public static int scale = 1;
	public static int particleNumber = 200;
	public static int particleSize = 4; // size of the particle

	private String title = "Universe!";
	private boolean running = false;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	private Thread thread;
	private Screen screen;
	private JFrame frame;
	private ArrayList<Particle> particles;

	public Sim() {
		Dimension size = new Dimension(width * scale, height * scale);
		this.setPreferredSize(size);
		
		screen = new Screen(width, height);
		particles = new ArrayList<Particle>(); 
		for(int i = 0; i < particleNumber; i++){
			particles.add(new Particle(particleSize,screen));
		}
		//Particle particle = new Particle(particleSize,screen);
		//particle.m = 250000;
		//particle.x = this.getWidth()/2;
		//particle.y = this.getHeight()/2;
		//particles.add(particle);
		frame = new JFrame();
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		this.requestFocusInWindow(true);
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + "  |  " + updates + "ups, " + frames + "fps");
				frames = 0;
				updates = 0;
			}
		}
	}

	private void update() {
		//int particleSize = particles.size();
		for(int i = 0; i < particles.size(); i++){
			for(int j = 0; (j != i) && (j < particles.size()); j++){
				particles.get(i).attracted(particles.get(j));
			}
			
			particles.get(i).move();
			for(int k = 0; k != i; k++){
				particles.get(i).collide(particles.get(k));
			}
			if(particles.get(i).eaten){
				particles.remove(i);
			}
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		screen.clear(); 
		for(int j = 0; j < particles.size();j++){
			screen.renderParticle(particles.get(j));
			//screen.renderCircle(particles.get(j));
		}

		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
		//g.setColor(Color.white);
		//g.drawString("" + this.particles, getWidth()/2, getHeight()/2);
		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		Sim sim = new Sim();
		sim.frame.setResizable(false);
		sim.frame.setTitle(sim.title);
		sim.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sim.frame.add(sim);
		sim.frame.pack();
		sim.frame.setLocationRelativeTo(null);
		sim.frame.setVisible(true);

		sim.start();
	}
}
