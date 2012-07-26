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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.ua.eng.software.novel.importing.NovelImporter;
import edu.ua.eng.software.novel.importing.NovelImporter.SourceRepoType;

/**
 * Create a dialog to
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 * @author Casey Ferris <cmferris1@crimson.ua.edu>
 */
public class NovelSetRepoPrefPane extends NovelPrefPane
{
    public NovelSetRepoPrefPane(Preferences prefs) {
        super(prefs);
        this.changed = false;
        super.setLayout(new BorderLayout());
        super.setPreferredSize(new Dimension(400, 240));

        JPanel composite = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        composite.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // add text boxes + browse buttons
        JPanel selection = new JPanel();
        selection.setLayout(new GridBagLayout());
        GridBagConstraints cSel = new GridBagConstraints();
        selection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Set repository location"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        final JTextField dirText = new JTextField("Choose repo directory or url...",
                15);

        JButton dirBrowse = new JButton("Browse");

        // add actions for browse buttons
        ActionListener browseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == "SETDIR") {
                    JFileChooser chooser = chooseSourceDir();
                    if (buttonClicked == JFileChooser.APPROVE_OPTION) {
                        sourceDir = chooser.getSelectedFile();
                        dirText.setText(sourceDir.getAbsolutePath());
                    }
                }
            }
        };

        dirBrowse.setActionCommand("SETDIR");
        dirBrowse.addActionListener(browseListener);
        dirBrowse.setMnemonic(KeyEvent.VK_R);

        // add radio buttons to select clone results type
        JPanel radioButtons = new JPanel();
        radioButtons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Repository type:"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // add actions for radio buttons
        ActionListener radioListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importType = SourceRepoType.valueOf(e.getActionCommand());
            }
        };

        JRadioButton selectSVN = new JRadioButton("SVN");
        selectSVN.setMnemonic(KeyEvent.VK_S);
        selectSVN.setActionCommand("SVN");
        selectSVN.addActionListener(radioListener);
        JRadioButton selectGit = new JRadioButton("Git");
        selectGit.setMnemonic(KeyEvent.VK_G);
        selectGit.setActionCommand("GIT");
        selectGit.addActionListener(radioListener);

        ButtonGroup buttons = new ButtonGroup();
        buttons.add(selectSVN);
        buttons.add(selectGit);

        selectSVN.setSelected(true);
        importType = SourceRepoType.SVN;

        final JPanel panel = this;
        // add cancel & confirm buttons
        ActionListener confirmAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == "CANCEL")
                    setVisible(false);
                else {
                    if (sourceDir == null)
                        JOptionPane.showMessageDialog(panel,
                                "Please select a repository path",
                                "Set Error", JOptionPane.ERROR_MESSAGE);
                    else {
                        setVisible(false);
                        try {
                            NovelPanelController.getInstance().updateStatus(
                                    "Set repository path to "
                                            + sourceDir.getParentFile().getName() + "/" + sourceDir.getName());
                            NovelImporter.importBugs(sourceDir,
                                    importType);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(getParent(),
                                    ex.getMessage(), "File Not Found",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };

        JPanel confirmButtons = new JPanel();
        JButton confirm = new JButton("Okay");
        confirm.setToolTipText("Confirm repository settings");
        confirm.setMnemonic(KeyEvent.VK_O);
        confirm.setActionCommand("OKAY");
        confirm.addActionListener(confirmAction);
        JButton cancel = new JButton("Cancel");
        cancel.setToolTipText("Abort repository dialog");
        cancel.setMnemonic(KeyEvent.VK_C);
        cancel.setActionCommand("CANCEL");
        cancel.addActionListener(confirmAction);

        // add components
        cSel.weightx = 1.0;
        cSel.gridx = 0;
        cSel.gridy = 1;
        cSel.fill = GridBagConstraints.HORIZONTAL;
        selection.add(dirText, cSel);
        cSel.gridx = 1;
        selection.add(dirBrowse, cSel);
        radioButtons.add(selectSVN);
        radioButtons.add(selectGit);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        composite.add(selection, c);
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        composite.add(radioButtons, c);
        confirmButtons.add(confirm);
        confirmButtons.add(cancel);
        super.add(composite, BorderLayout.CENTER);
        super.add(confirmButtons, BorderLayout.SOUTH);

        super.setVisible(true);
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public SourceRepoType getImportType() {
        return importType;
    }

    public void apply() {

    }

    public boolean isChanged() {
        return changed;
    }

    private JFileChooser chooseSourceDir() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle("Choose Source Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        buttonClicked = chooser.showOpenDialog(this);
        return chooser;
    }

    private File sourceDir;
    private SourceRepoType importType;

    private int buttonClicked;
    private boolean changed;
}