/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import javax.swing.SwingUtilities;

/**
 * @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @author      Colin C. Hemphill <colin@hemphill.us>
 */
public class Novel {

    public static void main (String [] args) throws Exception {

        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                NovelFrame ui = new NovelFrame();
                ui.initUI();
                ui.setVisible(true);
            }
        });
    }
}