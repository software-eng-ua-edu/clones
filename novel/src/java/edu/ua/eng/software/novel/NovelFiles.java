/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import de.uni_bremen.st.rcf.imports.NiCadImport;
import de.uni_bremen.st.rcf.model.File;
import de.uni_bremen.st.rcf.model.RCF;

/**
 * Initializes the tree structure for clone pairs
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */

public class NovelFiles extends JPanel
{

    private JScrollPane filesPane;
    private String chooserPath;

    public NovelFiles(String path) {
        super(new GridLayout(1, 0));

        chooserPath = path;
        if (chooserPath == null) {
            JList fileList = new JList();
            filesPane = new JScrollPane(fileList);
        } else
            populate();
    }

    public void populate() {

        java.io.File rcfFile = new java.io.File("test/test.rcf");
        NiCadImport nci = new NiCadImport(rcfFile);
        java.io.File nicadFile = new java.io.File(chooserPath);

        nci.addVersion(nicadFile, "rhino-1.6R5");
        RCF rcf = nci.getRCF();

        Vector<File> filesVector = new Vector<File>();
        for (File f : rcf.getVersions().getFirstEntry().getFiles()) {
            filesVector.add(f);
        }
        JList fileList = new JList(filesVector);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileList.setVisibleRowCount(-1);

        fileList.setCellRenderer(new FileCellRenderer());

        filesPane = new JScrollPane(fileList);
    }

    public JScrollPane getFilesPane() {

        return filesPane;
    }

    public class FileCellRenderer extends DefaultListCellRenderer
    {
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            String s = ((File) value).getRelativePath();
            return super.getListCellRendererComponent(list, s, index,
                    isSelected, cellHasFocus);
        }
    }
}