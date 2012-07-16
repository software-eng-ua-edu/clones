/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import javax.swing.JFileChooser;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Frame;

import java.io.File;

import edu.ua.eng.software.novel.NovelImporter.ReportType;

/**
 * Initializes the tree structure for clone pairs
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
public class NovelImportDialog extends JDialog {

    public NovelImportDialog(Frame parent) {
        super(parent, "Import", true);
        super.setLayout(new BorderLayout());
        super.setSize(new Dimension(320, 240));
        super.setResizable(false);
        super.setLocationRelativeTo(null);
        
        JPanel radioButtons = new JPanel();
        radioButtons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Select Clone Results and Source Folder"),
                BorderFactory.createEmptyBorder(5,5,5,5)));
        
        JRadioButton selectRCF = new JRadioButton("RCF");
        selectRCF.setActionCommand("RCF");
        JRadioButton selectNiCad = new JRadioButton("NICAD");
        selectNiCad.setActionCommand("NICAD");

        ActionListener buttonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importType = ReportType.valueOf(e.getActionCommand());
            }
        };

        ButtonGroup buttons = new ButtonGroup();
        buttons.add(selectRCF);
        buttons.add(selectNiCad);
        
        selectRCF.setSelected(true);

        JPanel panel = new JPanel();

        //Add text boxes + browse buttons, attach listeners to buttons

        //Add to panel

        radioButtons.add(selectRCF);
        radioButtons.add(selectNiCad);
        super.add(panel, BorderLayout.NORTH);
        super.add(radioButtons, BorderLayout.SOUTH);
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
        chooser.setDialogTitle("Import File");
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
        chooser.setDialogTitle("Import File");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        buttonClicked = chooser.showOpenDialog(this);
        return chooser;
    }

    private File importFile;
    private File sourceDir;
    private ReportType importType;

    private int buttonClicked;
}