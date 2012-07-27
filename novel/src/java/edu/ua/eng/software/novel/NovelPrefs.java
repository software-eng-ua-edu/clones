/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.util.List;
import java.util.LinkedList;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import net.java.dev.designgridlayout.DesignGridLayout;

/**
 * Create a preferences dialog
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
@SuppressWarnings("serial")
public class NovelPrefs extends JDialog
{
    public NovelPrefs(final Frame parent) {
        super(parent, "NoVEL Preferences", true);
        super.setLayout(new BorderLayout());
        super.setPreferredSize(new Dimension(640, 480));
        super.setResizable(false);

        prefs = Preferences.userRoot().node(this.getClass().getName());

        themePane = new NovelThemePrefPane(prefs);
        //DesignGridLayout vLayout = new DesignGridLayout(visual);

        importPane = new NovelImportPrefPane(prefs);
        repoPane = new NovelSetRepoPrefPane(prefs);

        // create tabbed pane and add its components
        prefsTabs = new JTabbedPane();
        prefsTabs.addTab("Import Settings", importPane);
        prefsTabs.addTab("Repository Settings", repoPane);
        prefsTabs.addTab("Theme Settings", themePane);
        prefsTabs.addTab("Other Settings", new JLabel("Other Settings",
                JLabel.CENTER));
        prefsTabs.addTab("Moar Settings", new JLabel("Moar Settings",
                JLabel.CENTER));
        prefsTabs.addTab("Awesome Settings!", new JLabel(
                "N.o.V.e.L. is AWESOME!", JLabel.CENTER));

        // create okay and cancel buttons
        JPanel buttons = new JPanel();
        DesignGridLayout bLayout = new DesignGridLayout(buttons);
        JButton apply = new JButton("Apply");
        JButton okay = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        // add action commands
        apply.setActionCommand("APPLY");
        okay.setActionCommand("OK");
        cancel.setActionCommand("CANCEL");

        apply.addActionListener(listener);
        okay.addActionListener(listener);
        cancel.addActionListener(listener);

        apply.setMnemonic(KeyEvent.VK_A);
        okay.setMnemonic(KeyEvent.VK_O);
        cancel.setMnemonic(KeyEvent.VK_C);

        // add components
        bLayout.row().right().add(apply, okay, cancel);
        //vLayout.row().left().fill().add(sourceTheme);
        super.add(prefsTabs);
        super.add(buttons, BorderLayout.SOUTH);

        super.pack();
        super.setLocationRelativeTo(null);

        applyPrefs();
    }

    public void selectImportPane() {
        prefsTabs.setSelectedComponent(importPane);
    }

    public void selectRepoPane() {
        prefsTabs.setSelectedComponent(repoPane);
    }

    protected void apply() {
        for(int i = 0; i < prefsTabs.getTabCount(); i++) {
            Component c = prefsTabs.getComponentAt(i);
            if (c instanceof NovelPrefPane) {
                NovelPrefPane pane = (NovelPrefPane) c;
                if (pane.isChanged()) {
                    pane.apply();
                }
            }
        }
    }

    protected void applyPrefs() {
       for(int i = 0; i < prefsTabs.getTabCount(); i++) {
            Component c = prefsTabs.getComponentAt(i);
            if (c instanceof NovelPrefPane) {
                ((NovelPrefPane) c).applyPrefs();
            }
        } 
    }

    ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("CANCEL")) {
                setVisible(false);
            } else if (e.getActionCommand().equals("OK")) {
                apply();
                setVisible(false);
            } else if (e.getActionCommand().equals("APPLY")) {
                apply();
            }
        }
    };

    private Preferences prefs;

    private NovelPrefPane themePane;
    private NovelPrefPane importPane;
    private NovelPrefPane repoPane;
    private JTabbedPane prefsTabs;
}