/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.GridLayout;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Creates the contents of the Source Viewer tab. This class uses the
 * JSyntaxPane library to enable syntax highlighting in the source viewer.
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */

public class NovelSourceViewer extends JPanel
{
    JEditorPane sourcePane;
    JScrollPane sourceView;

    public NovelSourceViewer() {
        super(new GridLayout(1, 0));

        jsyntaxpane.DefaultSyntaxKit.initKit();
        sourcePane = new JEditorPane();
        sourcePane.setEditable(false);
        sourceView = new JScrollPane(sourcePane);
        sourcePane.setContentType("text/java");
    }

    public JScrollPane blankSource() {
        return sourceView;
    }

    public JScrollPane setSource(String sourceFile) {
        try {
            FileReader fr = new FileReader(sourceFile);
            sourcePane.read(fr, null);
        } catch (IOException e) {
            System.err.println(e);
        }
        return sourceView;
    }
}