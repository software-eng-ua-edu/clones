/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import de.uni_bremen.st.rcf.model.File;
import de.uni_bremen.st.rcf.model.Directory;

import edu.ua.eng.software.novel.NovelClassesTree.ClassCell;
import edu.ua.eng.software.novel.NovelClassesTree.FragmentCell;

import java.util.List;
import java.util.LinkedList;

/**
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 * @author Casey Ferris <cmferris1@crimson.ua.edu>
 * @author Colin Hemphill <colin@hemphill.us>
 */

public class NovelPanelController
{
    public static NovelPanelController getInstance() {
        return singleton;
    }

    public void setPanel(NovelPanel panel) {
        this.panel = panel;
    }

    public void fileSelected(File file) {
        CloneDataModel model = CloneDataModel.getInstance();
        String path = file.getRelativePath();
        if (path.startsWith("./")) {
            path = path.substring(2, path.length());
        }
        panel.updateFileSelected(file);
    }

    public void classCellSelected(ClassCell classCell) {
        fragmentCellsSelected(classCell.getChildren().subList(0,2));
    }

    public void fragmentCellsSelected(List<FragmentCell> fragments) {
        panel.updateFragmentsSelected(fragments);
    }

    public void updateStatus(String status) {
        panel.updateStatus(status);
    }

    public void loadCloneData() {
        panel.getClassesTree().loadFromDataModel();
        panel.getFilesList().loadFromDataModel();
    }

    public void loadBugData() {
        panel.getBugLinkView(); 
    }

    protected NovelPanelController() {

    }

    private static NovelPanelController singleton = new NovelPanelController();

    private NovelPanel panel;
}