import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by 寒烛 on 2017/3/27.
 */
public class cosineListener extends JFrame implements ActionListener {
    private MainWindow mainWindow;
    private JFrame frame1 = new JFrame("频谱");
    private JFrame frame2 = new JFrame("余弦逆变换图像");
    private JLabel label5=new JLabel();
    private JLabel label6=new JLabel();
    private JSlider slider1,slider2,slider3,slider4;
    private JLabel label1,label2,label3,label4;
    private JButton ok,cancle;
    private int a, b, c, d;

    public void setWidth(int width){
        slider1.setMaximum(width);
        slider2.setMaximum(width);
    }

    public void setHeight(int height){
        slider3.setMaximum(height);
        slider4.setMaximum(height);
    }

    public cosineListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        JPanel panel = new JPanel();
        panel.setLayout(null);
        slider1 = new JSlider(0,0);
        slider2 = new JSlider(0,0);
        slider3 = new JSlider(0,0);
        slider4 = new JSlider(0,0);
        label1 = new JLabel("0");
        label2 = new JLabel("0");
        label3 = new JLabel("0");
        label4 = new JLabel("0");
        panel.add(slider1);
        slider1.setBounds(20, 20, 270, 20);
        panel.add(label1);
        label1.setBounds(290, 20, 30, 20);
        panel.add(slider2);
        slider2.setBounds(20, 70, 270, 20);
        panel.add(label2);
        label2.setBounds(290, 70, 30, 20);
        panel.add(slider3);
        slider3.setBounds(20, 120, 270, 20);
        panel.add(label3);
        label3.setBounds(290, 120, 30, 20);
        panel.add(slider4);
        slider4.setBounds(20, 170, 270, 20);
        panel.add(label4);
        label4.setBounds(290, 170, 30, 20);

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                label1.setText(String.valueOf(slider1.getValue()));
            }
        });

        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                label2.setText(String.valueOf(slider2.getValue()));
            }
        });

        slider3.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                label3.setText(String.valueOf(slider3.getValue()));
            }
        });

        slider4.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                label4.setText(String.valueOf(slider4.getValue()));
            }
        });
        ok = new JButton("确定");
        panel.add(ok);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(mainWindow.image!=null) {
                    a = slider1.getValue();
                    b = slider2.getValue();
                    c = slider3.getValue();
                    d = slider4.getValue();
                    FFT trans = new FFT(mainWindow.image);
                    BufferedImage[] spec = trans.fct_image(a,b,c,d);
                    label5.setIcon(new ImageIcon(spec[1]));
                    Container c1 = frame1.getContentPane();
                    c1.add(label5);
                    frame1.pack();
                    frame1.setVisible(true);
                    label6.setIcon(new ImageIcon(spec[0]));
                    Container c2 = frame2.getContentPane();
                    c2.add(label6);
                    frame2.pack();
                    frame2.setVisible(true);
                    dispose();
                }
            }
        });
        ok.setBounds(60, 220, 80, 30);
        cancle = new JButton("取消");
        panel.add(cancle);
        cancle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancle.setBounds(200, 220, 80, 30);

        Container c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(panel);
        this.setVisible(false);
        this.setLocationRelativeTo(null);
        this.setSize(380, 300);
        this.setResizable(false);
        this.setTitle("像素采样[x1,x2]->[y1,y2]");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        this.setVisible(true);
    }

}
