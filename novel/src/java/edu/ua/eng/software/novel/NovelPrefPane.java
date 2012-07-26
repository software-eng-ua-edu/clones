/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import javax.swing.JPanel;
import java.util.prefs.Preferences;

public abstract class NovelPrefPane extends JPanel
{
    public NovelPrefPane(Preferences prefs) {
        this.prefs = prefs;
    }

    public abstract void apply();

    public abstract boolean isChanged();

    protected Preferences prefs;
}