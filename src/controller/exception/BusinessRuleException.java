package controller.exception;

public class BusinessRuleException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;
    
    public BusinessRuleException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
