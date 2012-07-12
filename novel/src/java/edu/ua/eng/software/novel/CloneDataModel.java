/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel;

import java.util.HashMap;
import java.util.HashSet;

import de.uni_bremen.st.rcf.model.Version;
import de.uni_bremen.st.rcf.model.CloneClasses;
import de.uni_bremen.st.rcf.model.CloneClass;
import de.uni_bremen.st.rcf.model.Fragments;
import de.uni_bremen.st.rcf.model.Fragment;
import de.uni_bremen.st.rcf.model.Files;
import de.uni_bremen.st.rcf.model.File;
import de.uni_bremen.st.rcf.model.SourcePosition;

/**
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 * @author Casey Ferris <cmferris1@crimson.ua.edu>
 */

public class CloneDataModel
{
    public CloneDataModel(Version latest) {
        version = latest;
        classes = latest.getCloneClasses();
        fileToClones = new HashMap<File, HashSet<Fragment>>();
        cloneToClass = new HashMap<Fragment, CloneClass>();

        populateMaps();
    }

    private void populateMaps() {
        for (CloneClass class : classes) {
            for (Fragment clone : class.getFragments()) {
                cloneToClass.put(clone, class);
                File file = clone.getStart().getFile();
                if (!fileToClones.hasKey(file)) {
                    fileToClones.put(file, new HashSet<Fragment>());
                }
                fileToClones.get(file).add(clone);
            }
        }
    }

    private CloneClasses getCloneClasses() {
        return classes;
    }

    private CloneClass getCloneClass(Fragment clone) {
        return cloneToClass.get(clone);
    }

    private Fragments getClones(CloneClass class) {
        return class.getFragments();
    }

    private Fragments getClones(File file) {
        return fileToClones.get(file);
    }

    private File getFile(Fragment clone) {
        return clone.getStart().getFile();
    }

    private Version version;
    private CloneClasses classes;
    private HashMap<File, HashSet<Fragment>> fileToClones;
    private HashMap<Fragment, CloneClass> cloneToClass;
}