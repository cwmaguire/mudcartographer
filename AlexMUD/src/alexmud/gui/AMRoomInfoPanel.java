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
import mudcartographer.event.RoomEventListener;
import mudcartographer.gui.MudCartographerPanel;
import mudcartographer.map.Room;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles creating and maintaining a panel to
 * display room information
 *
 * NOT THREAD SAFE
 */
public class AMRoomInfoPanel extends MudCartographerPanel {
    private static int ROOM_PROPERTIES = AMRoomGeneralInfoPanel.ROOM_PROPERTIES | AMRoomFlagPanel.ROOM_PROPERTIES;

    private MudController controller;
    private MudCartographerPanel infoPanel;
    private MudCartographerPanel flagPanel;

    public AMRoomInfoPanel(){}

    public void initialize(MudController controller){
        this.controller = controller;

        setupComponents();
    }

    public void setupComponents(){
        this.setLayout(new BorderLayout());

        infoPanel = new AMRoomGeneralInfoPanel();
        flagPanel = new AMRoomFlagPanel();

        infoPanel.initialize(controller);
        flagPanel.initialize(controller);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Info", wrapInScrollPane(infoPanel));
        tabbedPane.add("Flags", wrapInScrollPane(flagPanel));
        //tabbedPane.setPreferredSize(new Dimension(355, 500));

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    private JScrollPane wrapInScrollPane(JPanel panel){
        return new JScrollPane(panel);
    }

    public List<RoomEventListener> getListeners(){
        List<RoomEventListener> listeners = infoPanel.getListeners();
        List<RoomEventListener> listeners2 = flagPanel.getListeners();

        List<RoomEventListener> allListeners = new ArrayList<RoomEventListener>();
        for(RoomEventListener listener : listeners){
            allListeners.add(listener);
        }
        for(RoomEventListener listener : listeners2){
            allListeners.add(listener);
        }
        return allListeners;
    }

    public void updateRoom(Room room){
        if(!(room instanceof AMRoom)){
            return;
        }
        infoPanel.updateRoom(room);
        flagPanel.updateRoom(room);
    }

    public int getRelevantRoomEventFlags(){
        return ROOM_PROPERTIES;
    }

}