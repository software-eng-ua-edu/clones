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

    public void showSource(File file) {
        CloneDataModel model = CloneDataModel.getInstance();
        String path = file.getRelativePath();
        if (path.startsWith("./")) {
            path = path.substring(2, path.length());
        }
        panel.updateSource(model.getVersion().getBasepath() + "/" + path);
        panel.showSourcePane();
    }

    public void updateStatus(String status) {
        panel.updateStatus(status);
    }

    public void loadCloneData() {
        panel.getClassesTree().loadFromDataModel();
        panel.getFilesList().loadFromDataModel();
    }

    public void loadBugData() {
        BugDataModel bdm = BugDataModel.getInstance();
    }

    protected NovelPanelController() {

    }

    private static NovelPanelController singleton = new NovelPanelController();

    private NovelPanel panel;
}