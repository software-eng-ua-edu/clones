/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

/**
 * Creates the visible UI panel and initializes components
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */

public class NovelPanel extends JPanel
{
    private JLabel statusLabel;
    private NovelSourceViewer sourcePane;
    private JScrollPane sourceView;

    public JPanel createContentPane() {

        JPanel composite = new JPanel();
        composite.setLayout(new BorderLayout());

        // instantiate status bar
        statusLabel = new JLabel("Press F1 for help.");
        Border statusBorder = BorderFactory.createMatteBorder(1, 0, 0, 0,
                Color.GRAY);
        Border statusPad = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(statusBorder,
                statusPad));
        statusLabel.setFont(new Font(statusLabel.getFont().getName(),
                Font.ITALIC, statusLabel.getFont().getSize()));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.LIGHT_GRAY);

        // build trees
        NovelPairsTree pairsTree = new NovelPairsTree();
        JScrollPane treePairs = pairsTree.getTreePane();
        NovelClassesTree classesTree = new NovelClassesTree();
        JScrollPane treeClasses = classesTree.getTreePane();

        // initialize source viewer pane
        sourcePane = new NovelSourceViewer();
        sourceView = sourcePane.blankSource();

        // resizable pane
        JSplitPane tabPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        tabPanels.setDividerLocation(225);
        tabPanels.setResizeWeight(0.2);
        tabPanels.setOneTouchExpandable(false);
        tabPanels.setContinuousLayout(true);

        // left tabbed pane
        JTabbedPane leftPane = new JTabbedPane();
        leftPane.addTab("Files", treePairs);
        leftPane.addTab("Clone Classes", treeClasses);

        // right tabbed pane
        JTabbedPane rightPane = new JTabbedPane();
        rightPane.addTab("Source", sourceView);
        rightPane.addTab("Pie Chart", new JLabel("Pie Chart", JLabel.CENTER));
        rightPane.addTab("Bars", new JLabel("Bars", JLabel.CENTER));
        rightPane.addTab("Tree Map", new JLabel("Tree Map", JLabel.CENTER));

        tabPanels.setLeftComponent(leftPane);
        tabPanels.setRightComponent(rightPane);

        // add components to the composite JPanel
        composite.add(statusLabel, BorderLayout.SOUTH);
        composite.add(tabPanels, BorderLayout.CENTER);
        return composite;
    }

    public void updateStatus(String status) {

        statusLabel.setText(status);
    }

    public void updateSource(String sourcePath) {

        sourcePane.setSource(sourcePath);
    }
}