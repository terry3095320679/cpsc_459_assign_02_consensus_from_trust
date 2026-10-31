import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private final int numRounds;
    private int proposalCalls;
    private Set<Transaction> pendingTransactions;
    private Set<Transaction> finalTransactions;
    private boolean[] followees;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        this.numRounds = numRounds;
        this.proposalCalls = 0;
        this.pendingTransactions = new HashSet<Transaction>();
        this.finalTransactions = new HashSet<Transaction>();
        this.followees = new boolean[0];
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees.clone();
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.pendingTransactions = new HashSet<Transaction>(pendingTransactions);
    }

    public Set<Transaction> getProposals() {
        if (proposalCalls >= numRounds) {
            finalTransactions = new HashSet<Transaction>(pendingTransactions);
            return new HashSet<Transaction>(finalTransactions);
        }

        proposalCalls++;
        return new HashSet<Transaction>(pendingTransactions);
    }

    public void receiveCandidates(ArrayList<Integer[]> candidates) {
        if (candidates == null) {
            return;
        }

        for (Integer[] candidate : candidates) {
            if (candidate == null || candidate.length < 2 || candidate[0] == null || candidate[1] == null) {
                continue;
            }

            int sender = candidate[1];
            if (sender >= 0 && sender < followees.length && followees[sender]) {
                pendingTransactions.add(new Transaction(candidate[0]));
            }
        }

        if (proposalCalls >= numRounds) {
            finalTransactions = new HashSet<Transaction>(pendingTransactions);
        }
    }
}
