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
 */

public class NovelPairsTree extends JPanel implements TreeSelectionListener
{
    private JTree treePairs;
    private JScrollPane treePane;

    public NovelPairsTree() {
        super(new GridLayout(1, 0));

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("ProjectRoot");
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
        category = new DefaultMutableTreeNode("src");
        top.add(category);
        category2 = new DefaultMutableTreeNode("bin");
        top.add(category2);

        category.add(new DefaultMutableTreeNode("File1.java"));
        category.add(new DefaultMutableTreeNode("File2.java"));
        category.add(new DefaultMutableTreeNode("File3.java"));
        category.add(new DefaultMutableTreeNode("File4.java"));
        category.add(new DefaultMutableTreeNode("File5.java"));
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