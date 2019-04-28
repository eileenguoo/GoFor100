/**
 * @author Eileen Guo
 * @email guoh@brandeis.edu
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TARecord {

    /**
     * TA name
     */
    private String name;
    
    /**
     * start line numbers
     */
    private LinkedList<Integer> startLineNumbers;

    /**
     * maps a start line number to invoiceIDs
     */
    private HashMap<Integer, Integer> lineNumberToIds;
    
    /**
     * unstarted lines
     */
    private List<Integer> unstartedLineNumbers;
    
    /**
     * maps a batch line to all start line numbers
     */
    private HashMap<Integer, List<Integer>> batches;
    
    public TARecord(String name) {
        this.name = name;
        startLineNumbers = new LinkedList<>();
        lineNumberToIds = new HashMap<>();
        unstartedLineNumbers = new ArrayList<>();
        batches = new HashMap<>();
    }

    /**
     * get current record's TA name
     * @return TA name
     */
    public String getName() {
        return name;
    }
    
    /**
     * add start event
     * 
     * @param lineNumber start line number
     */
    public void addStart(int lineNumber) {
        startLineNumbers.add(lineNumber);
    }

    /**
     * add end event
     * 
     * @param lineNumber line number
     * @param invoiceIDs invoice IDs
     */
    public void addEnd(int lineNumber, ArrayList<Integer> invoiceIds) {
        // this is a single invoice ID
        if (invoiceIds.size() == 1) {
            // there's an unstarted job
            if (startLineNumbers.size() == 0) {
                unstartedLineNumbers.add(lineNumber);
                return;
            }
            int startLineNumber = startLineNumbers.removeFirst();
            lineNumberToIds.put(startLineNumber, invoiceIds.get(0));
        }
        // this is a batch
        else {
            List<Integer> lineNumbers = new ArrayList<>();
            for (int invoiceId : invoiceIds) {
                int startLineNumber = startLineNumbers.removeFirst();
                lineNumberToIds.put(startLineNumber, invoiceId);
                lineNumbers.add(startLineNumber);
            }
            batches.put(lineNumber, lineNumbers);
        }
    }

    /**
     * @param lineNumber line number
     * @return true if it is a batch, false otherwise
     */
    public boolean isBatch(int lineNumber) {
        if (batches.containsKey(lineNumber)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 
     * @param lineNumber line number
     * @return the start line numbers in this batch
     */
    public List<Integer> getBatchLineNumbers(int lineNumber) {
        return batches.get(lineNumber);
    }
    
    /**
     * @param lineNumber line number
     * @return true if its a start line, false otherwise
     */
    public boolean isStart(int lineNumber) {
        if (lineNumberToIds.containsKey(lineNumber)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * get invoice ID for a start event
     *
     * @param lineNumber line number
     * @return its index
     */
    public int getInvoiceId(int startLineNumber) {
        return lineNumberToIds.get(startLineNumber);
    }
    
    /**
     * @param lineNumber line number
     * @return true if its unstarted, false otherwise
     */
    public boolean isUnstarted(int lineNumber) {
        if (unstartedLineNumbers.contains(lineNumber)) {
            return true;
        }
        else {
            return false;
        }
    }
}
