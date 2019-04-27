/** 
 * @author Elieen Guo
 * @email guoh@brandeis.edu
 */
public class WorkLog {
    private int[] invoiceIDs;
    private int lineNumber;
    private boolean batch;
    
    public WorkLog(int[] invoiceIDs, int lineNumber) {
        this.setInvoiceIDs(invoiceIDs);
        this.setLineNumber(lineNumber);
        if (invoiceIDs.length > 1) {
            batch = true;
        }
        else {
            batch = false;
        }
    }
    public int[] getInvoiceIDs() {
        return invoiceIDs;
    }
    public void setInvoiceIDs(int[] invoiceIDs) {
        this.invoiceIDs = invoiceIDs;
    }
    public int getLineNumber() {
        return lineNumber;
    }
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    public boolean isBatch() {
        return batch;
    }
}
