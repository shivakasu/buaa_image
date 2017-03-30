import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorModel;

/**
 * Created by 寒烛 on 2017/3/27.
 */
public class GrayListener implements ActionListener {
    private MainWindow mainWindow;

    public GrayListener(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        int[] array = new int[mainWindow.height * mainWindow.width];
        ColorModel colorModel = ColorModel.getRGBdefault();
        int i, j, k, r, g, b;
        for (i = 0; i < mainWindow.height; i++) {
            for (j = 0; j < mainWindow.width; j++) {
                k = i * mainWindow.width + j;
                r = colorModel.getRed(mainWindow.currPixArray[k]);
                g = colorModel.getGreen(mainWindow.currPixArray[k]);
                b = colorModel.getBlue(mainWindow.currPixArray[k]);
                r = g = b = (int) (r * 0.3 + g * 0.59 + b * 0.11);
                array[k] = (255 << 24) | (r << 16) | (g << 8) | b;
            }
        }
        mainWindow.currPixArray = array;
        mainWindow.showImage(array);
    }
}