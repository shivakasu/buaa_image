import Jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by 寒烛 on 2017/3/29.
 */

public class waterMark {
    private blockDCT dct = new blockDCT(8);
    private BufferedImage watermark;
    private BufferedImage carrier;
    private int nwidth,nheight;
    private double alpha;
    private int[][][] waterRecover;

    public waterMark(BufferedImage carrier,double alpha,BufferedImage water){
        this.alpha = alpha;
        this.carrier = carrier;
        this.watermark = water;
    }

    public waterMark(BufferedImage carrier,double alpha,int nwidth,int nheight){
        this.carrier = carrier;
        this.alpha = alpha;
        this.nwidth = nwidth;
        this.nheight = nheight;
    }

    public double[] feeddback(){
        double[] res = new double[3];
        res[0]=alpha;
        res[1]=nwidth;
        res[2]=nheight;
        return res;
    }

    private int[][][] getExpandCarry(int w1,int h1){
        int[][][] carryPix = FFT.imgToMatrix(carrier,carrier.getWidth(),carrier.getHeight(),false,true);
        int[][][] carryArr = new int[3][w1][h1];
        for(int a=0;a<3;a++)
            for (int i = 0; i < carrier.getWidth(); i++)
                for (int j = 0; j < carrier.getHeight(); j++)
                    carryArr[a][i][j]=carryPix[a][i][j]-128;
        return carryArr;
    }

    private int[][][] getExpandwater(int w1,int h1){
        int[][][] waterPix = FFT.imgToMatrix(watermark,watermark.getWidth(),watermark.getHeight(),false,true);
        int w = (int)Math.ceil((watermark.getWidth()+0.0)/(w1/dct.getN()))*(w1/dct.getN());
        int h = (int)Math.ceil((watermark.getHeight()+0.0)/(h1/dct.getN()))*(h1/dct.getN());
        int[][][] waterArr = new int[3][w][h];
        for(int a=0;a<3;a++)
            for (int i = 0; i < watermark.getWidth(); i++)
                for (int j = 0; j < watermark.getHeight(); j++)
                    waterArr[a][i][j]=waterPix[a][i][j];
        this.nwidth = (w/(w1/dct.getN()));
        this.nheight = (h/(h1/dct.getN()));
        return waterArr;
    }

    private int[][][] doubleToInt(double[][][] a){
        int[][][] res = new int[3][a[0].length][a[0][0].length];
        for(int i=0;i<3;i++)
            for(int j=0;j<a[0].length;j++)
                for(int k=0;k<a[0][0].length;k++)
                    res[i][j][k] = (int)a[i][j][k];
        return res;
    }

    private double[][][] addWater(double[][][] carry,int[][][] water,int count){
        double[][][] res = carry.clone();
        int wnum = water[0].length/nwidth;
        int h = ((count-1)/wnum)*nheight;
        int w = ((count-h/nheight*wnum)-((count-h/nheight*wnum)/(wnum+1))*wnum-1)*nwidth;
        int[] buffer = new int[nwidth*nheight];
        for(int a=0;a<3;a++) {
            int c = 0;
            for (int i = w; i < w + nwidth; i++)
                for (int j = h; j < h + nheight; j++) {
                    buffer[c] = water[a][i][j];
                    c++;
                }
            c=0;
            loop:for(int i=0;i<dct.getN();i++)
                for(int j=0;j<dct.getN();j++){
                    if(i+j>=3 && i+j<=5) {
                        res[a][i][j] = alpha*(buffer[c]-128);
                        c++;
                        if(c==buffer.length)break loop;
                    }
                }
        }
        return res;
    }

    private void minusWater(double[][][] carry,int count,int a){
        double[][][] res = carry.clone();
        int wnum = waterRecover[0].length/nwidth;
        int h = ((count-1)/wnum)*nheight;
        int w = ((count-h/nheight*wnum)-((count-h/nheight*wnum)/(wnum+1))*wnum-1)*nwidth;
        double[] buffer = new double[nwidth*nheight];
        int c = 0;
        loop:for(int i=0;i<dct.getN();i++)
            for(int j=0;j<dct.getN();j++){
                if(i+j>=3 && i+j<=5) {
                    buffer[c] = carry[a][i][j]/alpha+128;
                    c++;
                    if(c==buffer.length)break loop;
                }
            }
        c=0;
        for (int i = w; i < w + nwidth; i++)
            for (int j = h; j < h + nheight; j++) {
                waterRecover[a][i][j] = (int)buffer[c];
                c++;
            }

    }

    public BufferedImage insert(){
        int rows = carrier.getWidth();
        int columns =carrier.getHeight();
        int row2 = (int)Math.ceil(rows/(dct.getN()*1.0))*dct.getN();
        int column2 = (int)Math.ceil(columns/(dct.getN()*1.0))*dct.getN();
        double[][][] databuffer = new double[3][dct.getN()][dct.getN()];
        double[][][] temp;
        double[][][] temp2;
        int[][][] temp3;
        int[][][]  output= new int[3][row2][column2];
        double[][][] recovered;
        dct.initMatrix();
        BufferedImage imageOutput = new BufferedImage(rows, columns, TYPE_INT_RGB);
        int[][][] carryArr = getExpandCarry(row2,column2);
        int[][][] waterArr = getExpandwater(row2,column2);
        int count=1;
        for(int a=0;a<3;a++) {
            for (int i = 0; i < row2; i = (i + dct.getN())) {
                for (int j = 0; j < column2; j = (j + dct.getN())) {
                    for (int k = 0; k < dct.getN(); k++) {
                        for (int l = 0; l < dct.getN(); l++) {
                            databuffer[a][k][l] = (double) carryArr[a][(k + i)][(j + l)];
                        }
                    }
                    temp = dct.dct(databuffer);
                    temp2 = addWater(temp, waterArr, count);
                    count++;
                    temp3 = doubleToInt(temp2);
                    recovered = dct.idct(temp3);
                    for (int k = 0; k < dct.getN(); k++) {
                        for (int l = 0; l < dct.getN(); l++) {
                            output[a][(k + i)][(j + l)] = (int) recovered[a][(k)][(l)] + 128;
                            if (output[a][(k + i)][(j + l)] > 255) output[a][(k + i)][(j + l)] = 255;
                            if (output[a][(k + i)][(j + l)] < 0) output[a][(k + i)][(j + l)] = 0;
                        }
                    }
                }
            }
            count=1;
        }
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                int R = (int)output[0][i][j];
                int G = (int)output[1][i][j];
                int B = (int)output[2][i][j];
                if(R>255)R=255;if(R<0)R=0;
                if(G>255)G=255;if(G<0)G=0;
                if(B>255)B=255;if(B<0)B=0;
                int rgb=Math.round((R * 256 + G) * 256 + B);
                imageOutput.setRGB(i, j,rgb);
            }
        return imageOutput;
    }

    public BufferedImage extract(){
        int rows = carrier.getWidth();
        int columns =carrier.getHeight();
        int row2 = (int)Math.ceil(rows/(dct.getN()*1.0))*dct.getN();
        int column2 = (int)Math.ceil(columns/(dct.getN()*1.0))*dct.getN();
        int waterW = row2/dct.getN()*nwidth;
        int waterH = column2/dct.getN()*nheight;
        waterRecover = new int[3][waterW][waterH];
        double[][][] databuffer = new double[3][dct.getN()][dct.getN()];
        double[][][] temp;
        dct.initMatrix();
        BufferedImage imageOutput = new BufferedImage(waterW, waterH, TYPE_INT_RGB);
        int[][][] carryArr = getExpandCarry(row2,column2);
        int count=1;
        for(int a=0;a<3;a++) {
            for (int i = 0; i < row2; i = (i + dct.getN())) {
                for (int j = 0; j < column2; j = (j + dct.getN())) {
                    for (int k = 0; k < dct.getN(); k++) {
                        for (int l = 0; l < dct.getN(); l++) {
                            databuffer[a][k][l] = (double) carryArr[a][(k + i)][(j + l)];
                        }
                    }
                    temp = dct.dct(databuffer);
                    minusWater(temp, count, a);
                    count++;
                }
            }
            count=1;
        }

        for (int i = 0; i < waterW; i++)
            for (int j = 0; j < waterH; j++) {
                int R = waterRecover[0][i][j];
                int G = waterRecover[1][i][j];
                int B = waterRecover[2][i][j];
                if(R>255)R=255;if(R<0)R=0;
                if(G>255)G=255;if(G<0)G=0;
                if(B>255)B=255;if(B<0)B=0;
                int rgb=Math.round((R * 256 + G) * 256 + B);
                imageOutput.setRGB(i, j,rgb);
            }
        return imageOutput;
    }
}
