package Tests;

import Graphics.MenuGUI;

import javax.swing.*;

/**
 * Created by giogio on 12/28/16.
 */
public class MenuTest {
    public static void main(String[] args){

        JFrame frame = new JFrame();
        MenuGUI menuGUI = new MenuGUI(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(menuGUI);
        frame.pack();
        frame.setLocationRelativeTo(null);



        frame.setVisible(true);

    }
}
