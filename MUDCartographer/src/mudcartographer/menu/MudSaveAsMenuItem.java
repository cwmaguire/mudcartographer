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

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;

public class MudSaveAsMenuItem extends MudMenuItem {
    public MudSaveAsMenuItem(){
        setText("Save as ...");
        setMnemonic(KeyEvent.VK_A);

        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        getAccessibleContext().setAccessibleDescription("Save the map in a particular format");

        addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG", "jpg"));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML", "xml"));
                fileChooser.showSaveDialog(null);
                File file = fileChooser.getSelectedFile();
                System.out.println("File name is " + (file == null ? "[null]" : file.getName()));
            }

            public void writeJPG(File file){

            }

            public void writeXML(File file){
                try {
                    // Create file output stream.
                    FileOutputStream fstream = new FileOutputStream(file);

                    try {
                        // Create XML encoder.
                        XMLEncoder ostream = new XMLEncoder(fstream);

                        try {
                            // Write object.
                            ostream.writeObject(new Object());
                            ostream.flush();
                        } finally {
                            // Close object stream.
                            ostream.close();
                        }
                    } finally {
                        // Close file stream.
                        fstream.close();
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });

    }

    public void setIsMapLoaded(boolean isMapLoaded) {
        setEnabled(isMapLoaded);
    }
}
