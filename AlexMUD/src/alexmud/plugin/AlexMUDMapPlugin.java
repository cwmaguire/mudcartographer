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
package alexmud.plugin;

import alexmud.constants.ExternalConstants;
import alexmud.file.AlexMUDFileWriter;
import alexmud.file.XMLFileWriter;
import alexmud.gui.AMRoomInfoPanel;
import alexmud.gui.AMRoomKeywordDescriptionsPanel;
import alexmud.map.AMRoom;
import mudcartographer.file.MudFileWriter;
import mudcartographer.map.MudMap;
import mudcartographer.plugin.Plugin;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlexMUDMapPlugin implements Plugin {
    private Map<FileFilter, MudFileWriter> fileWritersByFilter;

    public Class getRoomDescriptionPanelClass() {
        return AMRoomKeywordDescriptionsPanel.class;
    }

    public Class getRoomInfoPanelClass() {
        return AMRoomInfoPanel.class;
    }

    public Class getRoomClass() {
        return AMRoom.class;
    }

    public void setup(){
        ExternalConstants.load();
        fileWritersByFilter = new HashMap<FileFilter, MudFileWriter>();
        fileWritersByFilter.put(new FileNameExtensionFilter("AlexMUD", "mca"), new AlexMUDFileWriter());
        fileWritersByFilter.put(new FileNameExtensionFilter("MudCartographer", "mcd"), new XMLFileWriter());
    }

    public List<Component> getMenuComponents(String menuText) {
        return null;
    }

    public List<FileFilter> getFileFilters(){
        return Arrays.asList((FileFilter) new FileNameExtensionFilter("AlexMUD", "mca"));
    }

    public void writeMap(File file, MudMap map, FileFilter fileFilter) {
        MudFileWriter fileWriter = fileWritersByFilter.get(fileFilter);
        if(fileWriter != null){
             fileWriter.write(file, map);
        }
    }
}
