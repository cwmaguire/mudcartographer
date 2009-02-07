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

package alexmud.gui;

import alexmud.map.AMRoom;
import mudcartographer.MudController;
import mudcartographer.event.RoomEvent;
import mudcartographer.gui.MudCartographerPanel;
import mudcartographer.map.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AMRoomFlagPanel extends MudCartographerPanel {
    private static Dimension LABEL_DIMENSION = new Dimension(80,20);
    private static Dimension COLOR_LABEL_DIMENSION = new Dimension(20, 20);
    public static int ROOM_PROPERTIES = Room.RoomProperty.BACKGROUND_COLOR.getFlagBits() |
                                         Room.RoomProperty.TEXT_COLOR.getFlagBits() |
                                         Room.RoomProperty.ID.getFlagBits() |
                                         Room.RoomProperty.SYMBOL.getFlagBits();

    private AMRoom room;
    private MudController controller;

    private JLabel roomIdLabel = new JLabel("Room ID: ");
    private JLabel roomNameLabel = new JLabel("Name: ");
    private JLabel roomSymbolLabel = new JLabel("Symbol: ");
    private JLabel roomColorLabel = new JLabel("Color: ");
    private JLabel roomTextColorLabel = new JLabel("fg: ");
    private JLabel roomBackgroundColorLabel = new JLabel("bg: ");
    private JLabel roomTerrainLabel = new JLabel("Terrain: ");

    private JTextField roomIdField;
    private JTextField roomNameField;
    private JTextField roomSymbolField;

    private JButton textColorButton;
    private JButton backgroundColorButton;


    public void initialize(MudController controller) {
        this.controller = controller;
        createFlagTabComponents();
        sizeFlagTabComponents();
        layoutFlagTabComponents();
        addFlagTabPanelActionListeners();
    }

    private void createFlagTabComponents() {
        // create colour labels
        textColorButton = new JButton();
        backgroundColorButton = new JButton("");

        // create text fields
        roomIdField = new JTextField(10);
        roomSymbolField = new JTextField(1);
    }

    private void sizeFlagTabComponents() {
        roomIdLabel.setPreferredSize(LABEL_DIMENSION);
        roomNameLabel.setPreferredSize(LABEL_DIMENSION);
        roomSymbolLabel.setPreferredSize(LABEL_DIMENSION);
        roomTextColorLabel.setPreferredSize(LABEL_DIMENSION);
        roomBackgroundColorLabel.setPreferredSize(LABEL_DIMENSION);
        textColorButton.setPreferredSize(COLOR_LABEL_DIMENSION);
        backgroundColorButton.setPreferredSize(COLOR_LABEL_DIMENSION);
    }

    private void layoutFlagTabComponents(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 10, 0, 0);
        this.add(roomIdLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets.set(20, 0, 0, 20);
        //c.weightx = 1.0;
        this.add(roomIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets.set(5,10,0,0);
        this.add(roomSymbolLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets.set(5,0,0,20);
        this.add(roomSymbolField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets.set(5,10,0,0);
        this.add(roomTextColorLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets.set(5,0,0,20);
        this.add(textColorButton, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets.set(5, 10, 0, 0);
        this.add(roomBackgroundColorLabel, c);

        c.gridx = 1;
        c.gridy = 3;
        c.insets.set(5, 0, 0, 20);
        this.add(backgroundColorButton, c);

        // create a final, empty panel to take up all the remaining space
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.weighty = 1.0;
        this.add(new JPanel(), c);
    }

    private void addFlagTabPanelActionListeners(){
        // user shouldn't have to select the char
        roomSymbolField.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                super.mouseClicked(e);
                roomSymbolField.selectAll();
            }
        });

        roomSymbolField.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                room.setSymbol(Character.isLetterOrDigit(e.getKeyChar()) ? e.getKeyChar() : ' ');
                controller.fireRoomEvent(new RoomEvent(room, Room.RoomProperty.SYMBOL.getFlagBits(), AMRoomFlagPanel.this));
            }
        });

        textColorButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Color newColor = JColorChooser.showDialog(AMRoomFlagPanel.this, "Choose text Color", textColorButton.getBackground());
                if(newColor != null){
                    textColorButton.setBackground(newColor);
                    room.setTextColor(newColor);
                    controller.fireRoomEvent(new RoomEvent(room, Room.RoomProperty.TEXT_COLOR.getFlagBits(), AMRoomFlagPanel.this));
                }
            }
        });

        backgroundColorButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Color newColor = JColorChooser.showDialog(AMRoomFlagPanel.this, "Choose text Color", backgroundColorButton.getBackground());
                if(newColor != null){
                    backgroundColorButton.setBackground(newColor);
                    room.setBackgroundColor(newColor);
                    controller.fireRoomEvent(new RoomEvent(room, Room.RoomProperty.BACKGROUND_COLOR.getFlagBits(), AMRoomFlagPanel.this));
                }
            }
        });
    }
    public void updateRoom(Room room){
        if(!(room instanceof AMRoom)){
            return;
        }
        this.room = (AMRoom) room;
        roomIdField.setText(String.valueOf(room.getID()));
        roomSymbolField.setText(String.valueOf(room.getSymbol()));
        textColorButton.setBackground(room.getTextColor());
        backgroundColorButton.setBackground(room.getBackgroundColor());
    }

    public int getRelevantRoomEventFlags() {
        return ROOM_PROPERTIES; 
    }


}
