/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import de.uni_bremen.st.rcf.imports.NiCadImport;
import de.uni_bremen.st.rcf.model.File;
import de.uni_bremen.st.rcf.model.RCF;

/**
 * Initializes the tree structure for clone pairs
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
@SuppressWarnings("serial")
public class NovelFilesList extends JList
{

    public NovelFilesList() {
        super.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        super.setVisibleRowCount(-1);

        super.setCellRenderer(new FileCellRenderer());
        super.addMouseListener(new NovelFilesListMouseListener());
    }

    public void loadFromDataModel() {
        CloneDataModel model = CloneDataModel.getInstance();
        super.setListData(model.getFiles());
    }

    public class FileCellRenderer extends DefaultListCellRenderer
    {
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            String s = (index + 1) + ":  " + ((File) value).getRelativePath();
            return super.getListCellRendererComponent(list, s, index,
                    isSelected, cellHasFocus);
        }
    }

    private class NovelFilesListMouseListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent evt) {
            JList list = (JList) evt.getSource();
            if (evt.getClickCount() == 2) {
                int index = list.locationToIndex(evt.getPoint());
                File file = (File) list.getModel().getElementAt(index);
                NovelPanelController.getInstance().showSource(file);
            }
        }
    }

    private String chooserPath;
}