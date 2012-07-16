/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
/**
 * @author Paige A. Rodeghero <parodeghero@bsu.edu>
 */
public class TreeRenderer extends DefaultTreeCellRenderer
{
	public Component getTreeCellRendererComponent(JTree tree, Object val, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus ){
		super.getTreeCellRendererComponent(tree, val, sel, expanded, leaf, row, hasFocus);
		
		this.setLeafIcon(null);

		return this;
	}	
}