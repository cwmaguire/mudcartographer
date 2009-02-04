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
package mudcartographer;

import mudcartographer.event.RoomEvent;
import mudcartographer.event.RoomEventListener;

import java.util.HashSet;
import java.util.Set;

/**
 * MVC Controller that handles communication between the GUI, Map and Room objects
 */
public class MudController{
    private static MudController mudController;

    private Set<RoomEventListener> listeners;

    /**
     * Don't allow users to create new MudControllers
     */
    private MudController(){
        listeners = new HashSet<RoomEventListener>();
    }

    /**
     * Only allow one MudController
     *
     * @return the only MudController
     */
    public static MudController getMudController(){
        if(mudController == null){
            mudController = new MudController();
        }
        return mudController;
    }

    /**
     * Allow classes to cause events through the controller
     * @param roomEvent the event to be caused
     */
    public void fireRoomEvent(RoomEvent roomEvent){
        // fire events against registered targets that aren't the source
        for(RoomEventListener listener : listeners){
            // to match one set to another I going to bitwise and them
            if(!listener.equals(roomEvent.getSource()) && (roomEvent.getProperties() & listener.getRelevantRoomEventFlags()) > 0){
                listener.updateRoom(roomEvent.getRoom());
            }
        }
    }

    public void addListener(RoomEventListener listener){
        listeners.add(listener);
    }

    public void removeListener(RoomEventListener listener){
        listeners.remove(listener);
    }
}
