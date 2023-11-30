package mobile.shop.holub.sqlengine.value;

public class BooleanValue extends Value {
    boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }


    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean getBooleanValue() {
        return value;
    }
}
