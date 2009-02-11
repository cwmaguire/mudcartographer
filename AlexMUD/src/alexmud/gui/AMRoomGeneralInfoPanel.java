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

public class AMRoomGeneralInfoPanel extends MudCartographerPanel {
    private static Dimension LABEL_DIMENSION = new Dimension(80, 20);
    private static Dimension COLOR_LABEL_DIMENSION = new Dimension(20, 20);
    public static int ROOM_PROPERTIES = RoomProperty.BACKGROUND_COLOR.getFlagBits() |
            RoomProperty.TEXT_COLOR.getFlagBits() |
            RoomProperty.ID.getFlagBits() |
            RoomProperty.SYMBOL.getFlagBits() |
            RoomProperty.TERRAIN.getFlagBits();

    private AMRoom room;
    private MudController controller;

    private JLabel roomIdLabel = new JLabel("Room ID: ");
    private JLabel roomNameLabel = new JLabel("Name: ");
    private JLabel roomSymbolLabel = new JLabel("Symbol: ");
    //private JLabel roomColorLabel = new JLabel("Color: ");
    private JLabel roomTextColorLabel = new JLabel("fg: ");
    private JLabel roomBackgroundColorLabel = new JLabel("bg: ");
    private JLabel roomTerrainLabel = new JLabel("Terrain: ");
    private JLabel roomDescriptionLabel = new JLabel("Description: ");

    private JTextField roomIdField;
    private JTextField roomNameField;
    private JTextField roomSymbolField;
    private JTextArea roomDescriptionTextArea;

    private JButton textColorButton;
    private JButton backgroundColorButton;

    private JComboBox roomTerrainComboBox;

    private JScrollPane roomDescriptionScrollPane;

    public void initialize(MudController controller) {
        this.controller = controller;
        createGeneralTabComponents();
        layoutGeneralTabComponents();
        sizeGeneralTabComponents();
        addGeneralTabPanelActionListeners();
    }

    private void createGeneralTabComponents() {
        textColorButton = new JButton();
        backgroundColorButton = new JButton("");

        // ToDo: make roomIdField numbers only (use a NumberFormatter with NumberFormat?)
        roomIdField = new JTextField(5);
        roomSymbolField = new JTextField(1);
        roomNameField = new JTextField();
        roomTerrainComboBox = new JComboBox(getTerrainTypes());
        roomDescriptionTextArea = new JTextArea();
        roomDescriptionTextArea.setLineWrap(true);
        roomDescriptionScrollPane = new JScrollPane(roomDescriptionTextArea);
    }

    private String[] getTerrainTypes() {
        ArrayList<String> terrainTypes = new ArrayList<String>(ExternalConstants.getConstants(ExternalConstants.TERRAIN_TYPES_KEY));
        terrainTypes.add(0, "");
        return terrainTypes.toArray(new String[terrainTypes.size()]);
    }

    private void sizeGeneralTabComponents() {
        roomIdLabel.setPreferredSize(LABEL_DIMENSION);
        roomNameLabel.setPreferredSize(LABEL_DIMENSION);
        roomSymbolLabel.setPreferredSize(LABEL_DIMENSION);
        roomTextColorLabel.setPreferredSize(LABEL_DIMENSION);
        roomBackgroundColorLabel.setPreferredSize(LABEL_DIMENSION);
        roomDescriptionLabel.setPreferredSize(LABEL_DIMENSION);
        textColorButton.setPreferredSize(COLOR_LABEL_DIMENSION);
        backgroundColorButton.setPreferredSize(COLOR_LABEL_DIMENSION);
        roomNameField.setPreferredSize(new Dimension(240, 20));
        roomDescriptionScrollPane.setPreferredSize(new Dimension(240, 90));
    }

    private void layoutGeneralTabComponents() {
        this.setMinimumSize(new Dimension(400, 200));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 0, 0, 0);
        this.add(roomIdLabel, c);

        c.gridx = 1;
        this.add(roomIdField, c);

        c.gridx = 2;
        c.gridheight = 8;
        c.weightx = 1;
        this.add(new JPanel());
        c.gridheight = 1;
        c.weightx = 0;

        c.gridx = 0;
        c.gridy = 1;
        c.insets.set(5, 0, 0, 0);
        this.add(roomNameLabel, c);

        c.gridx = 1;
        this.add(roomNameField, c);

        c.gridx = 0;
        c.gridy = 2;
        this.add(roomSymbolLabel, c);

        c.gridx = 1;
        this.add(roomSymbolField, c);

        c.gridx = 0;
        c.gridy = 3;
        this.add(roomTextColorLabel, c);

        c.gridx = 1;
        this.add(textColorButton, c);

        c.gridx = 0;
        c.gridy = 4;
        this.add(roomBackgroundColorLabel, c);

        c.gridx = 1;
        this.add(backgroundColorButton, c);

        c.gridx = 0;
        c.gridy = 5;
        this.add(roomTerrainLabel, c);

        c.gridx = 1;
        this.add(roomTerrainComboBox, c);

        c.gridx = 0;
        c.gridy = 6;
        this.add(roomDescriptionLabel, c);

        c.gridx = 1;
        this.add(roomDescriptionScrollPane, c);

        // create a final, empty panel to take up all the remaining space
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 2;
        c.weighty = 1.0;
        this.add(new JPanel(), c);
    }

    private void addGeneralTabPanelActionListeners() {

        addRoomIdFieldListners();

        addRoomNameFieldListeners();

        addRoomSymbolFieldListeners();

        addRoomTextColorButtonListeners();

        addRoomBackgroundColorButtonListeners();

        addRoomTerrainComboBoxListeners();

        addRoomDescriptionTextAreaListeners();
    }

    private void addRoomIdFieldListners() {
        roomIdField.addKeyListener(new KeyAdapter() {
            int newRoomID;

            public void keyReleased(KeyEvent e) {
                try {
                    newRoomID = Integer.parseInt(roomIdField.getText());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(AMRoomGeneralInfoPanel.this, "Room ID must be an integer", "Invalid Room ID", JOptionPane.ERROR_MESSAGE);
                    roomIdField.setText("0");
                    return;
                }
                if (room.getID() != newRoomID) {
                    room.setID(newRoomID);
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.ID.getFlagBits(), AMRoomGeneralInfoPanel.this));
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    controller.releaseFocus();
                }
            }
        });
    }

    private void addRoomNameFieldListeners() {
        roomNameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                    String newRoomName = roomNameField.getText();
                    if (!newRoomName.equals(room.getName())) {
                        room.setName(newRoomName);
                        controller.fireRoomEvent(new RoomEvent(room, RoomProperty.NAME.getFlagBits(), AMRoomGeneralInfoPanel.this));
                    }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    controller.releaseFocus();
                }
            }
        });
    }

    private void addRoomSymbolFieldListeners() {
        // user shouldn't have to select the char
        roomSymbolField.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                roomSymbolField.selectAll();
            }
        });

        roomSymbolField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                room.setSymbol(Character.isLetterOrDigit(e.getKeyChar()) ? e.getKeyChar() : ' ');
                controller.fireRoomEvent(new RoomEvent(room, RoomProperty.SYMBOL.getFlagBits(), AMRoomGeneralInfoPanel.this));
                controller.releaseFocus();
            }
        });
    }

    private void addRoomTextColorButtonListeners() {
        textColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(AMRoomGeneralInfoPanel.this, "Choose text Color", textColorButton.getBackground());
                if (newColor != null) {
                    textColorButton.setBackground(newColor);
                    room.setTextColor(newColor);
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.TEXT_COLOR.getFlagBits(), AMRoomGeneralInfoPanel.this));
                }
                controller.releaseFocus();
            }
        });
    }

    private void addRoomBackgroundColorButtonListeners() {
        backgroundColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(AMRoomGeneralInfoPanel.this, "Choose text Color", backgroundColorButton.getBackground());
                if (newColor != null) {
                    backgroundColorButton.setBackground(newColor);
                    room.setBackgroundColor(newColor);
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.BACKGROUND_COLOR.getFlagBits(), AMRoomGeneralInfoPanel.this));
                }
                controller.releaseFocus();
            }
        });
    }

    private void addRoomTerrainComboBoxListeners() {
        roomTerrainComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newRoomTerrain = (String) roomTerrainComboBox.getSelectedItem();

                if (!newRoomTerrain.equals(room.getTerrain())) {
                    room.setTerrain(newRoomTerrain);
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.TERRAIN.getFlagBits(), AMRoomGeneralInfoPanel.this));
                }

                controller.releaseFocus();
            }
        });
    }

    private void addRoomDescriptionTextAreaListeners() {
        InputMap inputMap = roomDescriptionTextArea.getInputMap();
        ActionMap actionMap = roomDescriptionTextArea.getActionMap();

        Object transferTextActionKey = "RELEASE_FOCUS";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), transferTextActionKey);

        actionMap.put(transferTextActionKey, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                controller.releaseFocus();
            }
        });

        roomDescriptionTextArea.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                String oldDescription = room.getDescription();
                String newDescription = roomDescriptionTextArea.getText();
                if (oldDescription == null || !oldDescription.equals(newDescription)) {
                    room.setDescription(newDescription);
                    controller.fireRoomEvent(new RoomEvent(room, RoomProperty.DESCRIPTION.getFlagBits(), AMRoomGeneralInfoPanel.this));
                }
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
        roomIdField.setText(String.valueOf(room.getID()));
        roomNameField.setText(this.room.getName());
        roomSymbolField.setText(String.valueOf(this.room.getSymbol()));
        textColorButton.setBackground(this.room.getTextColor());
        backgroundColorButton.setBackground(this.room.getBackgroundColor());
        roomTerrainComboBox.setSelectedItem(this.room.getTerrain());
        roomDescriptionTextArea.setText(this.room.getDescription());
    }

    public int getRelevantRoomEventFlags() {
        return ROOM_PROPERTIES;
    }
}
