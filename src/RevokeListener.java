import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 寒烛 on 2017/3/27.
 */

public class RevokeListener implements ActionListener {
    private MainWindow mainWindow;

    public RevokeListener(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }

    public void actionPerformed(ActionEvent e) {
        mainWindow.currPixArray=mainWindow.sourcePixArray;
        mainWindow.showImage(mainWindow.currPixArray);
    }
}