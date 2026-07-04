import java.util.List;

// Demonstrate a small score calculation with named sample data
public class Main {
	private static final List<Integer> SAMPLE_VALUES = List.of(2, 5, 8, 13);
	private static final int EVEN_DIVISOR = 2;
	private static final int ODD_MULTIPLIER = 2;

	// Compute the total score from the even and odd value rules
	private static int computeScore(List<Integer> values) {
		int total = 0;

		// Apply the scoring rule to each value
		for (int value : values) {
			// Score even values with the divisor rule
			if (value % EVEN_DIVISOR == 0) {
				total += value / EVEN_DIVISOR;
			}
			// Score odd values with the multiplier rule
			else {
				total += value * ODD_MULTIPLIER;
			}
		}

		return total;
	}

	/**
	 * @brief Print the sample score
	 *
	 * @param args Command-line arguments
	 */
	public static void main(String[] args) {
		System.out.println(computeScore(SAMPLE_VALUES));
	}
}
