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
import alexmud.map.RoomKeywordDescription;
import mudcartographer.MudController;
import mudcartographer.gui.MudCartographerPanel;
import mudcartographer.map.Room;
import mudcartographer.map.RoomProperty;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Handles creating and maintaining a panel to
 * display room descriptions
 */
public class AMRoomKeywordDescriptionsPanel extends MudCartographerPanel {
    private static final Dimension BUTTON_SIZE = new Dimension(50, 20);
    private static int ROOM_PROPERTIES = RoomProperty.KEYWORD_DESCRIPTIONS.getFlagBits();

    private AMRoom room;
    private MudController controller;

    private JTable keywordDescriptionTable;
    private DefaultTableModel defaultTableModel;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private JButton addButton;
    private JButton deleteButton;

    private boolean isFocusReleased;

    public AMRoomKeywordDescriptionsPanel() {
    }

    public void initialize(MudController controller) {
        this.controller = controller;
        createComponents();
        layoutComponents();
        addActionListeners();
    }

    public void createComponents() {
        defaultTableModel = new DefaultTableModel();
        keywordDescriptionTable = new JTable(defaultTableModel);
        defaultTableModel.addColumn("Keyword");
        defaultTableModel.addColumn("Description");
        defaultTableModel.addRow(new String[]{"keyword one", "descripion one"});
        scrollPane = new JScrollPane(keywordDescriptionTable);
        createButtonPanelComponents();
    }

    private void createButtonPanelComponents() {
        addButton = new JButton("+");
        deleteButton = new JButton("-");
        buttonPanel = new JPanel();
    }

    private void layoutComponents() {
        this.setLayout(new BorderLayout());
        scrollPane.setPreferredSize(new Dimension(1, 100));
        keywordDescriptionTable.setFillsViewportHeight(true);
        this.add(scrollPane, BorderLayout.CENTER);

        layoutButtonPanel();
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void layoutButtonPanel() {
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

    private void addActionListeners() {
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultTableModel.addRow(new String[]{"", ""});
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (keywordDescriptionTable.getSelectedRowCount() > 0) {
                    int[] rowIds = keywordDescriptionTable.getSelectedRows();
                    Arrays.sort(rowIds);
                    for (int i = rowIds.length - 1; i >= 0; i--) {
                        defaultTableModel.removeRow(i);
                    }
                    updateRoomKeywordDescriptions();
                }
            }
        });

        defaultTableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateRoomKeywordDescriptions();
                keywordDescriptionTable.getSelectionModel().clearSelection();
                isFocusReleased = true;
                controller.releaseFocus();
            }
        });

        keywordDescriptionTable.addFocusListener(new FocusAdapter(){
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if(isFocusReleased){
                    isFocusReleased = false;
                    controller.releaseFocus();
                }
            }
        });

    }

    public void updateRoomKeywordDescriptions() {
        List<RoomKeywordDescription> keywordDescriptions = new ArrayList<RoomKeywordDescription>();
        Vector<Vector<String>> rowVectors = (Vector<Vector<String>>) defaultTableModel.getDataVector();
        for (Vector<String> rowVector : rowVectors) {
            if (!rowVector.get(0).isEmpty() || !rowVector.get(1).isEmpty()) {
                keywordDescriptions.add(new RoomKeywordDescription(rowVector.toArray(new String[2])));
            }
        }
        this.room.setKeywordDescriptions(keywordDescriptions);
    }


    public void updateRoom(Room room) {
        if (!(room instanceof AMRoom)) {
            return;
        }
        this.room = (AMRoom) room;
        String[][] keywordDescriptions = RoomKeywordDescription.get2DArrayOfKeywordDescriptions(this.room.getKeywordDescriptions());
        defaultTableModel.setDataVector(keywordDescriptions, new String[]{"Keywords", "Description"});
    }

    public int getRelevantRoomEventFlags() {
        return ROOM_PROPERTIES;
    }
}