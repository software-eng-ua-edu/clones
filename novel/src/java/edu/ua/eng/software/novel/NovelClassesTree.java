/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Initializes the tree structure for clone classes
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 * @version 06/25/12
 * 
 */

public class NovelClassesTree {

    public JTree createTree() {

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Clone Classes");
        JTree treeClasses = new JTree(top);

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

        return treeClasses;
    }
}