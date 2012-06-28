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
 * @version 06/27/12
 * 
 */

public class NovelPanel extends JPanel {

    private JScrollPane treePairs;
    private JScrollPane treeClasses;
    private JLabel status;

    public JPanel createContentPane() {

        JPanel composite = new JPanel();
        composite.setLayout(new BorderLayout());

        status = new JLabel("Press F1 for help.");
        Border statusBorder = BorderFactory.createMatteBorder(1, 0, 0, 0,
                Color.GRAY);
        Border statusPad = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        status.setBorder(BorderFactory.createCompoundBorder(statusBorder,
                statusPad));
        status.setFont(new Font(status.getFont().getName(), Font.ITALIC, status
                .getFont().getSize()));
        status.setOpaque(true);
        status.setBackground(Color.WHITE);

        JSplitPane tabPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        tabPanels.setDividerLocation(250);
        tabPanels.setResizeWeight(0.2);
        tabPanels.setOneTouchExpandable(false);
        tabPanels.setContinuousLayout(true);

        NovelPairsTree pairsTree = new NovelPairsTree();
        treePairs = pairsTree.getTreePane();

        NovelClassesTree classesTree = new NovelClassesTree();
        treeClasses = classesTree.getTreePane();

        JTabbedPane leftPane = new JTabbedPane();
        leftPane.addTab("Clone Pairs", treePairs);
        leftPane.addTab("Clone Classes", treeClasses);

        JTabbedPane rightPane = new JTabbedPane();
        rightPane.addTab("Source", new JLabel("Source", JLabel.CENTER));
        rightPane.addTab("Pie Chart", new JLabel("Pie Chart", JLabel.CENTER));
        rightPane.addTab("Bars", new JLabel("Bars", JLabel.CENTER));
        rightPane.addTab("Tree Map", new JLabel("Tree Map", JLabel.CENTER));

        tabPanels.setLeftComponent(leftPane);
        tabPanels.setRightComponent(rightPane);

        composite.add(status, BorderLayout.SOUTH);
        composite.add(tabPanels, BorderLayout.CENTER);
        return composite;
    }

    public void updateStatus(String s) {

        status.setText(s);
    }
}