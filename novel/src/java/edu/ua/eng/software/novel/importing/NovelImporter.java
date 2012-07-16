/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.novel.importing;

import java.io.File;

import de.uni_bremen.st.rcf.imports.Import;
import de.uni_bremen.st.rcf.imports.Import.CloneFormat;
import de.uni_bremen.st.rcf.model.RCF;
import edu.ua.eng.software.novel.CloneDataModel;
import edu.ua.eng.software.novel.NovelPanelController;

/**
 * For importing the various clone detection files
 * 
 * @author Casey Ferris <cmferris1@crimson.ua.edu>
 * @author Blake Bassett <rbbassett@crimson.ua.edu>
 */

public class NovelImporter
{
    public static void importReport(File importFile, File sourceDir, ReportType type) {
        RCF imported = Import.importData(type.toCloneFormat(), importFile.getAbsolutePath(), "test/test.rcf", sourceDir.getAbsolutePath());

        CloneDataModel.importData(imported.getVersions().getFirstEntry());

        NovelPanelController.getInstance().loadCloneData(); 
    }

    protected NovelImporter() {

    }

    public enum ReportType {
        NICAD, RCF;

        public CloneFormat toCloneFormat()
        {
            switch(this) {
                case NICAD:
                    return CloneFormat.NICAD;
                case RCF:
                    return CloneFormat.RCF;
                default:
                    return null;
            }
        }
    }
}