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
import alexmud.map.Exit;
import mudcartographer.MudController;
import mudcartographer.event.RoomEvent;
import mudcartographer.event.RoomEventListener;
import mudcartographer.gui.MudCartographerPanel;
import mudcartographer.map.MudMap;
import mudcartographer.map.Room;
import mudcartographer.map.RoomProperty;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

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
    private JTextField exitDestinationIDField;
    private JTextArea exitLookDescriptionTextArea;
    private JTextArea exitKeywordsTextArea;

    private JComboBox exitDirectionComboBox;
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
        // ToDo: make exitDestinationIDField numbers only (use a NumberFormatter with NumberFormat?)
        exitDestinationIDField = new JTextField();
        exitDatabaseKeyField = new JTextField(5);
        exitDoorNameField = new JTextField();
        exitLockDifficultyField = new JTextField(5);
        exitVKeyField = new JTextField(5);

        exitKeywordsTextArea = new JTextArea();
        exitKeywordsTextArea.setLineWrap(true);
        exitKeywordsScrollPane = new JScrollPane(exitKeywordsTextArea);

        exitLookDescriptionTextArea = new JTextArea();
        exitLookDescriptionTextArea.setLineWrap(true);
        exitLookDescriptionScrollPane = new JScrollPane(exitLookDescriptionTextArea);
    }

    private String[] getDirections() {
        List<MudMap.Direction> directions = Arrays.asList(MudMap.Direction.values());
        String[] directionDescriptions = new String[directions.size()];
        for (MudMap.Direction direction : directions) {
            directionDescriptions[direction.ordinal()] = direction.getDescription();
        }
        return directionDescriptions;
    }

    private void sizeExitComponents() {
        exitDirectionLabel.setPreferredSize(LABEL_DIMENSION);
        exitDestinationLabel.setPreferredSize(LABEL_DIMENSION);
        exitLookDescriptionLabel.setPreferredSize(LABEL_DIMENSION);
        exitDoorNameLabel.setPreferredSize(LABEL_DIMENSION);
        exitDatabaseKeyLabel.setPreferredSize(LABEL_DIMENSION);
        exitKeywordsLabel.setPreferredSize(LABEL_DIMENSION);
        exitVKeyLabel.setPreferredSize(LABEL_DIMENSION);
        exitDestinationIDField.setPreferredSize(new Dimension(220, 20));
        exitDoorNameField.setPreferredSize(new Dimension(220, 20));
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

        componentAdder.add(Arrays.asList((JComponent) exitDirectionLabel, exitDirectionComboBox));
        componentAdder.add(Arrays.asList((JComponent) exitDestinationLabel, exitDestinationIDField));
        componentAdder.add(Arrays.asList((JComponent) exitLookDescriptionLabel, exitLookDescriptionScrollPane));
        componentAdder.add(Arrays.asList((JComponent) exitKeywordsLabel, exitKeywordsScrollPane));
        componentAdder.add(Arrays.asList((JComponent) exitDoorNameLabel, exitDoorNameField));
        componentAdder.add(Arrays.asList((JComponent) exitDatabaseKeyLabel, exitDatabaseKeyField));
        componentAdder.add(Arrays.asList((JComponent) exitLockDifficultyLabel, exitLockDifficultyField));
        componentAdder.add(Arrays.asList((JComponent) exitVKeyLabel, exitVKeyField));
        componentAdder.addFiller();
    }

    private void addExitActionListeners() {
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("Exit panel lost focus");
            }
        });

        addExitDirectionComboBoxListeners();

        addExitDestinationListeners();

        addExitLookDescriptionListeners();

        addExitKeywordsListeners();

        addTextComponentListeners();

    }

    private void addTextComponentListeners() {
        Field doorNameField;
        Field databaseKeyField;
        Field vKeyField;
        Field lockDifficultyField;

        try {
            doorNameField = Exit.class.getField("doorName");
            databaseKeyField = Exit.class.getField("databaseKey");
            vKeyField = Exit.class.getField("vKey");
            lockDifficultyField = Exit.class.getField("lockDifficulty");
        } catch (Exception e) {
            System.out.println("Error adding exit tab field listeners: " + e.getMessage());
            return;
        }

        exitDoorNameField.addKeyListener(new ExitKeyAdapter(doorNameField));
        exitDatabaseKeyField.addKeyListener(new ExitKeyAdapter(databaseKeyField));
        exitVKeyField.addKeyListener(new ExitKeyAdapter(vKeyField));
        exitLockDifficultyField.addKeyListener(new ExitKeyAdapter(lockDifficultyField));
    }

    private void addExitDirectionComboBoxListeners() {
        exitDirectionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String exitDirection = (String) exitDirectionComboBox.getSelectedItem();
                MudMap.Direction direction = MudMap.Direction.getDirection(exitDirection);
                Exit exit = room.getExit(direction);

                if (exit == null) {
                    exit = room.createExit(direction);
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.EXITS.getFlagBits(), AMRoomExitPanel.this));
                }

                update(exit);
                controller.releaseFocus();
            }
        });
    }

    private void addExitDestinationListeners() {
        exitDestinationIDField.addKeyListener(new KeyAdapter() {
            int newDestination;
            Exit currentExit;

            public void keyReleased(KeyEvent e) {
                try {
                    newDestination = Integer.parseInt(exitDestinationIDField.getText());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(AMRoomExitPanel.this, "Destination Room ID must be an integer", "Invalid Destination Room ID", JOptionPane.ERROR_MESSAGE);
                    exitDestinationIDField.setText("0");
                    return;
                }

                currentExit = getCurrentExit();

                if (currentExit.destination == null || currentExit.destination != newDestination) {
                    currentExit.destination = newDestination;
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.EXITS.getFlagBits(), AMRoomExitPanel.this));
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    controller.releaseFocus();
                }
            }
        });
    }

    private void addExitLookDescriptionListeners() {
        InputMap inputMap = exitLookDescriptionTextArea.getInputMap();
        ActionMap actionMap = exitLookDescriptionTextArea.getActionMap();

        String releaseFocusKey = "RELEASE_FOCUS";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), releaseFocusKey);

        actionMap.put(releaseFocusKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.releaseFocus();
            }
        });

        exitLookDescriptionTextArea.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                Exit currentExit = getCurrentExit();
                if (currentExit.lookDescription == null || !currentExit.lookDescription.equals(exitLookDescriptionTextArea.getText())) {
                    currentExit.lookDescription = exitLookDescriptionTextArea.getText();
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.EXITS.getFlagBits(), AMRoomExitPanel.this));
                }
            }
        });
    }

    private void addExitKeywordsListeners() {
        InputMap inputMap = exitKeywordsTextArea.getInputMap();
        ActionMap actionMap = exitKeywordsTextArea.getActionMap();

        Object transferTextActionKey = "RELEASE_FOCUS";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), transferTextActionKey);

        actionMap.put(transferTextActionKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Exit currentExit = getCurrentExit();
                if (currentExit.keywords == null || !currentExit.keywords.equals(exitKeywordsTextArea.getText())) {
                    currentExit.keywords = exitKeywordsTextArea.getText();
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.EXITS.getFlagBits(), AMRoomExitPanel.this));
                }
                controller.releaseFocus();
            }
        });

        exitKeywordsTextArea.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                Exit currentExit = getCurrentExit();
                if (currentExit.keywords == null || !currentExit.keywords.equals(exitKeywordsTextArea.getText())) {
                    currentExit.keywords = exitKeywordsTextArea.getText();
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.EXITS.getFlagBits(), AMRoomExitPanel.this));
                }
            }
        });
    }

    public Exit getCurrentExit() {
        return room.getExit(MudMap.Direction.getDirection((String) exitDirectionComboBox.getSelectedItem()));
    }

    public List<RoomEventListener> getListeners() {
        return Arrays.asList((RoomEventListener) this);
    }

    public void update(Exit exit) {
        if (exit == null) {
            clearExitFields();
        } else {
            updateExitFields(exit);
        }
    }

    public void clearExitFields() {
        exitDestinationIDField.setText(null);
        exitLookDescriptionTextArea.setText(null);
        exitKeywordsTextArea.setText(null);
        exitDoorNameField.setText(null);
        exitDatabaseKeyField.setText(null);
        exitLockDifficultyField.setText(null);
        exitVKeyField.setText(null);
    }

    public void updateExitFields(Exit exit) {
        exitDestinationIDField.setText(exit.destination == null ? "" : exit.destination.toString());
        exitLookDescriptionTextArea.setText(exit.lookDescription);
        exitKeywordsTextArea.setText(exit.keywords);
        exitDoorNameField.setText(exit.doorName);
        exitDatabaseKeyField.setText(exit.databaseKey);
        exitLockDifficultyField.setText(exit.lockDifficulty);
        exitVKeyField.setText(exit.vKey);
    }

    public void updateRoom(Room room) {
        if (!(room instanceof AMRoom)) {
            return;
        }
        this.room = (AMRoom) room;

        createMissingExits();

        exitDirectionComboBox.getActionListeners()[0].actionPerformed(new ActionEvent(this, -1, ""));

    }

    private void createMissingExits() {
        for (MudMap.Direction direction : MudMap.Direction.values()) {
            if (room.getRoom(direction) != null && room.getExit(direction) == null) {
                room.createExit(direction);
            }
        }
    }

    public int getRelevantRoomEventFlags() {
        return ROOM_PROPERTIES;
    }


    private class ExitKeyAdapter extends KeyAdapter {
        private Field exitField;

        public ExitKeyAdapter(Field exitField) {
            this.exitField = exitField;
        }

        public void keyReleased(KeyEvent e) {
            String oldValue = null;
            Exit currentExit = getCurrentExit();
            String newValue = ((JTextComponent) e.getSource()).getText();

            try {
                oldValue = (String) exitField.get(currentExit);
            } catch (Exception ex) {
                System.out.println("Could not retrieve value for Exit field " + exitField.getName());
            }

            if (!newValue.equals(oldValue)) {
                try {
                    exitField.set(currentExit, newValue);
                } catch (Exception ex) {
                    System.out.println("Error setting exit field value: " + exitField.getName() + " to " + newValue);
                }
                controller.fireRoomEvent(new RoomEvent(room, RoomProperty.EXITS.getFlagBits(), AMRoomExitPanel.this));
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                controller.releaseFocus();
            }
        }
    }
}