import java.util.ArrayList;

class Main {
  public static void main(String[] args) {
    ArrayList<Integer> nums = new ArrayList<Integer>();
    for (int i = 0; i < 10; i++) {
      int num = (int)((Math.random()*100)-25);
      nums.add(num);
    }
    
    System.out.println("\nOriginal ArrayList:");
    System.out.println(nums);
    System.out.println("\nEven numbers:");
    System.out.println(evenNums(nums));
    System.out.print("\nDo the numbers add to zero? ");
    System.out.println(sumZero(nums));
    System.out.println("\nWith the smallest removed:");
    System.out.println(removeSmallest(nums));
    System.out.println("\nAdd the sum to the end:");
    System.out.println(addSum(nums));
  }

  // 1. Write a method which takes in an ArrayList of numbers and returns an ArrayList containing of the even numbers.
  public static ArrayList<Integer> evenNums(ArrayList<Integer> arrList) {
    ArrayList<Integer> result = new ArrayList<Integer>();

    for (int i = 0; i < arrList.size(); i++) {
      if (arrList.get(i) % 2 == 0) {
        result.add(arrList.get(i));
      }
    }
    return result;
  }

  // 2. Write a method which takes in an ArrayList of numbers and returns true if two distinct numbers in the ArrayList sums to zero. The method should return false otherwise.
  public static boolean sumZero(ArrayList<Integer> arrList) {
    for (int i = 0; i < arrList.size(); i++) {
      for (int j = 0; j < arrList.size(); j++) {
        if (arrList.get(i) + arrList.get(j) == 0 && i != j) {
          return true;
        }
      }
    }
    return false;
  }

  // 3. Write a method which takes in an ArrayList of numbers and returns the ArrayList with the smallest number removed.
  public static ArrayList<Integer> removeSmallest(ArrayList<Integer> arrList) {
    int indexOfSmallest = 0;
    for (int i = 0; i < arrList.size(); i++) {
      if (arrList.get(i) < arrList.get(indexOfSmallest)) {
        indexOfSmallest = i;
      }
    }
    arrList.remove(indexOfSmallest);
    return arrList;
  }

  // 4. Write a method which takes in an ArrayList of numbers and returns the ArrayList with the sum of all the numbers added to the ArrayList.
  public static ArrayList<Integer> addSum(ArrayList<Integer> arrList) {
    int sum = 0;
    for (int i = 0; i < arrList.size(); i++) {
      sum += arrList.get(i);
    }
    arrList.add(sum);
    return arrList;
  }
}