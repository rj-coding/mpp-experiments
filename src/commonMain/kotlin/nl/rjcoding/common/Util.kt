package nl.rjcoding.common

tailrec fun gcd(a: Int, b: Int): Int {
    if (b == 0) return a
    else return gcd(b, a % b)
}