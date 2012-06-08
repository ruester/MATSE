public class Gitter {
	private static double RUHEABSTAND = 0.18;
	private static double TRAEGHEIT   = 0.2; /* Traegheit muss immer groesser als Daempfung sein */
	private static double DAEMPFUNG   = 0.1;
	private static double SCHWERKRAFT = 0.0;
	private int hoehe, breite;
	private Knoten [][]knoten;
	
	public static double getDaempfung() {
		return DAEMPFUNG;
	}
	
	public static double getRuheabstand() {
		return RUHEABSTAND;
	}
	
	public static double getTraegheit() {
		return TRAEGHEIT;
	}
	
	public static double getSchwerkraft() {
		return SCHWERKRAFT;
	}
	
	public static void setSchwerkraft(double d) {
		SCHWERKRAFT = d;
	}
	
	public static void setDaempfung(double d) {
		DAEMPFUNG = d;
	}
	
	public static void setRuheabstand(double r) {
		RUHEABSTAND = r;
	}
	
	public static void setTraegheit(double t) {
		TRAEGHEIT = t;
	}
	
	public int getHoehe() {
		return hoehe;
	}

	public int getBreite() {
		return breite;
	}
	
	public Knoten getKnoten(int x, int y) {
		return knoten[y][x];
	}

	public void initKnoten() {
		double px, py, pz;
		boolean fixed;

		//Standardwerte wieder herstellen
		TRAEGHEIT   = 0.2;
		DAEMPFUNG   = 0.1;

		px = py = pz = 0.0;
		
		for (int x = 0; x < breite; x++) {
			if (x == 0)
				px = 0.0;

			for (int y = 0; y < hoehe; y++) {
				if (y == 0)
					py = 0.0;
				
				fixed = false;
				
				if (x == 0 || y == 0 || x == breite - 1 || y == hoehe - 1)
				    fixed = true;
					//fixed = false;
				
				knoten[y][x].setX(px);
				knoten[y][x].setY(py);
				knoten[y][x].setZ(pz);
				knoten[y][x].setFixed(fixed);
				knoten[y][x].setRemoved(false);
				knoten[y][x].setCut(false);
				knoten[y][x].g.setX(0.0);
				knoten[y][x].g.setY(0.0);
				knoten[y][x].g.setZ(0.0);

				py += RUHEABSTAND * 1.3;
			}
			
			px += RUHEABSTAND * 1.3;
		}
	}
	
	public void initKnotenRutsche() {
		double px, py, pz;
		boolean fixed;

		//Standardwerte wieder herstellen
		TRAEGHEIT   = 0.2;
		DAEMPFUNG   = 0.1;

		px = py = pz = 0.0;
		
		for (int x = 0; x < breite; x++) {
			if (x == 0)
				px = 0.0;

			for (int y = 0; y < hoehe; y++) {
				if (y == 0)
					py = 0.0;
				
				fixed = false;
				
				if (x == 0 || x == breite - 1)
				    fixed = true;
					//fixed = false;
				
				knoten[y][x].setX(px);
				knoten[y][x].setY(py);
				knoten[y][x].setZ(pz);
				knoten[y][x].setFixed(fixed);
				knoten[y][x].setRemoved(false);
				knoten[y][x].setCut(false);
				knoten[y][x].g.setX(0.0);
				knoten[y][x].g.setY(0.0);
				knoten[y][x].g.setZ(0.0);

				py += RUHEABSTAND * 1.3;
			}
			
			px += RUHEABSTAND * 1.3;
		}
	}
	
	public void initNachbarn() {
		for (int y = 0; y < hoehe; y++) {
			for (int x = 0; x < breite; x++) {
				knoten[y][x].clearNachbarn();
				
				if (x != 0)
					knoten[y][x].addNachbar(knoten[y][x - 1]);

				if (x != breite - 1)
					knoten[y][x].addNachbar(knoten[y][x + 1]);

				if (y != 0)
					knoten[y][x].addNachbar(knoten[y - 1][x]);

				if (y != hoehe - 1)
					knoten[y][x].addNachbar(knoten[y + 1][x]);
			}
		}
	}
	
	public void loescheNachbarn() {
		for (int y = 0; y < hoehe; y++)
			for (int x = 0; x < breite; x++)
				knoten[y][x].getNachbarn().clear();
	}
	
	public Gitter(int h, int b) {
		hoehe  = h;
		breite = b;
		knoten = new Knoten[hoehe][breite];
		
		for (int x = 0; x < breite; x++)
			for (int y = 0; y < hoehe; y++)
				knoten[y][x] = new Knoten();
		
		initKnoten();
		initNachbarn();
		
		for (int y = 0; y < hoehe; y++)
			for (int x = 0; x < breite; x++)
				knoten[y][x].start();
	}
}
