import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

/**
 * Created by 寒烛 on 2017/3/27.
 */
public class OpenListener implements ActionListener {
    private MainWindow mainWindow;

    public OpenListener(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser file = new JFileChooser();
        String filePath;
        int feedback = file.showOpenDialog(null);
        if (feedback == JFileChooser.APPROVE_OPTION) {
            File target = file.getSelectedFile();
            if (target != null) {
                filePath = target.getAbsolutePath();
                try {
                    mainWindow.image = ImageIO.read(new File(filePath));
                    mainWindow.width = mainWindow.image.getWidth();
                    mainWindow.height = mainWindow.image.getHeight();

                    //////////////////dangerous/////////////
                    mainWindow.cosine.setWidth(mainWindow.image.getWidth());
                    mainWindow.cosine.setHeight(mainWindow.image.getHeight());
                    //////////////////////////////////////

                    PixelGrabber p;
                    int[] array = new int[mainWindow.width * mainWindow.height];
                    try {
                        p = new PixelGrabber(mainWindow.image, 0, 0, mainWindow.width, mainWindow.height, array, 0, mainWindow.width);
                        if (!p.grabPixels())
                            try {
                                throw new Exception();
                            } catch (Exception e1) {}
                    } catch (Exception e2){}
                    mainWindow.currPixArray = array;
                    mainWindow.sourcePixArray = array;
                    mainWindow.imageLabel.setIcon(new ImageIcon(mainWindow.image));
                } catch (IOException e3) {}
            }
        }
        mainWindow.repaint();
        mainWindow.setSize(mainWindow.width+22,mainWindow.height+76);
        mainWindow.pane.setSize(mainWindow.width,mainWindow.height);
    }
}