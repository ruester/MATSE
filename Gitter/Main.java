public class Main {
	public static void main(String[] args) {
		int laenge = 40,
			breite = 40;
		Gitter a = new Gitter(laenge, breite);
		GUI g = new GUI(a);
		double i, v;
		
		i = v = 0.0;
		
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (g.swing) {
				a.getKnoten(g.swing_x, g.swing_y).setDragged(true);
				i -= 0.05;

				a.getKnoten(g.swing_x, g.swing_y).setZ(3 * Math.sin(i) + v);
			} else {
				i = 0.0;
				v = a.getKnoten(g.swing_x, g.swing_y).getZ();

				a.getKnoten(g.swing_x, g.swing_y).setDragged(false);				
			}

			g.repaint();
		}
	}
}
