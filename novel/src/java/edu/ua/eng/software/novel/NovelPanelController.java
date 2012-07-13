/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import de.uni_bremen.st.rcf.model.File;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class NovelPanelController{
    public static NovelPanelController getInstance() {
        return singleton;
    }

    public void setPanel(NovelPanel panel) {
        this.panel = panel;
    }

    public void showSource(File file){
        panel.updateSource(file.getAbsolutePath());
        panel.showSourcePane();
    }

    protected NovelPanelController() {

    }

    private static NovelPanelController singleton = new NovelPanelController();

    private NovelPanel panel;
}