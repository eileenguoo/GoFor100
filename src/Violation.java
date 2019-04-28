/**
 * @author Eileen Guo
 * @email guoh@brandeis.edu
 */

public class Violation {
    private int lineNumber;
    private String name;
    private String type;
    private boolean include;
    
    public Violation(int lineNumber, String name, String type) {
        this.lineNumber = lineNumber;
        this.name = name;
        this.type = type;
        include = true;
    }
    
    /**
     * 
     * @return type
     */
    public String getType() {
        return type;
    }
    
    /**
     * 
     * @param include
     */
    public void setInclude(boolean include) {
        this.include = include;
    }
    
    /**
     * print violation content
     */
    public void print() {
        if (include) {
            System.out.println(lineNumber + ";" + name + ";" + type);
        }
    }
}
