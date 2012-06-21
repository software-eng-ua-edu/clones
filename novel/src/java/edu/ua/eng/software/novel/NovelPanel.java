/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

/**
 * @author      Colin C. Hemphill <colin@hemphill.us>
 */

public class NovelPanel extends JPanel {

    public JPanel createContentPane() {

        JPanel composite = panelSize(800, 600);
        composite.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = .5; c.weighty = 1.0;

        JTabbedPane leftPane = new JTabbedPane();
        leftPane.addTab("Clone Pairs", new JLabel("Clone Pairs"));
        leftPane.addTab("Clone Classes", new JLabel("Clone Classes"));
        c.gridx = 0; c.gridy = 0;
        c.gridheight = 2; c.gridwidth = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START; c.fill = GridBagConstraints.HORIZONTAL;
        composite.add(leftPane, c);

        JTabbedPane rightPane = new JTabbedPane();
        rightPane.addTab("Source", new JLabel("Source"));
        rightPane.addTab("Pie Chart", new JLabel("Pie Chart"));
        rightPane.addTab("Bars", new JLabel("Bars"));
        rightPane.addTab("Tree Map", new JLabel("Tree Map"));
        c.gridx = 1; c.gridy = 0;
        c.gridheight = 2; c.gridwidth = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_END; c.fill = GridBagConstraints.HORIZONTAL;
        composite.add(rightPane, c);

        JLabel statusBar = new JLabel("Status Bar");
        statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        c.gridx = 0; c.gridy = 1;
        c.gridheight = 1; c.gridwidth = 3;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        composite.add(statusBar, c);

        composite.setOpaque(true);
        return composite;
    }

    public JPanel panelSize(int x, int y) {

        JPanel tmpPanel = new JPanel();
        tmpPanel.setMinimumSize(new Dimension(x, y));
        tmpPanel.setPreferredSize(new Dimension(x,y));
        return tmpPanel;
    }
}