import java.util.*;
import java.util.regex.*;

public class SearchEngine {
    private Map<Integer, Set<String>> docIndex = new HashMap<>();
    private Map<String, Set<Integer>> tokenIndex = new HashMap<>();

    public static void main(String[] args) {
        SearchEngine engine = new SearchEngine();
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.startsWith("index")) {
                engine.handleIndexCommand(command);
            } else if (command.startsWith("query")) {
                engine.handleQueryCommand(command);
            } else {
                System.out.println("Unknown command");
            }
        }
        scanner.close();
    }

    private void handleIndexCommand(String command) {
        String[] parts = command.split(" ");
        try {
            int docId = Integer.parseInt(parts[1]);
            Set<String> tokens = new HashSet<>(Arrays.asList(parts).subList(2, parts.length));

            docIndex.put(docId, tokens);
            tokens.forEach(token -> {
                tokenIndex.putIfAbsent(token, new HashSet<>());
                tokenIndex.get(token).add(docId);
            });
            
            System.out.println("index ok " + docId);
        } catch (Exception e) {
            System.out.println("index error " + e.getMessage());
        }
    }

    private void handleQueryCommand(String command) {
        String query = command.substring(6).trim();  
        try {
            Set<Integer> results = evaluateQuery(query);
            if (results.isEmpty()) {
                System.out.println("query results");
            } else {
                System.out.print("query results");
                results.forEach(id -> System.out.print(" " + id));
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("query error " + e.getMessage());
        }
    }

    private Set<Integer> evaluateQuery(String query) {
        // Implementation of query parsing and execution logic
        Set<Integer> results = new HashSet<>();
        if (tokenIndex.containsKey(query)) {
            results.addAll(tokenIndex.get(query));
        }
        return results;
    }
}
