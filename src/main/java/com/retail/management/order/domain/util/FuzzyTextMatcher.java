package com.retail.management.order.domain.util;

import java.text.Normalizer;

public final class FuzzyTextMatcher {

    private FuzzyTextMatcher() {
    }

    public static boolean matches(String text, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        if (text == null || text.isBlank()) {
            return false;
        }

        String normalizedText = normalize(text);
        String normalizedQuery = normalize(query);

        if (normalizedText.contains(normalizedQuery)) {
            return true;
        }

        return fuzzyContains(normalizedText, normalizedQuery, maxAllowedDistance(normalizedQuery));
    }

    static String normalize(String input) {
        String decomposed = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = decomposed.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return withoutAccents
                .toLowerCase()
                .replaceAll("[,.:;'\"´`~!@#$%^&*()\\-_+=\\[\\]{}|\\\\/<>?]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static int maxAllowedDistance(String query) {
        int len = query.length();
        if (len <= 3) {
            return 0;
        }
        if (len <= 6) {
            return 1;
        }
        return 2;
    }

    private static boolean fuzzyContains(String text, String query, int maxDistance) {
        if (maxDistance == 0) {
            return false;
        }
        int queryLen = query.length();
        int textLen = text.length();
        if (queryLen > textLen + maxDistance) {
            return false;
        }

        int[] prev = new int[queryLen + 1];
        for (int j = 0; j <= queryLen; j++) {
            prev[j] = j;
        }
        int[] curr = new int[queryLen + 1];

        for (int i = 1; i <= textLen; i++) {
            curr[0] = 0;
            for (int j = 1; j <= queryLen; j++) {
                int cost = text.charAt(i - 1) == query.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
            }
            if (curr[queryLen] <= maxDistance) {
                return true;
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        return false;
    }

    static int levenshteinDistance(String a, String b) {
        int lenA = a.length();
        int lenB = b.length();
        int[] prev = new int[lenB + 1];
        int[] curr = new int[lenB + 1];

        for (int j = 0; j <= lenB; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= lenA; i++) {
            curr[0] = i;
            for (int j = 1; j <= lenB; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        return prev[lenB];
    }
}
