package Sintatico;

public class Token {

    private int id;
    private String lexeme;
    private int position;
    private int linha;

    public Token(int id, String lexeme, int position, int linha) {
        this.id = id;
        this.lexeme = lexeme;
        this.position = position;
        this.linha = linha;
    }

    public final int getId() {
        return id;
    }

    public int getLinha() {
        return linha;
    }

    public final String getLexeme() {
        return lexeme;
    }

    public final int getPosition() {
        return position;
    }

    public String toString() {
        return id + " ( " + lexeme + " ) @ " + position + ". Linha: " + linha;
    }
;
}
