/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Creates the main UI frame and menu
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
@SuppressWarnings("serial")
public class NovelFrame extends JFrame implements ActionListener
{
    private NovelPanel panel;
    private NovelPrefs prefs;

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
        JMenuItem fileSetRepo = new JMenuItem("Set Repository Path");
        JMenuItem filePrefs = new JMenuItem("Preferences");
        JMenuItem fileExit = new JMenuItem("Exit");
        JMenuItem editDif = new JMenuItem("Difference");
        JMenuItem editFilter = new JMenuItem("Filter");
        JMenuItem helpTut = new JMenuItem("Tutorials");
        JMenuItem helpAbout = new JMenuItem("About");

        // gray out currently unavailable menu items
        editDif.setEnabled(false);
        editFilter.setEnabled(false);
        helpTut.setEnabled(false);

        // set action commands
        fileImport.setActionCommand("OPEN");
        fileImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
                Event.CTRL_MASK));
        fileImport.addActionListener(this);
        fileSetRepo.setActionCommand("SET");
        fileSetRepo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                Event.CTRL_MASK));
        fileSetRepo.addActionListener(this);
        filePrefs.setActionCommand("PREFS");
        filePrefs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Event.CTRL_MASK));
        filePrefs.addActionListener(this);
        fileExit.setActionCommand("EXIT");
        fileExit.addActionListener(this);

        editDif.setActionCommand("DIFF");
        editDif.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                Event.CTRL_MASK + Event.SHIFT_MASK));
        editDif.addActionListener(this);
        editFilter.setActionCommand("FILTER");
        editFilter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                Event.CTRL_MASK + Event.SHIFT_MASK));
        editFilter.addActionListener(this);

        helpTut.setActionCommand("TUTORIAL");
        helpTut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpTut.setMnemonic(KeyEvent.VK_F1);
        helpTut.addActionListener(this);
        helpAbout.setActionCommand("ABOUT");
        helpAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        helpAbout.setMnemonic(KeyEvent.VK_F2);
        helpAbout.addActionListener(this);

        // add the sub menus
        file.add(fileImport);
        file.add(fileSetRepo);
        file.add(filePrefs);
        file.add(fileExit);
        edit.add(editDif);
        edit.add(editFilter);
        help.add(helpTut);
        help.add(helpAbout);
        setJMenuBar(menu);

        // initiate the window
        setTitle("NoVEL");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        prefs = new NovelPrefs(this);
        panel = new NovelPanel();
        setContentPane(panel);
        NovelPanelController.getInstance().setPanel(panel);
    }

    /**
     * Handle menu interactions
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("OPEN")) {
            prefsDialog(prefs.getImportPane());
        } else if (e.getActionCommand().equals("SET")) {
            prefsDialog(prefs.getRepoPane());
        } else if (e.getActionCommand().equals("PREFS"))
            prefsDialog();
        else if (e.getActionCommand().equals("EXIT"))
            System.exit(0);
        else if (e.getActionCommand().equals("TUTORIAL"))
            try {
                tutorialDialog();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        else if (e.getActionCommand().equals("ABOUT"))
            aboutDialog();
    }

    /**
     * Create dialog to handle user preferences
     */
    public void prefsDialog() {

        prefs.setVisible(true);
    }

    public void prefsDialog(Component c) {
        prefs.setSelectedComponent(c);
        prefsDialog();
    }

    /**
     * Open software tutorials, most likely to be hosted online
     * 
     * @throws IOException
     * @throws URISyntaxException
     */
    public void tutorialDialog() throws IOException, URISyntaxException {

        Desktop.getDesktop().browse(new URI("about:blank"));
    }

    /**
     * Create "About" dialog
     */
    public void aboutDialog() {

        JOptionPane.showMessageDialog(this, "NoVEL Copyright 2012"
                + "\nLast updated " + getVersion()
                + "\n\nBlake Bassett, Casey Ferris"
                + "\nColin Hemphill, Conor Kirkman"
                + "\nNicholas Kraft, Paige Rodeghero", "About NoVEL",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public String getVersion() {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder
                    .setGitDir(new File("../../clones/.git")).readEnvironment()
                    .findGitDir().build();
            RevWalk walk = new RevWalk(repository);
            ObjectId rootId = repository.resolve("HEAD");
            RevCommit root = walk.parseCommit(rootId);
            return root.getCommitterIdent().getWhen().toString()
                    .substring(0, 10);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}