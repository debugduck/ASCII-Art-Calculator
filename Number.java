import java.util.*;

public class Number implements Comparable<Number> {
    protected DigitList number; // linked list that holds the digits
    protected int digitCount = 0; // total number of digits in the number
    protected int decimalPlaces = 0; //number of digits after decimal place
    protected boolean negative = false; // sign (positive or negative)

    // Constructor
    Number() {
        number = new DigitList();

    }

    // constructor used within the class, mainly when you need
    // to make a copy of a Number and make changes to the
    // Number without actually changing the original
    // (for example, when calling compareToAbsolute() with a negative Number
    private Number(DigitList number, int digitCount, int decimalPlaces, boolean negative) {
        this.number = number;
        this.digitCount = digitCount;
        this.decimalPlaces = decimalPlaces;
        this.negative = negative;
    }

    // Constructor that takes a String representation of a number
    // (e.g. "-21.507"). Calls method accept().
    public Number(String str) {
        accept(str);
        if(str.charAt(0) == '-') {
            negative = true;
        }
    }

    // Builds a DigitList representation of the number represented
    // by str.
    public void accept(String str) {
        validate(str);
        int result;
        number = new DigitList();
        boolean afterDecimal = false;
        int decimalCount = 0;
        int digits = 0;
        for(int i = 0; i < str.length(); i++) {
            char t = str.charAt(i);
            if(i > 0 && str.length() > 1) {
                if(str.charAt(i - 1) == '.') {
                    afterDecimal = true;
                }
            }
            if(afterDecimal) decimalCount++;
            if (t != '.' && t != '-') {
                digits++;
                result = Character.getNumericValue(t);
                number.addLast(result);
            }
        }
        if(!str.contains(".")) {
            decimalPlaces = 0;
        }
        this.decimalPlaces = decimalCount;
        this.digitCount = digits;
    }

    // helper method to find the number
    // of digits.
    private int findDigitCount(String str) {
        int count = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) != '.' && str.charAt(i) != '-') {
                count++;
            }
        }
        return count;
    }

    // helper method, called by other methods
    // in order to make the digit counts equal
    // so that the Numbers can be added or
    // subtracted
    private void alignDigits(Number n) {
        int frontDigitsN = n.digitCount - n.decimalPlaces;
        int frontDigitsThis = this.digitCount - this.decimalPlaces;

        if(frontDigitsN > frontDigitsThis) {
            addFrontZeros(this, frontDigitsN - frontDigitsThis);
        }
        else if(frontDigitsThis > frontDigitsN) {
            addFrontZeros(n, frontDigitsThis - frontDigitsN);
        }

        if(n.decimalPlaces > this.decimalPlaces) {
            addTrailingZeros(this, n.decimalPlaces - this.decimalPlaces);
        }
        else if(this.decimalPlaces > n.decimalPlaces) {
            addTrailingZeros(n, this.decimalPlaces - n.decimalPlaces);
        }
    }

    // helper method to alignDigits(n)
    // adds zeros to the front of a Number
    private void addFrontZeros(Number n, int count) {
        for(int i = 0; i < count; i++) {
            n.number.addFirst(0);
            n.digitCount++;
        }
    }

    // helper method to alignDigits(n)
    // adds trailing zeros to a Number
    private void addTrailingZeros(Number n, int count) {
        for(int i = 0; i < count; i++) {
            n.number.addLast(0);
            n.digitCount++;
            n.decimalPlaces++;
        }
    }

    // Returns a Number that represents "this + n".
    // Target Complexity: O(n)
    public Number add(Number n) {
        Number sum = new Number();
        this.alignDigits(n);
        if(!this.negative && !n.negative) {
            sum = this.addAbsolute(n);
        }
        else if((!this.negative && n.negative) || (this.negative && !n.negative)) {
            if(this.negative) {
                this.reverseSign();
                sum = this.subtract(n);
                sum.reverseSign();
            }
            else {
                n.reverseSign();
                sum = this.subtract(n);
            }
        }
        else if(this.negative && n.negative) {
            n.reverseSign();
            sum = this.subtract(n);
        }
        this.adjustDecimals(n, sum);
        if(sum.number.sumDigits() == 0) {
            sum.negative = false;
        }
        if(sum.digitCount > 1) {
            sum.trim();
        }
        this.trim();
        n.trim();
        return sum;
    }

    // Returns a Number that represents "this - n".
    // Target Complexity: O(n)
    public Number subtract(Number n) {
        this.alignDigits(n);
        Number result = new Number();
        if(!negative && !n.negative) {
            if(this.compareToAbsolute(n) < 0) {
                result = n.subtractAbsolute(this);
                result.reverseSign();
            }
            else {
                result = this.subtractAbsolute(n);
            }
        }
        else if(!negative && n.negative) {
            result = this.addAbsolute(n);
        }
        else if(negative && !n.negative) {
            result = n.addAbsolute(this);
            result.reverseSign();
        }
        else if(negative && n.negative) {
            n.reverseSign();
            result = this.add(n);
            //result.reverseSign();
        }
        this.adjustDecimals(n, result);
        if(result.number.sumDigits() == 0) {
            result.negative = false;
        }
        if(result.digitCount > 1) {
            result.trim();
        }
        this.trim();
        n.trim();
        return result;
    }

    // helper function that adjusts the decimal places
    // after some kind of operation is performed
    // on the Numbers
    private void adjustDecimals(Number n, Number result) {
        String str = result.toString();
        result.digitCount = findDigitCount(str);
        if(this.decimalPlaces > n.decimalPlaces) {
            result.decimalPlaces = this.decimalPlaces;
        }
        else {
            result.decimalPlaces = n.decimalPlaces;
        }
        if(this.decimalPlaces == 0 && n.decimalPlaces == 0) {
            result.decimalPlaces = 0;
        }
    }

    // Returns a Number that represents "this * n".
    // Target Complexity for multiplication of an n digit number and an m
    // digit number: O(m x n)
    public Number multiply(Number n) {
        Number product = new Number("0");
        Node nCurrent = n.number.high;
        while(nCurrent != null) {
            Number partialProduct = new Number();
            int carry = 0;
            Node thisCurrent = this.number.low;
            while(thisCurrent != null) {
                int newDigit = (nCurrent.data * thisCurrent.data) + carry;
                carry = newDigit / 10;
                newDigit %= 10;
                partialProduct.number.addFirst(newDigit);
                partialProduct.digitCount++;
                thisCurrent = thisCurrent.prev;
            }
            if(carry != 0) {
                partialProduct.number.addFirst(carry);
                partialProduct.digitCount++;
            }
            product = product.add(partialProduct);
            product.number.addLast(0);
            product.digitCount++;
            nCurrent = nCurrent.next;
        }
        product.decimalPlaces = this.decimalPlaces + n.decimalPlaces;
        Number thisNum = this;
        product.setNegative(thisNum, n);
        product.decimalPlaces++;
        this.trim();
        n.trim();
        product.trim();
        return product;
    }

    // helper function that determines the sign
    // of the product after multiplying two Numbers
    private void setNegative(Number thisNum, Number n) {
        if(thisNum.negative == n.negative) {
            this.negative = false;
        }
        else {
            this.negative = true;
        }
        if(this.compareToAbsolute(new Number("0")) == 0) {
            this.negative = false;
        }
    }

    //Reverses the value of negative.
    public void reverseSign() {
        negative = !negative;
    }

    // Implements compareTo of Comparable
    public int compareTo(Number n) {
        if(negative && !n.negative) {
            return -1;
        }
        else if(!negative && n.negative) {
            return 1;
        }
        else {
            return compareNumbers(n);
        }
    }

    // helper function that compares
    // the Number's digits to see
    // if one is less than the other
    // or if they are equal
    private int compareNumbers(Number n) {
        this.alignDigits(n);
        ListIterator nItr = n.number.listIterator();
        ListIterator thisItr = this.number.listIterator();
        int nItrNext = 0;
        int thisItrNext = 0;
        while(thisItr.hasNext()) {
            nItrNext = (int) nItr.next();
            thisItrNext = (int) thisItr.next();
            int resultComparison = this.compareDigits(thisItrNext, nItrNext, n);
            if(resultComparison != 0) {
                return resultComparison;
            }
        }
        nItrNext = (int) nItr.next();
        thisItrNext = (int) thisItr.next();
        return this.compareDigits(thisItrNext, nItrNext, n);
    }

    // helper function that compares
    // individual digits of a Number
    private int compareDigits(int thisItrNext, int nItrNext, Number n) {
        if( thisItrNext > nItrNext) {
            if(negative && n.negative) {
                return -1;
            }
            else {
                return 1;
            }
        }
        else if( thisItrNext < nItrNext) {
            if (negative && n.negative) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }

    //These methods perform operations disregarding the signs (i.e. the
    //negative field) of the Numbers. See the descriptions below.
    // Target Complexity: O(n)
    private Number addAbsolute(Number n) {
        Number sum = new Number();
        int carry = 0;
        int newDigit;
        Node nCurrent = n.number.low;
        Node thisCurrent = this.number.low;
        while(nCurrent != null) {
            newDigit = nCurrent.data + thisCurrent.data + carry;
            if(newDigit >= 10) {
                newDigit = newDigit % 10;
                carry = 1;
            }
            else {
                carry = 0;
            }
            sum.number.addFirst(newDigit);
            sum.digitCount++;
            nCurrent = nCurrent.prev;
            thisCurrent = thisCurrent.prev;
        }
        if(carry != 0) {
            sum.number.addFirst(carry);
            sum.digitCount++;
        }
        return sum;
    }

    // Target Complexity: O(n)
    private Number subtractAbsolute(Number n) {
        Number result = new Number();
        int borrow = 0;
        int newDigit;
        Node nCurrent = n.number.low;
        Node thisCurrent = this.number.low;
        while(nCurrent != null) {
            newDigit = thisCurrent.data - nCurrent.data;
            newDigit -= borrow;
            if(newDigit < 0) {
                newDigit += 10;
                borrow = 1;
            }
            else {
                borrow = 0;
            }
            result.number.addFirst(newDigit);
            result.digitCount++;
            nCurrent = nCurrent.prev;
            thisCurrent = thisCurrent.prev;
        }
        if(borrow != 0) {
            result.number.addFirst(borrow);
            result.digitCount++;
        }
        return result;
    }

    // May call method compareTo()
    // Target Complexity: O(n)
    private int compareToAbsolute(Number n) {
        Number copyN = new Number(n.number, n.digitCount, n.decimalPlaces, false);
        Number copyThis = new Number(this.number, this.digitCount, this.decimalPlaces, false);
        return copyThis.compareTo(copyN);
    }

    // Returns a String representation of the Number.
    // Calls toString() of number.
    public String toString() {
        String digits = this.number.toString();
        int difference = digitCount - decimalPlaces;
        StringBuilder sb = new StringBuilder();
        if(negative) {
            sb.insert(0, "-");
        }
        for(int i = 0; i < digits.length(); i++) {
            if(i == difference && decimalPlaces != 0) {
                sb.append('.');
            }
            sb.append(digits.charAt(i));
        }
        return sb.toString();
    }

    // Removes all extra leading 0s which precede the decimal point
    // and all extra trailing 0s which follow the decimal point.
    public void trim() {
        int countLow = decimalPlaces;
        int countHigh = digitCount - decimalPlaces;
        int i = 1;
        Node highPtr = this.number.high;
        Node lowPtr = this.number.low;
        while(highPtr.data == 0 && i < countHigh) {
            this.number.removeFirst();
            digitCount--;
            i++;
            highPtr = highPtr.next;
        }
        if(countHigh == 1 && highPtr.data == 0) {
            this.number.removeFirst();
            digitCount--;
        }
        if(decimalPlaces > 0) {
            while (lowPtr.data == 0 && countLow != 0) {
                this.number.removeLast();
                digitCount--;
                decimalPlaces--;
                lowPtr = lowPtr.prev;
                countLow--;
            }
        }
        if(this.number.size() == 0) {
            number.addFirst(0);
        }
    }

    // Assures that the string passed to accept() or the constructor
    // represents a valid number.
    // Throws IllegalArgumentException if str is not valid.
    public void validate (String str) {
        int decimalFound = 0;
        if(str.length() == 0) {
            throw new IllegalArgumentException();
        }
        if(str.length() == 1 && ((int) str.charAt(0) < 48 || (int) str.charAt(0) > 57 )) {
            throw new IllegalArgumentException();
        }
        if(str.contains("/") || str.contains(",")) {
            throw new IllegalArgumentException();
        }
        for(int i = 0; i < str.length(); i++) {
            char t = str.charAt(i);
            if(i == 0 && ((int) t < 45 || (int) t > 57)) {
                throw new IllegalArgumentException();
            }
            if(t == '.') {
                decimalFound++;
            }
            if(decimalFound > 1) {
                throw new IllegalArgumentException();
            }
            if(i == str.length() - 1 && ((int) t < 48 || (int) t > 57)) {
                throw new IllegalArgumentException();
            }
        }
    }

}