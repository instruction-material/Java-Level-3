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

    // Linear probing method that returns the index of the element to be inserted
    public static void linearProbingInsert(int[] a, int num) {
        int hashCode = 0;
        int j = num;

        while (j >= 10) {
            hashCode += j % 10;
            j = j / 10;
        }
        hashCode += j;

        hashCode %= a.length;

        //First make sure there are open slots in the array
        boolean isFull = true;
        for (int n : a) {
            if (n == 0) {
                isFull = false;
            }
        }

        int i = 1;
        int index = hashCode;
        if (!isFull) {
            while (true) {
                if (a[index] == 0) {
                    a[index] = num;
                    break;
                }
                index = (hashCode + i) % a.length;
                i += 1;
            }
        }
    }

    public static void quadraticProbingInsert(int[] a, int num) {
        int hashCode = 0;
        int j = num;

        while (j >= 10) {
            hashCode += j % 10;
            j = j / 10;
        }
        hashCode += j;

        hashCode %= a.length;

        //First make sure there are open slots in the array
        boolean isFull = true;
        for (int n : a) {
            if (n == 0) {
                isFull = false;
            }
        }

        int i = 1;
        int index = hashCode;
        if (!isFull) {
            while (true) {
                if (a[index] == 0) {
                    a[index] = num;
                    break;
                }
                index = (hashCode + i * i) % a.length;
                i += 1;
            }
        }
    }
}
