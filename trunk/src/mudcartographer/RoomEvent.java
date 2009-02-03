/*
 * Copyright 2008 Entero Corporation. All Rights Reserved.
 * www.entero.com
 */
package mudcartographer;

/**
 * Records an event that happens in MUD object and the
 * source or that event
 */
public class RoomEvent{
    private Room room;
    private int properties;
    private Object source;

    public RoomEvent(Room room, int properties, Object source){
        this.room = room;
        this.properties = properties;
        this.source = source;
    }

    public Room getRoom(){
        return room;
    }

    public void setRoom(Room room){
        this.room = room;
    }

    public int getProperties(){
        return properties;
    }

    public void setProperties(int properties){
        this.properties = properties;
    }

    public Object getSource(){
        return source;
    }

    public void setSource(Object source){
        this.source = source;
    }
}
