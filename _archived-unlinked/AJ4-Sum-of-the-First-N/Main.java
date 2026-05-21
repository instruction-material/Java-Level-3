class Main {

  public static void main(String[] args) {
    //print the sum of the first 5 numbers
    System.out.println();
    System.out.println(iterativeSumOfTheFirst(5));
    System.out.println(sumOfTheFirst(5));

  }

  //iteritive
  public static int iterativeSumOfTheFirst(int n){
      int total = 0;
      for(int i = 1; i <= n; i++){
        total += i;
      }
      return total;
  }

  //recursive
  public static int sumOfTheFirst(int n){
    //base case
    if(n <= 1){
      return n;
    }
    //recursive case
    int answer = n + sumOfTheFirst(n-1);
    return answer;
  }
}