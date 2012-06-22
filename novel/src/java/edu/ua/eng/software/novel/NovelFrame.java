/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author      Colin C. Hemphill <colin@hemphill.us>
 */

public class NovelFrame extends JFrame {

    public final void initUI() {

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu help = new JMenu("Help");
        menu.add(file); menu.add(edit); menu.add(help);

        JMenuItem fileImport = new JMenuItem("Import/Run");
	JMenuItem fileRerun = new JMenuItem("Rerun");
        JMenuItem filePrefs = new JMenuItem("Preferences");
        JMenuItem fileExit = new JMenuItem("Exit");

        JMenuItem editDif = new JMenuItem("Difference");
        JMenuItem editFilter = new JMenuItem("Filter");

        JMenuItem helpTut = new JMenuItem("Tutorials");
        JMenuItem helpAbout = new JMenuItem("About");

        file.add(fileImport); file.add(fileRerun);file.add(filePrefs); file.add(fileExit);
        edit.add(editDif); edit.add(editFilter);
        help.add(helpTut); help.add(helpAbout);
        file.setMnemonic(KeyEvent.VK_F);
        setJMenuBar(menu);

        setDefaultLookAndFeelDecorated(true);
        setTitle("N.o.V.E.L.");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        NovelPanel panel = new NovelPanel();
        setContentPane(panel.createContentPane());
    }
}