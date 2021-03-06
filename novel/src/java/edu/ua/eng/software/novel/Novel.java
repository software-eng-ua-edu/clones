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

// import de.uni_bremen.st.rcf.model.File;
// import de.uni_bremen.st.rcf.persistence.AbstractPersistenceManager;
// import de.uni_bremen.st.rcf.persistence.PersistenceManagerFactory;
// import de.uni_bremen.st.rcf.model.RCF;

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