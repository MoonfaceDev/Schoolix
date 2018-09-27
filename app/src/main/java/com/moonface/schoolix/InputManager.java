package com.moonface.schoolix;

class InputManager {
    public enum ValidType{Letters, Digits, LettersAndNumbers}

    static boolean isValid(String string, ValidType type, char... additional_chars) {
        char[] chars = string.toCharArray();
        for (char c : chars) {
            switch (type) {
                case Letters:
                    if (!Character.isLetter(c) && !Character.isSpaceChar(c) && isValid(c, additional_chars)) {
                        return false;
                    }
                    break;
                case Digits:
                    if (!Character.isDigit(c) && !Character.isSpaceChar(c) && isValid(c, additional_chars)) {
                        return false;
                    }
                    break;
                case LettersAndNumbers:
                    if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c) && isValid(c, additional_chars)) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    static boolean isValid(char character, char[] validChars) {
        boolean valid = false;
        for (char v : validChars) {
            if (character == v) {
                valid = true;
            }
        }
        return valid;
    }
}
