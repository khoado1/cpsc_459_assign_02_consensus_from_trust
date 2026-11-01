
import csv
import itertools
import subprocess

GRAPH = [0.1, 0.2, 0.3]
MALICIOUS = [0.15, 0.30, 0.45]
DISTRIBUTION = [0.01, 0.05, 0.10]
ROUNDS = [10, 20]

TRIALS = 50

with open("extra_credit_results.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerow([
        "p_graph", "p_malicious",
        "p_txDistribution", "numRounds",
        "successful_trials", "total_trials",
        "success_rate"
    ])

    for graph, malicious, distribution, rounds in itertools.product(
        GRAPH, MALICIOUS, DISTRIBUTION, ROUNDS
    ):
        successes = 0

        for trial in range(TRIALS):
            result = subprocess.run(
                [
                    "java", "ConsensusSimulation",
                    str(graph), str(malicious),
                    str(distribution), str(rounds)
                ],
                capture_output=True,
                text=True,
                timeout=120
            )

            passed = (
                result.returncode == 0
                and "All compliant nodes agree: true" in result.stdout
                and "Nonempty consensus: true" in result.stdout
            )

            if passed:
                successes += 1

        success_rate = successes / TRIALS

        writer.writerow([
            graph, malicious, distribution, rounds,
            successes, TRIALS, round(success_rate, 3)
        ])
        f.flush()

        print(
            f"graph={graph}, malicious={malicious}, "
            f"distribution={distribution}, rounds={rounds}: "
            f"{successes}/{TRIALS} successful"
        )

print("Finished. Results saved to extra_credit_results.csv")
