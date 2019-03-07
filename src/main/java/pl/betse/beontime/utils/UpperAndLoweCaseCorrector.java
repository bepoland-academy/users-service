package pl.betse.beontime.utils;

public class UpperAndLoweCaseCorrector {

    public static String fix(String toCorrect) {
        return toCorrect.substring(0, 1).toUpperCase() + toCorrect.substring(1).toLowerCase();
    }
}
