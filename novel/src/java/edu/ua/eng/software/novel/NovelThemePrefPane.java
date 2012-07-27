/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.util.prefs.Preferences;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;

/**
 * @author      Blake Bassett <rbbassett@crimson.ua.edu>
 */
public class NovelThemePrefPane extends NovelPrefPane
{
    public NovelThemePrefPane(Preferences prefs) {
        super(prefs);

        setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));
        setBorder(BorderFactory.createTitledBorder("Set Source Theme"));

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

        ActionListener radioListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentTheme = e.getActionCommand();
                changed = true;
            }
        };

        eclipse.addActionListener(radioListener);
        standard.addActionListener(radioListener);
        dark.addActionListener(radioListener);
        vs.addActionListener(radioListener);

        applyPrefs();

        if (currentTheme.equals("ECLIPSE")) {
            themes.setSelected(eclipse.getModel(), true);
        } else if (currentTheme.equals("STANDARD")) {
            themes.setSelected(standard.getModel(), true);
        } else if (currentTheme.equals("DARK")) {
            themes.setSelected(dark.getModel(), true);
        } else if (currentTheme.equals("VS")) {
            themes.setSelected(vs.getModel(), true);
        }
        add(eclipse);
        add(standard);
        add(dark);
        add(vs);
    }

    public void setPrefs() {
        prefs.put(SOURCE_THEME, currentTheme);
    }

    public void apply() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setPrefs();
                NovelPanelController.getInstance().setSourceTheme(currentTheme);
            }
        });
        changed = false;
    }

    public void applyPrefs() {
        currentTheme = prefs.get(SOURCE_THEME, "ECLIPSE");
        apply();
    }

    public boolean isChanged() {
        return changed;
    }

    private ButtonGroup themes;
    private String currentTheme;
    private boolean changed = false;

    private static final String SOURCE_THEME = "SourceTheme";
}