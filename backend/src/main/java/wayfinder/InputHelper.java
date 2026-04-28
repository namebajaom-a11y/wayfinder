package wayfinder;

import java.util.Scanner;

public final class InputHelper {

    private static final Scanner SCANNER = new Scanner(System.in);

    private InputHelper() {
    }

    public static Scanner scanner() {
        return SCANNER;
    }
}
