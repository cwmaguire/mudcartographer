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
package gui;

import event.RoomEventListener;
import map.Room;
import mudcartographer.MudController;
import event.RoomEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Handles creating and maintaining a panel to
 * display room information
 *
 * NOT THREAD SAFE
 */
public class RoomInfoPanel extends JPanel implements RoomEventListener {
    private static Dimension LABEL_DIMENSION = new Dimension(80,20);
    private static Dimension COLOR_LABEL_DIMENSION = new Dimension(20, 20);
    private static int ROOM_PROPERTIES = Room.RoomProperty.BACKGROUND_COLOR.getFlag() |
                                         Room.RoomProperty.TEXT_COLOR.getFlag() |
                                         Room.RoomProperty.ID.getFlag() |
                                         Room.RoomProperty.SYMBOL.getFlag();

    private Room room;
    private MudController controller;
    private int ID;

    private JTextField roomIdField;
    private JTextField roomSymbolField;
    private JButton textColorButton;
    private JButton backgroundColorButton;
    private JLabel roomIdLabel = new JLabel("Room ID: ");
    private JLabel roomSymbolLabel = new JLabel("Symbol: ");
    private JLabel roomTextColorLabel = new JLabel("Text: ");
    private JLabel roomBackgroundColorLabel = new JLabel("Background: ");

    public RoomInfoPanel(MudController controller){
        this.controller = controller;
        createComponents();
        layoutComponents();
        addActionListeners();
    }

    /**
     * Create all the components that are used on the room info panel
     */
    public void createComponents(){

        roomIdLabel.setPreferredSize(LABEL_DIMENSION);
        roomSymbolLabel.setPreferredSize(LABEL_DIMENSION);
        roomTextColorLabel.setPreferredSize(LABEL_DIMENSION);
        roomBackgroundColorLabel.setPreferredSize(LABEL_DIMENSION);

        // create colour labels
        textColorButton = new JButton();
        textColorButton.setPreferredSize(COLOR_LABEL_DIMENSION);

        backgroundColorButton = new JButton("");
        backgroundColorButton.setPreferredSize(COLOR_LABEL_DIMENSION);

        // create text fields
        roomIdField = new JTextField(10);
        roomIdField.setEnabled(false);

        roomSymbolField = new JTextField(1);
    }

    /**
     * Layout the info panel components
     */
    private void layoutComponents(){
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

    /**
     * Add the action listeners that register when the user has done something or changed something
     */
    private void addActionListeners(){
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
                controller.fireRoomEvent(new RoomEvent(room, Room.RoomProperty.SYMBOL.getFlag(), RoomInfoPanel.this));
            }
        });

        textColorButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Color newColor = JColorChooser.showDialog(RoomInfoPanel.this, "Choose text Color", textColorButton.getBackground());
                if(newColor != null){
                    textColorButton.setBackground(newColor);
                    room.setTextColor(newColor);
                    controller.fireRoomEvent(new RoomEvent(room, Room.RoomProperty.TEXT_COLOR.getFlag(), RoomInfoPanel.this));
                }
            }
        });

        backgroundColorButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Color newColor = JColorChooser.showDialog(RoomInfoPanel.this, "Choose text Color", backgroundColorButton.getBackground());
                if(newColor != null){
                    backgroundColorButton.setBackground(newColor);
                    room.setBackgroundColor(newColor);
                    controller.fireRoomEvent(new RoomEvent(room, Room.RoomProperty.BACKGROUND_COLOR.getFlag(), RoomInfoPanel.this));
                }
            }
        });
    }

    /**
     * Facade method for setting all the information
     * in the room info panel
     *
     * @param room the room to update the info panel with
     */
    public void updateRoom(Room room){
        this.room = room;
        roomIdField.setText(String.valueOf(room.getID()));
        roomSymbolField.setText(String.valueOf(room.getSymbol()));
        textColorButton.setBackground(room.getTextColor());
        backgroundColorButton.setBackground(room.getBackgroundColor());
    }

    public int getRelevantRoomEventFlags(){
        return ROOM_PROPERTIES;
    }

    /**
     * Painter is the only one with the graphics object, so
     * we'll tell painter to repaint the room
     */
    /*
    private void repaintRoom(){
        // give the focus back to the painter so the user doesn't wonder why
        // key strokes aren't registering anymore
        painter.requestFocus();
        painter.repaint();
    }
    */
    // The controller will do this

}
