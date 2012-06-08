public class Main {
	public static void main(String[] args) {
		int laenge, breite;
		
		if (args.length < 2) {
			laenge = breite = 40;
	    } else {
			laenge = Integer.valueOf(args[0]);
			breite = Integer.valueOf(args[1]);
		}
		
		if (breite < 1 || laenge < 1) {
			System.err.println("Breite und Hoehe muss groesser als 0 sein");
			breite = laenge = 40;
		}
		
		Gitter a = new Gitter(laenge, breite);
		GUI g = new GUI(a);
		Knoten k;
		double i, v;
		
		i = v = 0.0;
		
		while (true) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (g.swing) {
				a.getKnoten(g.swing_x, g.swing_y).setDragged(true);
				i -= 0.1;

				a.getKnoten(g.swing_x, g.swing_y).setZ(3 * Math.sin(i) + v);
			} else {
				i = 0.0;
				
				k = a.getKnoten(g.swing_x, g.swing_y);
				
				if (k == null)
					continue;
				
				v = k.getZ();

				a.getKnoten(g.swing_x, g.swing_y).setDragged(false);				
			}

			g.repaint();
		}
	}
}
