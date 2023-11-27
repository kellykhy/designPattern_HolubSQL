package mobile.shop.holub.sqlengine.value;

public class StringValue extends Value {
    private String value;

    public StringValue(String lexeme) {
        value = lexeme.replaceAll("['\"](.*?)['\"]", "$1");
    }

    @Override
    public String getStringValue() {
        return value;
    }

    public String toString() {
        return value;
    }
}