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
import edu.ua.eng.software.novel.importing.NovelImporter.ReportType;

/**
 * Create a dialog to
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 * @author Casey Ferris <cmferris1@crimson.ua.edu>
 */
public class NovelImportPrefPane extends JDialog
{
    public NovelImportPrefPane(final Frame parent) {
        super(parent, "Import", true);
        super.setLayout(new BorderLayout());
        super.setPreferredSize(new Dimension(400, 240));
        super.setResizable(false);

        JPanel composite = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        composite.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // add text boxes + browse buttons
        JPanel selection = new JPanel();
        selection.setLayout(new GridBagLayout());
        GridBagConstraints cSel = new GridBagConstraints();
        selection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Set import location"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        final JTextField fileText = new JTextField("Choose clone results...",
                15);
        final JTextField dirText = new JTextField("Choose source directory...",
                15);
        fileText.setEditable(false);
        dirText.setEditable(false);

        JButton fileBrowse = new JButton("Browse");
        JButton dirBrowse = new JButton("Browse");

        // add actions for browse buttons
        ActionListener browseListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == "SETCLONES") {
                    JFileChooser chooser = chooseImportFile();
                    if (buttonClicked == JFileChooser.APPROVE_OPTION) {
                        importFile = chooser.getSelectedFile();
                        fileText.setText(importFile.getAbsolutePath());
                    }
                } else {
                    JFileChooser chooser = chooseSourceDir();
                    if (buttonClicked == JFileChooser.APPROVE_OPTION) {
                        sourceDir = chooser.getSelectedFile();
                        dirText.setText(sourceDir.getAbsolutePath());
                    }
                }
            }
        };

        fileBrowse.setActionCommand("SETCLONES");
        dirBrowse.setActionCommand("SETDIR");
        fileBrowse.addActionListener(browseListener);
        dirBrowse.addActionListener(browseListener);
        fileBrowse.setMnemonic(KeyEvent.VK_B);
        dirBrowse.setMnemonic(KeyEvent.VK_R);

        // add radio buttons to select clone results type
        JPanel radioButtons = new JPanel();
        radioButtons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Clone results type:"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // add actions for radio buttons
        ActionListener radioListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importType = ReportType.valueOf(e.getActionCommand());
            }
        };

        JRadioButton selectRCF = new JRadioButton("RCF");
        selectRCF.setMnemonic(KeyEvent.VK_F);
        selectRCF.setActionCommand("RCF");
        selectRCF.addActionListener(radioListener);
        JRadioButton selectNiCad = new JRadioButton("NiCad");
        selectNiCad.setMnemonic(KeyEvent.VK_N);
        selectNiCad.setActionCommand("NICAD");
        selectNiCad.addActionListener(radioListener);

        ButtonGroup buttons = new ButtonGroup();
        buttons.add(selectRCF);
        buttons.add(selectNiCad);

        selectRCF.setSelected(true);
        importType = ReportType.RCF;

        // add cancel & confirm buttons
        ActionListener confirmAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == "CANCEL")
                    setVisible(false);
                else {
                    if (importFile == null)
                        JOptionPane.showMessageDialog(parent,
                                "Please select clone results to import",
                                "Import Error", JOptionPane.ERROR_MESSAGE);
                    else if (sourceDir == null)
                        JOptionPane.showMessageDialog(parent,
                                "Please select a source directory",
                                "Import Error", JOptionPane.ERROR_MESSAGE);
                    else {
                        setVisible(false);
                        try {
                            NovelPanelController.getInstance().updateStatus(
                                    "Imported clone results from "
                                            + importFile.getName());
                            NovelImporter.importReport(importFile, sourceDir,
                                    importType);
                        } catch (FileNotFoundException ex) {
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
        confirm.setToolTipText("Confirm import settings");
        confirm.setMnemonic(KeyEvent.VK_O);
        confirm.setActionCommand("OKAY");
        confirm.addActionListener(confirmAction);
        JButton cancel = new JButton("Cancel");
        cancel.setToolTipText("Abort import dialog");
        cancel.setMnemonic(KeyEvent.VK_C);
        cancel.setActionCommand("CANCEL");
        cancel.addActionListener(confirmAction);

        // add components
        cSel.gridx = 0;
        cSel.gridy = 0;
        cSel.weightx = 1.0;
        cSel.weighty = 0.5;
        cSel.fill = GridBagConstraints.HORIZONTAL;
        selection.add(fileText, cSel);
        cSel.gridx = 1;
        cSel.weightx = 0;
        selection.add(fileBrowse, cSel);
        cSel.gridx = 0;
        cSel.gridy = 1;
        cSel.fill = GridBagConstraints.HORIZONTAL;
        selection.add(dirText, cSel);
        cSel.gridx = 1;
        selection.add(dirBrowse, cSel);
        radioButtons.add(selectRCF);
        radioButtons.add(selectNiCad);
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

        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }

    public File getImportFile() {
        return importFile;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public ReportType getImportType() {
        return importType;
    }

    private JFileChooser chooseImportFile() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle("Import Clone Results");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".rcf and .xml", "rcf", "xml");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        buttonClicked = chooser.showOpenDialog(this);
        return chooser;
    }

    private JFileChooser chooseSourceDir() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle("Choose Source Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        buttonClicked = chooser.showOpenDialog(this);
        return chooser;
    }

    private File importFile;
    private File sourceDir;
    private ReportType importType;

    private int buttonClicked;
}