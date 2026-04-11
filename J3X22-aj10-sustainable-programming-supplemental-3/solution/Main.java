import java.util.List;

public class Main {
	private static int computeScore(List<Integer> values) {
		int total = 0;
		for (int value : values) {
			total += value % 2 == 0 ? value / 2 : value * 2;
		}
		return total;
	}

	public static void main(String[] args) {
		System.out.println(computeScore(List.of(2, 5, 8, 13)));
	}
}
