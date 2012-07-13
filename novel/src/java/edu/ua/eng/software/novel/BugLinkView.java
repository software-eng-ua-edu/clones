/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * 
 * @author Paige A. Rodeghero <parodeghero@bsu.edu>
 */
public class BugLinkView extends JPanel
{
	private JTree bugTree;
	private JScrollPane scrollPane;

	public BugLinkView(){
		bugTree = new JTree();
		bugTree.setModel(createModel());

		scrollPane = new JScrollPane();
		scrollPane.add(bugTree);
		add(scrollPane);

		add(bugTree);
	}

	private DefaultTreeModel createModel(){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Buggies");
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode bug = null;
		
		category = new DefaultMutableTreeNode("Bug types");
		root.add(category);

		bug = new DefaultMutableTreeNode("This is a bug.  aka bug #1 or tech. bug #0...yeah");
		category.add(bug);

		return new DefaultTreeModel(root);
	}
}