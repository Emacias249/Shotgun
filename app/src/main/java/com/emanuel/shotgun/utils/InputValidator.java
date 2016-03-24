package com.emanuel.shotgun.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Daniel on 3/23/2016.
 */
public class InputValidator {
    public static boolean isValidString(String input) {
        Pattern pattern = Pattern.compile("[\\w\\s\\-]+");
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }
}
