/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Initialize the NoVEL interface
 * 
 * @author Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @author Colin C. Hemphill <colin@hemphill.us>
 */
public class Novel
{
    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    for (LookAndFeelInfo info : UIManager
                            .getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (UnsupportedLookAndFeelException e) {
                    // handle exception
                } catch (ClassNotFoundException e) {
                    // handle exception
                } catch (InstantiationException e) {
                    // handle exception
                } catch (IllegalAccessException e) {
                    // handle exception
                }
                NovelFrame ui = new NovelFrame();
                ui.initUI();
                ui.setVisible(true);
            }
        });
    }
}

/*
 * private static void testNiCad() { java.io.File rcfFile = new
 * java.io.File("test/test.rcf"); NiCadImport nci = new NiCadImport(rcfFile);
 * java.io.File nicadFile = new java.io.File("test/rhino-1.6R5_clones.xml");
 * 
 * nci.addVersion(nicadFile, "rhino-1.6R5"); RCF rcf = nci.getRCF();
 * 
 * System.out.println("\n\n\nNiCad:\n\n" +
 * rcf.getVersions().getFirstEntry().getBasepath());
 * 
 * for (File f : rcf.getVersions().getFirstEntry().getFiles()) {
 * System.out.println(f.getAbsolutePath()); } }
 * 
 * private static void testRCF() throws Exception { java.io.File file = new
 * java.io.File("test/wget.rcf"); AbstractPersistenceManager apm =
 * PersistenceManagerFactory.getPersistenceManager(file); RCF rcf =
 * apm.load(file);
 * 
 * System.out.println("\n\n\nRCF:\n\n" +
 * rcf.getVersions().getFirstEntry().getBasepath());
 * 
 * for (File f : rcf.getVersions().getFirstEntry().getFiles()) {
 * System.out.println(f.getAbsolutePath()); } }
 */