package fm.last.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SystemUtil {

	public static final class SystemUsage
	{
		public double ramPercentage;
		public double cpuPercentage;
		public double swap;
		public double ramTotal;
		public double ramFree;
		public double ramUsed;
		public double swapTotal;
		public double swapFree;
		public double swapUsed;		
		public double totalCPUPercentage;
	}
	
	public static SystemUsage getSystemUsage()
	{
        try {
			Process p = Runtime.getRuntime().exec("top -b -n 1");
			p.waitFor();
			BufferedReader ins = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
 
			line = ins.readLine();
			line = ins.readLine();
			String cpu = ins.readLine();
			String ram = ins.readLine();
			String swap = ins.readLine();
			
			// skip lines
			ins.readLine();
			ins.readLine();
			
   
			double cpuTotalPercentage = 0;
			while((line = ins.readLine()) != null)
			{
				line = line.replaceAll("  ", " ");
				line = line.replaceAll("  ", " ");
				line = line.replaceAll("  ", " ");

				String[] tokens1= line.split(" ");
				int j=8;
				if(tokens1[0].isEmpty())
					j=j+1;
				
				String cpuUsage = tokens1[j].replace(",", ".");
				
	            j++;
	            Float procMemUtil = Float.parseFloat(tokens1[j].replace(",", "."));
	            j = j+2;
	            String procName = new String(tokens1[j]);

	            System.out.println(procName+"\t"+cpuUsage+"\t"+procMemUtil);
	            
	            
	            cpuTotalPercentage += Double.parseDouble(cpuUsage);
			}
			
			cpu = cpu.replace("%Cpu(s): ", "");
			String cpuInfo[] = cpu.split(", ");
			cpu = cpuInfo[0].replace(" us", "");

			ram = ram.replace("KiB Mem : ", "");
			String ramInfo[] = ram.split(", ");
			String ramTotal = ramInfo[0].replace(" total", "");
			String ramFree = ramInfo[1].replace(" free", "");
			String ramUsed = ramInfo[2].replace(" used", "");
			
			swap = swap.replace("KiB Swap: ", "");
			
			String swapInfo[] = swap.split(", ");
			String swapTotal = swapInfo[0].replace("+total", "");
			String swapFree = swapInfo[1].replace("+free", "");
//			String swapUsed = swapInfo[2].replace(" used", "");
//			String swapAvail = swapInfo[3].replace(" avail Mem", "");
//						
			SystemUsage systemUsage = new SystemUsage();
						
			systemUsage.ramUsed = Double.parseDouble(ramUsed.replace(",", "."));
			systemUsage.ramFree = Double.parseDouble(ramFree.replace(",", "."));
			systemUsage.ramTotal = Double.parseDouble(ramTotal.replace(",", "."));
			systemUsage.swapFree = Double.parseDouble(swapFree.replace(",", "."));
			systemUsage.swapTotal = Double.parseDouble(swapTotal.replace(",", "."));

			systemUsage.ramPercentage = systemUsage.ramUsed / systemUsage.ramTotal * 100;
			
			systemUsage.cpuPercentage = Double.parseDouble(cpu.replace(",", "."));
			
			systemUsage.swapUsed = systemUsage.swapTotal - systemUsage.swapFree;

			systemUsage.swap = systemUsage.swapUsed / systemUsage.swapTotal * 100;

			systemUsage.totalCPUPercentage = cpuTotalPercentage;
			
			return systemUsage;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
	}
	
	public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
	    Path p = Paths.get(zipFilePath);
	    try
	    {	final ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p)); 
	        final Path pp = Paths.get(sourceDirPath);
	        Files.walk(pp)
	          .filter(new Predicate<Path>() {

				@Override
				public boolean test(Path path) {
					return !Files.isDirectory(path);					
				}
			})
	          .forEach(new Consumer<Path>() {

				@Override
				public void accept(Path path) {
					ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
		              try {
		                  zs.putNextEntry(zipEntry);
		                  Files.copy(path, zs);
		                  zs.closeEntry();
		            } catch (IOException e) {
		                System.err.println(e);
		            }					
				}
			});
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();	    	
	    }
	}
	
	public static void unpack(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null)
            {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) 
                {
                	fos.write(buffer, 0, len);
                }
                
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                
                if(newFile.getName().endsWith(".sh"))
                {
                	Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
     	       	    perms.add(PosixFilePermission.OWNER_READ);
     	       	    perms.add(PosixFilePermission.OWNER_WRITE);
     	       	    perms.add(PosixFilePermission.OWNER_EXECUTE);
     	
     	       	    perms.add(PosixFilePermission.OTHERS_READ);
     	       	    perms.add(PosixFilePermission.OTHERS_WRITE);
     	       	    perms.add(PosixFilePermission.OTHERS_EXECUTE);
     	
     	       	    perms.add(PosixFilePermission.GROUP_READ);
     	       	    perms.add(PosixFilePermission.GROUP_WRITE);
     	       	    perms.add(PosixFilePermission.GROUP_EXECUTE);
     	
     	       	    Files.setPosixFilePermissions(newFile.toPath(), perms);
     	        	    
     	       	    newFile.setWritable(true);
     	       	    newFile.setReadable(true);
     	       	    newFile.setExecutable(true);        		 
                }
                
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
}
