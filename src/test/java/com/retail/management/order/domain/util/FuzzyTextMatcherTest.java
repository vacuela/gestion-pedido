package com.retail.management.order.domain.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuzzyTextMatcherTest {

    @Nested
    @DisplayName("Exact and partial matching")
    class ExactAndPartialMatching {

        @Test
        @DisplayName("should match exact text")
        void shouldMatchExactText() {
            assertTrue(FuzzyTextMatcher.matches("SANTA FE", "SANTA FE"));
        }

        @Test
        @DisplayName("should match partial text (contains)")
        void shouldMatchPartialText() {
            assertTrue(FuzzyTextMatcher.matches("L  SANTA FE", "santa"));
        }

        @Test
        @DisplayName("should return false when text does not match")
        void shouldReturnFalseWhenNoMatch() {
            assertFalse(FuzzyTextMatcher.matches("SANTA FE", "zzzznonexistent"));
        }

        @Test
        @DisplayName("should return true when query is null")
        void shouldReturnTrueWhenQueryNull() {
            assertTrue(FuzzyTextMatcher.matches("any text", null));
        }

        @Test
        @DisplayName("should return true when query is blank")
        void shouldReturnTrueWhenQueryBlank() {
            assertTrue(FuzzyTextMatcher.matches("any text", "  "));
        }

        @Test
        @DisplayName("should return false when text is null")
        void shouldReturnFalseWhenTextNull() {
            assertFalse(FuzzyTextMatcher.matches(null, "query"));
        }
    }

    @Nested
    @DisplayName("Case insensitive matching")
    class CaseInsensitive {

        @Test
        @DisplayName("should match regardless of case")
        void shouldMatchIgnoringCase() {
            assertTrue(FuzzyTextMatcher.matches("SANTA FE", "santa fe"));
            assertTrue(FuzzyTextMatcher.matches("santa fe", "SANTA FE"));
            assertTrue(FuzzyTextMatcher.matches("Santa Fe", "sAnTa fE"));
        }
    }

    @Nested
    @DisplayName("Accent/diacritic insensitive matching")
    class AccentInsensitive {

        @Test
        @DisplayName("should match ignoring accents in text")
        void shouldMatchIgnoringAccentsInText() {
            assertTrue(FuzzyTextMatcher.matches("Liverpool Galerías Toluca", "galerias"));
        }

        @Test
        @DisplayName("should match ignoring accents in query")
        void shouldMatchIgnoringAccentsInQuery() {
            assertTrue(FuzzyTextMatcher.matches("Liverpool Galerias Toluca", "galerías"));
        }

        @Test
        @DisplayName("should match with ñ and other special chars")
        void shouldMatchWithSpecialChars() {
            assertTrue(FuzzyTextMatcher.matches("Pantalón Levi's", "pantalon"));
        }
    }

    @Nested
    @DisplayName("Punctuation insensitive matching")
    class PunctuationInsensitive {

        @Test
        @DisplayName("should ignore commas")
        void shouldIgnoreCommas() {
            assertTrue(FuzzyTextMatcher.matches("Store, Name", "store name"));
        }

        @Test
        @DisplayName("should ignore apostrophes and special marks")
        void shouldIgnoreApostrophes() {
            assertTrue(FuzzyTextMatcher.matches("Levi's Pants", "levis pants"));
        }
    }

    @Nested
    @DisplayName("Fuzzy matching with minor typos")
    class FuzzyMatching {

        @Test
        @DisplayName("should match with one character typo for medium-length query")
        void shouldMatchWithOneCharTypo() {
            assertTrue(FuzzyTextMatcher.matches("Laptop Lenovo", "laptob"));
        }

        @Test
        @DisplayName("should match with two character typos for long query")
        void shouldMatchWithTwoCharTypos() {
            assertTrue(FuzzyTextMatcher.matches("Laptop Lenovo thinkpad", "thimkpab"));
        }

        @Test
        @DisplayName("should not match with typos in very short query")
        void shouldNotMatchTyposInShortQuery() {
            assertFalse(FuzzyTextMatcher.matches("Laptop", "xyx"));
        }
    }

    @Nested
    @DisplayName("Normalize utility")
    class NormalizeTest {

        @Test
        @DisplayName("should remove accents and lowercase")
        void shouldNormalizeText() {
            assertEquals("galerias", FuzzyTextMatcher.normalize("Galerías"));
        }

        @Test
        @DisplayName("should remove commas and punctuation")
        void shouldRemovePunctuation() {
            assertEquals("hello world", FuzzyTextMatcher.normalize("Hello, World!"));
        }

        @Test
        @DisplayName("should collapse whitespace")
        void shouldCollapseWhitespace() {
            assertEquals("l santa fe", FuzzyTextMatcher.normalize("L  SANTA  FE"));
        }
    }

    @Nested
    @DisplayName("Levenshtein distance")
    class LevenshteinTest {

        @Test
        @DisplayName("should return 0 for identical strings")
        void shouldReturnZeroForIdentical() {
            assertEquals(0, FuzzyTextMatcher.levenshteinDistance("abc", "abc"));
        }

        @Test
        @DisplayName("should return correct distance for one substitution")
        void shouldReturnOneForSubstitution() {
            assertEquals(1, FuzzyTextMatcher.levenshteinDistance("abc", "aXc"));
        }

        @Test
        @DisplayName("should return correct distance for insertion")
        void shouldReturnOneForInsertion() {
            assertEquals(1, FuzzyTextMatcher.levenshteinDistance("abc", "abXc"));
        }

        @Test
        @DisplayName("should return correct distance for deletion")
        void shouldReturnOneForDeletion() {
            assertEquals(1, FuzzyTextMatcher.levenshteinDistance("abcd", "abd"));
        }
    }
}
