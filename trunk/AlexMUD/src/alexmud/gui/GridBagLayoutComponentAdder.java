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
 */
package alexmud.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GridBagLayoutComponentAdder {
    private Insets initialInsets;
    private Insets regularInsets;
    private int row;
    public JPanel panel;
    public GridBagConstraints c;

    public GridBagLayoutComponentAdder(int initialTopOffset, int regularTopOffset){
        this.initialInsets = new Insets(initialTopOffset, 0, 0, 0);
        this.regularInsets = new Insets(regularTopOffset, 0, 0, 0);
    }

    public void add(List<JComponent> components){
        if(components != null && components.size() > 1){
            if(row == 0){
                c.insets = initialInsets;
            }else{
                c.insets = regularInsets;
            }

            c.gridx = 0;
            c.gridy = row++;
            panel.add(components.get(0), c);

            c.gridx = 1;
            panel.add(components.get(1), c);
        }
    }

    public void addFiller(){
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        c.weighty = 1.0;
        panel.add(new JPanel(), c);
    }
}
