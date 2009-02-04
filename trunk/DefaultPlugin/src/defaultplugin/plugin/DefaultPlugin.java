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
package defaultplugin.plugin;

import defaultplugin.file.reader.DefaultReader;
import defaultplugin.file.writer.DefaultWriter;
import defaultplugin.map.DefaultRoom;
import defaultplugin.gui.DefaultDescriptionPanel;
import defaultplugin.gui.DefaultInfoPanel;
import mudcartographer.file.reader.FileReader;
import mudcartographer.file.writer.FileWriter;
import mudcartographer.plugin.Plugin;

public class DefaultPlugin implements Plugin {
    public Class getRoomDescriptionPanelClass() {
        return DefaultDescriptionPanel.class;
    }

    public Class getRoomInfoPanelClass() {
        return DefaultInfoPanel.class;
    }

    public Class getRoomClass() {
        return DefaultRoom.class;
    }

    public FileReader getFileReader() {
        return new DefaultReader();
    }

    public FileWriter getFileWriter() {
        return new DefaultWriter();
    }
}
