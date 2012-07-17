/*
 * @(#)Main.java
 *
 * Copyright (c) 2007 by the original authors of JHotDraw and all its
 * contributors. All rights reserved.
 *
 * You may not use, copy or modify this file, except in compliance with the 
 * license agreement you entered into with the copyright holders. For details
 * see accompanying license terms.
 */

package org.jhotdraw.samples.odg;

import org.jhotdraw.app.*;

/**
 * Main.
 *
 * @author Werner Randelshofer
 * @version $Id: Main.java 717 2010-11-21 12:30:57Z rawcoder $
 */
public class Main {
    
    /** Creates a new instance. */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application app = new OSXApplication();
        ApplicationModel appModel = new ODGApplicationModel();
        app.setModel(appModel);
        app.launch(args);
        // TODO code application logic here
    }
    
}
