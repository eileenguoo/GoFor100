/**
 * @author Eileen Guo
 * @email guoh@brandeis.edu
 */
import java.io.*;
import java.util.*;

/**
 * 
 * @author eileenguo
 * TAChecker is the main class that checks all the TA worklogs for instances of “fraud”
 * 
 */
public class TAChecker {

    private ArrayList<String> TANames;
    private HashMap<String, TARecord> TARecords;
    private String fileName;

    public TAChecker(String s) {
        TARecords = new HashMap<>();
        TANames = new ArrayList<>();
        fileName = s;
    }
    
    
    /**
     * read text file to ta record for each unqiue TA
     *
     */
    public void sortWorkLog() {
        Scanner br = null;
        try {
            br = new Scanner(new File(fileName));
            int lineNumber = 1;
            while (br.hasNextLine()) {
                String line = br.nextLine();
                String[] input = line.split(";");
                String name = input[0].trim(); //reads TA name
                TARecord record;
                if (TARecords.containsKey(name)) { //checks if TA name alreay existed in TARecords keyset
                    record = TARecords.get(name); //assign record as the curr TARecord with corresponding key name
                } else {
                    record = new TARecord(name); //if not existed, create a new TARecord for this name 
                    TARecords.put(name, record); //put TA name and record in the hashmap
                }
                TANames.add(name);
                String action = input[1]; 
                // Job start event
                if (action.equals("START")) {
                    record.addStart(lineNumber);
                }
                else {
                    // Job end events
                    String[] ids = action.split(",");
                    ArrayList<Integer> invoiceIds = new ArrayList<>();
                    for (String id : ids) {
                        invoiceIds.add(Integer.parseInt(id.trim())); //add ids to the invoiceIDs list
                    }
                    // sort INVOICE id
                    Collections.sort(invoiceIds);
                    record.addEnd(lineNumber, invoiceIds); //add linenum and id to curr TA's record
                }
                lineNumber++;
            }
        } catch (Exception e) { //catch exceptions 
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * check input record validity, go through each TA
     */
    public void checkValidity() {
        // previous invoice id
        int preId = 0;
        HashMap<Integer, Violation> violations = new HashMap<> ();
        
        //go through each TA in the list
        for (int i = 0; i < TANames.size(); ++i) {
            int lineNumber = i + 1;
            String name = TANames.get(i);
            TARecord record = TARecords.get(name); //corresponding TA record with name at index i

            if (record.isStart(lineNumber)) {
                //  get its invoice id
                int currentId = record.getStartInvoiceId(lineNumber);
                if (currentId > preId) {
                    continue;
                }
                else {
                    // if invoice id larger than submit id before start => SHORTENED_JOB
                    Violation violation = new Violation(lineNumber, name, "SHORTENED_JOB");
                    violations.put(lineNumber, violation);
                }
            }
            // this is unstarted job
            else if (record.isUnstarted(lineNumber)) {
                Violation violation = new Violation(lineNumber, name, "UNSTARTED_JOB");
                violations.put(lineNumber, violation);
            }
            else {
                // this is a batch
                if (record.isBatch(lineNumber)) {
                    List<Integer> batchLineNumbers = record.getBatchLineNumbers(lineNumber);
                    // count of shortened jobs
                    List<Integer> shortenedJobs = new ArrayList<>();
                    for (int batchLineNumber : batchLineNumbers) {
                        if (violations.containsKey(batchLineNumber)) {
                            Violation violation = violations.get(batchLineNumber);
                            String type = violation.getType();
                            if (type.equals("SHORTENED_JOB")) {
                                shortenedJobs.add(batchLineNumber);
                            }
                        }
                    }
                    // at least 1 shortened job
                    if (shortenedJobs.size() > 0) {
                        if (shortenedJobs.size() < batchLineNumbers.size()) {
                            // this is a SUSPICIOUS_BATCH
                            // don't include shortened jobs
                            for (int shortenedJob : shortenedJobs) {
                                Violation violation = violations.get(shortenedJob);
                                violation.setInclude(false);
                            }
                            Violation violation = new Violation(lineNumber, name, "SUSPICIOUS_BATCH");
                            violations.put(lineNumber, violation);
                        }
                    }
                }
                // update preId
                int currentId = record.getEndInvoiceId(lineNumber);
                if (currentId > preId) {
                    preId = currentId;
                }
            }
        }
        for (int i = 0; i < TANames.size(); ++i) {
            int lineNumber = i + 1;
            if (violations.containsKey(lineNumber)) {
                Violation violation = violations.get(lineNumber);
                violation.print();
            }
        }        
    }                    
    
    public static void main(String[] args) {
        System.out.println("Enter a work log:");
        Scanner scanner = new Scanner(System.in);
        String file = scanner.nextLine();
        TAChecker taChecker = new TAChecker(file);
        taChecker.sortWorkLog();
        taChecker.checkValidity();
        scanner.close();
    }
}
