package fm.last.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
}
