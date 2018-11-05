package jack.net;

import java.util.Random;

public class Particle {

	public double x, y; // x, y position of the particle
	private double vx, vy; // speed of the particle
	public double m; // mass of the particle
	public int size; // size of the particle, needs to be a sqare
	private static double maxMass = 20;
	private static double minMass = 4;
	private static double g = 0.01; // gravitational Constant
	private static double offset = 1; // boudnry where gravity take no effect
	private Random random = new Random();
	private int[] pixels;
	private double speedOffset = 0.5;
	private double speedControl = 0.1; // limitation of speed between 0-1
	private double cml = 0; // collision momentum lost
	public boolean eaten = false;

	public Particle(int size, Screen screen) {
		x = random.nextInt(screen.width);
		y = random.nextInt(screen.height);
		this.size = size;
		m = random.nextDouble() * maxMass;
		pixels = new int[size * size];
		generateVelocity();
	}

	public Particle(int x, int y, int size, Screen screen) {
		this.x = x;
		this.y = y;
		this.size = size;
		m = 5;
		pixels = new int[size * size];
		generateVelocity();
	}

	private void generateVelocity() {
		vx = (random.nextDouble() - speedOffset) * speedControl;
		vy = (random.nextDouble() - speedOffset) * speedControl;
		//vx = 0;
		//vy = 0;
		System.out.println(vx + "   " + vy);
	}

	public void move() {
		x += vx;
		y += vy;
	}

	public void attracted(Particle theOther) {
		double dx = x - theOther.x;
		double dy = y - theOther.y;
		double dxAb = Math.abs(dx);
		double dyAb = Math.abs(dy);
		double d = Math.sqrt(dxAb * dxAb + dyAb * dyAb);
		double force = (m * theOther.m) / (d * d) * g;
		// if ((dxAb <= 1) && (dyAb <= 1)) return;
		if ((dx >= 0) && (dy >= 0)) {
			vx -= force * (Math.cos(Math.atan(dyAb / dxAb)))/m;
			vy -= force * (Math.sin(Math.atan(dyAb / dxAb)))/m;
		}
		if ((dx >= 0) && (dy < 0)) {
			vx -= force * (Math.cos(Math.atan(dyAb / dxAb)))/m;
			vy += force * (Math.sin(Math.atan(dyAb / dxAb)))/m;
		}
		if ((dx < 0) && (dy >= 0)) {
			vx += force * (Math.cos(Math.atan(dyAb / dxAb)))/m;
			vy -= force * (Math.sin(Math.atan(dyAb / dxAb)))/m;
		}
		if ((dx < 0) && (dy < 0)) {
			vx += force * (Math.cos(Math.atan(dyAb / dxAb)))/m;
			vy += force * (Math.sin(Math.atan(dyAb / dxAb)))/m;
		}

	}

	public void collide(Particle theOther) {
		double dx = x - theOther.x;
		double dy = y - theOther.y;
		double dxAb = Math.abs(dx);
		double dyAb = Math.abs(dy);
		if ((dxAb <= 1) && (dyAb <= 1)) {
			if (theOther.m >= m) {
//				if (theOther.vx > 0) vx -= cml;
//				if (theOther.vx < 0) vx += cml;
//				if (theOther.vy > 0) vy -= cml;
//				if (theOther.vy < 0) vy += cml;
				vx = 0.5 * vx;
				vy = 0.5 * vy;
				theOther.m += m;
				//theOther.size += Math.cbrt(size);
				eaten = true;
			}
			if (m > theOther.m){
//				if (vx > 0) vx -= cml;
//				if (vx < 0) vx += cml;
//				if (vy > 0) vy -= cml;
//				if (vy < 0) vy += cml;
				vx = 0.5 * vx;
				vy = 0.5 * vy;
				m += theOther.m;
				//size += Math.cbrt(theOther.size);
				theOther.eaten = true;
			}
		}
	}
}
