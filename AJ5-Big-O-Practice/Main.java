class Main {
  // f(n) = 12n^2 + n
  // O(n^2) - Quadratic time


  // f(n) = n - sqrt(n) + 0
  // O(n) - Linear time


  // f(n) = 11,000,000n + 5
  // O(n) - Linear time


  // f(n) = 1,000,000n - 1,000,000
  // O(n) - Linear time


  // f(n) = 1,234,567 + 6 - 5
  // O(1) - Constant time


  // f(n) = log(n) + 2
  // O(logn) - Logarithmic time
  // log_base(n) tells us to what power the base needs to be raised to get us to n. The base doesn’t matter when computing the time complexity, but we usually just assume that the base is 2.


  // f(n) = n + log(n)
  // O(n) - Linear time


  // These next 3 are challenge problems!
  // f(n) = 1 + 2 + 3 + 4 + 5 + 6 ... + n (The exact sum is n(n+1)/2)
  // O(n^2) - Quadratic time
  // The exact sum is n(n+1)/2


  // f(n, m) = 3log2(n) + m^2
  // O(log(n) + m^2)
  // Note that since there are two variables, both are included in the BigO.


  // f(n) = 1 + f(n/2)
  // f(1) = 0
  // O(logn) - Logarithmic time
  // This is a recursive problem: how many times do we need to divide n by 2 to get to 1?
}