import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Compute transition probabilities between FSM states from FreeMind event logs.
 *
 * Expected transition log format:
 * YYYY-MM-DD HH:mm:ss.SSS|event=transition|from=S1|to=S3|input=...|outcome=...|...
 *
 * If logs include user information as user=<id>, probabilities are printed per user.
 * Otherwise, all transitions are grouped under GLOBAL.
 */
public class ComputeTransitionProbabilities {

    private static final String DEFAULT_USER = "GLOBAL";

    private static class TransitionKey {
        final String from;
        final String to;

        TransitionKey(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TransitionKey)) {
                return false;
            }
            TransitionKey other = (TransitionKey) o;
            return from.equals(other.from) && to.equals(other.to);
        }

        @Override
        public int hashCode() {
            return from.hashCode() * 31 + to.hashCode();
        }
    }

    private static class TargetProbability {
        final String toState;
        final int count;
        final double probability;

        TargetProbability(String toState, int count, double probability) {
            this.toState = toState;
            this.count = count;
            this.probability = probability;
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ComputeTransitionProbabilities <log_file_path>");
            System.exit(1);
        }

        Path logPath = Paths.get(args[0]);
        if (!Files.exists(logPath)) {
            System.err.println("Log file not found: " + logPath);
            System.exit(1);
        }

        try {
            Map<String, Set<String>> traversedStatesByUser = new HashMap<>();
            Map<String, Map<TransitionKey, Integer>> transitionCountsByUser = new HashMap<>();

            parseAndCollect(logPath, traversedStatesByUser, transitionCountsByUser);
            printReport(traversedStatesByUser, transitionCountsByUser);
        } catch (IOException e) {
            System.err.println("Failed to read log file: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void parseAndCollect(
            Path logPath,
            Map<String, Set<String>> traversedStatesByUser,
            Map<String, Map<TransitionKey, Integer>> transitionCountsByUser) throws IOException {

        try (BufferedReader reader = Files.newBufferedReader(logPath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Map<String, String> record = parseLogLine(line);
                if (record == null) {
                    continue;
                }
                if (!"transition".equals(record.get("event"))) {
                    continue;
                }

                String fromState = record.get("from");
                String toState = record.get("to");
                if (fromState == null || toState == null || fromState.isEmpty() || toState.isEmpty()) {
                    continue;
                }

                String user = record.getOrDefault("user", DEFAULT_USER);

                traversedStatesByUser.computeIfAbsent(user, k -> new HashSet<>()).add(fromState);
                traversedStatesByUser.get(user).add(toState);

                Map<TransitionKey, Integer> pairCounts =
                        transitionCountsByUser.computeIfAbsent(user, k -> new HashMap<>());
                TransitionKey key = new TransitionKey(fromState, toState);
                pairCounts.put(key, pairCounts.getOrDefault(key, 0) + 1);
            }
        }
    }

    private static Map<String, String> parseLogLine(String rawLine) {
        if (rawLine == null) {
            return null;
        }
        String line = rawLine.trim();
        if (line.isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\|");
        if (parts.length < 2) {
            return null;
        }

        Map<String, String> record = new HashMap<>();
        record.put("timestamp", parts[0].trim());

        StringBuilder detailsBuilder = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            String token = parts[i].trim();
            int eq = token.indexOf('=');
            if (eq > 0) {
                String key = token.substring(0, eq).trim();
                String value = token.substring(eq + 1).trim();
                record.put(key, value);
            } else if (!token.isEmpty()) {
                if (detailsBuilder.length() > 0) {
                    detailsBuilder.append("|");
                }
                detailsBuilder.append(token);
            }
        }
        if (detailsBuilder.length() > 0) {
            record.put("details", detailsBuilder.toString());
        }
        return record;
    }

    private static void printReport(
            Map<String, Set<String>> traversedStatesByUser,
            Map<String, Map<TransitionKey, Integer>> transitionCountsByUser) {

        Set<String> usersSet = new HashSet<>();
        usersSet.addAll(traversedStatesByUser.keySet());
        usersSet.addAll(transitionCountsByUser.keySet());

        if (usersSet.isEmpty()) {
            System.out.println("No transition events found.");
            return;
        }

        List<String> users = new ArrayList<>(usersSet);
        Collections.sort(users);

        Set<String> traversedStateSet = new HashSet<>();
        for (Set<String> statesForUser : traversedStatesByUser.values()) {
            traversedStateSet.addAll(statesForUser);
        }
        List<String> traversedStates = new ArrayList<>(traversedStateSet);
        Collections.sort(traversedStates);
        System.out.println();
        System.out.println("States traversed: "
                + (traversedStates.isEmpty() ? "(none)" : String.join(", ", traversedStates)));
        System.out.println();
        for (String user : users) {
            Map<TransitionKey, Integer> pairCounts = transitionCountsByUser.get(user);
            if (pairCounts == null || pairCounts.isEmpty()) {
                continue;
            }

            Map<String, Integer> outboundTotals = new HashMap<>();
            Map<String, List<TargetProbability>> byFromState = new HashMap<>();

            for (Map.Entry<TransitionKey, Integer> entry : pairCounts.entrySet()) {
                TransitionKey key = entry.getKey();
                int count = entry.getValue();
                outboundTotals.put(key.from, outboundTotals.getOrDefault(key.from, 0) + count);
            }

            for (Map.Entry<TransitionKey, Integer> entry : pairCounts.entrySet()) {
                TransitionKey key = entry.getKey();
                int count = entry.getValue();
                int total = outboundTotals.getOrDefault(key.from, 0);
                double probability = total == 0 ? 0.0 : ((double) count) / total;
                byFromState.computeIfAbsent(key.from, k -> new ArrayList<>())
                        .add(new TargetProbability(key.to, count, probability));
            }

            List<String> fromStates = new ArrayList<>(byFromState.keySet());
            Collections.sort(fromStates);

            List<String> formattedLines = new ArrayList<>();
            for (String fromState : fromStates) {
                List<TargetProbability> targets = byFromState.get(fromState);
                targets.sort(Comparator.comparing(tp -> tp.toState));
                for (TargetProbability tp : targets) {
                    String transitionName = formatTransitionName(fromState, tp.toState);
                    formattedLines.add(String.format(
                            Locale.ROOT,
                            "The probability of %s is %.4f.",
                            transitionName,
                            tp.probability));
                }
            }
            for (String line : formattedLines) {
                System.out.println(line);
            }
        }
    }

    private static String formatTransitionName(String fromState, String toState) {
        if (toState != null && toState.startsWith("S") && toState.length() > 1) {
            return fromState + toState.substring(1);
        }
        return fromState + toState;
    }
}
