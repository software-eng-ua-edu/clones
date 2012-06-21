import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;

/**
 * @author Paige Rodeghero <parodeghero@gmail.com>
 */
public class IceCream {
    @SuppressWarnings("rawtypes")
	public static void main(String args[]) throws SVNException{
        DAVRepositoryFactory.setup();

		String url = "https://jhotdraw.svn.sourceforge.net/svnroot/jhotdraw";
		long startingRevision = 0;
		long endingRevision = -1;
				
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
		Collection logEnteries = repository.log(new String[] {""}, null, startingRevision, endingRevision, true, true);
		Pattern pattern = Pattern.compile(".*BUG.*", Pattern.CASE_INSENSITIVE);

		for(Iterator entries = logEnteries.iterator();entries.hasNext();){
			SVNLogEntry logEntry = (SVNLogEntry)entries.next();				
			Matcher matcher = pattern.matcher(logEntry.toString());
				 
			if(matcher.find()){
				System.out.println("Message Log: " + logEntry.getMessage());
			}
		}	
	}
}
