import java.io.*;
import java.util.*;

public class TAChecker {
    private HashMap<String, TARecord> TARecords;
    private int maxInvoiceID;
    String fileName;
    
    public TAChecker(String s) {
        TARecords = new HashMap<String, TARecord>();
        maxInvoiceID = 0;
        fileName = s;
    }
   
    public void submitInvoice(TARecord record, int[] invoiceIDs, int line_num) {
		WorkLog workLog = new WorkLog(invoiceIDs, line_num);
    	Arrays.sort(invoiceIDs);
    	for (int i : invoiceIDs) {
    		if (i >= maxInvoiceID) {
    			maxInvoiceID = i;
    		}
    		else {
    			if (invoiceIDs.length > 1) {
    				record.addViolation(workLog, 0);
    			}
				record.addViolation(workLog, 1);
    		}
    	}
		record.submitInvoice(workLog);
    }
    
    public void sortWorkLog() throws FileNotFoundException {
        Scanner input = new Scanner(new File(fileName));
        int line_num = 0;
        while(input.hasNextLine()) {
        	line_num++;
        	String line = input.nextLine();
	        String[] str = line.split(";");
	        String name = str[0];
	        TARecord record;
	        if(TARecords.containsKey(name)) 
	        	record = TARecords.get(name);
	        else {
	        	record = new TARecord(name);
	        	TARecords.put(name, record);
	        }
	        
	    	if (str[1].equals("START"))
	    		record.startWork();
        	else {
        		String[] strIDs = str[1].split(",");
        		int[] invoiceIDs = new int[strIDs.length];
        		for(int i = 0; i < strIDs.length; i++) {
        			invoiceIDs[i] = Integer.parseInt(strIDs[i]);
        		}
        		submitInvoice(record, invoiceIDs, line_num);
        	}
        }
        input.close();
    }
    
    public void checkValidity() {
    	
    }
    
	public static void main(String [] args) throws FileNotFoundException {
		Scanner console = new Scanner(System.in);
		System.out.println("Enter a work log:");
		String s = console.nextLine();
		TAChecker check = new TAChecker(s);
		check.sortWorkLog();
		
	}
}
