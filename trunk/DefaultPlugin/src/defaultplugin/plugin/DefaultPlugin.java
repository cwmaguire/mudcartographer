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
import mudcartographer.plugin.Plugin;

import java.awt.*;
import java.util.List;

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
}
