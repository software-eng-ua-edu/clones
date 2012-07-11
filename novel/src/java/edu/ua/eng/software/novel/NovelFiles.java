/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.GridLayout;
import java.util.Vector;

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
    private JList fileList;
    private JScrollPane filesPane;

    public NovelFiles() {
        super(new GridLayout(1, 0));
        
        java.io.File rcfFile = new java.io.File("test/test.rcf");
        NiCadImport nci = new NiCadImport(rcfFile);
        java.io.File nicadFile = new java.io.File("test/rhino-1.6R5_clones.xml");

        nci.addVersion(nicadFile, "rhino-1.6R5");
        RCF rcf = nci.getRCF();
        
        Vector<java.io.File> filesVector = new Vector<java.io.File>();
        for(File f : rcf.getVersions().getFirstEntry().getFiles()) {
            filesVector.add(new java.io.File(f.getAbsolutePath()));
        }
        fileList = new JList(filesVector);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        fileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileList.setVisibleRowCount(-1);
        
        filesPane = new JScrollPane(fileList);
    }

    public JScrollPane getFilesPane() {
        return filesPane;
    }
}