
import java.util.HashSet;
import java.util.Set;

public class ConsensusCheck {

    public static boolean sameConsensus(
            Set<Transaction> first,
            Set<Transaction> second) {

        Set<Integer> firstIds = new HashSet<>();
        Set<Integer> secondIds = new HashSet<>();

        for (Transaction tx : first) {
            firstIds.add(tx.id);
        }

        for (Transaction tx : second) {
            secondIds.add(tx.id);
        }

        return firstIds.equals(secondIds);
    }
}
