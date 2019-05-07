import LCMFreqAlgo.AlgoLCMFreq;
import LCMFreqAlgo.Dataset;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Denis Pavlov
 */
public class Main {
    public static void main(String[] arg) throws IOException {
        HashMap<String, String> event_module = new HashMap<>();
        File input = new File(Constants.INPUT_PATH);

        File filteredLog = generateFilteredLogFile(event_module,
                input,
                Constants.FILTERED_LOG_PATH);
        File result = executeAlgorithmAndGenerateResultFile(filteredLog);

        List<String[]> pairs = getID_CountPairsFromResultFile(result);
        sortID_CountPairs(pairs);

        printTopNResults(pairs, event_module, 5);
    }

    /**
     * Creates a list of String pairs that consists of the number
     * of views of a file and its module ID.
     *
     * @param result The file with the result of the algorithm.
     * @return The List of String pairs with file ID and view count.
     * @throws IOException ...
     */
    static List<String[]> getID_CountPairsFromResultFile(File result) throws IOException {
        BufferedReader resultFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(result)));
        List<String[]> pairs = new ArrayList<>();
        String pairLine;
        while ((pairLine = resultFileReader.readLine()) != null) {
            String[] pair = new String[2];
            pair[0] = pairLine.split(" ")[1];
            pair[1] = pairLine.split(" ")[0];
            pairs.add(pair);
        }

        resultFileReader.close();
        return pairs;
    }

    /**
     * Sorts the list of pairs by number of views in descending order.
     *
     * @param pairs The List of String pairs with file ID and view count.
     */
    static List<String[]> sortID_CountPairs(List<String[]> pairs) {
        for (int i = 0; i < pairs.size() - 1; i++) {
            for (int j = 0; j < pairs.size() - i - 1; j++) {
                if (Integer.valueOf(pairs.get(j)[0]) < Integer.valueOf(pairs.get(j + 1)[0])) {
                    String[] temp = pairs.get(j);
                    pairs.set(j, pairs.get(j + 1));
                    pairs.set(j + 1, temp);
                }
            }
        }
        return pairs;
    }

    /**
     * Iterates over the input file and collects the items
     * that are files and are being viewed. Every time an item is
     * found, it is then added to a map with the name and the ID
     * of the file and to a "filtered log" file, that is later
     * used to calculate the result.
     *
     * @param event_module Map with file name and file ID
     * @param input The input file with all of the events.
     * @param filteredLogFilePath The path of the file to save the filtered log in.
     * @return The file with the correct items.
     * @throws IOException ...
     */
    static File generateFilteredLogFile(HashMap<String, String> event_module,
                                        File input,
                                        String filteredLogFilePath) throws IOException {
        File filteredLog = new File(filteredLogFilePath);
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
        BufferedWriter filteredLogWriter = new BufferedWriter(new FileWriter(filteredLog));

        String inputLine;
        while ((inputLine = inputReader.readLine()) != null) {
            String[] args = inputLine.split(",");
            if (args[3].equals("File") && args[4].equals("Course module viewed")) {
                String str = args[5].split("'")[5];
                filteredLogWriter.append(str);
                filteredLogWriter.newLine();
                event_module.put(str, args[2]);
            }
        }
        filteredLogWriter.close();
        return filteredLog;
    }

    /**
     * Runs the algorithm with the generated "filtered log" file
     * and generates the "result" file that consist of file ID
     * and number of views for that file.
     *
     * @param filteredLog The file with the correct items.
     * @return The file with the final result of the algorithm.
     * @throws IOException ...
     */
    static File executeAlgorithmAndGenerateResultFile(File filteredLog) throws IOException {
        File result = new File(Constants.RESULT_PATH);
        Dataset dataset = new Dataset(filteredLog.getPath());
        AlgoLCMFreq algorithm = new AlgoLCMFreq();
        algorithm.runAlgorithm(Constants.MINSUP_TO_CHECK_ALL_IDS, dataset, result.getPath());
        return result;
    }

    /**
     * Prints top N(if there are enough) entries in the "pairs" list.
     *
     * @param pairs The List of String pairs with file ID and view count.
     * @param event_module Map with file name and file ID
     * @param resultsCount The number of items to be shown.
     */
    private static void printTopNResults(List<String[]> pairs,
                                         HashMap<String, String> event_module,
                                         int resultsCount) {
        System.out.println("Top 5 most viewed files:\n" +
                "No    ID      Views    Name");
        int n = resultsCount > pairs.size() ? pairs.size() : resultsCount;
        for (int i = 0; i < n; i++) {
            System.out.println(formatSpacing(i + 1 + ":", 5) + " " +
                    formatSpacing(pairs.get(i)[1], 6) + "  " +
                    formatSpacing(pairs.get(i)[0], 8) + " " +
                    event_module.get(pairs.get(i)[1]));
        }
    }

    /**
     * Adds spaces to a String until it is the size of the
     * "width" parameter.
     *
     * @param str String to be formated.
     * @param width Wanted size of string.
     * @return Reformed string.
     */
    static String formatSpacing(String str, int width) {
        int spaces = width - str.length();
        if (spaces <= 0)
            return str;
        StringBuilder strBuilder = new StringBuilder(str);
        for (int i = 0; i < spaces; i++) {
            strBuilder.append(" ");
        }
        str = strBuilder.toString();
        return str;
    }

}
