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
package mudcartographer.menu;

import mudcartographer.plugin.PluginImpl;
import mudcartographer.MudCartographer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewMenuItem extends JMenuItem {
    public NewMenuItem(final PluginImpl pluginImpl) {
        setText(pluginImpl.getDescription());
        setMnemonic(pluginImpl.getKeyEvent());

        setAccelerator(pluginImpl.getKeyStroke());
        getAccessibleContext().setAccessibleDescription("Create a new " + pluginImpl.getDescription() + " map");

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MudCartographer.mudCartographer.loadPlugin(pluginImpl.getPlugin());
            }
        });


    }
}