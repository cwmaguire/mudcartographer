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
 */package mudcartographer;

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
