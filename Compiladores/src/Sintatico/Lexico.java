package Sintatico;

import Sintatico.Constants;
import Sintatico.LexicalError;
import static Sintatico.ScannerConstants.SCANNER_ERROR;
import static Sintatico.ScannerConstants.SCANNER_TABLE;
import static Sintatico.ScannerConstants.SCANNER_TABLE_INDEXES;
import static Sintatico.ScannerConstants.SPECIAL_CASES_INDEXES;
import static Sintatico.ScannerConstants.SPECIAL_CASES_KEYS;
import static Sintatico.ScannerConstants.SPECIAL_CASES_VALUES;
import static Sintatico.ScannerConstants.TOKEN_STATE;
import Sintatico.Token;

public class Lexico implements Constants {

    private int position;
    private int linha;
    private String input;

    public Lexico() {
        this(new java.io.StringReader(""));
    }

    public Lexico(java.io.Reader input) {
        setInput(input);
        linha = 1;
    }

    private String trataMensagem(String prMsg, char t) {
        return prMsg.replace("%x", "" + t);
    }

    public void setInput(java.io.Reader input) {
        StringBuffer bfr = new StringBuffer();
        try {
            int c = input.read();
            while (c != -1) {
                bfr.append((char) c);
                c = input.read();
            }
            this.input = bfr.toString();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        setPosition(0);
    }

    public void setPosition(int pos) {
        position = pos;
    }

    public String getClassName(int classe) {
        switch (classe) {
            case 2:
                return "identificador";
            case 3:
                return "constante inteira";
            case 4:
                return "constante real";
            case 5:
                return "constante string";
            case 6:
                return "constante char";
            default:
                if (classe >= 7 && classe <= 28) {
                    return "palavra reservada";
                } else {
                    return "símbolo especial";
                }
        }
    }

    public Token nextToken() throws LexicalError {
        if (!hasInput()) {
            return null;
        }

        int start = position;

        int state = 0;
        int lastState = 0;
        int endState = -1;
        int end = -1;
        int l = linha;

        while (hasInput()) {
            lastState = state;
            char c = nextChar();
            state = nextState(c, state);

            if (state < 0) {
                break;
            } else {
                if (c == '\n') {
                    l++;
                }
                if (tokenForState(state) >= 0) {
                    endState = state;
                    end = position;
                }
            }
        }
        if (endState < 0 || (endState != state && tokenForState(lastState) == -2)) {
            throw new LexicalError(trataMensagem(SCANNER_ERROR[lastState], input.charAt(position - 1)), start, linha);
        }

        position = end;

        int token = tokenForState(endState);

        linha = l;
        if (token == 0) {
            return nextToken();
        } else {
            String lexeme = input.substring(start, end);
            token = lookupToken(token, lexeme);
            return new Token(token, lexeme, start, linha);
        }
    }

    private int nextState(char c, int state) {
        int start = SCANNER_TABLE_INDEXES[state];
        int end = SCANNER_TABLE_INDEXES[state + 1] - 1;

        while (start <= end) {
            int half = (start + end) / 2;

            if (SCANNER_TABLE[half][0] == c) {
                return SCANNER_TABLE[half][1];
            } else if (SCANNER_TABLE[half][0] < c) {
                start = half + 1;
            } else //(SCANNER_TABLE[half][0] > c)
            {
                end = half - 1;
            }
        }

        return -1;
    }

    private int tokenForState(int state) {
        if (state < 0 || state >= TOKEN_STATE.length) {
            return -1;
        }

        return TOKEN_STATE[state];
    }

    public int lookupToken(int base, String key) {
        int start = SPECIAL_CASES_INDEXES[base];
        int end = SPECIAL_CASES_INDEXES[base + 1] - 1;

        while (start <= end) {
            int half = (start + end) / 2;
            int comp = SPECIAL_CASES_KEYS[half].compareTo(key);

            if (comp == 0) {
                return SPECIAL_CASES_VALUES[half];
            } else if (comp < 0) {
                start = half + 1;
            } else //(comp > 0)
            {
                end = half - 1;
            }
        }

        return base;
    }

    private boolean hasInput() {
        return position < input.length();
    }

    private char nextChar() {
        if (hasInput()) {
            return input.charAt(position++);
        } else {
            return (char) -1;
        }
    }
}