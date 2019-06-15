package Sintatico;

public class SyntaticError extends AnalysisError {

    public SyntaticError(String msg, int position, int linha) {
        super(msg, position, linha);
    }

    public SyntaticError(String msg) {
        super(msg);
    }
}
