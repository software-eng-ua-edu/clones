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

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.net.URI;
import java.net.URISyntaxException;

import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
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
        //testing
        setBaseURL("http://sourceforge.net/search/index.php?group_id=12679&type_of_search=artifact&q=&artifact_id=%s");
        
        setRootVisible(false);
        addMouseListener(new BugLinkViewMouseListener());

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
            DefaultMutableTreeNode klassNode = new DefaultMutableTreeNode(klass);
            root.add(klassNode);
            for(FragmentCell fragment : fragments) {
                String path = fragment.getFragment().getStart().getFile().getRelativePath();
                DefaultMutableTreeNode fragNode = new DefaultMutableTreeNode(fragment);
                klassNode.add(fragNode);
                for(FileChange change : commitData.getChanges(path)) {
                    if(change.getCommit().isBugFix()) {
                        fragNode.add(new BugIDCell(change.getCommit().getBugID()));
                    }
                }
            }
        }
        model.reload();
        expandAll();
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    protected void browseTo(BugIDCell cell) {
        String url = getURLForBugID(cell.getBugID());
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException ioe) {

        } catch (URISyntaxException use) {

        }
    }

    protected String getURLForBugID(String bugID) {
        return getBaseURL().replaceAll("%s", bugID);
    }

    protected void expandAll() {
        for(int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }

    private class BugIDCell extends DefaultMutableTreeNode {
        public BugIDCell(String bugID) {
            this.bugID = bugID;
        }

        public String getBugID() {
            return bugID;
        }

        public String toString() {
            return bugID;
        }

        private String bugID;
    }

    private class BugLinkViewMouseListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent evt) {
            BugLinkView tree = (BugLinkView) evt.getSource();
            if (evt.getClickCount() == 2) {
                TreePath path = getPathForLocation(evt.getX(), evt.getY());
                if(path != null) {
                    TreeNode node = (TreeNode) path.getLastPathComponent();
                    if(node instanceof BugIDCell) {
                        tree.browseTo((BugIDCell) node);
                    }
                }
            }
        }
    }

    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private String baseURL;
}
