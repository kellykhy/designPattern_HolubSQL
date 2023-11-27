package mobile.shop.holub.sqlengine.value;

import java.text.NumberFormat;

public class NumericValue extends Value {
    private double value;

    public NumericValue(double value)    // initialize from a double.
    {
        this.value = value;
    }

    public NumericValue(String s) throws java.text.ParseException {
        this.value = NumberFormat.getInstance().parse(s).doubleValue();
    }


    @Override
    public double getNumericValue() {
        return value;
    }


    public String toString() // round down if the fraction is very small
    {
        if (Math.abs(value - Math.floor(value)) < 1.0E-20) {
            return String.valueOf((long) value);
        } else {
            return String.valueOf(value);
        }
    }
}
