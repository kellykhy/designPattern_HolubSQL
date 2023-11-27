package mobile.shop.holub.sqlengine.value;

// tagging interface
public abstract class Value {

    public boolean getBooleanValue() {
        throw getException();
    }

    public double getNumericValue() {
        throw getException();
    }

    public String getStringValue() {
        throw getException();
    }

    private UnsupportedOperationException getException() {
        return new UnsupportedOperationException("");
    }
}
