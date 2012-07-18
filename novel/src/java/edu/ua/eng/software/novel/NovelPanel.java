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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

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
    private BugLinkView bugLinkPane;

    private NovelClassesTree classesTree;
    private NovelFilesList filesList;
    // private JPanel barStripesView;

    private JTabbedPane contentPane;

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
        bugLinkPane = new BugLinkView();

        // left tabbed pane
        JTabbedPane listPane = new JTabbedPane();
        listPane.addTab("Files", filesView);
        listPane.addTab("Clone Classes", classesPane);

        // right tabbed pane
        contentPane = new JTabbedPane();
        contentPane.addTab("Source", sourcePane);
        contentPane.addTab("Pie Chart", new JLabel("Pie Chart", JLabel.CENTER));
        contentPane.addTab("Bars", barStripesPane);
        contentPane.addTab("Tree Map", new JLabel("Tree Map", JLabel.CENTER));
        contentPane.addTab("BugLink", bugLinkPane);

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

    public void updateFileSelected(String path) {
        sourcePane.setSingleSource(path);
    }

    public void updateFragmentsSelected(List<FragmentCell> fragments) {

    }

    public void showSourcePane() {
        contentPane.setSelectedComponent(sourcePane);
    }

    public NovelClassesTree getClassesTree() {
        return classesTree;
    }

    public NovelFilesList getFilesList() {
        return filesList;
    }
}