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
    
            if (docIndex.containsKey(docId)) {
                Set<String> previousTokens = docIndex.get(docId);
                previousTokens.forEach(token -> {
                    tokenIndex.get(token).remove(docId);
                    if (tokenIndex.get(token).isEmpty()) {
                        tokenIndex.remove(token);
                    }
                });
            }
    
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
        Set<Integer> results = new HashSet<>();
        String[] tokens = query.split("\\s+");
    
        // Initialize the results set with the document IDs containing the first token
        if (tokenIndex.containsKey(tokens[0])) {
            results.addAll(tokenIndex.get(tokens[0]));
        }
    
        // Process subsequent tokens and perform set operations to refine the results
        for (int i = 1; i < tokens.length; i++) {
            String token = tokens[i];
            if (tokenIndex.containsKey(token)) {
                Set<Integer> matchingDocs = tokenIndex.get(token);
                results.retainAll(matchingDocs); // Intersection with the current token's documents
            } else {
                // No documents contain this token, so the result set should be empty
                results.clear();
                break;
            }
        }
    
        return results;
    }
    
}
