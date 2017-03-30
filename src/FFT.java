/**
 * Created by 寒烛 on 2017/3/18.
 */
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

public class FFT {
    private BufferedImage im;
    private Complex [][][] F;
    private double [][][] T;

    public FFT(BufferedImage image){
        this.im = image;
    }

    // convert RGB int matrix into image
    private static BufferedImage RGBtoImage (int[][][]m){
        int w = m[0].length;
        int h = m[0][0].length;
        BufferedImage output_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < h; j++){
            for (int i = 0; i < w; i++){
                int gray=(int)(m[0][i][j]*0.3+m[1][i][j]*0.59+m[2][i][j]*0.11);
                Color color = new Color(m[0][i][j],m[1][i][j],m[2][i][j]);
                output_img.setRGB(i,j, color.getRGB());
            }
        }
        return output_img;
    }

    public static Complex[] fft1(Complex[] array) {
        int n = array.length;
        if (n == 1) return new Complex[] { array[0] };
        if (n % 2 != 0) { throw new RuntimeException("n is not a power of 2"); }
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = array[2*k];
        }
        Complex[] q = fft1(even);
        Complex[] odd  = even;
        for (int k = 0; k < n/2; k++) {
            odd[k] = array[2*k + 1];
        }
        Complex[] r = fft1(odd);
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + n/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    public static Complex[][][] fft2(Complex[][][] m){
        int w = m[0].length;
        int h = m[0][0].length;
        Complex[][][] output = new Complex[3][w][h];
        Complex [] row = new Complex [w];
        Complex [] col = new Complex [h];
        for (int k = 0; k <3; k++){
            for (int j = 0; j < h; j++){
                for (int i = 0; i < w; i++){
                    row[i] = m[k][i][j];
                }
                row = fft1(row);
                for (int i = 0; i < w; i++)
                    output[k][i][j] = row[i];
            }
            for (int i = 0; i < w; i++){
                for (int j = 0; j < h; j++){
                    col[j] = output[k][i][j];
                }
                col = fft1(col);
                for (int j = 0; j < h; j++)
                    output[k][i][j] = col[j];
            }
        }
        return output;
    }

    private static Complex[] ifft1(Complex[] x) {
        int n = x.length;
        Complex[] y = new Complex[n];
        for (int i = 0; i < n; i++) {
            y[i] = x[i].conjugate();
        }
        y = fft1(y);
        for (int i = 0; i < n; i++) {
            y[i] = y[i].conjugate();
        }
        for (int i = 0; i < n; i++) {
            y[i] = y[i].scale(1.0 / n);
        }
        return y;
    }

    public static Complex[][][] ifft2 (Complex[][][] m){
        int w = m[0].length;
        int h = m[0][0].length;
        Complex[][][] output = new Complex[3][w][h];
        Complex [] row = new Complex [w];
        Complex [] col = new Complex [h];
        for (int k = 0; k <3; k++){
            for (int j = 0; j < h; j++){
                for (int i = 0; i < w; i++){
                    row[i] = m[k][i][j];
                }
                row = ifft1(row);
                for (int i = 0; i < w; i++)
                    output[k][i][j] = row[i];
            }
            for (int i = 0; i < w; i++){
                for (int j = 0; j < h; j++){
                    col[j] = output[k][i][j];
                }
                col = ifft1(col);
                for (int j = 0; j < h; j++) {
                    output[k][i][j] = col[j];
                }
            }
        }
        return output;
    }

    public static int[][][] imgToMatrix(BufferedImage img, int w, int h,boolean expend,boolean RGB){
        int w1,h1;
        int[][][] output;
        if (((w & (w - 1)) != 0  || (h & (h - 1)) != 0) && expend ){ // check if it is w or h is power of 2 int
            if ((w & (w - 1)) != 0 ) {w1 = (int)Math.pow(2,Math.ceil(Math.log(w)/Math.log(2)));}
            else{w1 = w;}
            if ((h & (h - 1)) != 0){h1 = (int)Math.pow(2,Math.ceil(Math.log(h)/Math.log(2)));}
            else{h1 = h;}
            output = new int [3][w1][h1];
            for (int k = 0; k <3; k++){
                for (int j = 0; j < h1; j++){
                    for (int i = 0; i < w1; i++){
                        output[k][i][j]=0;
                    }
                }
            }
        }
        else{
            output = new int[3][w][h];
        }
        for (int j = 0; j < h; j++){
            for (int i = 0; i < w; i++){
                int pixel = img.getRGB(i,j);
                Color color = new Color(pixel);
                int R = color.getRed();
                int G = color.getGreen();
                int B = color.getBlue();
                if(RGB){
                    output[0][i][j] = R;
                    output[1][i][j] = G;
                    output[2][i][j] = B;
                }else{
                    output[0][i][j] = (int)(0.299*R+0.587*G+0.114*B);
                    output[1][i][j] = (int)(-0.1687*R-0.3313*G+0.5*B+128);
                    output[2][i][j] = (int)(0.5*R-0.418*G-0.0813*B+128);
                }
            }
        }
        return output;
    }

    //The high frequency values will be shifted to the center in spectrum after applying "shift_to_center"
    private static int[][][] shift_to_center(int[][][] matrix){
        int w = matrix[0].length;
        int h = matrix[0][0].length;
        for (int k = 0; k <3; k++){
            for (int j = 0; j < h; j++){
                for (int i = 0; i < w; i++){
                    if ((i+j)%2 != 0){
                        matrix[k][i][j] = ~matrix[k][i][j];
                    }
                }
            }
        }
        return matrix;
    }

    //convert from int matrix to complex number matrix
    private static Complex[][][] getComplexMatrix(int [][][] matrix){
        int w = matrix[0].length;
        int h = matrix[0][0].length;
        Complex [][][] output = new Complex [3][w][h];
        for (int k = 0; k <3; k++){
            for (int j = 0; j < h; j++){
                for (int i = 0; i < w; i++){
                    output[k][i][j] = new Complex((double)(matrix[k][i][j]), (double)(0));
                }
            }
        }
        return output;
    }

    private static int max(int a, int b){
        if (a>=b)
            return a;
        else
            return b;
    }

    private static int scale(int a){ //keep image values within 0 to 255
        if (a<0)
            return 0;
        else if (a>255)
            return 255;
        else
            return a;
    }

    public static BufferedImage complexToImg (Complex [][][] matrix, Boolean spectrum){
        int w = matrix[0].length;
        int h = matrix[0][0].length;
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        int [][][] m = new int [3][w][h];
        int max = 0;
        for (int k = 0; k <3; k++){
            for (int j = 0; j<h; j++){
                for (int i = 0; i<w; i++){
                    if (spectrum){
                        m[k][i][j] = (int)Math.log(matrix[k][i][j].abs()+1);
                    }
                    else{
                        m[k][i][j] = (int)matrix[k][i][j].abs();
                        m[k][i][j] = scale(m[k][i][j]);
                    }
                    max = max(max, m[k][i][j]);
                }
            }
        }
        if (spectrum){
            int C = 255/max;	//cofficient C used to scale the image
            for (int k = 0; k <3; k++){
                for (int j = 0; j<h; j++){
                    for (int i = 0; i<w; i++){
                        m[k][i][j] = (int)(m[k][i][j]*C);
                    }
                }
            }
        }
        output = RGBtoImage(m);
        return output;
    }

    private static Complex [][][] crop(Complex [][][] F, int w, int h){
        Complex [][][] output = new Complex [3][w][h];
        for (int k = 0; k <3; k++){
            for (int j = 0; j < h; j++){
                for (int i = 0; i < w; i++){
                    output[k][i][j] = F[k][i][j];
                }
            }
        }
        return output;
    }

    public BufferedImage fft_image(){
        int w0 = im.getWidth();
        int h0 = im.getHeight();
        int [][][] colors = shift_to_center(imgToMatrix(im, w0, h0,true,true));
        Complex [][][] f = getComplexMatrix(colors);
        F = fft2(f);
        return complexToImg(F, true);
    }

    public BufferedImage ifft_image(){
        int w0 = im.getWidth();
        int h0 = im.getHeight();
        int w = F[0].length;
        int h = F[0][0].length;
        Complex [][][] filted = ifft2(F);
        if (w != w0 || h != h0) filted = crop(filted, w0, h0);
        return complexToImg(filted,false);
    }

    private static double[] fct1(double[] array) {
        int n = array.length;
        Complex[] fd = new Complex[2*n];
        for (int k = 0; k < n; k++) {
            fd[k] = new Complex(array[k],0);
        }
        for (int k = n; k < 2*n; k++) {
            fd[k] = new Complex(0,0);
        }
        Complex[] q = fft1(fd);
        double[] des = new double[n];
        double dtmp=1/Math.sqrt(n);
        des[0]=q[0].re()*dtmp;
        dtmp*=Math.sqrt(2);
        for(int i=1;i<n;i++)
            des[i]=(q[i].re()*Math.cos(i*Math.PI /(n*2))+
                    q[i].im()*Math.sin(i*Math.PI /(n*2)))*dtmp;
        return des;
    }

    private static double[][][] fct2(int[][][] m,int q){
        int w = m[0].length;
        int h = m[0][0].length;
        double[][][] output = new double[3][w][h];
        double [] row = new double [w];
        double [] col = new double [h];
        for (int k = 0; k <3; k++){
            for (int j = 0; j < h; j++){
                for (int i = 0; i < w; i++){
                    row[i] = m[k][i][j];
                }
                row = fct1(row);
                for (int i = 0; i < w; i++)
                    output[k][i][j] = row[i];
            }
            for (int i = 0; i < w; i++){
                for (int j = 0; j < h; j++){
                    col[j] = output[k][i][j];
                }
                col = fct1(col);
                for (int j = 0; j < h; j++) {
                    output[k][i][j] = col[j];
                }
            }
        }

        return output;
    }

    private static double[] ifct1(double[] array){
        int n = array.length;
        Complex[] fd = new Complex[2*n];
        for (int k = 0; k < n; k++) {
            fd[k] = (new Complex(array[k]*Math.cos(k*Math.PI /(n*2)),
                    array[k]*Math.sin(k*Math.PI /(n*2))));
        }
        for (int k = n; k < 2*n; k++) {
            fd[k] = new Complex(0,0);
        }
        Complex[] q = ifft1(fd);
        double[] des = new double[n];
        for(int i=0;i<n;i++)
            des[i]=(1.0/Math.sqrt(n)-
                    Math.sqrt(2.0/n))*array[0]+Math.sqrt(8.0*n)*q[i].re();
        return des;
    }

    private static double[][][] ifct2(double[][][] m){
        int w = m[0].length;
        int h = m[0][0].length;
        double[][][] output = new double[3][w][h];
        double [] row = new double [w];
        double [] col = new double [h];
        for (int k = 0; k <3; k++){
            for (int j = 0; j < h; j++){
                for (int i = 0; i < w; i++){
                    row[i] = m[k][i][j];
                }
                row = ifct1(row);
                for (int i = 0; i < w; i++)
                    output[k][i][j] = row[i];
            }
            for (int i = 0; i < w; i++){
                for (int j = 0; j < h; j++){
                    col[j] = output[k][i][j];
                }
                col = ifct1(col);
                for (int j = 0; j < h; j++)
                    output[k][i][j] = col[j];
            }
        }
        int[][][] rr= new int[3][w][h];
        for (int k = 0; k <3; k++)
            for (int j = 0; j < h; j++)
                for (int i = 0; i < w; i++){
                    rr[k][i][j] = (int)m[k][i][j];
                }
        return output;
    }

    private BufferedImage ifct_image(){
        int w0 = im.getWidth();
        int h0 = im.getHeight();
        int w = T[0].length;
        int h = T[0][0].length;
        double [][][] filted = ifct2(T);
        int[][][] TT = new int[3][w0][h0];
        for (int k = 0; k <3; k++)
            for (int j = 0; j < h0; j++)
                for (int i = 0; i < w0; i++){
                    TT[k][i][j] = (int)filted[k][i][j];
                }
        Complex[][][] d = getComplexMatrix(TT);

        Complex[][][] ff = null;
        if (w != w0 || h != h0)
            ff = crop(d, w0, h0);
        else
            ff=d;

        return complexToImg(ff,false);
    }

    public BufferedImage[] fct_image(int a,int b,int c,int d){
        int w0 = im.getWidth();
        int h0 = im.getHeight();
        BufferedImage cache = im;
        int [][][] colors = imgToMatrix(im, w0, h0,true,true);
        T = fct2(colors,1);
        int [][][] TT = new int[3][w0][h0];
        for (int k = 0; k <3; k++)
            for (int j = 0; j < h0; j++)
                for (int i = 0; i < w0; i++){
                    TT[k][i][j] = (int)T[k][i][j];
                }


        BufferedImage jjy=null;
        try{
            jjy=ImageIO.read(new File("E://jjy.jpg"));
        } catch(Exception eee){}
        int[][][] jjjy=imgToMatrix(jjy,jjy.getWidth(),jjy.getHeight(),false,true);
        for (int k = 0; k <3; k++)
            for (int j = 0; j < jjy.getHeight(); j++)
                for (int i = 0; i < jjy.getWidth(); i++){
                        T[k][w0-1-i][h0-1-j] += jjjy[k][i][j]/30;
                }

        int w1=0,w2=w0,h1=0,h2=h0;
        if(b>a && d>c){
            w1=a;w2=b;h1=c;h2=d;
        }
        for (int k = 0; k <3; k++)
            for (int j = 0; j < h0; j++)
                for (int i = 0; i < w0; i++){
                    if(i<w1||i>w2||j<h1||j>h2)
                        T[k][i][j] = 0.0;
                }
        BufferedImage[] res = new BufferedImage[2];
        res[0] = ifct_image();
        res[1]=complexToImg(getComplexMatrix(TT),true);
        return res;
    }

}
