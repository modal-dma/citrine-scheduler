package fm.last.commons.io;

import java.io.File;

public class FileUtils {

	public static final String getWorkingDirectory(String id)
	{
		String workingDirectory = System.getProperty("java.io.tmpdir") + "citrine/" + id;    
	    File f = new File(workingDirectory);
	    if(!f.exists())
	    	f.mkdirs();
	    
	    return workingDirectory;
	}
	
    
}
