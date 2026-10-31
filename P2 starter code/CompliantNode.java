import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

public class CompliantNode implements Node {

    private boolean[] followees;
    private Set<Transaction> pendingTransactions;
    private Map<Integer, Integer> votes;
    private int round;
    private int numRounds;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.numRounds = numRounds;
        this.round = 0;
        this.pendingTransactions = new HashSet<>();
        this.votes = new HashMap<>();
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.pendingTransactions.addAll(pendingTransactions);
    }

    public Set<Transaction> getProposals() {
        round++;

        return new HashSet<>(pendingTransactions);
    }

    public void receiveCandidates(ArrayList<Integer[]> candidates) {

        for (Integer[] candidate : candidates) {
            int txID = candidate[0];
            int senderID = candidate[1];

            if (followees[senderID]) {
                votes.put(txID, votes.getOrDefault(txID, 0) + 1);
            }
        }
    }
}
