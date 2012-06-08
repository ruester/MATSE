public class Vektor {
    private double x, y, z;

    public Vektor(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vektor(Vektor v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vektor() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public double betrag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void add(Vektor a) {
        this.x += a.x;
        this.y += a.y;
        this.z += a.z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public static double abstand(Vektor a, Vektor b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2)
                         + Math.pow(a.y - b.y, 2)
                         + Math.pow(a.z - b.z, 2));
    }
}
