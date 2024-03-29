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

import mudcartographer.event.RoomEvent;
import mudcartographer.gui.MapPainter;
import mudcartographer.gui.MudCartographerPanel;
import mudcartographer.map.MudMap;
import mudcartographer.map.RoomProperty;
import mudcartographer.menu.MudMenuBar;
import mudcartographer.plugin.Plugin;

import javax.swing.*;
import java.awt.*;

/**
 * A MudCartographer application including a map display and room information area
 */
public class MudCartographer{
    private final int FRAME_WIDTH = 700;
    private final int FRAME_HEIGHT = 500;
    private final String FRAME_TITLE = "Mud Cartographer";

    public static MudCartographer mudCartographer;
    private JFrame frame;
    private MudMap mudMap;
    private MudController controller;
    private JScrollPane mapPainterScrollPane;
    private MudCartographerPanel roomInfoPanel;
    private MudCartographerPanel roomDescriptionPanel;
    private JPanel mainPanel;

    public static void main(String[] args){
        mudCartographer = new MudCartographer();
        mudCartographer.load(null);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void load(Plugin plugin){
        MudMenuBar mudMenuBar;

        if(frame != null){
            frame.remove(mainPanel);
        }else{
            frame = createAndSetupFrame();
            controller = MudController.getMudController();
        }

        if(plugin == null){
            controller.plugin = null;
            controller.mudMap = null;
            frame.add(createAndAddEmptyMainPanel());
            mudMenuBar = createMenu(controller);
            frame.setJMenuBar(mudMenuBar);
            mudMenuBar.setIsEditing(false);
        }else{
            plugin.setup();
            loadPlugin(plugin);
            controller.plugin = plugin;
            controller.mudMap = mudMap;
            mudMenuBar = createMenu(controller);
            frame.setJMenuBar(mudMenuBar);
            mudMenuBar.setIsEditing(true);
        }

        frame.pack();
        frame.setVisible(true);
        frame.repaint();
    }

    public void loadPlugin(Plugin plugin) {
        controller.removeAllListeners();

        createMap(plugin.getRoomClass());
        createSubPanels(plugin);

        frame.add(createMainPanelAndAddSubPanels());

        setupEventListeners();
        fireInitialRoomEvent();

        frame.pack();
        frame.setVisible(true);
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

    private void createMap(Class roomClass) {
        mudMap = new MudMap(roomClass);
        mudMap.setupInitialRoom();
    }

    private void createSubPanels(Plugin plugin) {
        createMapPainterScrollPane();
        try{
            roomInfoPanel = (MudCartographerPanel) plugin.getRoomInfoPanelClass().newInstance();
            roomDescriptionPanel = (MudCartographerPanel) plugin.getRoomDescriptionPanelClass().newInstance();
        }catch(Exception e){

            e.printStackTrace();
            throw new RuntimeException("Could not create new description and info panels");
        }

        roomInfoPanel.initialize(controller);
        roomDescriptionPanel.initialize(controller);
    }

    private void createMapPainterScrollPane() {
        mapPainterScrollPane = new JScrollPane(createMapPainter());
        mapPainterScrollPane.setFocusable(false);
    }

    private MapPainter createMapPainter() {
        MapPainter painter = new MapPainter(mudMap);
        painter.setupEventHandling(controller);
        return painter;
    }

    private MudMenuBar createMenu(){
        return createMenu(null);
    }

    private MudMenuBar createMenu(MudController controller){
        MudMenuBar mudMenuBar = new MudMenuBar(controller);
        mudMenuBar.setup();
        return mudMenuBar;
    }

    private JPanel createAndAddEmptyMainPanel(){
        mainPanel = new JPanel();
        JLabel instructionLabel = new JLabel("Open a map or create a new map to start.");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(instructionLabel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createMainPanelAndAddSubPanels() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setFocusable(false);
        mainPanel.add(mapPainterScrollPane, BorderLayout.CENTER);
        mainPanel.add(roomInfoPanel, BorderLayout.EAST);
        mainPanel.add(roomDescriptionPanel, BorderLayout.SOUTH);
        return mainPanel;
    }

    private void fireInitialRoomEvent() {
        controller.fireRoomEvent(new RoomEvent(mudMap.getCurrentRoom(), RoomProperty.getAll(), this));
    }

    private void setupEventListeners() {
        controller.addListeners(roomInfoPanel.getListeners());
        controller.addListeners(roomDescriptionPanel.getListeners());
    }

    public MudMap getMudMap(){
        return mudMap;
    }
}
