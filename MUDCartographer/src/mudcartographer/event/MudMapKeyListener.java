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
package mudcartographer.event;

import mudcartographer.MudController;
import mudcartographer.map.MudMap;
import mudcartographer.map.Room;
import mudcartographer.map.RoomProperty;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles the key events for a Mud map
 */
public class MudMapKeyListener extends KeyAdapter{

    private MudMap map;
    private MudController controller;

    public MudMapKeyListener(MudMap map, MudController controller){
        this.map = map;
        this.controller = controller;
    }

    /**
     * Invoked when a key has been released.
     */
    public void keyReleased(KeyEvent e){
        //MapPainter painter = (MapPainter) e.getSource();
        Room room;
        int property = 0;

        if(Character.isLetter(e.getKeyChar())){
            room = map.getCurrentRoom();
            room.setSymbol(e.getKeyChar());
            property = RoomProperty.SYMBOL.getFlagBits();
        }else{
            room = map.selectOrCreateRoom(e);
            property = RoomProperty.getAll();
        }

        // update the room info panel
        //roomInfoPanel.updateRoomInfo(map.getCurrentRoom());


        //painter.repaint();

        // tell the scroll pane to recalculate it's scroll bars
        //painter.revalidate();

        controller.fireRoomEvent(new RoomEvent(room, property, this));
    }
}
