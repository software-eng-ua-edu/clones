/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.BorderLayout;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * Creates the contents of the Source Viewer tab
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */

public class NovelSourceViewer extends JPanel
{
    RSyntaxTextArea sourcePane;
    RTextScrollPane sourceView;

    public NovelSourceViewer() {
        super(new BorderLayout());

        sourcePane = new RSyntaxTextArea();
        sourcePane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        sourcePane.setCodeFoldingEnabled(true);
        sourcePane.setAntiAliasingEnabled(true);
        sourcePane.setEditable(false);

        Theme theme;
        try {
            theme = Theme.load(getClass().getResourceAsStream("eclipse.xml"));
            theme.apply(sourcePane);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sourceView = new RTextScrollPane(sourcePane);
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