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

import edu.ua.eng.software.novel.NovelImporter.ReportType;

/**
 * Initializes the tree structure for clone pairs
 * 
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
public class NovelImportDialog {

    public NovelImportDialog() {
        dialog = new JDialog(dialog, "Import", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(new Dimension(320, 240));
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        
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
                importType = (ReportType) Enum.parse(typeof(ReportType), e.getActionCommand(), true);
            }
        };

        buttons = new ButtonGroup();
        buttons.add(selectRCF);
        buttons.add(selectNiCad);
        
        selectRCF.setSelected(true);

        JPanel panel = new JPanel();

        radioButtons.add(selectRCF);
        radioButtons.add(selectNiCad);
        dialog.add(panel, BorderLayout.NORTH);
        dialog.add(radioButtons, BorderLayout.SOUTH);
    }

    public void show() {
        dialog.setVisible(true);
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

    public JFileChooser chooseImportFile() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle("Import File");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".rcf and .xml", "rcf", "xml");
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        buttonClicked = chooser.showOpenDialog(dialog);
        return chooser;
    }

    public JFileChooser chooseSourceDir() {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileHidingEnabled(false);
        chooser.setDialogTitle("Import File");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        buttonClicked = chooser.showOpenDialog(dialog);
        return chooser;
    }

    private ButtonGroup buttons;

    private File importFile;
    private File sourceDir;
    private RecordType importType;

    private JDialog dialog;

    private int buttonClicked;
}