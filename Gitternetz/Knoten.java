import java.util.ArrayList;

public class Knoten extends Thread {
    private Vektor pos;
    public Vektor g;
    private ArrayList<Knoten> nachbarn;
    private boolean fixed, dragged, removed, cut;

    public Knoten() {
        nachbarn = new ArrayList<Knoten>();
        pos      = new Vektor();
        g        = new Vektor();
        fixed    = dragged = removed = cut = false;
    }

    public Knoten(double x, double y, double z, boolean f) {
        nachbarn = new ArrayList<Knoten>();
        pos      = new Vektor(x, y, z);
        g        = new Vektor();
        fixed    = f;
        dragged  = removed = cut = false;
    }

    public void addNachbar(Knoten k) {
        nachbarn.add(k);
    }

    public void clearNachbarn() {
        nachbarn.clear();
    }

    private Vektor getRichtungsvektor(Knoten a) {
        return new Vektor(a.getX() - pos.getX(),
                          a.getY() - pos.getY(),
                          a.getZ() - pos.getZ());
    }

    public Vektor getKraftvektor(Knoten a) {
        Vektor v, h;
        double b;

        v = getRichtungsvektor(a);

        h = new Vektor(v);
        b = h.betrag();

        if (b == 0.0)
            return h;

        h.setX((h.getX() / b) * Gitter.getRuheabstand());
        h.setY((h.getY() / b) * Gitter.getRuheabstand());
        h.setZ((h.getZ() / b) * Gitter.getRuheabstand());

        v.setX(v.getX() - h.getX());
        v.setY(v.getY() - h.getY());
        v.setZ(v.getZ() - h.getZ());

        return v;
    }

    public void run() {
        Vektor v = new Vektor();

        while (isAlive()) {
            try {
                sleep(10);
            } catch (InterruptedException e) {
                return;
            }

            if (dragged || fixed || removed)
                continue;

            v.setX(0.0);
            v.setY(0.0);
            v.setZ(0.0);

            for (Knoten i : nachbarn)
                if (!i.removed && !(cut && i.cut))
                    v.add(getKraftvektor(i));

            g.add(v);

            pos.setX(pos.getX() + Gitter.getDaempfung() * g.getX());
            pos.setY(pos.getY() + Gitter.getDaempfung() * g.getY());
            pos.setZ(pos.getZ() + Gitter.getDaempfung() * g.getZ());

            g.setX(g.getX() - Gitter.getTraegheit() * g.getX());
            g.setY(g.getY() - Gitter.getTraegheit() * g.getY());
            g.setZ(g.getZ() - Gitter.getTraegheit() * g.getZ()
                   - Gitter.getSchwerkraft());
        }
    }

    public double getX() {
        return pos.getX();
    }

    public void setX(double x) {
        pos.setX(x);
    }

    public double getY() {
        return pos.getY();
    }

    public void setY(double y) {
        pos.setY(y);
    }

    public double getZ() {
        return pos.getZ();
    }

    public void setZ(double z) {
        pos.setZ(z);
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public void setRemoved(boolean r) {
        this.removed = r;
    }

    public void setCut(boolean c) {
        this.cut = c;
    }

    public boolean isDragged() {
        return dragged;
    }

    public boolean isRemoved() {
        return removed;
    }

    public boolean isCut() {
        return cut;
    }

    public void setDragged(boolean dragged) {
        this.dragged = dragged;
    }

    public ArrayList<Knoten> getNachbarn() {
        return nachbarn;
    }

    public Vektor getPosition() {
        return pos;
    }

    public double abstand(Knoten k) {
        return Vektor.abstand(pos, k.pos);
    }
}
