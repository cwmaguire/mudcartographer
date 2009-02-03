/*
Copyright 2008 Chris Maguire (cwmaguire@gmail.com)

MUD Cartographer is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

MUD Cartographer is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with MUD Cartographer.  If not, see <http://www.gnu.org/licenses/>.
 */package mudcartographer;

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
