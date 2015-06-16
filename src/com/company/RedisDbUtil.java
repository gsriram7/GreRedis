package com.company;

import redis.clients.jedis.Jedis;

import java.util.Scanner;

public class RedisDbUtil {

    Jedis jedis;

    public RedisDbUtil() {
        jedis = new Jedis();
    }

    public void execute() {
        int ch = 0;
        Scanner kb = new Scanner(System.in);
        while (ch < 5) {
            System.out.println("1. Enter word\n2. Search meaning\3. Get description\n4. Get words\n5+. Quit");
            switch (ch) {
                case 1:
                    System.out.println("Enter new word to be added");
                    String word = kb.nextLine();
                    System.out.println("Enter meaning for new word");
                    String meaning = kb.nextLine();
                    System.out.println("Enter description for new word");
                    String description = kb.nextLine();
                    newWord(word, meaning, description);
                    break;
                case 2:
                    searchWord();
                    break;
                case 3:
                    getDescription();
                    break;
                case 4:
                    getWords();
                    break;
                default:
                    break;
            }
        }
    }

    private void getWords() {

    }

    private void getDescription() {

    }

    private void searchWord() {
    }

    private void newWord(String word, String meaning, String description) {
        if (!jedis.exists(word)) {
            jedis.set(word, meaning);
            jedis.set(word+"_desc", description);
            jedis.incr("wordCount");
        }
        else {
            System.out.println("Already exists");
        }
    }
}
