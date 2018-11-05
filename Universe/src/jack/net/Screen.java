package jack.net;

/*
 * Draw the screen
 */

import java.util.Random;

public class Screen {

	public int width, height;
	public int[] pixels;
	private Random random = new Random();

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	public void renderParticle(Particle p) {
		for (int y = 0; y < p.size; y++) {
			int yp = (int) (y + p.y);
			if (yp >= height || yp <= 0) break;
			for (int x = 0; x < p.size; x++) {
				int xp = (int) (x + p.x);
				if (xp >= width || xp <= 0) break;
				pixels[xp + yp * width] = 0xff0000;
			}
		}
	}

	public void renderCircle(Particle p) {
		int x0 = (int)p.x;
		int y0 = (int)p.y;
		int radius = p.size;
		int x = radius, y = 0;
		int radiusError = 1 - x;
		while (x >= y) {
			if (((y0 - x) <= 0) || ((y0 - y) <= 0)) break;
			if (((y0 + x) >= height) || ((y0 + y) >= height)) break;
			if (((x0 - x) <= 0) || ((x0 - y) <= 0)) break;
			if (((x0 + x) >= width) || ((x0 + y) >= width)) break;
//			pixels[x + x0 + (y + y0) * width] = 0xff00ff;
//			pixels[-x + x0 + (y + y0) * width] = 0xff00ff;
//			pixels[y + x0 + (x + y0) * width] = 0xff00ff;
//			pixels[-y + x0 + (x + y0) * width] = 0xff00ff;
//			pixels[-x + x0 + (-y + y0) * width] = 0xff00ff;
//			pixels[x + x0 + (-y + y0) * width] = 0xff00ff;
//			pixels[y + x0 + (-x + y0) * width] = 0xff00ff;
//			pixels[-y + x0 + (-x + y0) * width] = 0xff00ff;
			line(x0, y0, x+x0, y+y0);
			line(x0, y0, -x+x0, y+y0);
			line(x0, y0, y+x0, x+y0);
			line(x0, y0, -y+x0, x+y0);
			line(x0, y0, -x+x0, -y+y0);
			line(x0, y0, x+x0, -y+y0);
			line(x0, y0, y+x0, -x+y0);
			line(x0, y0, -y+x0, -x+y0);
			y++;
			if (radiusError < 0) {
				radiusError += 2 * y + 1;
			}
			else {
				x--;
				radiusError += 2 * (y - x + 1);
			}
		}
	}
	
	public void line(int x,int y,int x2, int y2) {
	    int w = x2 - x ;
	    int h = y2 - y ;
	    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
	    if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
	    if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
	    if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
	    int longest = Math.abs(w) ;
	    int shortest = Math.abs(h) ;
	    if (!(longest>shortest)) {
	        longest = Math.abs(h) ;
	        shortest = Math.abs(w) ;
	        if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
	        dx2 = 0 ;            
	    }
	    int numerator = longest >> 1 ;
	    for (int i=0;i<=longest;i++) {
	        pixels[x + y * width] = 0xff00ff;
	        numerator += shortest ;
	        if (!(numerator<longest)) {
	            numerator -= longest ;
	            x += dx1 ;
	            y += dy1 ;
	        } else {
	            x += dx2 ;
	            y += dy2 ;
	        }
	    }
	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
}
