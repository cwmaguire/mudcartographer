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
package mudcartographer.gui;

import mudcartographer.MudController;
import mudcartographer.event.RoomEventListener;
import mudcartographer.map.Room;

import javax.swing.*;

/**
 * Handles creating and maintaining a panel to
 * display room descriptions
 */
public abstract class RoomDescriptionPanel extends JPanel implements RoomEventListener {
    private static int ROOM_PROPERTIES = Room.RoomProperty.DESCRIPTION.getFlag();

    public RoomDescriptionPanel(){}

    public abstract void initialize(MudController controller);

    /**
     * Sets the enabled state of description box
     * @param isEnabled whether this panel should be enabled or not
     */
    @Override
    public void setEnabled(boolean isEnabled){
        super.setEnabled(isEnabled);
    }

    public boolean getEnabled(){
        return super.isEnabled();
    }

    /**
     * Facade method for setting all the information
     * in the room description panel
     *
     * @param room the room to update the description panel with
     */
    public abstract void updateRoom(Room room);

    /**
     * Get the list of Room properties that this component listens for
     * @return list of properties to listen for
     */
    public int getRelevantRoomEventFlags(){
        return ROOM_PROPERTIES;
    }
}