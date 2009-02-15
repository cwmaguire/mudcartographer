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

import mudcartographer.MudCartographer;
import mudcartographer.MudController;
import mudcartographer.plugin.Plugin;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

public class MudSaveAsMenuItem extends MudMenuItem {
    List<FileFilter> pluginFileFilters;

    public MudSaveAsMenuItem(final MudController controller) {
        final Plugin plugin = controller.plugin;
        if(plugin != null){
            pluginFileFilters = plugin.getFileFilters();
        }

        setText("Save as ...");
        setMnemonic(KeyEvent.VK_A);

        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        getAccessibleContext().setAccessibleDescription("Save the map in a particular format");

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setAcceptAllFileFilterUsed(false);
                if (pluginFileFilters != null) {
                    for (FileFilter filter : pluginFileFilters) {
                        fileChooser.addChoosableFileFilter(filter);
                    }
                } else {
                    throw new RuntimeException("Plugin " + plugin.getClass().getName() + " does not specify any file extensions. That's bad. \"Bad plugin!\"");
                }
                fileChooser.showSaveDialog(null);
                File file = fileChooser.getSelectedFile();
                System.out.println("File name is " + (file == null ? "[null]" : file.getName()));

                if(file != null){
                    addFileExtensionIfNecessary(fileChooser, file);

                    plugin.writeMap(file, controller.mudMap, fileChooser.getFileFilter());
                }
            }
        });

    }

    private void addFileExtensionIfNecessary(JFileChooser fileChooser, File file) {
        FileFilter fileFilter = fileChooser.getFileFilter();

        if(fileFilter.getClass().equals(FileNameExtensionFilter.class) && !file.getName().contains(".")){
            boolean isRenamed = file.renameTo(new File(file.getName() + "." + ((FileNameExtensionFilter) fileFilter).getExtensions()[0]));
            if(!isRenamed){
                JOptionPane.showMessageDialog(MudCartographer.mudCartographer.getFrame(), "Unable to add filename extension", "Unable To Add Extension", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setIsMapLoaded(boolean isMapLoaded) {
        setEnabled(isMapLoaded);
    }
}
