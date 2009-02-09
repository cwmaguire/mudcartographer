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
package alexmud.map;

import mudcartographer.map.MudMap;
import mudcartographer.map.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AMRoom extends Room {
    public static final int BOX_WIDTH_HEIGHT = 20;
    public static final int ARC_WIDTH_HEIGHT = 7;
    public static final int BOX_SPACING = BOX_WIDTH_HEIGHT;
    public static final int TEXT_SPACING = 2;
    public static final int PADDING = BOX_SPACING;// ToDo: I'm hokey, please fix me!
    public static final int TEXT_HEIGHT = 10;
    public static final char DEFAULT_SYMBOL = ' ';

    public static int nextID;
    // use the Direction enum to get the ordinals into the array of rooms
    private Room[] rooms = new Room[10];
    private Map<String, Boolean> flags = new HashMap<String, Boolean>();
    private char symbol = 'a';
    // what was the ID of the last operation performed on this room
    private int currentOperationID;
    private int ID = 0;
    private Rectangle rectangle;
    private Point point;
    private Color textColor = Color.BLACK;
    private Color backgroundColor = Color.WHITE;
    private boolean isChanged;
    private java.util.List<RoomKeywordDescription> keywordDescriptions;
    private String description = "";
    private String name = "";
    private String terrain = "";

    public AMRoom() {
        this(DEFAULT_SYMBOL);
    }

    public AMRoom(char symbol) {
        this.symbol = symbol;
        this.ID = useID();
        this.keywordDescriptions = new ArrayList<RoomKeywordDescription>();
    }

    /**
     * Get a room attached to this room
     *
     * @param d the direction of the attached room
     * @return the room in the specified direction or null if no room exists
     */
    public Room getRoom(MudMap.Direction d) {
        return rooms[d.ordinal()];
    }

    public void setRoom(Room r, MudMap.Direction d) {
        rooms[d.ordinal()] = r;
    }

    public boolean isFlagSet(String flagName){
        return(flags.get(flagName) != null && flags.get(flagName));
    }

    public void setFlag(String flagName, boolean isSet){
        flags.put(flagName, isSet);
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
        this.setChanged(true);
    }

    public java.util.List<RoomKeywordDescription> getKeywordDescriptions() {
        return keywordDescriptions;
    }

    public void setKeywordDescriptions(java.util.List<RoomKeywordDescription> descriptions) {
        this.keywordDescriptions = descriptions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrentOperationID() {
        return currentOperationID;
    }

    public void setCurrentOperationID(int currentOperationID) {
        this.currentOperationID = currentOperationID;
    }

    private int useID() {
        return nextID++;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    /**
     * Get the point and dimensions for the current drawing of this room
     *
     * @return pixel point and pixel dimensions of the room
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Set the point and dimensions for the current drawing of this room
     *
     * @param rectangle pixel point and pixel dimensions for the current drawing of this room
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Get the current coordinates (in rooms, not pixels) of this room
     *
     * @return current room coordinates of this room
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Set the current room coordinates of this room
     *
     * @param point coordinates of the current room (in rooms, not pixels)
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        this.setChanged(true);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.setChanged(true);
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    /**
     * Wrap the paint function to allow setting the rectangle
     *
     * @param g2        the graphics object to draw to
     * @param rectangle the rectangle to draw in
     * @param isCurrent whether or not to highlight the room
     */
    public void paint(Graphics2D g2, Rectangle rectangle, boolean isCurrent) {
        setRectangle(rectangle);
        paint(g2, isCurrent);
    }

    /**
     * Paint this room
     *
     * @param g2        the graphics object to paint to
     * @param isCurrent whether to highlight the current room
     */
    public void paint(Graphics2D g2, boolean isCurrent) {
        //Rectangle rectangle = getRectangle();
        int x = (int) rectangle.getX();
        int y = (int) rectangle.getY();

        // draw the currentRoom's background
        g2.setColor(getBackgroundColor());
        g2.fillRoundRect(x, y, (int) rectangle.getWidth(), (int) rectangle.getHeight(), ARC_WIDTH_HEIGHT, ARC_WIDTH_HEIGHT);

        // draw the currentRoom's outline
        g2.setColor(isCurrent ? Color.RED : Color.BLACK);
        g2.drawRoundRect(x, y, (int) rectangle.getWidth(), (int) rectangle.getHeight(), ARC_WIDTH_HEIGHT, ARC_WIDTH_HEIGHT);

        // draw the currentRoom's text
        g2.setColor(getTextColor());
        g2.drawString(String.valueOf(getSymbol()) /*+ r.getID()*/, (int) rectangle.getX() + TEXT_SPACING, (int) rectangle.getY() + TEXT_SPACING + TEXT_HEIGHT);

    }
}
