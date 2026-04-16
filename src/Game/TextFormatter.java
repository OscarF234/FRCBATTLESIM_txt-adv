package Game;

public class TextFormatter {

    private static final String TITLE_DIVIDER = "==================================================";
    private static final String SECTION_DIVIDER = "--------------------------------------------------";

    public static void printTitle(String title) {
        System.out.println();
        System.out.println(TITLE_DIVIDER);
        System.out.println(" " + title);
        System.out.println(TITLE_DIVIDER);
    }

    public static void printSection(String title) {
        System.out.println();
        System.out.println(SECTION_DIVIDER);
        System.out.println(" " + title);
        System.out.println(SECTION_DIVIDER);
    }

    public static void printMenu(String title, String... options) {
        System.out.println(title);

        for (String option : options) {
            System.out.println("  " + option);
        }
    }

    public static void printPrompt(String prompt) {
        System.out.print(prompt + " ");
    }

    public static void printInfo(String message) {
        System.out.println("> " + message);
    }

    public static void printWarning(String message) {
        System.out.println("! " + message);
    }

    public static void printSuccess(String message) {
        System.out.println("* " + message);
    }

    public static void printBattleStatus(String enemyLabel, int playerHealth, int enemyHealth) {
        System.out.println("ALT-F4 HP : " + playerHealth);
        System.out.println(enemyLabel + " HP : " + enemyHealth);
    }
}
