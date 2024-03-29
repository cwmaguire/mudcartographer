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

import defaultplugin.gui.DefaultDescriptionPanel;
import defaultplugin.gui.DefaultInfoPanel;
import defaultplugin.map.DefaultRoom;
import defaultplugin.file.XMLFileWriter;
import mudcartographer.plugin.Plugin;
import mudcartographer.map.MudMap;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.List;
import java.util.Arrays;
import java.io.File;

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

    public void setup(){

    }

    public List<Component> getMenuComponents(String menuText) {
        return null;
    }

    public List<FileFilter> getFileFilters(){
        return Arrays.asList((FileFilter) new FileNameExtensionFilter("MudCartographer", "mcd"));
    }

    public void writeMap(File file, MudMap map, FileFilter fileFilter) {
        new XMLFileWriter().write(file, map);
    }
}
