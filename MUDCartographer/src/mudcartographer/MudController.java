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
import mudcartographer.map.MudMap;
import mudcartographer.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MVC Controller that handles communication between the GUI, Map and Room objects
 */
public class MudController{
    private static MudController mudController;
    public Plugin plugin;
    public MudMap mudMap;

    private Set<RoomEventListener> listeners;

    private MudController(){
        listeners = new HashSet<RoomEventListener>();
    }

    public static MudController getMudController(){
        if(mudController == null){
            mudController = new MudController();
        }
        return mudController;
    }

    public void fireRoomEvent(RoomEvent roomEvent){
        for(RoomEventListener listener : listeners){
            if(isListenerNotSource(roomEvent.getSource(), listener) && isListenerInterestedInEvent(roomEvent, listener)){
                listener.updateRoom(roomEvent.getRoom());
            }
        }
    }

    private boolean isListenerInterestedInEvent(RoomEvent roomEvent, RoomEventListener listener) {
        return (roomEvent.getProperties() & listener.getRelevantRoomEventFlags()) > 0;
    }

    private boolean isListenerNotSource(Object source, RoomEventListener listener) {
        return !listener.equals(source);
    }

    public void releaseFocus(){
        for(RoomEventListener listener : listeners){
           if(listener.takeFocus()){
               return;
            }
        }
    }

    public void addListeners(List<RoomEventListener> listeners){
        for(RoomEventListener listener : listeners){
            addListener(listener);
        }
    }

    public void addListener(RoomEventListener listener){
        listeners.add(listener);
    }

    public void removeListener(RoomEventListener listener){
        listeners.remove(listener);
    }

    public void removeAllListeners(){
        listeners.clear();
    }
}
