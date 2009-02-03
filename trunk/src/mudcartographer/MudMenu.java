/*
 * Copyright 2008 Chris Maguire. All Rights Reserved.
 */
package mudcartographer;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class MudMenu extends JMenuBar{

    public void setup(){
        JMenu menu;
        JMenuItem menuItem;

        menu = new JMenu("File");

        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext(). setAccessibleDescription( "Open, Save, Export, etc.");

        this.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("Save as ...", KeyEvent.VK_A);

        menuItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_A,ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription( "Save the map in a particular format");

        menu.add(menuItem);
    }
}
