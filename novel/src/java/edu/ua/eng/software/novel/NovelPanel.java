/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import de.uni_bremen.st.rcf.model.File;
import edu.ua.eng.software.novel.NovelClassesTree.FragmentCell;

/**
 * Creates the visible UI panel and initializes components
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 * @author Paige A. Rodeghero <parodeghero@bsu.edu>
 */
@SuppressWarnings("serial")
public class NovelPanel extends JPanel
{
    private JLabel statusLabel;

    private NovelSourceViewer sourcePane;
    private NovelBarsViewer barStripesPane;
    private BugLinkView bugLinkView;

    private NovelClassesTree classesTree;
    private NovelFilesList filesList;
    // private JPanel barStripesView;

    private JTabbedPane contentPane;

    private FragmentCell leftFrag;
    private FragmentCell rightFrag;

    // private JPanel bugLinkPanel;

    public NovelPanel() {
        super.setLayout(new BorderLayout());

        // instantiate status bar
        statusLabel = new JLabel("Press F1 for help.");
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        statusLabel.setFont(new Font(statusLabel.getFont().getName(),
                Font.ITALIC, statusLabel.getFont().getSize()));

        // build file list and clone pairs tree
        filesList = new NovelFilesList();
        JScrollPane filesView = new JScrollPane(filesList);
        classesTree = new NovelClassesTree();
        JScrollPane classesPane = new JScrollPane(classesTree);

        // initialize source viewer pane
        sourcePane = new NovelSourceViewer();

        // initialize bar and stripe pane
        barStripesPane = new NovelBarsViewer();
        bugLinkView = new BugLinkView();
        JScrollPane bugLinkPane = new JScrollPane(bugLinkView);

        // left tabbed pane
        JTabbedPane listPane = new JTabbedPane();
        listPane.addTab("Files", filesView);
        listPane.addTab("Clone Classes", classesPane);

        listPane.setMnemonicAt(0, KeyEvent.VK_F);
        listPane.setMnemonicAt(1, KeyEvent.VK_C);

        // right tabbed pane
        contentPane = new JTabbedPane();
        contentPane.addTab("Source", sourcePane);
        contentPane.addTab("Pie Chart", new JLabel("Pie Chart", JLabel.CENTER));
        contentPane.addTab("Bars", new JLabel("Bars and Stripes Chart",
                JLabel.CENTER));
        contentPane.addTab("Tree Map", new JLabel("Tree Map", JLabel.CENTER));
        contentPane.addTab("BugLink", bugLinkPane);

        contentPane.setMnemonicAt(0, KeyEvent.VK_S);
        contentPane.setMnemonicAt(1, KeyEvent.VK_P);
        contentPane.setMnemonicAt(2, KeyEvent.VK_B);
        contentPane.setMnemonicAt(3, KeyEvent.VK_T);
        contentPane.setMnemonicAt(4, KeyEvent.VK_L);

        // resizable pane
        JSplitPane tabPanels = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                listPane, contentPane);
        tabPanels.setDividerLocation(300);
        tabPanels.setResizeWeight(0.2);
        tabPanels.setOneTouchExpandable(false);
        tabPanels.setContinuousLayout(true);

        // add components to the super JPanel
        super.add(statusLabel, BorderLayout.SOUTH);
        super.add(tabPanels, BorderLayout.CENTER);
    }

    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    public void updateFileSelected(File file) {
        sourcePane.setSingleSource(file);
    }

    public void updateFragmentsSelected(List<FragmentCell> fragments) {
        leftFrag = fragments.get(0);
        rightFrag = fragments.get(1);
        sourcePane.setSplitSource(leftFrag.getFragment(),
                rightFrag.getFragment());
        bugLinkView.showFragments(fragments);
    }

    public void showSourcePane() {
        contentPane.setSelectedComponent(sourcePane);
    }

    public void setSourceTheme(String theme) {
        if (theme.equals("ECLIPSE")) {
            sourcePane.setTheme("res/eclipseTheme.xml");
        } else if (theme.equals("STANDARD")) {
            sourcePane.setTheme("res/defaultTheme.xml");
        } else if (theme.equals("DARK")) {
            sourcePane.setTheme("res/darkTheme.xml");
        } else if (theme.equals("VS")) {
            sourcePane.setTheme("res/vsTheme.xml");
        }
    }

    public NovelClassesTree getClassesTree() {
        return classesTree;
    }

    public NovelFilesList getFilesList() {
        return filesList;
    }

    public BugLinkView getBugLinkView() {
        return bugLinkView;
    }
}