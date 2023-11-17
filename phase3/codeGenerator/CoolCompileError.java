package codeGenerator;

public class CoolCompileError extends Exception {
    private String message;

    public CoolCompileError(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
