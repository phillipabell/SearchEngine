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
            Map<Integer, Double> relevanceScores = evaluateQuery(query);
            if (relevanceScores.isEmpty()) {
                System.out.println("query results");
            } else {
                System.out.print("query results");
                relevanceScores.keySet().forEach(id -> System.out.print(" " + id));
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("query error " + e.getMessage());
        }
    }
    

    private Map<Integer, Double> evaluateQuery(String query) {
        Map<Integer, Double> relevanceScores = new HashMap<>();
        String[] tokens = query.split("\\s+");
    
        for (String token : tokens) {
            if (tokenIndex.containsKey(token)) {
                Set<Integer> matchingDocs = tokenIndex.get(token);
                double idf = Math.log((double) docIndex.size() / matchingDocs.size()); // Inverse Document Frequency
                for (int docId : matchingDocs) {
                    int tf = Collections.frequency(docIndex.get(docId), token); // Term Frequency in the document
                    double tfidf = tf * idf; // TF-IDF score
                    relevanceScores.put(docId, relevanceScores.getOrDefault(docId, 0.0) + tfidf); // Update relevance score
                }
            }
        }
    
        return relevanceScores;
    }
    
    
}
