import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

public class CompliantNode implements Node {

    private boolean[] followees;
    private Set<Transaction> pendingTransactions;
    private Map<Integer, Integer> votes;
    private Map<Integer, Set<Integer>> transactionSupport;
    private int round;
    private int numRounds;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.numRounds = numRounds;
        this.round = 0;
        this.pendingTransactions = new HashSet<>();
        this.votes = new HashMap<>();
        this.transactionSupport = new HashMap<>();
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.pendingTransactions.addAll(pendingTransactions);
    }

    
public Set<Transaction> getProposals() {
    round++;

    // During simulation rounds, broadcast known transactions.
    if (round <= numRounds) {
        return new HashSet<>(pendingTransactions);
    }

    // After the final round, return transactions
    // supported by a majority of followed nodes.
    Set<Transaction> consensus = new HashSet<>();

    int followeeCount = 0;

    if (followees != null) {
        for (boolean followed : followees) {
            if (followed) {
                followeeCount++;
            }
        }
    }

    int threshold = followeeCount / 2 + 1;

    for (Map.Entry<Integer, Set<Integer>> entry
            : transactionSupport.entrySet()) {

        if (entry.getValue().size() >= threshold) {
            consensus.add(new Transaction(entry.getKey()));
        }
    }

    return consensus;
}


    

public void receiveCandidates(ArrayList<Integer[]> candidates) {

    // Count unique supporters for this round.
    Map<Integer, Set<Integer>> roundSupport = new HashMap<>();

    for (Integer[] candidate : candidates) {
        if (candidate == null || candidate.length < 2) {
            continue;
        }

        int txID = candidate[0];
        int senderID = candidate[1];

        if (followees != null
                && senderID >= 0
                && senderID < followees.length
                && followees[senderID]) {

            roundSupport
                .computeIfAbsent(txID, k -> new HashSet<Integer>())
                .add(senderID);
        }
    }

    // Keep only support observed in the current round.
    transactionSupport = roundSupport;

    // Forward transactions supported by at least two followees.
    for (Map.Entry<Integer, Set<Integer>> entry
            : roundSupport.entrySet()) {

        if (entry.getValue().size() >= 2) {
            pendingTransactions.add(
                new Transaction(entry.getKey())
            );
        }
    }
}

}
