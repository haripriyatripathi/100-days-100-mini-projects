import java.util.*;
import java.util.concurrent.*;

public class WordScrambleGame {
    // Bot name
    private static final String BOT_NAME = "AXL";

    // ANSI escape codes for colors (works in most terminals including PowerShell)
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";

    // Word bank with hints and categories
    private static final String[][] WORDS = {
        {"java", "tech", "A programming language you see everywhere"},
        {"python", "tech", "Named after a snake, but it's code"},
        {"keyboard", "tech", "You’re probably smashing me rn"},
        {"galaxy", "space", "A system of stars and planets"},
        {"nebula", "space", "Cloud of gas + dust in space"},
        {"internet", "tech", "The reason you can talk to me"},
        {"scramble", "game", "Literally what I’m doing here"},
        {"matrix", "movies", "A cult sci-fi film"}
    };

    private static Scanner sc = new Scanner(System.in);
    private static Random rand = new Random();

    public static void main(String[] args) {
        showIntro();
        typeOut(CYAN + ">>> Welcome to Word Scramble! <<<" + RESET);
        typeOut(BOT_NAME + ":   Yo, I’m AXL. Let’s see if you can outsmart me.");

        int score = 0;
        boolean playAgain = true;

        while (playAgain) {
            // Ask user to pick category
            System.out.println();
            System.out.println(CYAN + "Choose a category: tech, space, game, movies" + RESET);
            System.out.print("Your choice > ");
            String categoryChoice = sc.nextLine().toLowerCase();

            // Filter words by chosen category
            List<String[]> filteredWords = new ArrayList<>();
            for (String[] word : WORDS) {
                if (word[1].equals(categoryChoice)) {
                    filteredWords.add(word);
                }
            }

            if (filteredWords.isEmpty()) {
                System.out.println(RED + "No words found in that category. Picking random instead." + RESET);
                filteredWords.addAll(Arrays.asList(WORDS));
            }

            // Pick word
            String[] selected = filteredWords.get(rand.nextInt(filteredWords.size()));
            String originalWord = selected[0];
            String category = selected[1];
            String hint = selected[2];
            String scrambled = scrambleWord(originalWord);

            System.out.println();
            typeOut(BOT_NAME + ":   Word scrambled. Category: " + YELLOW + category.toUpperCase() + RESET);
            System.out.println(">>> " + YELLOW + "SCRAMBLED WORD: " + scrambled.toUpperCase() + RESET + " <<<");
            System.out.println("Type 'hint' if you’re stuck. You got 3 tries. (30 sec each)");

            boolean guessedCorrectly = false;
            for (int attempt = 1; attempt <= 3; attempt++) {
                System.out.print("Try " + attempt + " > ");

                String guess = getUserInputWithCountdown(10); // countdown timer

                if (guess == null) {
                    showStickman("tired");
                    System.out.println("\n" + BOT_NAME + ":   " + RED + "Time's up! You wasted that try." + RESET);
                    continue;
                }

                guess = guess.toLowerCase();

                if (guess.equals("hint")) {
                    System.out.println(YELLOW + "Hint: " + hint + RESET);
                    attempt--; // don’t count as an attempt
                    continue;
                }

                if (guess.equals(originalWord)) {
                    showStickman("dance");
                    typeOut(BOT_NAME + ":   " + GREEN + "Nice! You got it!" + RESET);
                    score += 10;
                    guessedCorrectly = true;
                    break;
                } else {
                    typeOut(BOT_NAME + ":   " + RED + "nah, not it." + RESET);
                }
            }

            if (!guessedCorrectly) {
                showStickman("dead");
                typeOut(BOT_NAME + ":   " + RED + "Nope. The word was >>> " + originalWord.toUpperCase() + " <<<" + RESET);
                score -= 3;
            }

            // Rank output
            System.out.println(GREEN + ">>> Score: " + score + " | Rank: " + getRank(score) + " <<<" + RESET);

            System.out.print("Play again? (yes/no): ");
            playAgain = sc.nextLine().equalsIgnoreCase("yes");
        }

        typeOut(BOT_NAME + ":   Done already? Final Score: " + GREEN + score + RESET +
                " | Rank: " + getRank(score));
        showStickman("wave");
        typeOut("Respect. Peace out.");
    }

    // Function to scramble word
    private static String scrambleWord(String word) {
        List<Character> letters = new ArrayList<>();
        for (char c : word.toCharArray()) {
            letters.add(c);
        }
        Collections.shuffle(letters);
        StringBuilder sb = new StringBuilder();
        for (char c : letters) {
            sb.append(c);
        }
        return sb.toString();
    }

    // Function to assign ranks
    private static String getRank(int score) {
        if (score < 0) return "NPC";
        if (score < 20) return "Noob";
        if (score < 50) return "Casual";
        if (score < 80) return "Pro";
        return "God-Tier";
    }

    // Typing effect for bot messages
    private static void typeOut(String message) {
        for (char c : message.toCharArray()) {
            System.out.print(c);
            try { Thread.sleep(35); } catch (InterruptedException e) {}
        }
        System.out.println();
    }

    // Get user input with visible countdown
    private static String getUserInputWithCountdown(int seconds) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> sc.nextLine());

        for (int i = seconds; i > 0; i--) {
            if (future.isDone()) break;
            System.out.print("\rTime left: " + i + "s   "); // overwrite line
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
        System.out.print("\r                          \r"); // clear line after countdown

        try {
            return future.isDone() ? future.get() : null;
        } catch (Exception e) {
            return null;
        } finally {
            executor.shutdownNow();
        }
    }

    // Stickman mascot with reactions
    private static void showStickman(String action) {
        switch (action) {
            case "intro":
                System.out.println("   o/");
                System.out.println("  <| ");
                System.out.println("  / \\");
                System.out.println();
                System.out.println(" Bot v1.0");
                System.out.println(" Here when you need me.");
                break;
            case "wave":
                System.out.println(" o/");
                System.out.println("<| ");
                System.out.println("/ \\");
                break;
            case "dance":
                System.out.println(" \\o/");
                System.out.println("  | ");
                System.out.println(" / \\");
                break;
            case "dead":
                System.out.println(" x_x");
                System.out.println(" /|\\");
                System.out.println(" / \\");
                break;
            case "tired":
                System.out.println("(－‸ლ)");
                break;
        }
        System.out.println();
    }

    // Separate intro function
    private static void showIntro() {
        showStickman("intro");
        System.out.println();
    }
}
