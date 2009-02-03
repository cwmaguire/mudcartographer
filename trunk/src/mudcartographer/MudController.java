/*
 * Copyright 2008 Entero Corporation. All Rights Reserved.
 * www.entero.com
 */
package mudcartographer;

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
            if(!listener.equals(roomEvent.getSource()) && (roomEvent.getProperties() & listener.getProperties()) > 0){
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
