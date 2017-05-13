import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by 寒烛 on 2017/5/13.
 */
public class edgeListener implements ActionListener {
    private MainWindow mainWindow;
    private String mod;

    public edgeListener(MainWindow mainWindow,String mod){
        this.mainWindow = mainWindow;
        this.mod = mod;
    }

    public void actionPerformed(ActionEvent e) {
        if(mainWindow.image==null)return;
        int[][] result = new int[mainWindow.width][mainWindow.height];
        int[][] gray = new int[mainWindow.width][mainWindow.height];
        for(int j=0;j<mainWindow.width;j++)
            for(int k=0;k<mainWindow.height;k++) {
                Color pixel = new Color(mainWindow.image.getRGB(j, k));
                gray[j][k]=(int)(pixel.getRed()*0.3+pixel.getGreen()*0.59+pixel.getBlue()*0.11);
                if(gray[j][k]>255)gray[j][k]=255;
                if(gray[j][k]<0)gray[j][k]=0;
            }
        for(int j=0;j<mainWindow.width;j++)
            for(int k=0;k<mainWindow.height;k++){
                if(j==0||k==0||j==mainWindow.width-1||k==mainWindow.height-1) {
                    result[j][k] = 0;
                }
                else{
                    if(this.mod.equals("sobel")) {
                        int gradx = gray[j - 1][k - 1] + 2 * gray[j - 1][k] + gray[j - 1][k + 1]
                                - gray[j + 1][k - 1] - 2 * gray[j + 1][k] - gray[j + 1][k + 1];
                        int grady = gray[j - 1][k - 1] + 2 * gray[j][k - 1] + gray[j + 1][k - 1]
                                - gray[j - 1][k + 1] - 2 * gray[j][k + 1] - gray[j + 1][k + 1];
                        result[j][k] = (int) Math.sqrt(gradx * gradx + grady * grady);
                    }else if(this.mod.equals("prewitt")){
                        int gradx = gray[j - 1][k - 1] + gray[j - 1][k] + gray[j - 1][k + 1]
                                - gray[j + 1][k - 1] - gray[j + 1][k] - gray[j + 1][k + 1];
                        int grady = gray[j - 1][k - 1] + gray[j][k - 1] + gray[j + 1][k - 1]
                                - gray[j - 1][k + 1] - gray[j][k + 1] - gray[j + 1][k + 1];
                        result[j][k] = (int) Math.sqrt(gradx * gradx + grady * grady);
                    }else if(this.mod.equals("laplace")){
                        result[j][k] = Math.abs(gray[j - 1][k]+gray[j + 1][k]+gray[j][k + 1]+gray[j][k - 1]-4*gray[j][k]);
                    }
                    if(result[j][k]>255)
                        result[j][k]=255;
                    if(result[j][k]<0)
                        result[j][k]=0;

                }
            }
        BufferedImage imageOutput=new BufferedImage(mainWindow.width, mainWindow.height, TYPE_INT_RGB);
        for (int i = 0; i < mainWindow.width; i++)
            for (int j = 0; j < mainWindow.height; j++) {
                imageOutput.setRGB(i,j,new Color(result[i][j],result[i][j],result[i][j]).getRGB());
            }
        JFrame frame3 = new JFrame();
        JLabel label3 = new JLabel();
        label3.setIcon(new ImageIcon(imageOutput));
        Container c3 = frame3.getContentPane();
        c3.add(label3);
        frame3.pack();
        frame3.setTitle(this.mod+"边缘检测");
        frame3.setVisible(true);
    }
}
