/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.Desktop;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Creates the main UI frame and menu
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
public class NovelFrame extends JFrame implements ActionListener
{
    NovelPanel panel;

    public final void initUI() {

        // top level menu items
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu help = new JMenu("Help");
        menu.add(file);
        menu.add(edit);
        menu.add(help);

        // sub menu items
        JMenuItem fileImport = new JMenuItem("Import/Run");
        JMenuItem filePrefs = new JMenuItem("Preferences");
        JMenuItem fileExit = new JMenuItem("Exit");
        JMenuItem editDif = new JMenuItem("Difference");
        JMenuItem editFilter = new JMenuItem("Filter");
        JMenuItem helpTut = new JMenuItem("Tutorials");
        JMenuItem helpAbout = new JMenuItem("About");

        // set action commands
        fileImport.setActionCommand("OPEN");
        fileImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
                Event.CTRL_MASK));
        fileImport.addActionListener(this);
        filePrefs.setActionCommand("PREFS");
        filePrefs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Event.CTRL_MASK));
        filePrefs.addActionListener(this);
        fileExit.setActionCommand("EXIT");
        fileExit.addActionListener(this);
        helpTut.setActionCommand("TUTORIAL");
        helpTut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpTut.addActionListener(this);
        helpAbout.setActionCommand("ABOUT");
        helpAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        helpAbout.addActionListener(this);

        // add the sub menus
        file.add(fileImport);
        file.add(filePrefs);
        file.add(fileExit);
        edit.add(editDif);
        edit.add(editFilter);
        help.add(helpTut);
        help.add(helpAbout);
        setJMenuBar(menu);

        setTitle("N.o.V.E.L.");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panel = new NovelPanel();
        setContentPane(panel.createContentPane());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OPEN"))
            openDialog();
        else if (e.getActionCommand().equals("PREFS"))
            prefsDialog();
        else if (e.getActionCommand().equals("EXIT"))
            System.exit(0);
        else if (e.getActionCommand().equals("TUTORIAL"))
            try {
                openTutorial();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        else if (e.getActionCommand().equals("ABOUT"))
            createAboutMenu();
    }

    public void openDialog() {

        final JFileChooser chooser = new JFileChooser(
                System.getProperty("user.dir"));
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle("Import/Run");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".java and .txt Files", "java", "txt");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            panel.updateStatus("Imported file " + file.getAbsolutePath());
            panel.updateSource(file.toString());
        }
    }

    public void prefsDialog() {

        // will use java.util.prefs package to handle preference storage
        // will most likely use a new "NovelPrefs" class to display
    }

    public void openTutorial() throws IOException, URISyntaxException {

        Desktop.getDesktop().browse(new URI("about:blank"));
    }

    public void createAboutMenu() {

        JOptionPane.showMessageDialog(this, "N.o.V.E.L. © Copyright 2012"
                + "\nVersion 1.0" + "\n\nBlake Bassett, Casey Ferring"
                + "\nColin Hemphill, Conor Kirkman"
                + "\nNicholas Kraft, Paige Rodeghero", "About N.o.V.E.L.",
                JOptionPane.INFORMATION_MESSAGE);
    }
}