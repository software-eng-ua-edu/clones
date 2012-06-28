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
 * Initializes the tree structure for clone classes
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */

public class NovelClassesTree extends JPanel implements TreeSelectionListener
{
    private JTree treeClasses;
    private JScrollPane treePane;

    public NovelClassesTree() {
        super(new GridLayout(1, 0));

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Clone Classes");
        createNodes(top);

        treeClasses = new JTree(top);
        treeClasses.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeClasses.addTreeSelectionListener(this);

        treePane = new JScrollPane(treeClasses);
    }

    public void createNodes(DefaultMutableTreeNode top) {

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

    public JScrollPane getTreePane() {
        return treePane;
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeClasses
                .getLastSelectedPathComponent();

        if (node == null)
            return;
    }
}