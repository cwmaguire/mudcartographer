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
package mudcartographer.plugin;

import mudcartographer.gui.MapPainter;
import mudcartographer.file.reader.FileReader;
import mudcartographer.file.writer.FileWriter;

import javax.swing.*;

public interface Plugin {
    public JPanel getRoomDescriptionPanel();
    public JPanel getRoomInfoPanel();
    public MapPainter getMapPainter();
    public FileReader getFileReader();
    public FileWriter getFileWriter();
}
