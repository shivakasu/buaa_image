/**
 * Created by 寒烛 on 2017/3/18.
 */
public class Complex {
    private final double re;
    private final double im;

    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    public double abs() {
        return Math.hypot(re, im);
    }

    public Complex plus(Complex b) {
        Complex a = this;
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    public Complex scale(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    public Complex conjugate() {
        return new Complex(re, -im);
    }

    public double re(){return re;}

    public double im(){return im;}

}