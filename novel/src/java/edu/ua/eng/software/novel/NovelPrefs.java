/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import net.java.dev.designgridlayout.DesignGridLayout;

/**
 * Create a preferences dialog
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
@SuppressWarnings("serial")
public class NovelPrefs extends JDialog
{
    private Preferences prefs;
    private ButtonGroup themes;
    private String currentTheme;

    private static final String SOURCE_THEME = "SourceTheme";

    public NovelPrefs(final Frame parent) {
        super(parent, "NoVEL Preferences", true);
        super.setLayout(new BorderLayout());
        super.setPreferredSize(new Dimension(640, 480));
        super.setResizable(false);

        initializePrefs();

        JPanel visual = new JPanel();
        DesignGridLayout vLayout = new DesignGridLayout(visual);

        // create tabbed pane and add its components
        JTabbedPane prefsTabs = new JTabbedPane();
        prefsTabs.addTab("Visual", visual);
        prefsTabs.addTab("Other Settings", new JLabel("Other Settings",
                JLabel.CENTER));
        prefsTabs.addTab("Moar Settings", new JLabel("Moar Settings",
                JLabel.CENTER));
        prefsTabs.addTab("Awesome Settings!", new JLabel("Awesome Settings!",
                JLabel.CENTER));

        // set source theme
        JPanel sourceTheme = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 25, 0));
        sourceTheme.setBorder(BorderFactory
                .createTitledBorder("Set Source Theme"));
        JRadioButton eclipse = new JRadioButton("Eclipse (default)");
        JRadioButton standard = new JRadioButton("Standard");
        JRadioButton dark = new JRadioButton("Dark");
        JRadioButton vs = new JRadioButton("Visual Studio");
        themes = new ButtonGroup();
        themes.add(eclipse);
        themes.add(standard);
        themes.add(dark);
        themes.add(vs);

        eclipse.setActionCommand("ECLIPSE");
        standard.setActionCommand("STANDARD");
        dark.setActionCommand("DARK");
        vs.setActionCommand("VS");

        if (currentTheme.equals("ECLIPSE")) {
            themes.setSelected(eclipse.getModel(), true);
        } else if (currentTheme.equals("STANDARD")) {
            themes.setSelected(standard.getModel(), true);
        } else if (currentTheme.equals("DARK")) {
            themes.setSelected(dark.getModel(), true);
        } else if (currentTheme.equals("VS")) {
            themes.setSelected(vs.getModel(), true);
        }
        sourceTheme.add(eclipse);
        sourceTheme.add(standard);
        sourceTheme.add(dark);
        sourceTheme.add(vs);

        // create okay and cancel buttons
        JPanel buttons = new JPanel();
        DesignGridLayout bLayout = new DesignGridLayout(buttons);
        JButton okay = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        // add action commands
        okay.setActionCommand("OK");
        cancel.setActionCommand("CANCEL");
        okay.addActionListener(listener);
        cancel.addActionListener(listener);
        okay.setMnemonic(KeyEvent.VK_O);
        cancel.setMnemonic(KeyEvent.VK_C);

        // add components
        bLayout.row().right().add(okay, cancel);
        vLayout.row().left().fill().add(sourceTheme);
        super.add(prefsTabs);
        super.add(buttons, BorderLayout.SOUTH);

        super.pack();
        super.setLocationRelativeTo(null);
    }

    public void initializePrefs() {

        prefs = Preferences.userRoot().node(this.getClass().getName());
        currentTheme = prefs.get(SOURCE_THEME, "ECLIPSE");
        
        // not sure why this line doesn't work
        // but it's supposed to update the theme based on
        // preferences whenever you start the program again
        // NovelPanelController.getInstance().setSourceTheme(currentTheme);
    }

    public void setPrefs() {

        prefs.put(SOURCE_THEME, themes.getSelection().getActionCommand());
    }

    public void setTheme() {

        NovelPanelController.getInstance().setSourceTheme(
                themes.getSelection().getActionCommand());
    }

    ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("CANCEL")) {
                setVisible(false);
            } else if (e.getActionCommand().equals("OK")) {
                setPrefs();
                setTheme();
                setVisible(false);
            }
        }
    };
}