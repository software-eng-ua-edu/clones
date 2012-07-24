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
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
    public NovelPrefs(final Frame parent) {
        super(parent, "NoVEL Preferences", true);
        super.setLayout(new BorderLayout());
        super.setPreferredSize(new Dimension(640, 480));
        super.setResizable(false);

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
        JPanel sourceTheme = new JPanel();
        sourceTheme.setBorder(BorderFactory
                .createTitledBorder("Set Source Theme"));
        JRadioButton eclipse = new JRadioButton("Eclipse (default)", true);
        JRadioButton standard = new JRadioButton("Standard");
        ButtonGroup themes = new ButtonGroup();
        themes.add(eclipse);
        themes.add(standard);

        sourceTheme.add(eclipse);
        sourceTheme.add(standard);

        // create okay and cancel buttons
        JPanel buttons = new JPanel();
        DesignGridLayout bLayout = new DesignGridLayout(buttons);
        JButton okay = new JButton("Okay");
        JButton cancel = new JButton("Cancel");

        // add action commands
        okay.setActionCommand("OKA");
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

    ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("CANCEL")) {
                setVisible(false);
            } else if (e.getActionCommand().equals("OKAY")) {
                setVisible(false);
            }
        }
    };
}