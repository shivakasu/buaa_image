import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by 寒烛 on 2017/3/27.
 */
public class watermarkListener extends JFrame implements ActionListener {
    private MainWindow mainWindow;
    private JSlider slider1;
    private JLabel label1;
    private JButton ok= new JButton("确定");
    private JButton cancle=new JButton("取消");
    private JRadioButton PNG = new JRadioButton("png");
    private JLabel png = new JLabel("PNG");
    private JRadioButton JPG = new JRadioButton("jpg");
    private JLabel jpg = new JLabel("JPG");
    private JRadioButton BMP = new JRadioButton("bmp");
    private JLabel bmp = new JLabel("BMP");
    private JRadioButton GIF = new JRadioButton("gif");
    private JLabel gif = new JLabel("GIF");
    private String type;

    public watermarkListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;


        JPanel panel = new JPanel();
        panel.setLayout(null);
        slider1 = new JSlider(1, 100,1);
        label1 = new JLabel("0.01");
        panel.add(slider1);
        slider1.setBounds(30, 30, 270, 40);
        panel.add(label1);
        label1.setBounds(310, 30, 40, 40);

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                label1.setText(String.valueOf(slider1.getValue()/100.0));
            }
        });

        panel.add(PNG);
        PNG.setBounds(30, 60, 20, 40);
        panel.add(png);
        png.setBounds(50, 60, 50, 40);
        panel.add(BMP);
        BMP.setBounds(90, 60, 20, 40);
        panel.add(bmp);
        bmp.setBounds(110, 60, 50, 40);
        panel.add(JPG);
        JPG.setBounds(150, 60, 20, 40);
        panel.add(jpg);
        jpg.setBounds(170, 60, 50, 40);
        panel.add(GIF);
        GIF.setBounds(210, 60, 20, 40);
        panel.add(gif);
        gif.setBounds(230, 60, 50, 40);
        ButtonGroup group = new ButtonGroup();// 创建单选按钮组
        group.add(PNG);
        group.add(BMP);
        group.add(JPG);
        group.add(GIF);
        PNG.setSelected(true);

        panel.add(ok);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mainWindow.image != null) {
                    if(PNG.isSelected())
                        type="png";
                    else if(BMP.isSelected())
                        type="bmp";
                    else if(GIF.isSelected())
                        type="gif";
                    else
                        type="jpg";

                    JFrame frame2 = new JFrame();
                    JLabel label2 = new JLabel();
                    waterMark trans = new waterMark(mainWindow.image,slider1.getValue()/100.0);
                    BufferedImage spec = trans.insert();
                    label2.setIcon(new ImageIcon(spec));
                    File outputfile = new File("water."+type);
                    try {
                        ImageIO.write(spec, type, outputfile);
                    }catch (Exception ee){}
                    Container c2 = frame2.getContentPane();
                    c2.add(label2);
                    frame2.pack();
                    double[] back = trans.feeddback();
                    frame2.setTitle("水印嵌入完成 alpha="+back[0]+" W="+back[1]+" H="+back[2]);
//                        picture.delete();
                    frame2.setVisible(true);
                    dispose();
                }
            }
        });
        ok.setBounds(60, 120, 80, 30);
        panel.add(cancle);
        cancle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancle.setBounds(200, 120, 80, 30);

        Container c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(panel);
        this.setVisible(false);
        this.setLocationRelativeTo(null);
        this.setSize(380, 200);
        this.setResizable(false);
        this.setTitle("水印嵌入参数 alpha");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (mainWindow.image != null)
            ok.setEnabled(true);
        else
            ok.setEnabled(false);
        BufferedImage carrier;
        JFileChooser file = new JFileChooser();
        String filePath;
        int feedback = file.showOpenDialog(null);
        if (feedback == JFileChooser.APPROVE_OPTION) {
            File target = file.getSelectedFile();
            if (target != null) {
                filePath = target.getAbsolutePath();
                try {
                    carrier = ImageIO.read(new File(filePath));
                    PixelGrabber p;
                    int[] array = new int[carrier.getWidth() * carrier.getHeight()];
                    try {
                        p = new PixelGrabber(carrier, 0, 0, carrier.getWidth(), carrier.getHeight(), array, 0, carrier.getWidth());
                        if (!p.grabPixels())
                            try {
                                throw new Exception();
                            } catch (Exception e1) {}
                    } catch (Exception e2){}
                } catch (IOException e3) {}

                this.setVisible(true);
            }
        }
    }
}