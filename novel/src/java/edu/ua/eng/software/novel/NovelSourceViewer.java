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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * Creates the contents of the Source Viewer tab
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
@SuppressWarnings("serial")
public class NovelSourceViewer extends JPanel
{
    RSyntaxTextArea sourceLeft;
    RSyntaxTextArea sourceRight;
    JLabel pathLeft;
    JLabel pathRight;
    JSplitPane sourcePaneLeft;
    JSplitPane sourcePaneRight;
    JSplitPane splitSource;

    public NovelSourceViewer() {
        super(new BorderLayout());

        sourceLeft = new RSyntaxTextArea();
        sourceLeft.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        sourceLeft.setCodeFoldingEnabled(true);
        sourceLeft.setAntiAliasingEnabled(true);
        sourceLeft.setEditable(false);

        sourceRight = new RSyntaxTextArea();
        sourceRight.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        sourceRight.setCodeFoldingEnabled(true);
        sourceRight.setAntiAliasingEnabled(true);
        sourceRight.setEditable(false);

        RTextScrollPane scrollLeft = new RTextScrollPane(sourceLeft);
        RTextScrollPane scrollRight = new RTextScrollPane(sourceRight);

        pathLeft = new JLabel("Source Location:");
        pathRight = new JLabel("Source Location:");
        pathLeft.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pathRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sourcePaneLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pathLeft,
                scrollLeft);
        sourcePaneLeft.setDividerLocation(25);
        sourcePaneLeft.setDividerSize(0);
        sourcePaneLeft.setEnabled(false);

        sourcePaneRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pathRight,
                scrollRight);
        sourcePaneRight.setDividerLocation(25);
        sourcePaneRight.setDividerSize(0);
        sourcePaneRight.setEnabled(false);

        splitSource = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitSource.setResizeWeight(0.5);
        splitSource.setDividerSize(0);
        splitSource.setEnabled(false);
        initialize();
    }
    
    private void initialize() {
        super.add(sourcePaneLeft, BorderLayout.CENTER);
    }

    public void setSingleSource(String sourceFile) {

        try {
            FileReader fr = new FileReader(sourceFile);
            sourceLeft.read(fr, null);
            pathLeft.setText(sourceFile);
            sourceLeft.setCaretPosition(0);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void setSplitSource(String sourceFileLeft, String sourceFileRight) {
        try {
            FileReader frLeft = new FileReader(sourceFileLeft);
            sourceLeft.read(frLeft, null);
            splitSource.setLeftComponent(sourcePaneLeft);
            sourceLeft.setCaretPosition(0);
        } catch (IOException e) {
            System.err.println(e);
        }

        try {
            FileReader frRight = new FileReader(sourceFileRight);
            sourceRight.read(frRight, null);
            splitSource.setRightComponent(sourcePaneRight);
            sourceRight.setCaretPosition(0);
        } catch (IOException e) {
            System.err.println(e);
        }
        super.add(splitSource);
    }
}