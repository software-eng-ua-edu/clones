/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * Initializes the tree structure for clone pairs
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 * @version 06/27/12
 * 
 */

public class NovelPairsTree extends JPanel implements TreeSelectionListener {

    private JTree treePairs;
    private JScrollPane treePane;

    public NovelPairsTree() {
        super(new GridLayout(1, 0));

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Clone Pairs");
        createNodes(top);

        treePairs = new JTree(top);
        treePairs.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        treePairs.addTreeSelectionListener(this);

        treePane = new JScrollPane(treePairs);
    }

    public void createNodes(DefaultMutableTreeNode top) {

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
        category2.add(new DefaultMutableTreeNode("SubCategory4"));
        category2.add(new DefaultMutableTreeNode("SubCategory5"));
        category2.add(new DefaultMutableTreeNode("SubCategory6"));
        category2.add(new DefaultMutableTreeNode("SubCategory7"));
        category2.add(new DefaultMutableTreeNode("SubCategory8"));
        category2.add(new DefaultMutableTreeNode("SubCategory9"));
        category2.add(new DefaultMutableTreeNode("SubCategory10"));
        category2.add(new DefaultMutableTreeNode("SubCategory11"));
        category2.add(new DefaultMutableTreeNode("SubCategory12"));
        category2.add(new DefaultMutableTreeNode("SubCategory13"));
        category2.add(new DefaultMutableTreeNode("SubCategory14"));
        category2.add(new DefaultMutableTreeNode("SubCategory15"));
        category2.add(new DefaultMutableTreeNode("SubCategory16"));
        category2.add(new DefaultMutableTreeNode("SubCategory17"));
        category2.add(new DefaultMutableTreeNode("SubCategory18"));
        category2.add(new DefaultMutableTreeNode("SubCategory19"));
        category2.add(new DefaultMutableTreeNode("SubCategory20"));
        category2.add(new DefaultMutableTreeNode("SubCategory21"));
        category2.add(new DefaultMutableTreeNode("SubCategory22"));
        category2.add(new DefaultMutableTreeNode("SubCategory23"));
        category2.add(new DefaultMutableTreeNode("SubCategory24"));
        category2.add(new DefaultMutableTreeNode("SubCategory25"));
    }

    public JScrollPane getTreePane() {
        return treePane;
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePairs
                .getLastSelectedPathComponent();

        if (node == null)
            return;
    }
}