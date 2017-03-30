import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by 寒烛 on 2017/3/27.
 */
public class compressListener extends JFrame implements ActionListener {
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
    private int a;
    private String type;

    public compressListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        JPanel panel = new JPanel();
        panel.setLayout(null);
        slider1 = new JSlider(1, 10,1);
        label1 = new JLabel("1");
        panel.add(slider1);
        slider1.setBounds(30, 30, 270, 40);
        panel.add(label1);
        label1.setBounds(310, 30, 40, 40);

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                label1.setText(String.valueOf(slider1.getValue()));
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
                    a = slider1.getValue();
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
                    Compress trans = new Compress(mainWindow.image,a);
                    BufferedImage spec = trans.compress();
                    label2.setIcon(new ImageIcon(spec));
                    File outputfile = new File("cache."+type);
                    try {
                        ImageIO.write(spec, type, outputfile);
                    }catch (Exception ee){}
                    File picture = new File("cache."+type);
                    DecimalFormat df = new DecimalFormat("#.00");
                    String size = df.format(picture.length()/1024.0);
                    Container c2 = frame2.getContentPane();
                    c2.add(label2);
                    frame2.pack();
                    frame2.setTitle("压缩后图像 QUALITY="+String.valueOf(a)+"  SIZE="+size);
//                        picture.delete();
                    frame2.setVisible(true);
                    a = slider1.getValue();
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
        this.setTitle("量化参数Q");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (mainWindow.image != null)
            ok.setEnabled(true);
        else
            ok.setEnabled(false);
        this.setVisible(true);
    }
}