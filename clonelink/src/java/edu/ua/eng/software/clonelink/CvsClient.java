package edu.ua.eng.software.clonelink;

import org.netbeans.lib.cvsclient.CVSRoot;
import org.netbeans.lib.cvsclient.Client;
import org.netbeans.lib.cvsclient.connection.PServerConnection;
import org.netbeans.lib.cvsclient.command.history.HistoryCommand;
import org.netbeans.lib.cvsclient.admin.StandardAdminHandler;
import org.netbeans.lib.cvsclient.commandLine.BasicListener;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class CvsClient {
    
    public CvsClient() throws Exception {
        GlobalOptions globalOptions = new GlobalOptions();
        globalOptions.setCVSRoot(ROOT);

        Client client = connect();
            
        HistoryCommand command = new HistoryCommand();
        //Not sure if HistoryCommand is right, or how to set it up.

        client.executeCommand(command, globalOptions);
    }

    private Client connect() throws Exception {
        CVSRoot rootData = CVSRoot.parse(ROOT);
        PServerConnection c = new PServerConnection(rootData);
        c.open();

        Client client = new Client(c, new StandardAdminHandler());
        //client.setLocalPath(CVS_PATH);
        client.getEventManager().addCVSListener(new BasicListener());

        return client;
    }

    private final String ROOT = ":pserver:anonymous@jhotdraw.cvs.sourceforge.net:/cvsroot/jhotdraw";
}
