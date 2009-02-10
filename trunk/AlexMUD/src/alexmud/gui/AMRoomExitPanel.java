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

import alexmud.constants.ExternalConstants;
import alexmud.map.AMRoom;
import mudcartographer.MudController;
import mudcartographer.event.RoomEvent;
import mudcartographer.event.RoomEventListener;
import mudcartographer.gui.MudCartographerPanel;
import mudcartographer.map.Room;
import mudcartographer.map.RoomProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AMRoomExitPanel extends MudCartographerPanel {
    private static Dimension LABEL_DIMENSION = new Dimension(90, 20);
    public static int ROOM_PROPERTIES = RoomProperty.EXITS.getFlagBits();

    private AMRoom room;
    private MudController controller;

    private JLabel exitDirectionLabel = new JLabel("Direction: ");
    private JLabel exitDestinationLabel = new JLabel("Destination: ");
    private JLabel exitLookDescriptionLabel = new JLabel("Look Desc: ");
    private JLabel exitKeywordsLabel = new JLabel("Keywords: ");
    private JLabel exitDoorNameLabel = new JLabel("Door Name: ");
    private JLabel exitDatabaseKeyLabel = new JLabel("Database Key: ");
    private JLabel exitLockDifficultyLabel = new JLabel("Lock Difficulty: ");
    private JLabel exitVKeyLabel = new JLabel("V Key: ");

    private JTextField exitDoorNameField;
    private JTextField exitDatabaseKeyField;
    private JTextField exitLockDifficultyField;
    private JTextField exitVKeyField;
    private JTextArea exitLookDescriptionTextArea;
    private JTextArea exitKeywordsArea;

    private JComboBox exitDirectionComboBox;
    private JComboBox exitDestinationIDComboBox;
    private JScrollPane exitLookDescriptionScrollPane;
    private JScrollPane exitKeywordsScrollPane;

    public void initialize(MudController controller) {
        this.controller = controller;
        createExitComponents();
        layoutExitComponents();
        sizeExitComponents();
        addExitActionListeners();
    }

    private void createExitComponents() {

        exitDirectionComboBox = new JComboBox(getDirections());
        // ToDo: make exitDestinationIDComboBox numbers only (use a NumberFormatter with NumberFormat?)
        exitDestinationIDComboBox = new JComboBox();
        exitDatabaseKeyField = new JTextField(5);
        exitDoorNameField = new JTextField();
        exitLockDifficultyField = new JTextField(5);
        exitVKeyField = new JTextField(5);

        exitKeywordsArea = new JTextArea();
        exitKeywordsArea.setLineWrap(true);
        exitKeywordsScrollPane = new JScrollPane(exitKeywordsArea);

        exitLookDescriptionTextArea = new JTextArea();
        exitLookDescriptionTextArea.setLineWrap(true);
        exitLookDescriptionScrollPane = new JScrollPane(exitLookDescriptionTextArea);
    }

    private String[] getDirections() {
        ArrayList<String> directions = new ArrayList<String>(ExternalConstants.getConstants(ExternalConstants.DIRECTIONS_KEY));
        directions.add(0, "");
        return directions.toArray(new String[directions.size()]);
    }

    private void sizeExitComponents() {
        exitDirectionLabel.setPreferredSize(LABEL_DIMENSION);
        exitDestinationLabel.setPreferredSize(LABEL_DIMENSION);
        exitLookDescriptionLabel.setPreferredSize(LABEL_DIMENSION);
        exitDoorNameLabel.setPreferredSize(LABEL_DIMENSION);
        exitDatabaseKeyLabel.setPreferredSize(LABEL_DIMENSION);
        exitKeywordsLabel.setPreferredSize(LABEL_DIMENSION);
        exitVKeyLabel.setPreferredSize(LABEL_DIMENSION);
        exitDoorNameField.setPreferredSize(new Dimension(220,20));
        exitKeywordsScrollPane.setPreferredSize(new Dimension(220, 40));
        exitLookDescriptionScrollPane.setPreferredSize(new Dimension(220, 40));
    }

    private void layoutExitComponents() {
        this.setMinimumSize(new Dimension(350, 200));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayoutComponentAdder componentAdder = new GridBagLayoutComponentAdder(20, 5);

        c.anchor = GridBagConstraints.NORTHWEST;
        componentAdder.c = c;
        componentAdder.panel = this;

        componentAdder.add(Arrays.asList((JComponent) exitDestinationLabel, exitDestinationIDComboBox));
        componentAdder.add(Arrays.asList((JComponent) exitLookDescriptionLabel, exitLookDescriptionScrollPane));
        componentAdder.add(Arrays.asList((JComponent) exitKeywordsLabel, exitKeywordsScrollPane));
        componentAdder.add(Arrays.asList((JComponent) exitDoorNameLabel, exitDoorNameField));
        componentAdder.add(Arrays.asList((JComponent) exitDatabaseKeyLabel, exitDatabaseKeyField));
        componentAdder.add(Arrays.asList((JComponent) exitLockDifficultyLabel, exitLockDifficultyField));
        componentAdder.add(Arrays.asList((JComponent) exitVKeyLabel, exitVKeyField));
        componentAdder.addFiller();
    }

    private void addExitActionListeners() {

        /*addRoomIdFieldListners();

        addRoomNameFieldListeners();

        addRoomSymbolFieldListeners();

        addRoomTerrainComboBoxListeners();

        addRoomDescriptionTextAreaListeners();*/
    }

    private void addRoomIdFieldListners() {
        exitKeywordsArea.addKeyListener(new KeyAdapter() {
            int newRoomID;

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        newRoomID = Integer.parseInt(exitKeywordsArea.getText());
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(AMRoomExitPanel.this, "Room ID must be an integer", "Invalid Room ID", JOptionPane.ERROR_MESSAGE);
                        exitKeywordsArea.setText("0");
                        return;
                    }
                    if (room.getID() != newRoomID) {
                        room.setID(newRoomID);
                        controller.fireRoomEvent(new RoomEvent(room, RoomProperty.ID.getFlagBits(), AMRoomExitPanel.this));
                    }

                    controller.releaseFocus();
                }
            }
        });
    }

    private void addRoomNameFieldListeners() {
        exitDoorNameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String newRoomName = exitDoorNameField.getText();
                    if (!newRoomName.equals(room.getName())) {
                        room.setName(newRoomName);
                        controller.fireRoomEvent(new RoomEvent(room, RoomProperty.NAME.getFlagBits(), AMRoomExitPanel.this));
                    }

                    controller.releaseFocus();
                }
            }
        });
    }

    private void addRoomSymbolFieldListeners() {
        // user shouldn't have to select the char
        exitDatabaseKeyField.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                exitDatabaseKeyField.selectAll();
            }
        });

        exitDatabaseKeyField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                room.setSymbol(Character.isLetterOrDigit(e.getKeyChar()) ? e.getKeyChar() : ' ');
                controller.fireRoomEvent(new RoomEvent(room, RoomProperty.SYMBOL.getFlagBits(), AMRoomExitPanel.this));
            }
        });
    }

    private void addRoomTerrainComboBoxListeners() {
        exitDestinationIDComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newRoomTerrain = (String) exitDestinationIDComboBox.getSelectedItem();

                if (!newRoomTerrain.equals(room.getTerrain())) {
                    room.setTerrain(newRoomTerrain);
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.TERRAIN.getFlagBits(), AMRoomExitPanel.this));
                }

                controller.releaseFocus();
            }
        });
    }

    private void addRoomDescriptionTextAreaListeners() {
        InputMap inputMap = exitLookDescriptionTextArea.getInputMap();
        ActionMap actionMap = exitLookDescriptionTextArea.getActionMap();

        Object transferTextActionKey = "RELEASE_FOCUS";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), transferTextActionKey);

        actionMap.put(transferTextActionKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!room.getDescription().equals(exitLookDescriptionTextArea.getText())) {
                    room.setDescription(exitLookDescriptionTextArea.getText());
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.DESCRIPTION.getFlagBits(), AMRoomExitPanel.this));
                }
                controller.releaseFocus();
            }
        });
    }

    public java.util.List<RoomEventListener> getListeners() {
        return Arrays.asList((RoomEventListener) this);
    }

    public void updateRoom(Room room) {
        if (!(room instanceof AMRoom)) {
            return;
        }
        this.room = (AMRoom) room;
        /*
        exitKeywordsArea.setText(String.valueOf(room.getID()));
        exitDoorNameField.setText(this.room.getName());
        exitDatabaseKeyField.setText(String.valueOf(this.room.getSymbol()));
        exitDestinationIDComboBox.setSelectedItem(this.room.getTerrain());
        exitLookDescriptionTextArea.setText(this.room.getDescription());
        */
    }

    public int getRelevantRoomEventFlags() {
        return ROOM_PROPERTIES;
    }
}