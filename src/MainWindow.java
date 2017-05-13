/**
 * Created by 寒烛 on 2017/3/8.
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import javax.imageio.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.text.DecimalFormat;

public class MainWindow extends JFrame {
    public int currPixArray[] = null;
    public int sourcePixArray[] = null;
    public JLabel imageLabel = null;
    public JScrollPane pane=null;
    public int height;
    public int width;
    public BufferedImage image=null;

    public cosineListener cosine=new cosineListener(this);

    private MainWindow(String title) {
        super(title);
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件  ");
        bar.add(fileMenu);
        JMenuItem openImage = new JMenuItem("打开图片");
        fileMenu.add(openImage);
        openImage.addActionListener(new OpenListener(this));
        JMenuItem exitItem = new JMenuItem("退出");
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu processMenu = new JMenu("p1  ");
        bar.add(processMenu);
        JMenuItem revoke = new JMenuItem("还原");
        processMenu.add(revoke);
        revoke.addActionListener(new RevokeListener(this));
        JMenuItem toGray = new JMenuItem("灰度化");
        processMenu.add(toGray);
        toGray.addActionListener(new GrayListener(this));
        JMenuItem toBalance = new JMenuItem("直方图均衡");
        processMenu.add(toBalance);
        toBalance.addActionListener(new HistogramListener(this));
        JMenuItem grayStretch = new JMenuItem("线性拉伸");
        processMenu.add(grayStretch);
        grayStretch.addActionListener(new GrayStretchListener(this));

        JMenu processMenu2 = new JMenu("p2  ");
        bar.add(processMenu2);
        JMenuItem fourier = new JMenuItem("DFT / IDFT");
        processMenu2.add(fourier);
        fourier.addActionListener(new fourierListener(this));
        JMenuItem cosin = new JMenuItem("DCT / IDCT");
        processMenu2.add(cosin);
        cosin.addActionListener(cosine);
        JMenuItem compress = new JMenuItem("DCT图像压缩");
        processMenu2.add(compress);
        compress.addActionListener(new compressListener(this));
        JMenuItem watermark = new JMenuItem("DCT数字水印");
        processMenu2.add(watermark);
        watermark.addActionListener(new watermarkListener(this));
        JMenuItem waterrecover = new JMenuItem("数字水印提取");
        processMenu2.add(waterrecover);
        waterrecover.addActionListener(new waterrecoverListener(this));

        JMenu processMenu3 = new JMenu("p3  ");
        bar.add(processMenu3);
        JMenuItem sobel = new JMenuItem("sobel边缘检测");
        processMenu3.add(sobel);
        sobel.addActionListener(new edgeListener(this,"sobel"));
        JMenuItem prewitt = new JMenuItem("prewitt边缘检测");
        processMenu3.add(prewitt);
        prewitt.addActionListener(new edgeListener(this,"prewitt"));
        JMenuItem laplace = new JMenuItem("laplace边缘检测");
        processMenu3.add(laplace);
        laplace.addActionListener(new edgeListener(this,"laplace"));

        this.setJMenuBar(bar);

        imageLabel = new JLabel("");
        pane = new JScrollPane(imageLabel);
        this.add(pane, BorderLayout.CENTER);
    }


    public void showImage(int[] array) {
        Image pic = this.createImage(new MemoryImageSource(width, height,array, 0, width));
        ImageIcon icon = new ImageIcon(pic);
        imageLabel.setIcon(icon);
        imageLabel.repaint();
    }

    public static void main(String args[]) {
        MainWindow process = new MainWindow("IM");
        process.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        process.setLocationRelativeTo(null);
        process.setMinimumSize(new Dimension(300,300));
        process.setSize(300, 300);
        process.setVisible(true);
    }
}