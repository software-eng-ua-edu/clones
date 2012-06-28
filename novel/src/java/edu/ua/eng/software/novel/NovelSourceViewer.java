/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

/**
 * Creates the contents of the Source Viewer tab
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */

public class NovelSourceViewer extends JPanel
{
    JTextComponent sourcePane;
    JScrollPane sourceView;

    public NovelSourceViewer() {
        super(new GridLayout(1, 0));

        final Font monospace = new Font("Monospaced", Font.PLAIN, 12);

        sourcePane = new JTextPane();
        sourcePane.setEditable(false);
        sourcePane.setFont(monospace);
        sourceView = new JScrollPane(sourcePane);
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