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

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * Creates the visible UI panel and initializes components
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 * @version 06/27/12
 * 
 */

public class NovelPanel extends JPanel implements TreeSelectionListener {

    private JTree treePairs;
    private JTree treeClasses;
    private JEditorPane treePane;

    public JPanel createContentPane() {

        JPanel composite = new JPanel();
        composite.setLayout(new BorderLayout());

        JLabel statusBar = new JLabel("Status Bar");
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                Color.GRAY));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.WHITE);

        JSplitPane tabPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        tabPanels.setDividerLocation(250);
        tabPanels.setResizeWeight(0.2);
        tabPanels.setContinuousLayout(true);

        ClonePairsTree pairsTree = new ClonePairsTree();
        treePairs = pairsTree.createTree();

        CloneClassesTree classesTree = new CloneClassesTree();
        treeClasses = classesTree.createTree();

        JTabbedPane leftPane = new JTabbedPane();
        leftPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        leftPane.addTab("Clone Pairs", treePairs);
        leftPane.addTab("Clone Classes", treeClasses);

        JTabbedPane rightPane = new JTabbedPane();
        rightPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        rightPane.addTab("Source", new JLabel("Source", JLabel.CENTER));
        rightPane.addTab("Pie Chart", new JLabel("Pie Chart", JLabel.CENTER));
        rightPane.addTab("Bars", new JLabel("Bars", JLabel.CENTER));
        rightPane.addTab("Tree Map", new JLabel("Tree Map", JLabel.CENTER));

        tabPanels.setLeftComponent(leftPane);
        tabPanels.setRightComponent(rightPane);

        composite.add(statusBar, BorderLayout.SOUTH);
        composite.add(tabPanels, BorderLayout.CENTER);
        return composite;
    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
        // TODO Auto-generated method stub
    }
}
