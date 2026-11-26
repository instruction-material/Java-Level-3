class Main {
    public static void main(String[] args) {
        // Assume any position that is 0 in the array is "empty"
        int[] arr = new int[7];
        int[] arr2 = new int[7];
        linearProbingInsert(arr, 1052);
        linearProbingInsert(arr, 1247);
        linearProbingInsert(arr, 1111);
        linearProbingInsert(arr, 1822);
        linearProbingInsert(arr, 2252);
        linearProbingInsert(arr, 1214);

        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();

        quadraticProbingInsert(arr2, 1052);
        quadraticProbingInsert(arr2, 1247);
        quadraticProbingInsert(arr2, 1111);
        quadraticProbingInsert(arr2, 1822);
        quadraticProbingInsert(arr2, 1321);
        quadraticProbingInsert(arr2, 3214);

        for (int num : arr2) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    /** Return whether the next linear probe sequence has a slot for this nonzero key. */
    public static boolean linearProbingInsert(int[] table, int key) {
        return insert(table, key, false);
    }

    /** Quadratic probing can fail to reach an empty slot in part of the table. */
    public static boolean quadraticProbingInsert(int[] table, int key) {
        return insert(table, key, true);
    }

    private static boolean insert(int[] table, int key, boolean quadratic) {
        if (table.length == 0 || key == 0) return false;
        int home = digitSum(key) % table.length;
        for (int step = 0; step < table.length; step++) {
            long offset = quadratic ? (long) step * step : step;
            int index = (int) ((home + offset) % table.length);
            if (table[index] == 0) {
                table[index] = key;
                return true;
            }
        }
        return false;
    }

    private static int digitSum(int key) {
        long digits = Math.abs((long) key);
        int sum = 0;
        while (digits > 0) {
            sum += (int) (digits % 10);
            digits /= 10;
        }
        return sum;
    }
}
