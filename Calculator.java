import java.util.Scanner;

public class Calculator {

    String input; // string that represents the first thing the user inputs
    String input2; // string that represents the number for the operation

    Number value; // number that the first string is converted to, also the result of all operations is set to this field.
    Number value2; // number that comes from the second input string
    boolean changedSign;

    public Calculator() {
        value = new Number("0");
        value2 = new Number("0");
        changedSign = false;
    }

    public static void main(String[] args) {

        Calculator calc = new Calculator();
        Scanner s = new Scanner(System.in);
        int count = 0;

        calc.printCalculatorMenu();
        boolean stop = false;

        while(true) {

            try {
                if(count > 0) {
                    System.out.println("-> " + calc.value.toString());
                }
                System.out.print("-> ");
                String command = s.next();

                switch(command.toLowerCase()) {

                    case "q":
                        stop = true;
                        break; // if the user chooses quit
                    case "e": // if the user chooses to enter a new value
                        calc.printCalculatorEnter();
                        calc.input = s.next();
                        calc.value = new Number(calc.input);
                        calc.printCurrentCalculator();
                        break;
                    case "h": // the help command, displays the options menu
                        calc.printCalculatorMenu();
                        break;
                    case "a": // prompts the user to enter a new value to add
                        calc.addCalc();
                        break;
                    case "s": // prompts the user to enter a new value to subtract
                        calc.subtractCalc();
                        break;
                    case "m": // prompts the user to enter a new value to multiply
                        calc.multiplyCalc();
                        break;
                    case "c": // clears the calculator, sets value to 0
                        calc.value = new Number("0");
                        calc.value2 = new Number("0");
                        calc.changedSign = false;
                        calc.printCalculatorClear();
                        break;
                    case "r": // reverses the sign of the value, unless the value is 0.
                        if(!(calc.value.compareTo(new Number("0")) == 0)) {
                            calc.value.reverseSign();
                        }
                        calc.printCurrentCalculator();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                if(stop) {
                    break;
                }
                count++;
            }
            catch(IllegalArgumentException i) { // catches any invalid input
                calc.printCalculatorError();
            }
        }
    }

    // handles all the gross input-y output-y stuff for the addition operation
    public void addCalc() {
        printCalculatorEnter();
        Scanner s = new Scanner(System.in);
        input2 = s.next();
        Number copy = new Number(value.toString());
        value2 = new Number(input2);
        if(value2.negative) {
            changedSign = true;
        }
        Number copyValue = new Number(value.toString());
        value = copyValue.add(value2);
        printCalculatorAdd(copy, value2, value);
    }

    // handles all the gross input-y output-y stuff for the subtraction operation
    public void subtractCalc() {
        printCalculatorEnter();
        Scanner s = new Scanner(System.in);
        input2 = s.next();
        Number copy = new Number(value.toString());
        value2 = new Number(input2);
        Number copyValue = new Number(value.toString());
        value = copyValue.subtract(value2);
        if(value2.negative) {
            value2.reverseSign();
            changedSign = true;
        }
        printCalculatorSubtract(copy, value2, value);
    }

    // handles all the gross input-y output-y stuff for the multiplication operation
    public void multiplyCalc() {

        printCalculatorEnter();
        Scanner s = new Scanner(System.in);
        input2 = s.next();
        Number copy = new Number(value.toString());
        value2 = new Number(input2);
        Number copyValue = new Number(value.toString());
        value = copyValue.multiply(value2);
        printCalculatorMultiply(copy, value2, value);

    }

    // prints the initial menu for the calculator
    public void printCalculatorMenu() {

        printTopScreen();
        System.out.println("|   | Enter a value: e         Add: a                  |   |");
        System.out.println("|   | Subtract: s              Multiply: m             |   |");
        System.out.println("|   | Reverse sign: r          Clear: c                |   |");
        System.out.println("|   | Quit: q                  Help: h                 |   |");
        System.out.println("|   |__________________________________________________|   |");
        printBottomHalf();


    }

    // prints the calculator that displays an error
    // when the user enters an invalid number
    public void printCalculatorError() {

        printTopScreen();
        System.out.println("|   | Sorry, you have entered something that is not a  |   |");
        System.out.println("|   | valid number. Please try again.                  |   |");
        printBottomScreen();
        printBottomHalf();
    }

    // prints the calculator with value's current number
    public void printCurrentCalculator() {

        printTopScreen();
        printValueOnly(value.toString());
        System.out.println("|   |                                                  |   |");
        printBottomScreen();
        printBottomHalf();
    }

    // prints the calculator that asks
    // for the user to enter a number
    public void printCalculatorEnter() {

        printTopScreen();
        System.out.println("|   |  Enter a number, please.                         |   |");
        System.out.println("|   |                                                  |   |");
        printBottomScreen();
        printBottomHalf();
    }

    // prints the calculator after addition
    public void printCalculatorAdd(Number copy, Number value2, Number value) {

        printTopScreen();
        printEquations("a", copy.toString(), value2.toString(), value.toString());
        printBottomScreen();
        printBottomHalf();

    }

    // prints the calculator after subtraction
    public void printCalculatorSubtract(Number copy, Number value2, Number value) {

        printTopScreen();
        printEquations("s", copy.toString(), value2.toString(), value.toString());
        printBottomScreen();
        printBottomHalf();

    }

    // prints the calculator after clearing
    public void printCalculatorClear() {

        printTopScreen();
        System.out.println("|   |  All clear!                                      |   |");
        System.out.println("|   |  Value has been set to 0.                        |   |");
        printBottomScreen();
        printBottomHalf();
    }

    // prints the calculator after multiplication
    public void printCalculatorMultiply(Number copy, Number value2, Number value) {

        printTopScreen();
        printEquations("m", copy.toString(), value2.toString(), value.toString());
        printBottomScreen();
        printBottomHalf();

    }

    // prints the top half of the top screen of the calculator
    public void printTopScreen() {

        System.out.println(" __________________________________________________________");
        System.out.println("|                                                          |");
        System.out.println("|  ASCIILATOR-5000                                         |");
        System.out.println("|                                                          |");
        System.out.println("|    __________________________________________________    |");
        System.out.println("|   |                                                  |   |");

    }

    public void printBottomScreen() {

        System.out.println("|   | (For help, type 'h')                             |   |");
        System.out.println("|   |                                                  |   |");
        System.out.println("|   |__________________________________________________|   |");

    }

    // prints the bottom half of the calculator
    // which is always the same
    public void printBottomHalf() {

        System.out.println("|                                                          |");
        System.out.println("|    --------------------------------------------------    |");
        System.out.println("|                                                          |");
        System.out.println("|                                                          |");
        System.out.println("|        __________      __________      __________        |");
        System.out.println("|       |          |    |          |    |          |       |");
        System.out.println("|       |    7     |    |     8    |    |    9     |       |");
        System.out.println("|       |__________|    |__________|    |__________|       |");
        System.out.println("|                                                          |");
        System.out.println("|                                                          |");
        System.out.println("|        __________      __________      __________        |");
        System.out.println("|       |          |    |          |    |          |       |");
        System.out.println("|       |    4     |    |     5    |    |    6     |       |");
        System.out.println("|       |__________|    |__________|    |__________|       |");
        System.out.println("|                                                          |");
        System.out.println("|                                                          |");
        System.out.println("|        __________      __________      __________        |");
        System.out.println("|       |          |    |          |    |          |       |");
        System.out.println("|       |    1     |    |    2     |    |     3    |       |");
        System.out.println("|       |__________|    |__________|    |__________|       |");
        System.out.println("|                                                          |");
        System.out.println("|                                                          |");
        System.out.println("|        __________      __________      __________        |");
        System.out.println("|       |          |    |          |    |          |       |");
        System.out.println("|       |    0     |    |     .    |    |   ( - )  |       |");
        System.out.println("|       |__________|    |__________|    |__________|       |");
        System.out.println("|                                                          |");
        System.out.println("|                                                          |");
        System.out.println("|                                                          |");
        System.out.println("|__________________________________________________________|");
    }


    // calculates the spaces for the methods that require
    // two lines of calculations: the operation and the new value
    public void printEquations(String command, String copy,  String value2, String value) {
        String operator = setOperator(command, changedSign);
        int line1 = command.length() + copy.length() + value2.length();
        int line2 = value.length();
        int numSpaces1 = 49 - line1;
        int numSpaces2 = 49 - line2;
        String spaces1 = makeSpaces(numSpaces1);
        String spaces2 = makeSpaces(numSpaces2);
        System.out.println("|   | " + copy + operator + value2 + spaces1 + "|   |" );
        System.out.println("|   | " + spaces2 + value + "|   |");

    }

    public String makeSpaces(int numSpaces) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < numSpaces; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    // helper that printCalculatedSpaces() uses to find out which operator to display
    // returns the operator as a String that can be appended to the line
    public String setOperator(String command, boolean changedSign) {
        String operator;

        switch (command) {

            case "m":
                operator = "*";
                break;
            case "a":
                if(changedSign) {
                    operator = "-";
                }
                else {
                    operator = "+";
                }
                break;
            case "s":
                if(changedSign) {
                    operator = "+";
                }
                else {
                    operator = "-";
                }
                break;
            default:
                operator = " ";
                break;
        }
        return operator;
    }

    // calculates the spacing for the calculators that only
    // require one line of display, such as displaying
    // the current number after a new one is entered
    public void printValueOnly(String value) {
        int lines = value.length();
        int lineSpaces = 49 - lines;
        String spaces = "";
        for(int i = 0; i < lineSpaces; i++) {
            spaces += " ";
        }
        System.out.println("|   | " + value + spaces + "|   |");
    }

}