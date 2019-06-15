package Sintatico;

public class AnalysisError extends Exception {

    private int position;
    private int linha;

    public AnalysisError(String msg, int position, int linha) {
        super(msg);
        this.position = position;
        this.linha = linha;
    }

    public AnalysisError(String msg) {
        super(msg);
        this.position = -1;
    }

    public int getPosition() {
        return position;
    }

    public String toString() {
        return super.toString() + ", @ " + position;
    }

    public int getLinha() {
        return linha;
    }
}
