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
import mudcartographer.event.MudMapKeyListener;
import mudcartographer.event.RoomEvent;
import mudcartographer.event.RoomEventListener;
import mudcartographer.map.MudMap;
import mudcartographer.map.Room;
import mudcartographer.map.RoomProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Paints a MUD Map by drawing it's rooms and connections
 */
public class MapPainter extends JComponent implements Scrollable, RoomEventListener {
    private static int NEXT_ID;
    private static int RELEVANT_ROOM_EVENT_FLAGS = RoomProperty.BACKGROUND_COLOR.getFlagBits() |
                                         RoomProperty.TEXT_COLOR.getFlagBits() |
                                         RoomProperty.SYMBOL.getFlagBits() |
                                         RoomProperty.PAINT.getFlagBits();

    private MudMap map;
    public MudController controller;
    private BufferedImage buff;
    private Graphics2D g2Buff;
    private int currentOperationID;
    private static Map<Room, List<Room>> drawnLines = new HashMap<Room, List<Room>>();
    private Point[] maxPoints = {new Point(0,0), new Point(0,0)};
    private int ID;
    private Map<Rectangle, Room> rectangleRooms = new HashMap<Rectangle, Room>();

    /**
     * Construct a new Painter object
     *
     * @param map the mud map to paint
     */
    public MapPainter(MudMap map){
        this.ID = NEXT_ID++;
        this.map = map;
    }

    public int getID(){
        return this.ID;
    }

    /**
     * Tell any scroll pane or container what size we are after we've painted
     *
     * NOTE: I don't know why I need to add two to the total height and width of the map
     *       (if I don't, the last room gets cut off)
     *
     * @return the width of a room plus the between-room space multiplied by the number of rooms
     *         wide and high, plus spacing
     */
    @Override
    public Dimension getPreferredSize(){
        Point maxNW = maxPoints[MudMap.Points.NW.ordinal()];
        Point maxSE = maxPoints[MudMap.Points.SE.ordinal()];
        int heightPlusSpacing = Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING;
        return new Dimension(((int) maxNW.getX() - (int) maxSE.getX() + 2) * heightPlusSpacing + Room.PADDING, ((int) maxNW.getY() - (int) maxSE.getY() + 2) * heightPlusSpacing + Room.PADDING);
    }

    /**
     * Paint the MudMap out to the panel
     * @param g the graphics object to paint to
     */
    @Override
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Room initialRoom;
        Point initialPoint;
        Point maxSE;

        // if the map hasn't changed and we have a buffer image already drawn, then
        // only redraw what is required
        if(!map.isChanged() &&  buff != null){
            Room currentRoom = map.getCurrentRoom();

            // if the previously drawn image is still valid then simply redraw it
            // without having to go through each currentRoom
            if(!map.isCurrentRoomChanged() && !map.hasNewConnection() && !currentRoom.isChanged()){
                g2.drawImage(buff, 0, 0, null);
                return;
            }

            // if some rooms have changed, redraw them
            if(map.isCurrentRoomChanged()){
                // overwrite the new selected currentRoom
                currentRoom.paint(g2Buff, true);
                // overwrite the old selected currentRoom
                map.getPreviousRoom().paint(g2Buff, false);
                // unset the flag that says we need to redraw the previously and currently selected rooms
                map.setCurrentRoomChanged(false);
            }

            // if the current currentRoom's information is changed, redraw it
            if(currentRoom.isChanged()){
                currentRoom.paint(g2Buff, true);
                // unset the flag that says we need to redraw the current currentRoom
                currentRoom.setChanged(false);
            }

            // if a connection has been added, draw it
            if(map.hasNewConnection()){
                drawLine(map.getCurrentRoom().getPoint(), map.getPreviousRoom().getPoint());
                // unset the "has new connection" flag
                map.setHasNewConnection(false);
            }

        // else the map has changed or we don't yet have a buffered image
        }else{

            initialRoom = map.getInitialRoom();
            // get the max NW and SE coordinate for this map from the origin
            maxPoints = map.getMaxPoints();
            initialPoint = maxPoints[MudMap.Points.NW.ordinal()];
            maxSE = maxPoints[MudMap.Points.SE.ordinal()];

            // calculate the width and height of the buffered image
            int width = (((int) initialPoint.getX() + 1) - (int) maxSE.getX()) * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
            int height = (((int) initialPoint.getY() + 1) - (int) maxSE.getY()) * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;

            // create a buffered image to hold our map
            //buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            buff = (BufferedImage) this.createImage(width, height);
            g2Buff = buff.createGraphics();

            currentOperationID = map.getNewCurrentOperationID();
            rectangleRooms = new HashMap<Rectangle, Room>();

            // Start drawing with the initial room
            drawRoom(initialRoom, initialPoint);

            // clear the drawn lines register
            drawnLines.clear();

            // paint the buffered image out to the screen and unset the changed flags
            map.setChanged(false);
            map.setCurrentRoomChanged(false);

            // save the image to file
            /*
            try{
                File outputfile = new File("saved.png");
                ImageIO.write(buff, "png", outputfile);
            } catch(IOException e){
                System.out.println("Buffered image save failed");
            }
            */
        }

        g2.drawImage(buff, 0, 0, null);
    }

    /**
     * Draw a room and it's connections to other rooms
     *
     * @param r            the room to draw
     * @param currentGridCoordinate the coordinates to draw this room at
     */
    private void drawRoom(Room r, Point currentGridCoordinate){
        Room nextRoom;
        Point drawToPoint;
        List<Room> regdRooms1;
        List<Room> regdRooms2;
        Rectangle roomRectangle;

        // calculate where to draw based on where we are
        int x = (int) currentGridCoordinate.getX() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        int y = (int) currentGridCoordinate.getY() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;

        roomRectangle = new Rectangle(x, y, Room.BOX_WIDTH_HEIGHT, Room.BOX_WIDTH_HEIGHT);
        rectangleRooms.put(roomRectangle, r);

        // paint the room
        r.paint(g2Buff, roomRectangle, r.equals(map.getCurrentRoom()));

        // mark this room as drawn
        r.setCurrentOperationID(currentOperationID);

        // store the room (i.e. not pixel) coordinates so we can connect this room with other rooms later
        r.setPoint(currentGridCoordinate);

        // loop through the connected rooms and draw their connector lines
        for(MudMap.Direction d : MudMap.Direction.values()){
            // if we didn't find a room to draw then we might as well just continue
            if((nextRoom = r.getRoom(d)) == null){
                continue;
            }

            // get any rooms that have been registered as drawn for the current room
            regdRooms1 = drawnLines.get(r);
            regdRooms2 = drawnLines.get(nextRoom);

            // get the point we're going to draw a connection to
            drawToPoint = d.translatePoint(currentGridCoordinate, MudMap.Direction.CoordinateOrigin.SCREEN);

            // if we have a "from" point (i.e. not the initial room)
            // and we haven't drawn a connection from this room to an adjacent room,
            // then draw the connection
            if((regdRooms1 == null || !regdRooms1.contains(nextRoom)) && (regdRooms2 == null || !regdRooms2.contains(r))){

                // draw the connection
                drawLine(currentGridCoordinate, drawToPoint);

                // register that this connection has been drawn
                if(regdRooms1 == null){
                    drawnLines.put(r, Arrays.asList(nextRoom));
                } else{
                    // create a new array and add the most recent room
                    regdRooms1 = new ArrayList<Room>(regdRooms1);
                    regdRooms1.add(nextRoom);
                    drawnLines.put(r, regdRooms1);
                }
            }
        }

        // crank through all the connected rooms and draw them
        for(MudMap.Direction d : MudMap.Direction.values()){
            nextRoom = r.getRoom(d);
            if(nextRoom != null && nextRoom.getCurrentOperationID() < currentOperationID){
                drawRoom(r.getRoom(d), d.translatePoint(currentGridCoordinate, MudMap.Direction.CoordinateOrigin.SCREEN));
            }
        }
    }

    /**
     * Draw a line from a room at one point to a room at another point
     *
     * @param from the room to draw the line from
     * @param to   the room to draw the line to
     */
    private void drawLine(Point from, Point to){
        // calculate where to draw based on where we are
        int xFrom = (int) from.getX() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        int yFrom = (int) from.getY() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        int xTo = (int) to.getX() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        int yTo = (int) to.getY() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;

        // calculate where to move the end points relative to where the
        // two rooms are (i.e. move them to the sides or corners)
        int dx = new Integer((int) to.getX()).compareTo((int) from.getX());
        int dy = new Integer((int) from.getY()).compareTo((int) to.getY());

        // move the end points of the "from" room (they start at the top left corner)
        int dxFrom = dx * Room.BOX_WIDTH_HEIGHT / 2 + Room.BOX_WIDTH_HEIGHT / 2;
        int dyFrom = -dy * Room.BOX_WIDTH_HEIGHT / 2 + Room.BOX_WIDTH_HEIGHT / 2;

        // move the end points of the "to" room
        int dxTo = (-dx + 1) * Room.BOX_WIDTH_HEIGHT / 2;
        int dyTo = dy * Room.BOX_WIDTH_HEIGHT / 2 + Room.BOX_WIDTH_HEIGHT / 2;

        // we could actually just draw a line from the center of the room and then draw the room overtop, maybe

        // draw the line
        g2Buff.setColor(Color.BLACK);
        g2Buff.drawLine(xFrom + dxFrom, yFrom + dyFrom, xTo + dxTo, yTo + dyTo);
    }

    public Dimension getPreferredScrollableViewportSize(){
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction){
        return Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING;
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction){
        return Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING;
    }

    public boolean getScrollableTracksViewportWidth(){
        return false;
    }

    public boolean getScrollableTracksViewportHeight(){
        return false;
    }

    public int getRelevantRoomEventFlags(){
        return RELEVANT_ROOM_EVENT_FLAGS;
    }

    public boolean takeFocus(){
        this.requestFocusInWindow();
        return true;
    }

    public void updateRoom(Room room){
        this.repaint();
        this.revalidate();
        this.requestFocus();
    }

    public void setupEventHandling(MudController controller){
        this.controller = controller;
        setFocusable(true);
        addKeyListener(new MudMapKeyListener(map, controller));
        controller.addListener(this);
        addMouseListener();
    }

    private void addMouseListener(){
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Room clickedRoom = getRoomAtPoint(e.getPoint());
                if(clickedRoom != null){
                    map.setCurrentRoom(clickedRoom);
                    updateRoom(clickedRoom);
                    controller.fireRoomEvent(new RoomEvent(clickedRoom, RoomProperty.getAll(), this));
                }
            }
        });
    }

    private Room getRoomAtPoint(Point point){
        for(Rectangle rectangle : rectangleRooms.keySet()){
            if(rectangle.contains(point)){
                return rectangleRooms.get(rectangle);
            }
        }
        return null;
    }
}
