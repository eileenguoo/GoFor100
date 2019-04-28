import java.util.ArrayList;
import java.util.HashMap;

public class TARecord {
    /**
     * store every start line number
     */
    private ArrayList<Integer> startIndex = new ArrayList<>();

    /**
     * store end line number
     */
    private int endIndex = -1;
    /**
     * store end invoice ids
     */
    private ArrayList<Long> invoiceIds = new ArrayList<>();

    /**
     * ta name
     */
    private String name;

    /**
     * Constructor
     *
     * @param name ta's name
     */
    public TARecord(String name) {
        this.name = name;
    }

    /**
     * get ta's name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public void start(int line) {
        startIndex.add(line);
    }

    public void end(int line, ArrayList<Long> ids) {
        endIndex = line;
        invoiceIds.clear();
        invoiceIds.addAll(ids);
    }

    /**
     * check line i is start or not
     *
     * @param i
     * @return
     */
    public boolean isStart(int i) {
        return startIndex.contains(i);
    }

    /**
     * get invoiceIds
     *
     * @return
     */
    public ArrayList<Long> getInvoiceIds() {
        return invoiceIds;
    }

    /**
     * get start index
     *
     * @param i
     * @return
     */
    public int getStartIndex(int i) {
        return startIndex.get(i);
    }

    public int getEndIndex() {
        return endIndex;
    }
}
