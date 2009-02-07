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

import mudcartographer.MudController;
import mudcartographer.event.RoomEvent;
import mudcartographer.gui.MudCartographerPanel;
import mudcartographer.map.Room;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles creating and maintaining a panel to
 * display room descriptions
 */
public class AMRoomDescriptionPanel extends MudCartographerPanel {
    private static Dimension LABEL_DIMENSION = new Dimension(80,20);
    private static int ROOM_PROPERTIES = Room.RoomProperty.DESCRIPTION.getFlagBits();

    private Room room;
    private MudController controller;
    private boolean isEnabled;

    private JScrollPane scrollPane;
    private JPanel northPanel;
    private JTextArea roomDescriptionTextArea;
    private JButton editButton;
    private JLabel roomDescriptionLabel = new JLabel("  Description: ");

    public AMRoomDescriptionPanel(){
    }

    public void initialize(MudController controller){
        this.controller = controller;
        createComponents();
        layoutComponents();
        addActionListeners();
        setBorder(new LineBorder(Color.BLACK));
        setEnabled(false);
    }

    /**
     * Create all the components that are used on the room description panel
     */
    public void createComponents(){

        roomDescriptionLabel.setPreferredSize(LABEL_DIMENSION);
        roomDescriptionTextArea = new JTextArea(4, 50);

        editButton = new JButton("Set Enabled!");

        northPanel = new JPanel();

        scrollPane = new JScrollPane(roomDescriptionTextArea);
    }

    /**
     * Layout the description panel components
     */
    private void layoutComponents(){
        northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(roomDescriptionLabel);
        northPanel.add(editButton);
        northPanel.setAlignmentY(0.0F);

        this.setLayout(new BorderLayout());
        this.add(northPanel, BorderLayout.NORTH);
        //this.add(roomDescriptionLabel, BorderLayout.NORTH);
        //this.add(editButton, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets the enabled state of description box
     * @param isEnabled whether this panel should be enabled or not
     */
    @Override
    public void setEnabled(boolean isEnabled){
        super.setEnabled(isEnabled);
        this.isEnabled = isEnabled;
        roomDescriptionTextArea.setEditable(isEnabled);
        roomDescriptionTextArea.setBackground(isEnabled ? Color.WHITE : new Color(225, 225, 225));

        editButton.setText(isEnabled ? "Save" : "Edit");
    }

    public boolean getEnabled(){
        return isEnabled;
    }

    /**
     * Add the action listeners that register when the user has done something or changed something
     */
    private void addActionListeners(){

        editButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                AMRoomDescriptionPanel.this.setEnabled(!AMRoomDescriptionPanel.this.getEnabled());
                if(!AMRoomDescriptionPanel.this.getEnabled()){
                    room.setDescription(roomDescriptionTextArea.getText());
                    controller.fireRoomEvent(new RoomEvent(room, ROOM_PROPERTIES, AMRoomDescriptionPanel.this));
                }else{
                    roomDescriptionTextArea.requestFocus();
                }
            }
        });
    }

    public void updateRoom(Room room) {
        this.room = room;
        roomDescriptionTextArea.setText(room.getDescription());
    }

    /**
     * Get the list of Room properties that this component listens for
     * @return list of properties to listen for
     */
    public int getRelevantRoomEventFlags(){
        return ROOM_PROPERTIES;
    }
}