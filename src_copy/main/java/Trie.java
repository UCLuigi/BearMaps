import java.util.*;

/**
 * Created by LuisAlba on 7/30/16.
 */
public class Trie {

    /** Root of Trie. */
    private TrieNode root;

    /** Trie Constructor. */
    public Trie() {
        root = new TrieNode();
    }

    /** Add a word to Trie. */
    public void addWord(String word) {
        TrieNode children = root;
        TrieNode soFar;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (children.nextLetters.containsKey(c)) {
                soFar = children.nextLetters.get(c);
                soFar.addWord(word);
            } else {
                soFar = new TrieNode();
                children.nextLetters.put(c, soFar);
                soFar.addWord(word);
            }
            children = soFar;
        }
    }

    /** Searches for TrieNode with word accumulated. */
    public TrieNode search(String word) {
        TrieNode children = root;
        TrieNode search = null;
        String wordLower = word.toLowerCase();
        for (int i = 0; i < wordLower.length(); i++) {
            char c = wordLower.charAt(i);
            if (children.nextLetters.containsKey(c)) {
                search = children.nextLetters.get(c);
                children = search;
            } else {
                return null;
            }
        }
        return search;
    }

    /** Returns a list of words with prefix. */
    public List<String> autoComplete(String prefix) {
        TrieNode begin = search(prefix);
        if (begin == null) {
            return null;
        }
        return begin.getWords();
    }

    /** TrieNode class. */
    private class TrieNode {

        /** Mapping to next letters. */
        private Map<Character, TrieNode> nextLetters;
        private ArrayList<String> words;

        /** TrieNode constructor. */
        private TrieNode() {
            this.nextLetters = new HashMap<>();
        }

        /** Returns ArrayList of Strings. */
        public ArrayList<String> getWords() {
            return words;
        }

        /** Adds word to ArrayList. */
        public void addWord(String str) {
            if (words == null) {
                words = new ArrayList<>();
                words.add(str);
            } else {
                if (!words.contains(str)) {
                    words.add(str);
                }
            }
        }

    }

}
