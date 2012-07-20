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
        if(!fragments.isEmpty()) {
            CommitData commitData = BugDataModel.getInstance().getCommitData();
            ClassCell klass = fragments.get(0).getParent();
            DefaultMutableTreeNode klassNode = new DefaultMutableTreeNode(klass.toString());
            root.add(klassNode);
            for(FragmentCell fragment : fragments) {
                String path = fragment.getFragment().getStart().getFile().getRelativePath();
                List<FileChange> changes = commitData.getChanges(path);
                ListIterator<FileChange> it = changes.listIterator();

                //removing non-bug changes
                while(it.hasNext()) {
                    FileChange change = it.next();
                    if(!change.getCommit().isBugFix()) {
                        it.remove();
                    }
                }

                if(!changes.isEmpty()) {
                    DefaultMutableTreeNode fragNode = new DefaultMutableTreeNode(fragment.getFragment());
                    klassNode.add(fragNode);
                    for(FileChange change : changes) {
                        fragNode.add(new DefaultMutableTreeNode(change.getCommit().getBugID()));
                    }
                }
            }
            model.reload();
        }
    }

    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
}
