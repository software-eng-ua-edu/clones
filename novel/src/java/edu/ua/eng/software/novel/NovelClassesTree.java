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
import javax.swing.tree.TreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Enumeration;

import de.uni_bremen.st.rcf.model.Fragment;
import de.uni_bremen.st.rcf.model.File;
import de.uni_bremen.st.rcf.model.CloneClass;

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
        setRootVisible(false);
        getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        addTreeSelectionListener(this);

        setCellRenderer(new TreeRenderer());
    }

    public void loadFromDataModel() {
        CloneDataModel dataModel = CloneDataModel.getInstance();
        List<CloneClass> cloneClasses = dataModel.getCloneClasses();
        for(CloneClass cc : cloneClasses) {
            Set<Fragment> fragments = dataModel.getClones(cc);
            ClassCell classCell = new ClassCell(fragments);
            root.add(classCell);
            classCell.updateString();
        }
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

    private class ClassCell extends DefaultMutableTreeNode
    {
        public ClassCell(Set<Fragment> cloneClass) {
            for(Fragment fragment : cloneClass) {
                FragmentCell cell = new FragmentCell(fragment);
                add(cell);
            }
        }

        public void updateString() {
            setUserObject(this.toString());
            for(Enumeration children = children(); children.hasMoreElements();) {
                FragmentCell cell = (FragmentCell) children.nextElement();
                cell.updateString();
            }
        }

        public int getPosition() {
            return getParent().getIndex(this);
        }

        public String toString() {
            return String.format("%d CloneClass [%d]",
                getPosition() + 1,
                getChildCount());
        }
    }

    private class FragmentCell extends DefaultMutableTreeNode
    {
        public FragmentCell(Fragment f) {
            this.fragment = f;
        }

        public void updateString() {
            setUserObject(this.toString());
        }

        public Fragment getFragment() {
            return fragment;
        }

        public int getPosition() {
            return getParent().getIndex(this);
        }

        public String toString() {
            TreeNode parent = getParent();
            return String.format("%d.%d %d:%d::%s",
                parent.getParent().getIndex(parent),
                getPosition() + 1,
                getFragment().getStart().getLine(),
                getFragment().getEnd().getLine(),
                getFile().getRelativePath());
        }

        public File getFile() {
            return getFragment().getStart().getFile();
        }

        private Fragment fragment;
    }
}