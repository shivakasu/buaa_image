import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 寒烛 on 2017/3/27.
 */
public class HistogramListener implements ActionListener {
    private MainWindow mainWindow;

    public HistogramListener(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        int grayLevel; // 每个像素的灰度级
        int[] grayArray = new int[256]; // 记录每个灰度级出现的像素数量
        int[] resPixArray = new int[mainWindow.width * mainWindow.height]; // 均衡化之后的新像素矩阵
        for (int i = 0; i < mainWindow.height; i++) {
            for (int j = 0; j < mainWindow.width; j++) {
                grayLevel = mainWindow.currPixArray[i * mainWindow.width + j] & 0xff; // 后8位为该像素的灰度级
                grayArray[grayLevel]++;
            }
        }
        double[] p = new double[256]; // 记录每个灰度级的出现的概率
        for (int i = 0; i < 256; i++) {
            p[i] = (double) grayArray[i] / (mainWindow.width * mainWindow.height);
        }
        grayArray[0] = (int) (p[0] * 255 + 0.5);
        for (int i = 0; i < 255; i++) {
            p[i + 1] += p[i];
            grayArray[i + 1] = (int) (p[i + 1] * 255 + 0.5);
        }
        int oldGray, newGray;
        for (int i = 0; i < mainWindow.height; i++) {
            for (int j = 0; j < mainWindow.width; j++) {
                oldGray = mainWindow.currPixArray[i * mainWindow.width + j] & 0x0000ff;
                newGray = grayArray[oldGray];
                resPixArray[i * mainWindow.width + j] = 255 << 24 | newGray << 16 | newGray << 8 | newGray;
            }
        }
        mainWindow.currPixArray = resPixArray;
        mainWindow.showImage(resPixArray);
    }
}