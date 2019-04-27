/** 
 * @author Elieen Guo
 * @email guoh@brandeis.edu
 */
import java.util.ArrayList;

public class TARecord {
	private String name;
	// whether current TA has started work
	private int startedWorks;
	private ArrayList<WorkLog> workLogs;
	private ArrayList<String> violations;
	
	// violation types
	public static String[] violationType = {"SUSPICIOUS_BATCH",
	                                        "SHORTENED_JOB",
	                                        "UNSTARTED_JOB"};

	
	public TARecord (String name) {
	    this.name = name;
	    startedWorks = 0;
	}
	
	public void startWork () {
	    startedWorks ++;
	}

	public void endWork(int count) {
	    startedWorks -= count;
	}
	
	public void submitInvoice (WorkLog workLog) {
	    int[] invoiceIDs = workLog.getInvoiceIDs();
	    if (startedWorks < invoiceIDs.length) {
	        addViolation(workLog, 2);
	    }
	    workLogs.add(workLog);
	    endWork(invoiceIDs.length);
	}
	
	public void addViolation (WorkLog workLog, int type) {
	    violations.add(workLog.getLineNumber() + ";" + name + ";" + violationType[type]);
	}
}
	
	


	


