import java.util.*;

public class MoodBot {
    private Map<String, List<String>> commonResponses;
    private Map<String, List<String>> happyResponses;
    private Map<String, List<String>> neutralResponses;
    private Map<String, List<String>> hypeResponses;
    private Scanner scanner;
    private Random random;
    private String face;
    private String mood;

    // ANSI color codes
    private final String RESET = "\u001B[0m";
    private final String GREEN = "\u001B[32m";
    private final String YELLOW = "\u001B[33m";
    private final String CYAN = "\u001B[36m";

    public MoodBot(String moodChoice) {
        scanner = new Scanner(System.in);
        random = new Random();
        mood = moodChoice;

        // Set face and color based on mood
        switch (moodChoice) {
            case "1": face = "(^_^)"; break; // Chill / Happy
            case "2": face = "(¬_¬)"; break; // Sarcastic / Neutral
            case "3": face = "(•_•)"; break; // Hype / Excited
            default: face = "(¬_¬)";
        }

        // Initialize responses
        initResponses();
    }

    private void initResponses() {
        // Common responses
        commonResponses = new HashMap<>();
        commonResponses.put("hello", Arrays.asList(
                "Hey! Ready to level up your code?", 
                "Yo! Coding mode ON!", 
                "Sup coder, let's go!"
        ));
        commonResponses.put("hi", Arrays.asList(
                "Hi there! Let's talk code.", 
                "Hey! You look debug-ready.", 
                "Yo! Wanna solve some logic?"
        ));
        commonResponses.put("bye", Arrays.asList(
                "Catch you later!", 
                "Peace out, coder!", 
                "See you! Keep looping."
        ));
        commonResponses.put("who are you", Arrays.asList(
                "I am MoodBot, your coding buddy.", 
                "MoodBot at your service, let's debug life!", 
                "Your terminal coding sidekick!"
        ));
        commonResponses.put("tell me a joke", Arrays.asList(
                "Why do programmers prefer dark mode? Light attracts bugs.",
                "SQL walks into a bar, asks to join 2 tables.",
                "Why do Java devs wear glasses? Because they can't C#.",
                "Recursion joke? You’d have to call me to get it.",
                "Debugging: being detective AND villain at once."
        ));

        // Happy personality
        happyResponses = new HashMap<>();
        happyResponses.put("how are you", Arrays.asList(
                "Feeling great! Just squashed a bug monster.",
                "Happy as a coder who finally fixed that loop!",
                "Doing awesome! Just learned a new shortcut."
        ));
        happyResponses.put("error", Arrays.asList(
                "No worries. Errors are just stepping stones. Show me the stack trace.",
                "Another bug? Let's crush it together!",
                "Debug mode: activated. Fear no exception!"
        ));
        happyResponses.put("loop", Arrays.asList(
                "Loops are like anime filler arcs, they keep coming back!",
                "Loop life: infinite possibilities!",
                "Just another iteration in the code of life."
        ));
        happyResponses.put("challenge", Arrays.asList(
                "Mini challenge: Write a loop to print 'AnimeFan' 3 times in Java.",
                "Quick coding test: How do you repeat 'CodeIsLife' twice using a loop?"
        ));

        // Neutral/Sarcastic personality
        neutralResponses = new HashMap<>();
        neutralResponses.put("how are you", Arrays.asList(
                "Better than your last compile. How about you?",
                "Surviving exception after exception, like always.",
                "You know, just existing in the terminal."
        ));
        neutralResponses.put("error", Arrays.asList(
                "Classic. Another exception. Originality at its finest.",
                "Error? Again? Surprise!",
                "Welcome to coding life, where errors are friends."
        ));
        neutralResponses.put("loop", Arrays.asList(
                "Loops forever. How original.",
                "Ah yes, another infinite adventure.",
                "Iteration is life's cruel joke."
        ));
        neutralResponses.put("challenge", Arrays.asList(
                "Fine. What's wrong with 'int x = 5/0;'?",
                "Here's a brain teaser: what does this snippet print?"
        ));

        // Hype/Excited personality
        hypeResponses = new HashMap<>();
        hypeResponses.put("how are you", Arrays.asList(
                "Pumped and ready to smash bugs!",
                "Feeling electric! Let's code fast!",
                "All systems go! Debug mode ON!"
        ));
        hypeResponses.put("error", Arrays.asList(
                "Bug detected! Engage all brain cells!",
                "Error incoming! Let’s tackle it head-on!",
                "Whoa, exception spotted! Activate hype mode!"
        ));
        hypeResponses.put("loop", Arrays.asList(
                "Loops? Let's iterate like a coding hero!",
                "Power up! Another loop incoming!",
                "Iteration level: MAX!"
        ));
        hypeResponses.put("challenge", Arrays.asList(
                "Challenge mode: print 'MoodBot' 5 times using a loop!",
                "Let's do a rapid coding test! Print numbers 1-5."
        ));
    }

    public void start() {
        String color = getMoodColor();
        System.out.println(color + "\nMoodBot is online. Choose an option to chat. Type 7 to exit.\n" + RESET);

        while (true) {
            printMenu();
            System.out.print(face + " You: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("7")) {
                System.out.println(color + face + " MoodBot: Alright, coder. Catch you next time!" + RESET);
                break;
            }

            handleChoice(choice, color);
        }
    }

    private void printMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Say Hello / Hi");
        System.out.println("2. Ask How I’m doing");
        System.out.println("3. Hear a Joke");
        System.out.println("4. Talk about Errors");
        System.out.println("5. Talk about Loops");
        System.out.println("6. Take a Mini Coding Challenge");
        System.out.println("7. Exit");
    }

    private void handleChoice(String choice, String color) {
        switch (choice) {
            case "1": respondRandom("hello", color); break;
            case "2": respondRandom("how are you", color); break;
            case "3": respondRandom("tell me a joke", color); break;
            case "4": respondRandom("error", color); break;
            case "5": respondRandom("loop", color); break;
            case "6": miniCodingQuiz(color); break;
            default: System.out.println(color + face + " MoodBot: I didn't get that. Choose 1-7." + RESET);
        }
    }

    private void respondRandom(String key, String color) {
        List<String> responses;
        switch (mood) {
            case "1": responses = happyResponses.getOrDefault(key, commonResponses.get(key)); break;
            case "2": responses = neutralResponses.getOrDefault(key, commonResponses.get(key)); break;
            case "3": responses = hypeResponses.getOrDefault(key, commonResponses.get(key)); break;
            default: responses = commonResponses.get(key);
        }
        String reply = responses.get(random.nextInt(responses.size()));
        System.out.println(color + face + " MoodBot: " + reply + RESET);
    }

    private void miniCodingQuiz(String color) {
        System.out.println(color + face + " MoodBot: Ready for a mini coding quiz?" + RESET);
        System.out.println("1. Yes\n2. No");
        String ans = scanner.nextLine().trim();
        if(ans.equals("1")) {
            switch(mood) {
                case "1": System.out.println(color + "Write a loop to print 'AnimeFan' 3 times in Java." + RESET); break;
                case "2": System.out.println(color + "What happens with 'int x = 5/0;' in Java?" + RESET); break;
                case "3": System.out.println(color + "Write a loop to print 'MoodBot' 5 times quickly." + RESET); break;
            }
            scanner.nextLine(); // wait for user input to continue
            System.out.println(color + face + " MoodBot: Got it! Keep practicing.\n" + RESET);
        } else {
            System.out.println(color + face + " MoodBot: Alright, maybe next time!" + RESET);
        }
    }

    private String getMoodColor() {
        switch (mood) {
            case "1": return GREEN;
            case "2": return YELLOW;
            case "3": return CYAN;
            default: return RESET;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to MoodBot! Pick your coding vibe:");
        System.out.println("1. Chill / Happy (^_^)");
        System.out.println("2. Sarcastic / Neutral (¬_¬)");
        System.out.println("3. Hype / Excited (•_•)");
        System.out.print("Enter choice (1/2/3): ");
        String mood = scanner.nextLine().trim();

        MoodBot bot = new MoodBot(mood);
        bot.start();
    }
}
