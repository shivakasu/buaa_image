import Jama.Matrix;

import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by 寒烛 on 2017/3/20.
 */
public class Compress {
    private blockDCT dct;
    private BufferedImage im;

    public Compress(BufferedImage image,int QUALITY) {
        im=image;
        this.dct=new blockDCT(QUALITY);
    }

    public BufferedImage compress(){
        dct.initMatrix();
        int rows = im.getWidth();
        int columns =im.getHeight();
        int row2 = (int)Math.ceil(rows/(dct.getN()*1.0))*dct.getN();
        int column2 = (int)Math.ceil(columns/(dct.getN()*1.0))*dct.getN();
        int[][][] pixels;
        int[][][] pixel2 = new int[3][row2][column2];
        double[][][] databuffer = new double[3][dct.getN()][dct.getN()];
        double[][][] temp;
        int[][][]  output= new int[3][row2][column2];
        int[][][] quantizedOutput;
        int[][][] dequantized;
        double[][][] recovered;
        BufferedImage imageOutput = new BufferedImage(rows, columns, TYPE_INT_RGB);
        pixels = FFT.imgToMatrix(im,rows,columns,false,false);
        for(int a=0;a<3;a++)
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++)
                    pixel2[a][i][j]=pixels[a][i][j]-128;

        for(int a=0;a<3;a++)
            for (int i = 0; i < row2; i=(i+dct.getN())) {
                for (int j = 0; j < column2; j=(j+dct.getN())) {
                    for (int k = 0; k < dct.getN(); k++) {
                        for (int l = 0; l < dct.getN(); l++) {
                            databuffer[a][k][l] = (double) pixel2[a][(k+i)][(j+l)];
                        }
                    }
                    temp = dct.dct(databuffer);
                    quantizedOutput = dct.quantization(temp);
                    dequantized = dct.deQuantization(quantizedOutput);
                    recovered = dct.idct(dequantized);
                    for (int k = 0; k < dct.getN(); k++) {
                        for (int l = 0; l < dct.getN(); l++) {
                            output[a][(k+i)][(j+l)] = (int)recovered[a][(k)][(l)]+128;
                            if(output[a][(k+i)][(j+l)]>255)output[a][(k+i)][(j+l)]=255;
                            if(output[a][(k+i)][(j+l)]<0)output[a][(k+i)][(j+l)]=0;
                        }
                    }
                }
            }
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                int R = (int)(output[0][i][j]+1.402*(output[2][i][j]-128));
                int G = (int)(output[0][i][j]-0.34414*(output[1][i][j]-128)-0.71414*(output[2][i][j]-128));
                int B = (int)(output[0][i][j]+1.772*(output[1][i][j]-128));
                if(R>255)R=255;if(R<0)R=0;
                if(G>255)G=255;if(G<0)G=0;
                if(B>255)B=255;if(B<0)B=0;
                int rgb=Math.round((R * 256 + G) * 256 + B);
                imageOutput.setRGB(i, j,rgb);
            }
        return imageOutput;
    }
}