/*
Copyright 2008 Chris Maguire (cwmaguire@gmail.com)

Lulu's MUD Cartographer is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Lulu's MUD Cartographer is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Lulu's MUD Cartographer.  If not, see <http://www.gnu.org/licenses/>.

August 27, 2008
*/
package mudcartographer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A MudCartographer application including a map display and room information area
 */
public class MudCartographer{
    private final int FRAME_WIDTH = 600;
    private final int FRAME_HEIGHT = 400;
    private final String FRAME_TITLE = "Mud Cartographer";

    private MudMap map;
    private MudController controller;
    private JScrollPane painterScrollPane;
    private RoomInfoPanel roomInfoPanel;
    private RoomDescriptionPanel roomDescriptionPanel;
    private MudMenu mudMenu;

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

        f.setJMenuBar(mudMenu);
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
        painterScrollPane = createScrollingPainter();
        roomInfoPanel = new RoomInfoPanel(controller);
        roomDescriptionPanel = new RoomDescriptionPanel(controller);
    }

    private JScrollPane createScrollingPainter() {
        JScrollPane painterScrollPane = new JScrollPane();
        MapPainter painter = createMapPainter();

        //painterScrollPane.setFocusable(false);

        addPainterAndCreateFocusBypass(painterScrollPane, painter);

        return painterScrollPane;
    }

    // wraps temporal coupling
    private void addPainterAndCreateFocusBypass(JScrollPane painterScrollPane, MapPainter painter) {
        painterScrollPane.add(painter);
        setupMapPainterParentFocusBypass(painter);
    }

    private void setupMapPainterParentFocusBypass(final MapPainter painter) {
        System.out.println(painter.getParent().getClass().getName());

        painter.getParent().addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                System.out.println(e.getClass() + ": " + e.toString());
                painter.requestFocus();
            }

            public void focusLost(FocusEvent e) {
            }
        });
    }

    private MapPainter createMapPainter() {
        MapPainter painter = new MapPainter(map);
        painter.setupEventHandling(controller);
        return painter;
    }

    private void createMenu(){
        mudMenu = new MudMenu();
        mudMenu.setup();
    }

    private JPanel createMainPanelAndAddSubPanels() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setFocusable(false);
        mainPanel.add(painterScrollPane, BorderLayout.CENTER);
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
