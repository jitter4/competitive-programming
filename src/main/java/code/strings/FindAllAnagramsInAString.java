package code.strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindAllAnagramsInAString {

    class Solution {
        public List<Integer> findAnagrams(String s, String p) {
            List<Integer> result = new ArrayList<>();
            char[] a = s.toCharArray();
            char[] b = p.toCharArray();

            Map<Integer, Integer> x = new HashMap<>();
            for (char c : b) {
                x.put(c-'a', x.getOrDefault(c-'a', 0) + 1);
            }

            Map<Integer, Integer> y = new HashMap<>();
            for (int i = 0; i < a.length; i++) {
                if (i >= b.length) {
                    if (y.get(a[i-b.length]-'a') == 1) {
                        y.remove(a[i-b.length]-'a');
                    }
                    else {
                        y.put(a[i-b.length]-'a', y.get(a[i-b.length]-'a') - 1);
                    }
                }
                y.put(a[i] - 'a', y.getOrDefault(a[i]-'a', 0) + 1);
                if (x.equals(y)) {
                    result.add(i-b.length+1);
                }
            }
            return result;
        }
    }

    class Solution2 {
        public List<Integer> findAnagrams(String s, String p) {
            List<Integer> result = new ArrayList<>();
            char[] a = s.toCharArray();
            char[] b = p.toCharArray();

            Map<Integer, Integer> x = new HashMap<>();
            for (char c : b) {
                x.put(c-'a', x.getOrDefault(c-'a', 0) + 1);
            }

            for (int i = 0; i < a.length; i++) {
                if (i >= b.length) {
                    Integer count = x.get(a[i-b.length]-'a');
                    if (count != null && count == -1) {
                        x.remove(a[i-b.length]-'a');
                    }
                    else {
                        x.put(a[i-b.length]-'a', x.getOrDefault(a[i-b.length]-'a', 0) + 1);
                    }
                }
                Integer count = x.get(a[i]-'a');
                if (count != null && count == 1) {
                    x.remove(a[i]-'a');
                }
                else {
                    x.put(a[i] - 'a', x.getOrDefault(a[i]-'a', 0) - 1);
                }

                if (x.size() == 0) {
                    result.add(i-b.length+1);
                }
            }
            return result;
        }
    }

    class Solution3 {
        public List<Integer> findAnagrams(String s, String p) {
            List<Integer> result = new ArrayList<>();
            if (s == null || s.length() == 0 || p == null || p.length() == 0) return result;
            int[] hash = new int[26];
            for (char c : p.toCharArray()) hash[c-'a']++;
            char[] x = s.toCharArray();
            int left = 0, right = 0, count = p.length();
            while (right < s.length()) {
                if (hash[x[right++]-'a']-- >= 1) count--;
                if (count == 0) result.add(left);
                if (right - left == p.length() && hash[x[left++]-'a']++ >= 0) count++;
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println(
                new FindAllAnagramsInAString().new Solution2().findAnagrams("abab", "ab")
        );
    }

}
