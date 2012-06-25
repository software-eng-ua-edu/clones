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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Creates the visible UI panel and initializes components
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 * @version 06/25/12
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
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
                Color.GRAY));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.WHITE);

        JPanel tabPanels = new JPanel();
        tabPanels.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        createTree("pairs");
        createTree("classes");

        JTabbedPane leftPane = new JTabbedPane();
        leftPane.addTab("Clone Pairs", treePairs);
        leftPane.addTab("Clone Classes", treeClasses);
        c.weightx = .1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        leftPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
                Color.GRAY));
        tabPanels.add(leftPane, c);

        JTabbedPane rightPane = new JTabbedPane();
        rightPane.addTab("Source", new JLabel("Source", JLabel.CENTER));
        rightPane.addTab("Pie Chart", new JLabel("Pie Chart", JLabel.CENTER));
        rightPane.addTab("Bars", new JLabel("Bars", JLabel.CENTER));
        rightPane.addTab("Tree Map", new JLabel("Tree Map", JLabel.CENTER));
        c.weightx = .5;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.BOTH;
        tabPanels.add(rightPane, c);

        composite.add(statusBar, BorderLayout.SOUTH);
        composite.add(tabPanels);
        return composite;
    }

    public void createTree(String tree) {
        if (tree.equals("pairs")) {
            DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                    "Clone Pairs");
            createTreeNodesPairs(top);
            treePairs = new JTree(top);
        } else {
            DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                    "Clone Classes");
            createTreeNodesClasses(top);
            treeClasses = new JTree(top);
        }
    }

    private void createTreeNodesPairs(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode category2 = null;
        category = new DefaultMutableTreeNode("Clone Pair 1");
        top.add(category);
        category2 = new DefaultMutableTreeNode("Clone Pair 2");
        top.add(category2);

        category.add(new DefaultMutableTreeNode("SubCategory"));
        category.add(new DefaultMutableTreeNode("SubCategory2"));
        category.add(new DefaultMutableTreeNode("SubCategory3"));

        category2.add(new DefaultMutableTreeNode("SubCategory"));
        category2.add(new DefaultMutableTreeNode("SubCategory2"));
        category2.add(new DefaultMutableTreeNode("SubCategory3"));
    }

    private void createTreeNodesClasses(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode category2 = null;
        category = new DefaultMutableTreeNode("Clone Class 1");
        top.add(category);
        category2 = new DefaultMutableTreeNode("Clone Class 2");
        top.add(category2);

        category.add(new DefaultMutableTreeNode("SubCategory"));
        category.add(new DefaultMutableTreeNode("SubCategory2"));
        category.add(new DefaultMutableTreeNode("SubCategory3"));

        category2.add(new DefaultMutableTreeNode("SubCategory"));
        category2.add(new DefaultMutableTreeNode("SubCategory2"));
        category2.add(new DefaultMutableTreeNode("SubCategory3"));
    }

    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
        // TODO Auto-generated method stub
    }
}
