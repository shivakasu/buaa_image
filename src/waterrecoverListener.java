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
public class waterrecoverListener extends JFrame implements ActionListener {
    private MainWindow mainWindow;
    private JFrame frame1 = new JFrame("水印图像");
    private JLabel label5=new JLabel();
    private JLabel label6=new JLabel();
    private JSlider slider1,slider2,slider3;
    private JLabel label1,label2,label3;
    private JButton ok,cancle;
    private double a;
    private int b, c;

    public waterrecoverListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        JPanel panel = new JPanel();
        panel.setLayout(null);
        slider1 = new JSlider(1,100,1);
        slider2 = new JSlider(1,8,1);
        slider3 = new JSlider(1,8,1);
        label1 = new JLabel("0.01");
        label2 = new JLabel("1");
        label3 = new JLabel("1");
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

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                label1.setText(String.valueOf(slider1.getValue()/100.0));
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

        ok = new JButton("确定");
        panel.add(ok);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(mainWindow.image!=null) {
                    a = slider1.getValue()/100.0;
                    b = slider2.getValue();
                    c = slider3.getValue();
                    waterMark trans = new waterMark(mainWindow.image,a,b,c);
                    BufferedImage spec = trans.extract();
                    label5.setIcon(new ImageIcon(spec));
                    Container c1 = frame1.getContentPane();
                    c1.add(label5);
                    frame1.pack();
                    frame1.setVisible(true);
                    dispose();
                }
            }
        });
        ok.setBounds(60, 170, 80, 30);
        cancle = new JButton("取消");
        panel.add(cancle);
        cancle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancle.setBounds(200, 170, 80, 30);

        Container c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(panel);
        this.setVisible(false);
        this.setLocationRelativeTo(null);
        this.setSize(380, 250);
        this.setResizable(false);
        this.setTitle("参数设置 alpha-W-H");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        this.setVisible(true);
    }

}
