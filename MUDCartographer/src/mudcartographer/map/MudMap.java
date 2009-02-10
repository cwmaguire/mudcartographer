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
package mudcartographer.map;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages a collection of Rooms that, together, form a map.
 */
public class MudMap{
    private Room initialRoom;
    private Room currentRoom;
    private Room previousRoom;
    // in relation to all other rooms, what would the X and Y coords of the initial room be?
    // (i.e. if we have one room, the coords are 1,1; if we add a room to the west, the new
    //  coordinates of the initial room are now 2,1)
    private Point initialPoint = new Point(0,0);
    private int currentOperationID;
    private boolean isChanged;
    private boolean isCurrentRoomChanged;
    private boolean hasNewConnection;
    private Class roomClass;


    // create a map of keyboard keys to Directions
    private static Map<Integer, Direction> keyEventDirections = new HashMap<Integer, Direction>();

    public MudMap(Class pluginRoomClass){
        this.roomClass = pluginRoomClass;
    }

    static {
        /**
         * Register all the different applicable keys to the appropriate direction
         */
        keyEventDirections.put(KeyEvent.VK_8, Direction.NORTH);
        keyEventDirections.put(KeyEvent.VK_UP, Direction.NORTH);
        keyEventDirections.put(KeyEvent.VK_NUMPAD8, Direction.NORTH);
        keyEventDirections.put(KeyEvent.VK_2, Direction.SOUTH);
        keyEventDirections.put(KeyEvent.VK_DOWN, Direction.SOUTH);
        keyEventDirections.put(KeyEvent.VK_NUMPAD2, Direction.SOUTH);
        keyEventDirections.put(KeyEvent.VK_4, Direction.WEST);
        keyEventDirections.put(KeyEvent.VK_LEFT, Direction.WEST);
        keyEventDirections.put(KeyEvent.VK_NUMPAD4, Direction.WEST);
        keyEventDirections.put(KeyEvent.VK_6, Direction.EAST);
        keyEventDirections.put(KeyEvent.VK_RIGHT, Direction.EAST);
        keyEventDirections.put(KeyEvent.VK_NUMPAD6, Direction.EAST);
        keyEventDirections.put(KeyEvent.VK_7, Direction.NW);
        keyEventDirections.put(KeyEvent.VK_NUMPAD7, Direction.NW);
        keyEventDirections.put(KeyEvent.VK_9, Direction.NE);
        keyEventDirections.put(KeyEvent.VK_NUMPAD9, Direction.NE);
        keyEventDirections.put(KeyEvent.VK_1, Direction.SW);
        keyEventDirections.put(KeyEvent.VK_NUMPAD1, Direction.SW);
        keyEventDirections.put(KeyEvent.VK_3, Direction.SE);
        keyEventDirections.put(KeyEvent.VK_NUMPAD3, Direction.SE);
    }

    public Room getInitialRoom(){
        return initialRoom;
    }

    public void setupInitialRoom(){
        Room.resetRoomID();
        this.initialRoom = createRoomInstance();
        setCurrentRoom(initialRoom);
    }

    public Point getInitialPoint(){
        return initialPoint;
    }

    public void setInitialPoint(Point initialPoint){
        this.initialPoint = initialPoint;
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * Set the current room (for highlighting), mark the current rooms as changed
     * and set the previous room to, wonder of wonders, the previous room
     *
     * @param currentRoom the new current room
     */
    public void setCurrentRoom(Room currentRoom){
        if(this.currentRoom == null || !this.currentRoom.equals(currentRoom)){
            setPreviousRoom(this.currentRoom);
            setCurrentRoomChanged(true);
        }
        this.currentRoom = currentRoom;
    }

    public Room getPreviousRoom(){
        return previousRoom;
    }

    public void setPreviousRoom(Room previousRoom){
        this.previousRoom = previousRoom;
    }

    public int getNewCurrentOperationID(){
        return currentOperationID += 1;
    }


    /**
     * Has the map changed?
     *
     * @return whether the map has changed
     */
    public boolean isChanged(){
        return isChanged;
    }

    /**
     * Flag the map as changed (i.e. we've added a room)
     *
     * @param changed flag stating if the map has changed
     */
    public void setChanged(boolean changed){
        isChanged = changed;
    }

    /**
     * Is the current room now a different room?
     *
     * Note: this is manual
     *
     * @return true if the current room is not a different room
     */
    public boolean isCurrentRoomChanged(){
        return isCurrentRoomChanged;
    }

    public boolean hasNewConnection(){
        return hasNewConnection;
    }

    public void setHasNewConnection(boolean hasNewConnection){
        this.hasNewConnection = hasNewConnection;
    }

    /**
     * Mark the current room as changed or unchanged
     *
     * @param currentRoomChanged flag saying whether the room has been changed or not
     */
    public void setCurrentRoomChanged(boolean currentRoomChanged){
        isCurrentRoomChanged = currentRoomChanged;
    }

    /**
     * Create a new room in the map in the direction specified, relative to the current room
     * @param d the direction, relative to the current room, to create the new room in
     * @return the new room that was created
     */
    public Room createRoom(Direction d){
        return createRoom(d, Room.DEFAULT_SYMBOL);
    }

    /**
     * Create a new room in the map in the direction specified, relative to the current room
     * @param d the direction, relative to the current room, to create the new room in
     * @param symbol the symbol to use for the new room (i.e. it's "icon")
     * @return the new room that was created
     */
    public Room createRoom(Direction d, char symbol){
        Room newRoom = createRoomInstance();

        // ToDo: add insertion logic (i.e. inserting one room *between* two rooms

        // attach the new room to the current room in the specified direction ...
        currentRoom.setRoom(newRoom, d);
        // ... and attach this room to the new room in the opposite direction
        newRoom.setRoom(currentRoom, d.getOpposite());

        //currentRoom = newRoom;
        setCurrentRoom(newRoom);

        // mark that we've changed the map
        setChanged(true);

        return newRoom;
    }

    private Room createRoomInstance(){
        Room room;
        try{
            room = (Room) roomClass.newInstance();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Could not create new room of class " + (roomClass == null ? "[null]" : roomClass.getName()));
        }
        return room;
    }

    /**
     * Wrapper for selectOrCreateRoom
     * @param e key event specifying direction to move
     * @return the new current room with focus
     */
    public Room selectOrCreateRoom(KeyEvent e){
        Direction d = getKeyDirection(e);

        if(d != null){
            return selectOrCreateRoom(d);
        }

        return currentRoom;
    }

    /**
     * Move the focus to a new room; create the room if it doesn't exist
     * @param d the direction to move
     * @return the new current room with focus
     */
    public Room selectOrCreateRoom(Direction d){
        Room newCurrentRoom;
        Point origin = new Point(0,0);
        // update the current operation in case we need to get a room at a point
        currentOperationID = getNewCurrentOperationID();

        // Move to the room in the specified direction if one exists connected to this room
        if((newCurrentRoom = currentRoom.getRoom(d)) != null){
            setCurrentRoom(newCurrentRoom);

        // else connect and move to room in specified direction if one exists but is not directly
        // connected to this room
        }else if((newCurrentRoom = getRoomAtCoordinate(currentRoom, origin, d.translatePoint(origin))) != null){
            currentRoom.setRoom(newCurrentRoom, d);
            newCurrentRoom.setRoom(currentRoom, d.getOpposite());

            //currentRoom = newCurrentRoom;
            setCurrentRoom(newCurrentRoom);
            setHasNewConnection(true);

        // else create a new room in the specified direction
        }else{

            setCurrentRoom(createRoom(d));
        }

        return currentRoom;
    }

    /**
     * Walk through each room in the map and check if any room matches the
     * coordinates of the direction specified
     *
     * @param r the room to check to see if it is at the target coordinates
     * @param coordinate the coordinates of the room to check
     * @param targetCoordinate the target coordinates to check for a room
     * @return the adjacent room if one exists or null
     */
    private Room getRoomAtCoordinate(Room r, Point coordinate, Point targetCoordinate){
        Room nextRoom;
        Room adjacentRoom;

        if(coordinate.equals(targetCoordinate) || r == null){
            return r;
        }

        // mark this room as "touched" before we do anything
        r.setCurrentOperationID(currentOperationID);

        // ToDo optimize this so it tries the directions that are closest to the *current* target direction (e.g. if N, try NE and NW first, then E, W, etc.)
        for(Direction d: MudMap.Direction.values()){
            nextRoom = r.getRoom(d);
            if(nextRoom != null && nextRoom.getCurrentOperationID() < currentOperationID){
                adjacentRoom = getRoomAtCoordinate(nextRoom, d.translatePoint(coordinate), targetCoordinate);
                if(adjacentRoom != null){
                    return adjacentRoom;
                }
            }
        }

        return null;
    }

    /**
     * Return a point that represents the farthest possible room to the north and west.
     * This will tell us how far from the top of the screen and from the left of the screen
     * we need to draw the initial room in order to have enough space to draw rooms to the west
     * and north of that room.
     *
     * @return a point representing the farthest north and farthest west that any rooms are (not, however, a
     *         point representing a single room)
     */
    public Point[] getMaxPoints(){
        Point[] maxPoints = new Point[]{new Point(0,0), new Point(0,0)};
        currentOperationID = getNewCurrentOperationID();
        return getNorthWesternmostPoint(initialRoom, initialPoint, maxPoints);
    }

    /**
     * Simple constants for keeping track of which point
     * in an array of "max" points is NW and which is SE
     */
    public enum Points{
        NW,
        SE
    }

    /**
     * Recursively travel through all rooms comparing each rooms point, relative to a
     * starting point, to a "max" point and seeing if the room is farther north, or
     * farther west, than the current "max". It does not have to be both, all we're
     * looking for is how much space to leave on the screen to the north and west of
     * the initial room when we draw the map.
     *
     * @param r the room to check against the current "max" coordinate
     * @param p the point the room is at, relative to some origin room
     * @param maxPoints an array of the two current "max" coordinates to compare this room's coordinates to
     * @return the "max" coordinate after we've made any necessary updates to it
     */
    private Point[] getNorthWesternmostPoint(final Room r, final Point p, Point[] maxPoints){
        Room nextRoom;
        Point maxNW = maxPoints[Points.NW.ordinal()];
        Point maxSE = maxPoints[Points.SE.ordinal()];
        // mark this room as drawn
        r.setCurrentOperationID(currentOperationID);

        // if p is farther north or west then max, update max
        if(p.getX() > maxNW.getX()){
            maxNW.move((int) p.getX(), (int) maxNW.getY());
        }
        if(p.getY() > maxNW.getY()){
            maxNW.move((int) maxNW.getX(), (int) p.getY());
        }
        if(p.getX() < maxSE.getX()){
            maxSE.move((int) p.getX(), (int) maxSE.getY());
        }
        if(p.getY() < maxSE.getY()){
            maxSE.move((int) maxSE.getX(), (int) p.getY());
        }

        // travel through the connected rooms
        for(MudMap.Direction d : MudMap.Direction.values()){
            nextRoom = r.getRoom(d);
            if(nextRoom != null && nextRoom.getCurrentOperationID() < currentOperationID){
                // call ourself again with the next room and the coordinates of the
                // next room relative to this room (which will recursively relative
                // to the initial room).
                // Notice that we use the ROOM translation mode as going north means
                // we're a greater distance from the starting point.
                maxPoints = getNorthWesternmostPoint(nextRoom, d.translatePoint(p, Direction.CoordinateOrigin.ROOM), maxPoints);
            }
        }

        return maxPoints;
    }

    /**
     * Return the Direction corresponding to a keyboard key
     * @param e the key event to get the direction for (e.g. KeyEvent.VK_UP)
     * @return the direction corresponding to the supplied key event key
     */
    public Direction getKeyDirection(KeyEvent e){
        return keyEventDirections.get(e.getKeyCode());
    }

    /**
     * Enum to represent directions, their opposite directions and how to translate a point
     * to represent "movement" in that direction
     */
    public enum Direction{
        NORTH("North"){
            public Point getTranslation(){
                return new Point(0, 1);
            }
            public Direction getOpposite(){
                return SOUTH;
            }
        },
        SOUTH("South"){
            public Point getTranslation(){
                return new Point(0, -1);
            }
            public Direction getOpposite(){
                return NORTH;
            }
        },
        EAST("East"){
            public Point getTranslation(){
                return new Point(1, 0);
            }
            public Direction getOpposite(){
                return WEST;
            }
        },
        WEST("West"){
            public Point getTranslation(){
                return new Point(-1, 0);
            }
            public Direction getOpposite(){
                return EAST;
            }
        },
        NE("Northeast"){
            public Point getTranslation(){
                return new Point(1, 1);
            }
            public Direction getOpposite(){
                return SW;
            }
        },
        NW("Northwest"){
            public Point getTranslation(){
                return new Point(-1, 1);
            }
            public Direction getOpposite(){
                return SE;
            }
        },
        SE("Southeast"){
            public Point getTranslation(){
                return new Point(1, -1);
            }
            public Direction getOpposite(){
                return NW;
            }
        },
        SW("Southwest"){
            public Point getTranslation(){
                return new Point(-1, -1);
            }
            public Direction getOpposite(){
                return NE;
            }
        },
        UP("Up"){
            public Point getTranslation(){
                return new Point(0, 0);
            }
            public Direction getOpposite(){
                return DOWN;
            }
        },
        DOWN("Down"){
            public Point getTranslation(){
                return new Point(0, 0);
            }
            public Direction getOpposite(){
                return UP;
            }
        };

        /**
         * Map relative points to directions; i.e. if a target room is at a point relative a source room,
         * this map will tell what direction the target room is in relative to the source room
         */
        private static Map<Point, Direction> pointDirections = new HashMap<Point, Direction>();
        private static Map<String, Direction> descriptionDirections = new HashMap<String, Direction>();
        private String description;

        static {
            pointDirections.put(new Point(0,1), NORTH);
            pointDirections.put(new Point(0, -1), SOUTH);
            pointDirections.put(new Point(1, 0), EAST);
            pointDirections.put(new Point(-1, 0), WEST);
            pointDirections.put(new Point(1, 1), NE);
            pointDirections.put(new Point(1, -1), NW);
            pointDirections.put(new Point(-1, 1), SE);
            pointDirections.put(new Point(-1, -1), SW);

            for(Direction direction : Direction.values()){
                descriptionDirections.put(direction.getDescription(), direction);
            }
        }

        private Direction(String description){
            this.description = description;
        }

        public String getDescription(){
            return description;
        }

        /**
         * Wrapper for translatePoint with CoordinateOrigin defaulted to ROOM
         * @param p the point to translate
         * @return the point translated according to the ROOM coordinate origin
         */
        public Point translatePoint(Point p){
            return translatePoint(p, CoordinateOrigin.ROOM);
        }

        /**
         * Translate a point according to a direction and a translation mode. The direction determines where
         * the point will end up based on direction (e.g. North should be x, y+1), the translation mode determines
         * what the coordinate should look like based on what you're doing with it (Mapping relies on
         * coordinates being relative to a starting room, drawing relies on coordinates being relative to the
         * top left of the screen)
         *
         * For example, if I'm drawing rooms and I want to go north, I need my Y coordinate to *decrease* because
         * my distance from the top of the screen should be *less*.
         *
         * @param p the point to translate
         * @param mode the translation mode to use
         * @return a point translated according to the specified direction and translation mode
         */
        public Point translatePoint(Point p, CoordinateOrigin mode){
            Point newPoint = new Point(p);
            // get the direction's translation, then run it through the translation mode translator
            Point translation = mode.getTranslation(getTranslation());
            newPoint.translate((int)translation.getX(), (int)translation.getY());
            return newPoint;
        }

        /**
         * get the translation necessary to go in the specified direction
         * @return a point representing the delta-x and delta-y required to translate in the specified direction
         */
        protected abstract Point getTranslation();

        /**
         * Based on a direction, get its opposite direction
         * @return the opposite direction
         */
        public abstract Direction getOpposite();

        public static Direction getDirection(Point p){
            return pointDirections.get(p);
        }

        public static MudMap.Direction getDirection(String description){
          return descriptionDirections.get(description);
        }

        /**
         * Drawing and finding the max coordinates of a map require different
         * coordinate "strategies":
         *
         * Mapping: going North is positive, as usual, but going west is positive since
         * rooms to the west of the origin room mean _more_ distance from the origin room
         * to the edge of the screen
         *
         * Drawing: going West is negative, as usual, because we're going closer to the
         * left edge of the screen which means there is _less_ distance from the edge; going
         * North is negative as well, again because we're going closer to the top of the screen
         * meaning there is _less_ distance from the top of the screen
         *
         * (For the max possible coordinates, think of drawing a box around all the rooms
         *  and then finding where the top-left (or northwest) corner would be)
         */
        public enum CoordinateOrigin{
            ROOM{
                public Point getTranslation(Point p){
                    return new Point(- (int) p.getX(), (int) p.getY());
                }
            },
            SCREEN{
                public Point getTranslation(Point p){
                    return new Point((int) p.getX(), -(int) p.getY());
                }
            };

            public abstract Point getTranslation(Point p);
        }
    }
}
