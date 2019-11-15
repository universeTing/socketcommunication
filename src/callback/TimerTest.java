package callback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * @date 2019-11-15
 * @author wangyt
 */
public class TimerTest {

    public static void main(String[] args) {
        ActionListener listener = new TimePrinter();
        Timer t = new Timer(10000, listener);
        t.start();

        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);
    }
}
