import Jama.Matrix;

/**
 * Created by 寒烛 on 2017/3/29.
 */
public class blockDCT {
    private int QUALITY;
    private int N = 8;
    private Matrix C = null;
    private int[][] Y={
            {16,11,10,16,24,40,51,61},
            {12,12,14,19,26,58,60,55},
            {14,13,16,24,40,57,69,56},
            {14,17,22,29,51,87,80,62},
            {18,22,37,56,68,109,103,77},
            {24,35,55,64,81,104,113,92},
            {49,64,78,87,103,121,120,101},
            {72,92,95,98,112,100,103,99}
    };
    private int[][] UV={
            {17,18,24,47,99,99,99,99},
            {18,21,26,66,99,99,99,99},
            {24,26,56,99,99,99,99,99},
            {47,66,99,99,99,99,99,99},
            {99,99,99,99,99,99,99,99},
            {99,99,99,99,99,99,99,99},
            {99,99,99,99,99,99,99,99},
            {99,99,99,99,99,99,99,99}
    };

    public blockDCT(int QUALITY){
        this.QUALITY = QUALITY;
    }

    public void initMatrix(){
        double tmp[][] = new double[N][N];
        for(int j=0;j<N;j++)
            tmp[0][j] = Math.sqrt(1.0/N)*Math.cos((Math.PI*0*(j+0.5))/N);
        for(int i=1;i<N;i++)
            for(int j=0;j<N;j++)
                tmp[i][j] = Math.sqrt(2.0/N)*Math.cos((Math.PI*i*(j+0.5))/N);
        C = new Matrix(tmp);
    }

    public int[][][] quantization(double inputData[][][]) {
        int outputData[][][] = new int[3][N][N];
        for (int i=0; i<N; i++)
            for (int j=0; j<N; j++) {
                outputData[0][i][j] = (int)Math.round (inputData[0][i][j] / (Y[i][j] * QUALITY));
                outputData[1][i][j] = (int) Math.round(inputData[1][i][j] / (UV[i][j] * QUALITY));
                outputData[2][i][j] = (int) Math.round(inputData[2][i][j] / (UV[i][j] * QUALITY));
            }
        return outputData;
    }

    public int[][][] deQuantization(int[][][] inputData) {
        int outputData[][][] = new int[3][N][N];
        for (int i=0; i<N; i++)
            for (int j=0; j<N; j++) {
                outputData[0][i][j] =  inputData[0][i][j] * (Y[i][j] * QUALITY);
                outputData[1][i][j] =  inputData[1][i][j] * (UV[i][j] * QUALITY);
                outputData[2][i][j] =  inputData[2][i][j] * (UV[i][j] * QUALITY);
            }
        return outputData;
    }

    public double[][][] dct(double input[][][]) {
        double output[][][] = new double[3][N][N];
        for(int i=0;i<3;i++){
            Matrix f = new Matrix(input[i]);
            Matrix r = C.times(f).times(C.transpose());
            output[i]=r.getArray();
        }

        return output;
    }

    public double[][][] idct(int input[][][]) {
        double output[][][] = new double[3][N][N];
        double[][][] dinput = new double[3][N][N];
        for(int i=0;i<3;i++)
            for(int j=0;j<N;j++)
                for(int k=0;k<N;k++)
                    dinput[i][j][k]=(double) input[i][j][k];
        for(int i=0;i<3;i++){
            Matrix f = new Matrix(dinput[i]);
            Matrix r = C.transpose().times(f).times(C);
            output[i]=r.getArray();
        }
        return output;
    }

    public int getN(){
        return this.N;
    }

}
