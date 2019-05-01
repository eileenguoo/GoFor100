/**
 * @author Eileen Guo
 * @email guoh@brandeis.edu
 * this class checks the violation types and print the violation 
 */

public class Violation {
    private int lineNumber;
    private String name;
    private String type;
    private boolean include;
    
    /**
     * constructor
     * @param lineNumber violaton line num
     * @param name name of TA
     * @param type vioaltion type
     */
    public Violation(int lineNumber, String name, String type) {
        this.lineNumber = lineNumber;
        this.name = name;
        this.type = type;
        include = true;
    }
    
    /**
     * get tyoe
     * @return type type of violation
     */
    public String getType() {
        return type;
    }
    
    /**
     * set the include boolean
     * @param include boolean
     */
    public void setInclude(boolean include) {
        this.include = include;
    }
    
    /**
     * print violation contents
     */
    public void print() {
        if (include) {
            System.out.println(lineNumber + ";" + name + ";" + type);
        }
    }
}
