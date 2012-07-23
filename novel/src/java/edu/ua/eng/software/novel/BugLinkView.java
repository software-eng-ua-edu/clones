/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.util.List;
import java.util.ListIterator;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeCellRenderer;

import edu.ua.eng.software.clonelink.CommitData;
import edu.ua.eng.software.clonelink.Commit;
import edu.ua.eng.software.clonelink.FileChange;

import edu.ua.eng.software.novel.NovelClassesTree.FragmentCell;
import edu.ua.eng.software.novel.NovelClassesTree.ClassCell;

/**
 * @author Paige A. Rodeghero <parodeghero@bsu.edu>
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 */
@SuppressWarnings("serial")
public class BugLinkView extends JTree
{
    public BugLinkView() {
        setRootVisible(false);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        renderer.setLeafIcon(null);
        setCellRenderer(renderer);

        root = new DefaultMutableTreeNode("Bugs");
        model = new DefaultTreeModel(root);
        setModel(model);
    }

    public void showFragments(List<FragmentCell> fragments) {
        root.removeAllChildren();
        if(!fragments.isEmpty()) {
            fragments = fragments.get(0).getParent().getChildren();
            CommitData commitData = BugDataModel.getInstance().getCommitData();
            ClassCell klass = fragments.get(0).getParent();
            DefaultMutableTreeNode klassNode = new DefaultMutableTreeNode(klass.toString());
            root.add(klassNode);
            for(Commit commit : commitData.getBugCommits()) {
                for(FileChange change : commit.getFileChanges()) {
                    //if(change.getOldPath().startsWith("jhotdraw7")) {
                        System.out.println(change.getOldPath());
                    //}
                }
            }
            for(FragmentCell fragment : fragments) {
                String path = fragment.getFragment().getStart().getFile().getRelativePath();
                System.out.println(path);

                DefaultMutableTreeNode fragNode = new DefaultMutableTreeNode(fragment);
                klassNode.add(fragNode);
                for(FileChange change : commitData.getChanges(path)) {
                    if(change.getCommit().isBugFix()) {
                        fragNode.add(new DefaultMutableTreeNode(change.getCommit().getBugID()));
                    }
                }
            }
        }
        model.reload();
        expandAll();
    }

    protected void expandAll() {
        for(int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }

    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
}
