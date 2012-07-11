/*
 * @(#)Main.java
 *
 * Copyright (c) 1996-2010 by the original authors of JHotDraw and all its
 * contributors. All rights reserved.
 *
 * You may not use, copy or modify this file, except in compliance with the 
 * license agreement you entered into with the copyright holders. For details
 * see accompanying license terms.
 */

package org.jhotdraw.samples.svg;

import org.jhotdraw.app.*;
import org.jhotdraw.util.ResourceBundleUtil;
/**
 * Main.
 *
 * @author Werner Randelshofer.
 * @version $Id: Main.java 717 2010-11-21 12:30:57Z rawcoder $
 */
public class Main {
    
    /** Creates a new instance. */
    public static void main(String[] args) {
        // Debug resource bundle
        ResourceBundleUtil.setVerbose(true);

        Application app;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("mac")) {
            app = new OSXApplication();
        } else if (os.startsWith("win")) {
          //  app = new MDIApplication();
            app = new SDIApplication();
        } else {
            app = new SDIApplication();
        }
        SVGApplicationModel model = new SVGApplicationModel();
        model.setName("JHotDraw SVG");
        model.setVersion(Main.class.getPackage().getImplementationVersion());
        model.setCopyright("Copyright 2006-2010 (c) by the authors of JHotDraw.\n" +
                "This software is licensed under LGPL and Creative Commons 3.0 Attribution.");
        model.setViewClassName("org.jhotdraw.samples.svg.SVGView");
        app.setModel(model);
        app.launch(args);
    }
    
}
