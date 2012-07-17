/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import java.util.List;
import java.util.ArrayList;

import de.uni_bremen.st.rcf.model.Fragment;
import de.uni_bremen.st.rcf.model.File;

/**
 * Initializes the tree structure for clone classes
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
public class NovelClassesTree extends JTree implements TreeSelectionListener
{
    public NovelClassesTree() {
        root = new DefaultMutableTreeNode("Clone Classes");
        model = new DefaultTreeModel(root);
        setModel(model);
        createNodes();
        setRootVisible(false);
        getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        addTreeSelectionListener(this);

        setCellRenderer(new TreeRenderer());
    }

    public void createNodes() {
        DefaultMutableTreeNode category = null;
        DefaultMutableTreeNode category2 = null;
        category = new DefaultMutableTreeNode("Clone Class 1");
        root.add(category);
        category2 = new DefaultMutableTreeNode("Clone Class 2");
        root.add(category2);

        category.add(new DefaultMutableTreeNode("SubCategory"));
        category.add(new DefaultMutableTreeNode("SubCategory2"));
        category.add(new DefaultMutableTreeNode("SubCategory3"));

        category2.add(new DefaultMutableTreeNode("SubCategory"));
        category2.add(new DefaultMutableTreeNode("SubCategory2"));
        category2.add(new DefaultMutableTreeNode("SubCategory3"));

        model.reload();
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();

        if (node == null)
            return;
    }

    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;

    private class ClassCell
    {
        public ClassCell(List<Fragment> cloneClass, int position) {
            this.position = position;
            this.children = new ArrayList<FragmentCell>();
            int i = 0;
            for(Fragment fragment : cloneClass) {
                i++;
                children.add(new FragmentCell(fragment, this, i));
            }
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int i) {
            position = i;
        }

        public List<FragmentCell> getChildren() {
           return children; 
        }

        public String toString() {
            return String.format("%d CloneClass [%d]",
                getPosition(),
                getChildren().size());
        }

        private int position;
        private List<FragmentCell> children;

    }

    private class FragmentCell
    {
        public FragmentCell(Fragment f, ClassCell c, int position) {
            this.fragment = f;
            this.parent = c;
            this.position = position;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int i) {
            position = i;
        }

        public ClassCell getParent() {
            return parent;
        }

        public String toString() {
            return String.format("%d.%d %d:%d::%s",
                parent.getPosition(),
                getPosition(),
                getStart().getLine(),
                getEnd().getLine(),
                getFile().getRelativePath());
        }

        public File getFile() {
            return getStart().getFile();
        }

        private int position;
        private Fragment fragment;
        private ClassCell parent;
    }
}