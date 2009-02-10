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
import mudcartographer.event.RoomEventListener;
import mudcartographer.gui.MudCartographerPanel;
import mudcartographer.map.Room;
import mudcartographer.map.RoomProperty;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AMRoomFlagPanel extends MudCartographerPanel {
    private static Dimension LABEL_DIMENSION = new Dimension(100,20);
    public static int ROOM_PROPERTIES = RoomProperty.FLAGS.getFlagBits();

    private AMRoom room;
    private MudController controller;

    private List<String> flags = Arrays.asList("DARK", "SAFE", "FALL-TRAP", "INDOORS", "NO-SUMMON", "S-ENCOUNTER", "E-ENCOUNTER", "P-ENCOUNTER",
                                                    "HAS-LIGHT", "PRIVATE", "SUN-LIGHT", "NO-MAGIC", "RESTRICTED", "SOUNDPROOF", "NO-SLEEP",
                                                    "DANGEROUS", "PRISON", "WILDERNESS", "WATER", "SALTWATER", "POISONED", "BUSY", "NO-PURGE",
                                                    "ARENA", "TELEPORT", "SAVES");

    public void initialize(MudController controller) {
        this.controller = controller;
        createFlagTabComponents();
    }

    private void createFlagTabComponents() {
        JCheckBox flagCheckbox;
        JLabel flagLabel;
        String flag;
        int gridx = 0;
        int gridy = 0;

        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.FIRST_LINE_START;

        for (String flag1 : flags) {
            flag = flag1;

            c.gridx = gridx % 4;
            c.gridy = gridy / 2;
            c.insets = new Insets(5, 0, 0, 0);

            flagLabel = new JLabel(flag);
            flagLabel.setPreferredSize(LABEL_DIMENSION);
            this.add(flagLabel, c);

            gridx++;


            c.gridx = gridx % 4;
            c.gridy = gridy / 2;
            c.insets = new Insets(5, 0, 0, 10);

            flagCheckbox = new JCheckBox();
            flagCheckbox.setName(flag);
            flagCheckbox.addChangeListener(new CheckboxChangeListener());
            this.add(flagCheckbox, c);

            gridx++;
            gridy++;
        }

        // create a final, empty panel to take up all the remaining space
        c.gridx = 0;
        c.gridy = gridy;
        c.gridwidth = 4;
        c.weighty = 1.0;
        this.add(new JPanel(), c);
    }

    private class CheckboxChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            JCheckBox checkbox = (JCheckBox) e.getSource();
            room.setFlag(checkbox.getName(), checkbox.isSelected());
            controller.fireRoomEvent(new RoomEvent(room, RoomProperty.FLAGS.getFlagBits(), AMRoomFlagPanel.this));
            controller.releaseFocus();
        }
    }

    public java.util.List<RoomEventListener> getListeners() {
        return Arrays.asList((RoomEventListener) this);
    }

    public void updateRoom(Room room){
        if(!(room instanceof AMRoom)){
            return;
        }
        this.room = (AMRoom) room;

        for(Component component : this.getComponents()){
            if(component instanceof JCheckBox){
                ((JCheckBox) component).setSelected(this.room.isFlagSet(component.getName()));
            }
        }
    }

    public int getRelevantRoomEventFlags() {
        return ROOM_PROPERTIES;
    }


}
