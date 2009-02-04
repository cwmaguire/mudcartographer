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
 */package mudcartographer;

import mudcartographer.menu.MenuBar;

import javax.swing.*;
import java.awt.*;

import mudcartographer.map.MudMap;
import mudcartographer.gui.MapPainter;
import mudcartographer.map.Room;
import mudcartographer.gui.RoomDescriptionPanel;
import mudcartographer.gui.RoomInfoPanel;
import mudcartographer.event.RoomEvent;

/**
 * A MudCartographer application including a map display and room information area
 */
public class MudCartographer{
    private final int FRAME_WIDTH = 600;
    private final int FRAME_HEIGHT = 400;
    private final String FRAME_TITLE = "Mud Cartographer";

    private MudMap map;
    private MudController controller;
    private JScrollPane mapPainterScrollPane;
    private RoomInfoPanel roomInfoPanel;
    private RoomDescriptionPanel roomDescriptionPanel;
    private MenuBar menuBar;

    public static void main(String[] args){
        MudCartographer mudCartographer = new MudCartographer();
        mudCartographer.load();
    }

    /**
     * Load up the frame, controls and painter for a MudCartographer
     */
    public void load(){
        final JFrame f = createAndSetupFrame();
        controller = MudController.getMudController();

        createMap();
        createSubPanels();
        createMenu();

        f.setJMenuBar(menuBar);
        f.add(createMainPanelAndAddSubPanels());

        setupEventListeners();
        fireInitialRoomEvent();

        f.pack();
        f.setVisible(true);

    }

    private JFrame createAndSetupFrame() {
        JFrame frame = new JFrame(FRAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        frame.setFocusable(false);
        frame.getRootPane().setFocusable(false);
        frame.getLayeredPane().setFocusable(false);
        frame.getContentPane().setFocusable(false);
        frame.getGlassPane().setFocusable(false);
        return frame;
    }

    private void createMap() {
        map = new MudMap();
        map.setInitialRoom(new Room(' '));
    }

    private void createSubPanels() {
        createMapPainterScrollPane();
        roomInfoPanel = new RoomInfoPanel(controller);
        roomDescriptionPanel = new RoomDescriptionPanel(controller);
    }

    private void createMapPainterScrollPane() {
        mapPainterScrollPane = new JScrollPane(createMapPainter());
        mapPainterScrollPane.setFocusable(false);
    }

    // wraps temporal coupling
    private void addPainterAndCreateFocusBypass(MapPainter painter) {
        mapPainterScrollPane.setViewportView(painter);
    }

    private MapPainter createMapPainter() {
        MapPainter painter = new MapPainter(map);
        painter.setupEventHandling(controller);
        return painter;
    }

    private void createMenu(){
        menuBar = new mudcartographer.menu.MenuBar();
        menuBar.setup();
    }

    private JPanel createMainPanelAndAddSubPanels() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setFocusable(false);
        mainPanel.add(mapPainterScrollPane, BorderLayout.CENTER);
        mainPanel.add(roomInfoPanel, BorderLayout.EAST);
        mainPanel.add(roomDescriptionPanel, BorderLayout.SOUTH);
        return mainPanel;
    }

    private void fireInitialRoomEvent() {
        controller.fireRoomEvent(new RoomEvent(map.getCurrentRoom(), Room.RoomProperty.getAll(), this));
    }

    private void setupEventListeners() {
        controller.addListener(roomInfoPanel);
        controller.addListener(roomDescriptionPanel);
    }

    public MudMap getMudMap(){
        return map;
    }
}
