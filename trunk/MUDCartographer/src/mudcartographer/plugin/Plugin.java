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

import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

public interface Plugin {
    public Class getRoomDescriptionPanelClass();
    public Class getRoomInfoPanelClass();
    public Class getRoomClass();
    public List<Component> getMenuComponents(String menuText);
    public void setup();
    public java.util.List<FileFilter> getFileFilters();
    public void writeMap(File file, FileFilter fileFilter);
}
