import java.util.ArrayList;

public class Regression {
    private static double det(double a, double b,
                              double c, double d) {
        return a * d - b * c;
    }

    private static double det(double a, double b, double c,
                              double d, double e, double f,
                              double g, double h, double i) {
        return a * det(e, f, h, i) - b * det(d, f, g, i) + c * det(d, e, g, h);
    }

    private static double det(double a, double b, double c, double d,
                              double e, double f, double g, double h,
                              double i, double j, double k, double l,
                              double m, double n, double o, double p) {
        return a * det(f, g, h, j, k, l, n, o, p) - b * det(e, g, h, i, k, l, m, o, p)
             + c * det(e, f, h, i, j, l, m, n, p) - d * det(e, f, g, i, j, k, m, n, o);
    }

    public static double[] bestimme_linear(ArrayList<Punkt> p) {
        double[] ret = new double[2];
        int n = p.size();
        double sum_x2, sum_xy, sum_x, sum_y,
               det, d_m, d_n;

        sum_x2 = sum_xy = sum_x = sum_y = 0.0;

        for (int i = 0; i < n; i++) {
            sum_x  += p.get(i).x;
            sum_y  += p.get(i).y;
            sum_x2 += Math.pow(p.get(i).x, 2);
            sum_xy += p.get(i).y * p.get(i).x;
        }

        det = det(sum_x2, sum_x, sum_x, n);
        d_m = det(sum_xy, sum_x, sum_y, n);
        d_n = det(sum_x2, sum_xy, sum_x, sum_y);

        ret[0] = d_m / det;
        ret[1] = d_n / det;

        return ret;
    }

    public static double[] bestimme_parabel(ArrayList<Punkt> p) {
        double[] ret = new double[3];
        int n        = p.size();
        double sum_x4, sum_x3, sum_x2, sum_x,
               sum_x2y, sum_xy, sum_y,
               det, d_a, d_b, d_c;

        sum_x4  = sum_x3 = sum_x2 = sum_x =
        sum_x2y = sum_xy = sum_y  = 0.0;

        for (int i = 0; i < n; i++) {
            sum_x   += p.get(i).x;
            sum_y   += p.get(i).y;
            sum_x2  += Math.pow(p.get(i).x, 2);
            sum_x3  += Math.pow(p.get(i).x, 3);
            sum_x4  += Math.pow(p.get(i).x, 4);
            sum_xy  += p.get(i).y * p.get(i).x;
            sum_x2y += p.get(i).y * Math.pow(p.get(i).x, 2);;
        }

        det = det(sum_x4, sum_x3, sum_x2,
                  sum_x3, sum_x2, sum_x,
                  sum_x2, sum_x, n);

        d_a = det(sum_x2y, sum_x3, sum_x2,
                  sum_xy, sum_x2, sum_x,
                  sum_y, sum_x, n);

        d_b = det(sum_x4, sum_x2y, sum_x2,
                  sum_x3, sum_xy, sum_x,
                  sum_x2, sum_y, n);

        d_c = det(sum_x4, sum_x3, sum_x2y,
                  sum_x3, sum_x2, sum_xy,
                  sum_x2, sum_x, sum_y);

        ret[0] = d_a / det;
        ret[1] = d_b / det;
        ret[2] = d_c / det;

        return ret;
    }

    public static double[] bestimme_kubisch(ArrayList<Punkt> p) {
        double ret[] = new double[4];
        int n = p.size();
        double sum_x6, sum_x5, sum_x4,
               sum_x3, sum_x2, sum_x,
               sum_x3y, sum_x2y, sum_xy, sum_y,
               det, d_a, d_b, d_c, d_d;

        sum_x6 = sum_x5  = sum_x4  = sum_x3 = sum_x2 =
        sum_x  = sum_x3y = sum_x2y = sum_xy = sum_y  = 0.0;

        for (int i = 0; i < n; i++) {
            sum_x   += p.get(i).x;
            sum_y   += p.get(i).y;
            sum_x2  += Math.pow(p.get(i).x, 2);
            sum_x3  += Math.pow(p.get(i).x, 3);
            sum_x4  += Math.pow(p.get(i).x, 4);
            sum_x5  += Math.pow(p.get(i).x, 5);
            sum_x6  += Math.pow(p.get(i).x, 6);
            sum_xy  += p.get(i).y * p.get(i).x;
            sum_x2y += p.get(i).y * Math.pow(p.get(i).x, 2);
            sum_x3y += p.get(i).y * Math.pow(p.get(i).x, 3);
        }

        det = det(sum_x6, sum_x5, sum_x4, sum_x3,
                  sum_x5, sum_x4, sum_x3, sum_x2,
                  sum_x4, sum_x3, sum_x2, sum_x,
                  sum_x3, sum_x2, sum_x,  n);

        d_a = det(sum_x3y, sum_x5, sum_x4, sum_x3,
                  sum_x2y, sum_x4, sum_x3, sum_x2,
                  sum_xy, sum_x3, sum_x2,  sum_x,
                  sum_y, sum_x2, sum_x,    n);

        d_b = det(sum_x6, sum_x3y, sum_x4, sum_x3,
                  sum_x5, sum_x2y, sum_x3, sum_x2,
                  sum_x4, sum_xy, sum_x2,  sum_x,
                  sum_x3, sum_y, sum_x,    n);

        d_c = det(sum_x6, sum_x5, sum_x3y, sum_x3,
                  sum_x5, sum_x4, sum_x2y, sum_x2,
                  sum_x4, sum_x3, sum_xy,  sum_x,
                  sum_x3, sum_x2, sum_y,   n);

        d_d = det(sum_x6, sum_x5, sum_x4, sum_x3y,
                  sum_x5, sum_x4, sum_x3, sum_x2y,
                  sum_x4, sum_x3, sum_x2, sum_xy,
                  sum_x3, sum_x2, sum_x,  sum_y);

        ret[0] = d_a / det;
        ret[1] = d_b / det;
        ret[2] = d_c / det;
        ret[3] = d_d / det;

        return ret;
    }
}
