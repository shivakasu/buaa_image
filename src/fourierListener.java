import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by 寒烛 on 2017/3/27.
 */
public class fourierListener implements ActionListener {
    private MainWindow mainWindow;
    private JFrame frame1 = new JFrame("频谱");
    private JFrame frame2 = new JFrame("傅里叶逆变换");
    private JLabel label1=new JLabel();
    private JLabel label2=new JLabel();

    public fourierListener(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        if(mainWindow.image!=null) {
            FFT trans = new FFT(mainWindow.image);
            BufferedImage spec = trans.fft_image();
            label1.setIcon(new ImageIcon(spec));
            Container c1 = frame1.getContentPane();
            c1.add(label1);
            frame1.pack();
            frame1.setVisible(true);
            BufferedImage ifft = trans.ifft_image();
            label2.setIcon(new ImageIcon(ifft));
            Container c2 = frame2.getContentPane();
            c2.add(label2);
            frame2.pack();
            frame2.setVisible(true);
        }
    }
}