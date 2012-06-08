import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private double scale = 100.0;
	private final int radius = 5;
	private JFrame parameter;
	private Gitter gitter;
	public Knoten dragged;
	private Image buffer;
	private JSlider d, s, t, r;
	private JButton b;
	private double offset_x = 30,
				   offset_y = 50;
	public boolean swing = false,
	               swap = false,
	               show = false,
	               color = false;
	public int swing_x = 1, swing_y = 1;
    private boolean strg = false;

	public static double MAX(double a, double b) {
		return a > b ? a : b;
	}
	
	public static double MIN(double a, double b) {
		return a < b ? a : b;
	}
	
	public static int MAX(int a, int b) {
		return a > b ? a : b;
	}
	
	public static int MIN(int a, int b) {
		return a < b ? a : b;
	}
	
	private double abstand(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	private double toScreen(double a) {
		return a * scale + offset_x;
	}
	
	private double toScreenX(double a) {
		return a * scale + offset_x;
	}
	
	private double toScreenY(double a) {
		return a * scale + offset_y;
	}
	
	public GUI(Gitter g) {
		gitter  = g;
		dragged = null;
		parameter = new JFrame("Parameter");
		parameter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		parameter.setLayout(new GridLayout(5, 1));
		d = new JSlider(1, 600, (int) (Gitter.getDaempfung() * 1000.0));
		t = new JSlider(1, 600, (int) (Gitter.getTraegheit() * 1000.0));
		r = new JSlider(70, 600, (int) (Gitter.getRuheabstand() * 1000.0));
		s = new JSlider(0, 5000, (int) (Gitter.getSchwerkraft() * 100000.0));

		d.setToolTipText("Daempfung = " + Gitter.getDaempfung());
		t.setToolTipText("Traegheit = " + Gitter.getTraegheit());
		r.setToolTipText("Ruheabstand = " + Gitter.getRuheabstand());
		s.setToolTipText("Schwerkraft = " + Gitter.getSchwerkraft());
		
		d.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider a = (JSlider) e.getSource();
				double n;
				
				n = a.getValue() / 1000.0;
				
				if (n >= Gitter.getTraegheit() - 0.1) {
					n = Gitter.getTraegheit() - 0.1;
					a.setValue((int) (n * 1000.0));
				}
				
				Gitter.setDaempfung(n);
				a.setToolTipText("Daempfung = " + n);
			}
		});
		
		t.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider a = (JSlider) e.getSource();
				double n;
				
				n = a.getValue() / 1000.0;
				
				if (n <= Gitter.getDaempfung() + 0.1) {
					n = Gitter.getDaempfung() + 0.1;
	    			a.setValue((int) (n * 1000.0));
				}

				Gitter.setTraegheit(n);
				a.setToolTipText("Traegheit = " + n);
			}
		});
		
		r.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider a = (JSlider) e.getSource();
				double n;
				
				n = a.getValue() / 1000.0;
				
				Gitter.setRuheabstand(n);
				a.setToolTipText("Ruheabstand = " + n);
			}
		});
		
		s.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider a = (JSlider) e.getSource();
				double n;
				
				n = a.getValue() / 100000.0;
				
				Gitter.setSchwerkraft(n);
				a.setToolTipText("Schwerkraft = " + n);
			}
		});
		
		b = new JButton("Fixierung aufheben");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int y = 0; y < gitter.getHoehe(); y++)
					for (int x = 0; x < gitter.getBreite(); x++)
						gitter.getKnoten(x, y).setFixed(false);
			}
		});
		
		parameter.add(r);
		parameter.add(t);
		parameter.add(d);
		parameter.add(s);
		parameter.add(b);
		parameter.setSize(300, 150);
		parameter.setLocation(20, 30);
		parameter.setResizable(false);
		parameter.setAlwaysOnTop(true);
		parameter.setVisible(true);
		
		setTitle("Gitter");
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setVisible(true);
		
		buffer = createImage(getWidth(), getHeight());
		
        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) { }

			public void keyReleased(KeyEvent e) {
				int c = e.getKeyCode();

                strg = false;

                // Taste 'k'
                if (c == 75)
                	show = !show;

                // Taste 'c'
                if (c == 67)
                	color = !color;
			}

			public void keyPressed(KeyEvent e) {
				if (dragged == null)
					return;
				
				int c = e.getKeyCode();
				
				// Strg-Taste
                if (c == 17) {
                    dragged.setFixed(!dragged.isFixed());
                    strg = true;
                }
                
                // Entf-Taste
                if (c == 127) {
                	dragged.setRemoved(true);
                	dragged = null;
                }

                // Ende-Taste
                if (c == 35)
                	dragged.setCut(true);
			}
        });

		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				scale += -3.0 * e.getWheelRotation();

				if (scale <= 2)
					scale = 2;
			}
		});
		
		addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
				if (dragged != null)
					dragged.setDragged(false);
                dragged = null;
			}

			public void mousePressed(MouseEvent e) {
				if (dragged != null && !strg && !dragged.isFixed())
					dragged.setDragged(true);
			}

			public void mouseExited(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (strg && dragged != null)
                        dragged.setFixed(!dragged.isFixed());
                }

				if (e.getButton() == MouseEvent.BUTTON2) {
					swing = !swing;
					
					if (!swing)
						return;

					swing_x = gitter.getBreite() / 2;
					swing_y = gitter.getHoehe() / 2;

					for (int y = 0; y < gitter.getHoehe(); y++) {
						for (int x = 0; x < gitter.getBreite(); x++) {
							double px, py, pz, x1, y1;
							
							px = toScreenX(gitter.getKnoten(x, y).getX());
							py = toScreen(gitter.getKnoten(x, y).getY());
							pz = toScreenY(gitter.getKnoten(x, y).getZ());
							
							x1 = (int) (px + py / 2.0);
							y1 = getHeight() - (int) (pz + py / 2.0);
							
							if (abstand(x1, y1, (double) e.getX(), (double) e.getY()) <= radius * 2.0) {
								swing_x = x;
								swing_y = y;
								return;
							}
						}
					}
				}

				if (e.getButton() == MouseEvent.BUTTON3) {
                    offset_x = offset_y = 30;

					if (swap)
						gitter.initKnoten();
					else
						gitter.initKnotenRutsche();
					swap = !swap;
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			int x = -1, y = -1;
			
			public void mouseMoved(MouseEvent e) {
				dragged = null;
				x = e.getX();
				y = e.getY();

				for (int y = 0; y < gitter.getHoehe(); y++) {
					for (int x = 0; x < gitter.getBreite(); x++) {
						double px, py, pz, x1, y1;
						
						px = toScreenX(gitter.getKnoten(x, y).getX());
						py = toScreen(gitter.getKnoten(x, y).getY());
						pz = toScreenY(gitter.getKnoten(x, y).getZ());
						
						x1 = (int) (px + py / 2.0);
						y1 = getHeight() - (int) (pz + py / 2.0);
						
						if (!gitter.getKnoten(x, y).isRemoved()
							&& abstand(x1, y1, (double) e.getX(), (double) e.getY()) <= radius * 2.0) {
							dragged = gitter.getKnoten(x, y);
							break;
						}
					}
				}
			}

			public void mouseDragged(MouseEvent e) {
                if (strg)
                    return;

                if (dragged != null && dragged.isFixed())
                    return;

				if (dragged == null) {
					offset_x += e.getX() - x;
					offset_y += y - e.getY();
					x = e.getX();
					y = e.getY();
					return;
				}

				double dx, dy;

				dx = e.getX() - x;
				dy = y - e.getY();

				dragged.setX(dragged.getX() + dx * Gitter.getRuheabstand() / 7.0);
				dragged.setZ(dragged.getZ() + dy * Gitter.getRuheabstand() / 7.0);

				x = e.getX();
				y = e.getY();
			}
		});
	}

	private void bigPoint(Graphics g, int x, int y, int radius) {
		g.fillArc(x - radius, y - radius, 2 * radius, 2 * radius, 0, 360);
	}
	
	public void paint(Graphics g) {
		Graphics bufg;
		Color c;
		Knoten k;
		double r;

		if (buffer == null)
			return;

		bufg = buffer.getGraphics();
		r    = 0.0;
		
		bufg.setColor(Color.WHITE);
		bufg.fillRect(0, 0, getWidth(), getHeight());
		
		bufg.setColor(Color.BLACK);
		
		for (int y = 0; y < gitter.getHoehe(); y++) {
			for (int x = 0; x < gitter.getBreite(); x++) {
				double x1, x2, y1, y2, z1, z2;
				
				k = gitter.getKnoten(x, y);
				
				if (k.isRemoved())
					continue;
				
				x1 = toScreenX(k.getX());
				y1 = toScreen(k.getY());
				z1 = toScreenY(k.getZ());
				
				if (show) {
					bufg.setColor(Color.black);
					bigPoint(bufg, (int)(x1 + y1 / 2.0), getHeight() - (int)(z1 + y1 / 2.0), 2);
				}
				
				for (Knoten i: k.getNachbarn()) {
					if (i.isRemoved() || (k.isCut() && i.isCut()))
						continue;
					
					x2 = toScreenX(i.getX());
					y2 = toScreen(i.getY());
					z2 = toScreenY(i.getZ());

					if (color) {
						r = k.abstand(i) - Gitter.getRuheabstand();

						if (r > 0.0)
							r = MIN(r * 100.0, 255.0);
					}

					c = new Color((int) r, 0, 0);
					bufg.setColor(c);
					bufg.drawLine((int)(x1 + y1 / 2.0), getHeight() - (int)(z1 + y1 / 2.0),
							      (int)(x2 + y2 / 2.0), getHeight() - (int)(z2 + y2 / 2.0));
				}
			}
		}

		if (dragged != null) {
			double x1, y1, z1;
			
			x1 = toScreenX(dragged.getX());
			y1 = toScreen(dragged.getY());
			z1 = toScreenY(dragged.getZ());
			
			if (!swing) {
				if (show)
					bufg.setColor(Color.red);
				else
					bufg.setColor(Color.black);
				bigPoint(bufg, (int)(x1 + y1 / 2.0), getHeight() - (int)(z1 + y1 / 2.0), radius);
			}
		}
		
		g.drawImage(buffer, 0, 0, this);
	}
}
