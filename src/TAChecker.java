/**
 * @author Eileen Guo
 * @email guoh@brandeis.edu
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class TAChecker {

    ArrayList<String> TANames;
    HashMap<String, TARecord> TARecords;
    String fileName;

    public TAChecker(String s) {
        TARecords = new HashMap<>();
        TANames = new ArrayList<>();
        fileName = s;
    }
    
    
    /**
     * read text file to ta record
     *
     * @param file
     */
    public void sortWorkLog() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            int lineNumber = 1;
            while (line != null) {
                String[] input = line.split(";");
                String name = input[0].trim();
                TARecord record;
                if (TARecords.containsKey(name)) {
                    record = TARecords.get(name);
                } else {
                    record = new TARecord(name);
                    TARecords.put(name, record);
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
                        invoiceIds.add(Integer.parseInt(id.trim()));
                    }
                    // sort INVOICE id
                    Collections.sort(invoiceIds);
                    record.addEnd(lineNumber, invoiceIds);
                }
                line = br.readLine();
                lineNumber++;
            }
        } catch (Exception e) {
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
     * check input record validity
     */
    public void checkValidity() {
        // previous invoice id
        int preId = 0;
        HashMap<Integer, Violation> violations = new HashMap<> ();
        
        for (int i = 0; i < TANames.size(); ++i) {
            int lineNumber = i + 1;
            String name = TANames.get(i);
            TARecord record = TARecords.get(name);

            if (record.isStart(lineNumber)) {
                //  get its invoice id
                int currentId = record.getInvoiceId(lineNumber);
                if (currentId > preId) {
                    preId = currentId;
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
            // this is a batch
            else if (record.isBatch(lineNumber)) {
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
        Scanner console = new Scanner(System.in);
        String file = console.nextLine();
        TAChecker taChecker = new TAChecker(file);
        taChecker.sortWorkLog();
        taChecker.checkValidity();
        console.close();
    }
}
