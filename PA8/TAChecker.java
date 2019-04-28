import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TAChecker {

    int lineCount = 0;

    HashMap<String, TARecord> records = new HashMap<>();
    HashMap<Integer, String> lineToRecord = new HashMap<>();

    /**
     * read text file to ta record
     *
     * @param file
     */
    public void sortWorkLog(String file) {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        lineCount = lines.size();
        for (int i = 0; i < lines.size(); i++) {
            String[] input = lines.get(i).split(";");
            String name = input[0].trim();
            TARecord record;
            if (records.containsKey(name)) {
                record = records.get(name);
            } else {
                record = new TARecord(name);
                records.put(name, record);
            }
            lineToRecord.put(i, name);
            String action = input[1];
            // Job start event
            if (action.equals("START")) {
                record.start(i);
            } else {
                // Job completion events
                String[] ids = action.split(",");
                ArrayList<Long> idArray = new ArrayList<>();
                for (String id : ids) {
                    if (id.trim().length() == 0) {
                        continue;
                    }
                    long idLong = Long.parseLong(id.trim());
                    idArray.add(idLong);
                }
                // sort INVOICE id
                Collections.sort(idArray);
                record.end(i, idArray);
            }
        }
    }

    /**
     * check input record validity
     */
    public void checkValidity() {
        ArrayList<String> starts = new ArrayList<>();
        ArrayList<String> ends = new ArrayList<>();
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            int lineNumber = i + 1;

            String name = lineToRecord.get(i);
            TARecord record = records.get(name);

            if (record.isStart(i)) {
                starts.add(name);
            } else if (starts.contains(name)) {
                boolean hadError = false;
                for (int j = 0; j < starts.indexOf(name); j++) {
                    if (hadError)
                        break;
                    String before = starts.get(j);
                    if (!ends.contains(before)) {
                        continue;
                    }
                    TARecord preRecord = records.get(before);
                    ArrayList<Long> myIds = record.getInvoiceIds();
                    ArrayList<Long> preIds = preRecord.getInvoiceIds();
                    if (myIds.size() == 1) {
                        if (record.getStartIndex(0) > preRecord.getEndIndex())
                            // if invoice id smaller than submit id before start => SHORTENED_JOB
                            for (long preId : preIds) {
                                if (myIds.get(0) < preId) {
                                    results.add((record.getStartIndex(0) + 1) + ";" + name + ";" + "SHORTENED_JOB");
                                    hadError = true;
                                    break;
                                }
                            }
                    } else {
                        // if  invoice id is more than one and invoice id smaller than submit id before start => SUSPICIOUS_BATCH
                        for (long preId : preIds) {
                            if (hadError)
                                break;
                            for (long myId : myIds) {
                                if (myId < preId) {
                                    results.add(lineNumber + ";" + name + ";" + "SUSPICIOUS_BATCH");
                                    hadError = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                ends.add(name);
            } else {
                results.add(lineNumber + ";" + name + ";" + "UNSTARTED_JOB");
            }
        }

        for (String result : results) {
            System.out.println(result);
        }
    }

    public static void main(String[] args) {
        TAChecker taChecker = new TAChecker();
        System.out.println("Enter a work log:");
        Scanner scanner = new Scanner(System.in);
        String file = scanner.nextLine();
        taChecker.sortWorkLog(file);
        taChecker.checkValidity();
    }
}
