package Sintatico;


public class SemanticError extends AnalysisError {

    public SemanticError(String msg, int position, int linha) {
        super(msg, position, linha);
    }

    public SemanticError(String msg) {
        super(msg);
    }
}