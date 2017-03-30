import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 寒烛 on 2017/3/27.
 */
public class GrayStretchListener extends JFrame implements ActionListener {
    private MainWindow mainWindow;
    private JSlider slider1,slider2,slider3,slider4;
    private JLabel label1,label2,label3,label4;
    private JButton ok,cancle;
    private int a, b, c, d;
    public GrayStretchListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        JPanel panel = new JPanel();
        panel.setLayout(null);
        slider1 = new JSlider(0, 255);
        slider2 = new JSlider(0, 255);
        slider3 = new JSlider(0, 255);
        slider4 = new JSlider(0, 255);
        label1 = new JLabel("127");
        label2 = new JLabel("127");
        label3 = new JLabel("127");
        label4 = new JLabel("127");
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
            @Override
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
                a = slider1.getValue();
                b = slider2.getValue();
                c = slider3.getValue();
                d = slider4.getValue();
                int[] resultArray = new int[mainWindow.width * mainWindow.height];
                int oldGray, newGray;
                for (int i = 0; i < mainWindow.height; i++) {
                    for (int j = 0; j < mainWindow.width; j++) {
                        oldGray = mainWindow.currPixArray[i * mainWindow.width + j] & 0x0000ff;
                        if (oldGray < a)
                            newGray = c;
                        else if (oldGray < b)
                            newGray = (int) (1.0 * (d - c) / (b - a) * (oldGray - a) + c);
                        else
                            newGray = d;
                        resultArray[i * mainWindow.width + j] = 255 << 24 | newGray << 16 | newGray << 8 | newGray;
                    }
                }
                mainWindow.currPixArray = resultArray;
                mainWindow.showImage(resultArray);
                dispose();
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
        this.setTitle("区间设置[a,b]->[c,d]");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        this.setVisible(true);
    }
}