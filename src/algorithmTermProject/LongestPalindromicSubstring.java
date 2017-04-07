package algorithmTermProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;

/**
 * Algorithm term project
 * this project is going to test 3 algorithm 
 * to solve longest palindromic substring problem
 * 
 * @author liangjiwang
 *
 */

public class LongestPalindromicSubstring {
	private static Random randomGenerator = new Random();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// test funcionality
//		String temp = palindromeGenerator(101);
//		Print.print(validPalindrome(temp, 0, temp.length() - 1));
		//
		List<String> palString = new ArrayList<>();
		int palLen = 0;
		for (int i = 1; i <= 10; i++) {
			int len = i * i * 10;
			palLen += len;
			palString.add(palindromeGenerator(len));
		}
		System.out.format("all palindrome lenth: %s%n", palLen);
		List<String> testString = testStringGenerator(palString);
//		Print.print(testString);
		for (String test : testString) {
			long pre = System.currentTimeMillis();
//			bruteForce(test);
			System.out.format("Brute force: test string size: %s, "
					+ "longest palindrome len: %s, "
					+ "time use: %s ms%n", 
					test.length(), 
					bruteForce(test), 
					System.currentTimeMillis() - pre);
			pre = System.currentTimeMillis();
//			dynamicProgramming(test);
			System.out.format("DP: test string size: %s, "
					+ "longest palindrome len: %s, "
					+ "time use: %s ms%n", 
					test.length(), 
					dp(test), 
					System.currentTimeMillis() - pre);
			pre = System.currentTimeMillis();
//			dynamicProgramming(test);
			System.out.format("Manacher: test string size: %s, "
					+ "longest palindrome len: %s, "
					+ "time use: %s ms%n", 
					test.length(), 
					manacher(test), 
					System.currentTimeMillis() - pre);
			
		}
//		System.out.println(System.currentTimeMillis());
	}
	// constructor
	public LongestPalindromicSubstring() {
//		randomGenerator = new Random();
	}
	// given a int n, return a palindrome string with length n;
	private static String palindromeGenerator(int n) {
		if (n <= 0) return "";
		StringBuilder sb = new StringBuilder();
		if (n % 2 == 1) sb.append((char)(randomGenerator.nextInt(25) + 'a'));
		while (sb.length() < n) {
			char temp = (char)(randomGenerator.nextInt(25) + 'a');
			sb.insert(0, temp);
			sb.append(temp);
		}
		return sb.toString();
	}
	// generate test String with size of pal string * 20
	// contain 10 same pal substring and fill other with num
	private static List<String> testStringGenerator(List<String> palString) {
		List<String> list = new ArrayList<>();
		for (String pal : palString) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 10; i++) {
				sb.append(pal);
				for (int j = 0; j < pal.length(); j++) {
					sb.append(j % 10);
				}
			}
			list.add(sb.toString());
		}
		return list;
	}
	// give the target string, start index and end index
	// check whether the substring is a valid palindrome
	private static boolean validPalindrome(String s, int head, int tail) {
		if (tail - head == 0) return true;
		int left = head, right = tail;
		while (left <= right) {
			if (s.charAt(left) != s.charAt(right)) return false;
			left ++;
			right --;
		}
		return true;
	}
	private static int bruteForce(String test) {
		int max = 0, start = 0, end = 0;
		for (int i = 0; i < test.length(); i++) {
			for (int j = i; j < test.length(); j++) {
				if (validPalindrome(test, i, j) && (j - i + 1) > max) {
					max = j - i + 1;
					start = i;
					end = j;
				}
			}
		}
		return test.substring(start, end + 1).length();
	}
	public static int dp(String s) {
        int n = s.length();
        int palindromeStartsAt = 0, maxLen = 0;

        boolean[][] dp = new boolean[n][n];
        // dp[i][j] indicates whether substring s starting at index i and ending at j is palindrome
        
        for(int i = n-1; i >= 0; i--) { // keep increasing the possible palindrome string
            for(int j = i; j < n; j++) { // find the max palindrome within this window of (i,j)
                
                //check if substring between (i,j) is palindrome
                dp[i][j] = (s.charAt(i) == s.charAt(j)) // chars at i and j should match
                           && 
                           ( j-i < 3  // if window is less than or equal to 3, just end chars should match
                             || dp[i+1][j-1]  ); // if window is > 3, substring (i+1, j-1) should be palindrome too
                
                //update max palindrome string
                if(dp[i][j] && (j-i+1 > maxLen)) {
                    palindromeStartsAt = i;
                    maxLen = j-i+1;
                }
            }
        }
        return s.substring(palindromeStartsAt, palindromeStartsAt+maxLen).length();
    }
// super fast dp
//	private static int dynamicProgramming(String test) {
//		int[] farest = new int[test.length()];
//        for (int i = 0; i < farest.length; i++) {
//            farest[i] = i;
//        }
//        for (int i = 1; i < test.length(); i++) {
//            int first = farest[i - 1] - 1;
//            if (first >= 0 && test.charAt(i) == test.charAt(first)) {
//                farest[i] = first;
//            } else {
//                for (int j = first + 1; j <= i; j++) {
//                	if (validPalindrome(test, j , i)) {
//                        farest[i] = j;
//                        break;
//                    }
//                }
//            }
//        }
//        int maxDiff = 0;
//        int pos = 0;
//        for (int i = 0; i < test.length(); i++) {
//            if (i - farest[i] > maxDiff) {
//                maxDiff = i - farest[i];
//                pos = i;
//            }
//        }
//        return test.substring(farest[pos], pos + 1).length();
//	}
	
	private static int manacher(String s) {
        return longestPalindromeLinear(s).length();
    }
    
    private static String normalize(final String in) {
        final StringBuffer sb = new StringBuffer();
        sb.append('$');
        for (int i = 0; i < in.length(); i++) {
            sb.append(in.charAt(i));
            sb.append('$');
        }
        return sb.toString();
    }
    
    private static String longestPalindromeLinear(final String in) {
        /*
         * Normalize the length of the string by inserting special character ‘$’ in the space between each pair of
         * characters in the input and the two outside edges. This is done merely to make the computation identical for
         * both odd and even length input string.
         */
        final String S = normalize(in);
        int c = 0; // contains center of current palindrome
        int max = 0; // contains the right edge of current palindrome
    
        // P[i] contains the length of longest palindrome (in S, not original) centered at i
        final int[] P = new int[S.length()];
        for (int i = 0; i < P.length; i++) {
            P[i] = 0;
        }
        // for each position find longest palindrome centered at the position, save length in P
        for (int i = 1; i < S.length() - 1; i++) {
            final int i_mirror = 2 * c - i; // as (C - i_mirror) = (i - C) due to symmetric property
    
            /*
             * When max-i > P[i_mirror] then palindrome at center i_prime contained completely within palindrome
             * centered at c. Else max-i <= P[i_mirror] means that the palindrome at ceter i_mirror doesn’t fully
             * contained in the palindrome centered at c. In that case palindrome at i could be extended past max.
             */
    
            P[i] = (max > i) ? Math.min(P[i_mirror], max - i) : 0;
    
            // Try to expand the palindrome centered at i. if the palindrome centered at i could be extended past max
            // then extend max to the right edge of newly extended palindrome
            while ((i + P[i] + 1) < S.length() && (i - P[i] - 1) >= 0
                    && S.charAt(i + P[i] + 1) == S.charAt(i - P[i] - 1)) {
                P[i]++;
            }
            // If palindrome centered at was extend past max then update Center to i and update the right edge
            if (i + P[i] > max) {
                c = i;
                max = i + P[i];
            }
        }
        return extractLongest(in, P);
    }
    
    private static String extractLongest(final String in, final int[] P) {
        int longestCenter = 0;
        int longestLength = 0;
    
        for (int i = 0; i < P.length; i++) {
            if (P[i] > longestLength) {
                longestLength = P[i];
                longestCenter = i;
            }
        }
    
        final int offset = (longestCenter - longestLength) / 2;
        return in.substring(offset, offset + longestLength);
    }
}
