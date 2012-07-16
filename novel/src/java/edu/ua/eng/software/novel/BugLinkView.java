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
import javax.swing.tree.DefaultTreeModel;
/**
 * @author Paige A. Rodeghero <parodeghero@bsu.edu>
 */
public class BugLinkView extends JTree
{
	public BugLinkView(){
		setModel(createModel());

		setRootVisible(false);
		setCellRenderer(new TreeRenderer());
	}

	private DefaultTreeModel createModel(){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Buggies");
		DefaultMutableTreeNode bug = null;
		DefaultMutableTreeNode bugDescription = null;
		
		bug = new DefaultMutableTreeNode("Bug");
		root.add(bug);

		bugDescription = new DefaultMutableTreeNode("Bug Description");
		bug.add(bugDescription);

		return new DefaultTreeModel(root);
	}
}

