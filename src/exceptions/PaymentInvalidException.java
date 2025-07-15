package exceptions;

public class PaymentInvalidException extends RuntimeException {
    public PaymentInvalidException(String method){
        super("O método de pagamento '" + method + "' é inválido!");
    }
}
