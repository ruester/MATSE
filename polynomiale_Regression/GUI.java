import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JFrame;

public class GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private ArrayList<Punkt> p;
    private double m, n,
                   a, b, c,
                   k_a, k_b, k_c, k_d;
    private final double step = 0.1;
    private final int radius  = 5;
    private boolean change    = true;
    private int drag          = -1;
    private Image image;
    private Graphics buffer;

    public GUI() {
        p = new ArrayList<Punkt>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        setResizable(false);
        setTitle("polynomiale Regression");
        setVisible(true);

        image  = createImage(getWidth(), getHeight());
        buffer = image.getGraphics();

        addMouseListener(new MouseListener() {
            public void mouseEntered(MouseEvent e) { }
            public void mouseExited(MouseEvent e) { }
            public void mouseReleased(MouseEvent e) { }
            public void mousePressed(MouseEvent e) { }

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Punkt click;

                    //Punkt hinzufuegen, wenn noch nicht vorhanden
                    if (drag != -1)
                        return;

                    click = new Punkt(e.getX(), e.getY());

                    for (int i = 0; i < p.size(); i++)
                        if (p.get(i).x == click.x)
                            return;

                    p.add(click);
                    update_gleichungen();

                    change = true;
                    drag   = p.size() - 1;

                    repaint();

                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON2) {
                    if (drag != -1) {
                        change = true;

                        //ausgewaehlten Punkt loeschen
                        p.remove(drag);
                        drag   = -1;

                        update_gleichungen();
                        repaint();
                    }

                    return;
                }

                if (e.getButton() == MouseEvent.BUTTON3) {
                    change = true;

                    //alle Punkte loeschen
                    p.clear();
                    update_gleichungen();

                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                boolean c;

                c = false;

                if (drag != -1)
                    c = true;

                drag = -1;

                //Punkt ausgewaehlt?
                for (int i = 0; i < p.size(); i++) {
                    if (abstand(e.getX(), e.getY(), p.get(i).x, p.get(i).y) <= radius) {
                        drag = i;
                        repaint();

                        return;
                    }
                }

                if (c)
                    repaint();
            }

            public void mouseDragged(MouseEvent e) {
                if (drag != -1) {
                    p.get(drag).x = e.getX();
                    p.get(drag).y = e.getY();
                    change        = true;
                    update_gleichungen();

                    repaint();
                }
            }
        });
    }

    private void update_gleichungen() {
        double f[];

        //lineare Ausgleichsgerade bestimmen
        f = Regression.bestimme_linear(p);
        m = f[0];
        n = f[1];

        //Ausgleichsparabel bestimmen
        f = Regression.bestimme_parabel(p);
        a = f[0];
        b = f[1];
        c = f[2];

        //kubische Ausgleichskurve bestimmen
        f   = Regression.bestimme_kubisch(p);
        k_a = f[0];
        k_b = f[1];
        k_c = f[2];
        k_d = f[3];
    }

    private double abstand(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    private void bigPoint(Graphics g, int x, int y) {
        g.fillArc(x - radius, y - radius, 2 * radius, 2 * radius, 0, 360);
    }

    public void paint(Graphics g) {
        if (buffer == null)
            return;

        if (change) {
            buffer.setColor(Color.WHITE);
            buffer.fillRect(0, 0, getWidth(), getHeight());

            //Achsen zeichnen
            buffer.setColor(Color.BLACK);
            buffer.drawLine(99,  0, 99,  getHeight());
            buffer.drawLine(100, 0, 100, getHeight());
            buffer.drawLine(101, 0, 101, getHeight());

            buffer.drawLine(0, getHeight() - 99,  getWidth(), getHeight() - 99);
            buffer.drawLine(0, getHeight() - 100, getWidth(), getHeight() - 100);
            buffer.drawLine(0, getHeight() - 101, getWidth(), getHeight() - 101);

            //Gitterlinien zeichnen
            for (int i = 0; i < getHeight(); i += 100)
                buffer.drawLine(0, getHeight() - i, getWidth(), getHeight() - i);

            for (int i = 0; i < getWidth(); i += 100)
                buffer.drawLine(i, 0, i, getHeight());
        }

        if (p.isEmpty()) {
            change = false;
            g.drawImage(image, 0, 0, this);
            return;
        }

        //Punkte zeichnen
        buffer.setColor(Color.BLACK);

        for (int i = 0; i < p.size(); i++)
            bigPoint(buffer, (int) p.get(i).x, (int) p.get(i).y);

        if (drag != -1) {
            buffer.setColor(Color.RED);
            bigPoint(buffer, (int) p.get(drag).x, (int) p.get(drag).y);
        }

        if (change) {
            change = false;

            //lineare Ausgleichskurve zeichnen
            buffer.setColor(Color.ORANGE);
            buffer.drawLine(0, (int) n, getWidth(), (int) (getWidth() * m + n));
            buffer.drawLine(0, (int) (n + 1.0), getWidth(), (int) (getWidth() * m + n + 1.0));
            buffer.drawLine(0, (int) (n + 2.0), getWidth(), (int) (getWidth() * m + n + 2.0));
            buffer.drawLine(0, (int) (n - 1.0), getWidth(), (int) (getWidth() * m + n - 1.0));
            buffer.drawLine(0, (int) (n - 2.0), getWidth(), (int) (getWidth() * m + n - 2.0));

            for (double i = 0.0; i < getWidth(); i += step) {
                double i2;

                if (p.size() <= 2)
                    break;

                i2 = Math.pow(i, 2);

                //quadratische Ausgleichskurve zeichnen
                double y = a * i2 + b * i + c;
                buffer.setColor(Color.GREEN);
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);
                y++;
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);
                y++;
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);
                y -= 2;
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);
                y--;
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);

                if (p.size() <= 3)
                    continue;

                //kubische Ausgleichskurve zeichnen
                y = k_a * i2 * i + k_b * i2 + k_c * i + k_d;
                buffer.setColor(Color.BLUE);
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);
                y++;
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);
                y++;
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);
                y -= 2;
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);
                y--;
                buffer.drawLine((int) i, (int) y, (int) i, (int) y);
            }
        }

        //double buffering
        g.drawImage(image, 0, 0, this);
    }
}
