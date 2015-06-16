package com.company;

import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RedisDbUtil {

    Jedis jedis;
    private Scanner kb;

    public RedisDbUtil() {
        jedis = new Jedis();
    }

    public void execute() {
        int ch = 0;
        kb = new Scanner(System.in);
        while (ch < 5) {
            System.out.println("1. Enter word\n2. Search meaning\n3. Get description\n4. Get words\n5+. Quit");
            switch (ch = kb.nextInt()) {
                case 1:
                    kb.nextLine();
                    System.out.println("Enter new word to be added");
                    String word = readInput();
                    System.out.println("Enter meaning for new word");
                    String meaning = readInput();
                    System.out.println("Enter description for new word");
                    String description = readInput();
                    System.out.println(newWord(word, meaning, description));
                    System.out.println("There are totally "+jedis.get("wordCount")+" words");
                    break;
                case 2:
                    kb.nextLine();
                    System.out.println("Enter new word to be searched");
                    System.out.println(searchWord(readInput()));
                    break;
                case 3:
                    kb.nextLine();
                    System.out.println("Enter new word to be searched");
                    System.out.println(getDescription(readInput()));
                    break;
                case 4:
                    kb.nextLine();
                    System.out.println("Enter meaning to search with");
                    System.out.println(getWords(readInput()));
                    break;
                default:
                    jedis.save();
                    jedis.close();
                    break;
            }
        }
        kb.close();
    }

    private String readInput() {
        return kb.nextLine().toLowerCase();
    }

    private List<String> getWords(String meaning) {
        if (jedis.exists(meaning)) {
            List<String> words = jedis.lrange(meaning, 0, 50);
            return words;
        }
        return new LinkedList<>();
    }

    private String getDescription(String word) {
        if (jedis.exists(word)) {
            return "Meaning: "+jedis.get(word)+"\nDesc: "+jedis.get(word+"_desc");
        }
        return "Word not found";
    }

    private String searchWord(String word) {
        if (jedis.exists(word)) {
            return jedis.get(word);
        }
        else
            return "Word not found";
    }

    private String newWord(String word, String meaning, String description) {
        if (!jedis.exists(word)) {
            jedis.set(word, meaning);
            jedis.set(word+"_desc", description);
            jedis.lpush(meaning, word);
            jedis.incr("wordCount");
            return "Word added";
        }
        else {
            System.out.println("Already exists");
            return getDescription(word);
        }
    }
}
