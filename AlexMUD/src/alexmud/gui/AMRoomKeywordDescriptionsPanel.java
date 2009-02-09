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
import mudcartographer.gui.MudCartographerPanel;
import mudcartographer.map.Room;
import mudcartographer.map.RoomProperty;

import javax.swing.*;
import java.awt.*;

/**
 * Handles creating and maintaining a panel to
 * display room descriptions
 */
public class AMRoomKeywordDescriptionsPanel extends MudCartographerPanel {
    private static final Dimension BUTTON_SIZE = new Dimension(50,20);
    private static int ROOM_PROPERTIES = RoomProperty.DESCRIPTION.getFlagBits();

    private AMRoom room;
    private MudController controller;

    JTable keywordDescriptionTable;
    JScrollPane scrollPane;
    JPanel buttonPanel;
    JButton addButton;
    JButton deleteButton;

    public AMRoomKeywordDescriptionsPanel(){
    }

    public void initialize(MudController controller){
        this.controller = controller;
        createComponents();
        layoutComponents();
        addActionListeners();
    }

    public void createComponents(){
        keywordDescriptionTable = new JTable(new String[][]{{"",""}}, new String[]{"Keywords", "Description"});
        scrollPane = new JScrollPane(keywordDescriptionTable);
        createButtonPanelComponents();
    }

    private void createButtonPanelComponents() {
        addButton = new JButton("+");
        deleteButton = new JButton("-");
        buttonPanel = new JPanel();
    }

    private void layoutComponents(){
        this.setLayout(new BorderLayout());
        scrollPane.setPreferredSize(new Dimension(1, 100));
        keywordDescriptionTable.setFillsViewportHeight(true);
        this.add(scrollPane, BorderLayout.CENTER);

        layoutButtonPanel();
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void layoutButtonPanel(){
        GridBagConstraints c = new GridBagConstraints();
        buttonPanel.setLayout(new GridBagLayout());
        addButton.setPreferredSize(BUTTON_SIZE);
        deleteButton.setPreferredSize(BUTTON_SIZE);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);

        buttonPanel.add(addButton, c);

        c.gridx = 1;

        buttonPanel.add(deleteButton, c);

        c.gridx = 2;
        c.weightx = 1;

        buttonPanel.add(new JPanel(), c);

    }

    private void addActionListeners(){

    }

    public void updateRoom(Room room) {
        if (!(room instanceof AMRoom)) {
            return;
        }
        this.room = (AMRoom) room;
        //keywordDescriptionTable.setModel(new DefaultTableModel(this.room.getDesriptions(), this.room.getDescriptionColumns()));
    }

    public int getRelevantRoomEventFlags(){
        return ROOM_PROPERTIES;
    }
}