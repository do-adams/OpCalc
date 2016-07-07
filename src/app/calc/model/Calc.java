package app.calc.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


/**
 * Created by Damian on 3/11/2016.
 */
public class Calc {
    private char[] mValidInput;
    //Stores a validated form of the input in character form.
    private List mInfix;
    //Stores a validated form of the input infix expression in number and character form.
    private List mPostfix;
    //Stores the result of the postfix conversion.
    private Stack<Character> mCharStack;
    //Character stack used by toPostfix().
    private Stack<BigDecimal> mNumStack;
    //Number stack used by getResult().
    private List<String> mOperations;
    //Retains the operations required to solve the infix expression so they can be displayed to the user.

    public Calc(String input) throws IllegalArgumentException {
        mValidInput = process(validate(input));
        toList();
        toPostfix();
    }
    //Constructor
    //Takes an input string as argument, generates valid input, infix, and postfix data and stores it.

    private char[] validate(String input) throws IllegalArgumentException {
        //In case of empty input
        if (input.length() == 0)
            throw new IllegalArgumentException("Please enter valid input");

        input = input.trim();
        //Removes newline and whitespace characters at the end and beginning of the input expression.
        input = input.replace("\r\n", " ").replace("\n", " ");
        //Replaces newline characters with whitespace (platform-independent solution).
        input = removeElem(input, " ");
        //Removes whitespace in-between.

        if (input.charAt(0) == ('-')) //In the case that the first number in the input is negative.
            input = "0" + input.substring(0, input.length());

        inCaseOfSingleNum(input);

        char[] infix = input.toCharArray();
        initializeOperationsList(input);

        char previous = '0';
        int counter = 0;
        int parenthesisOne = 0, parenthesisTwo = 0;
        for (char ch : infix) { //Runs a variety of checks on user input (ugh, you don't even want to know).
            if (ch == '(' || ch == ')') {
                if (ch == '(')
                    parenthesisOne++;
                if (ch == ')')
                    parenthesisTwo++;
            }

            if (!Character.isDigit(infix[0]) && infix[0] != '(')
                throw new IllegalArgumentException("Incorrect input: cannot start an expression with " + infix[0]);

            if (!Character.isDigit(ch) && ch != '*' && ch != '/' && ch != '+' && ch != '-'
                    && ch != '(' && ch != ')' && ch != '.')
                throw new IllegalArgumentException("Incorrect character: " + ch);

            if (ch == '(' && Character.isDigit(previous) && counter != 0)
                throw new IllegalArgumentException("Incorrect sequence of parentheses.");

            if (Character.isDigit(ch) && previous == ')' && counter != 0)
                throw new IllegalArgumentException("Incorrect sequence of parentheses.");

            if ((ch == '(' && previous == ')') || (ch == ')' && previous == '('))
                throw new IllegalArgumentException("Incorrect sequence of parentheses.");

            if (!Character.isDigit(ch) && !Character.isDigit(previous)
                    && ch != '(' && ch != ')' && previous != '(' && previous != ')')
                throw new IllegalArgumentException("Incorrect sequence of operators: " + previous + ch);

            if (!Character.isDigit(infix[infix.length - 1]) && infix[infix.length - 1] != ')')
                throw new IllegalArgumentException("Incorrect input: cannot end an expression with " + infix[infix.length - 1]);
            previous = ch;
            counter++;
        }
        if (parenthesisOne != parenthesisTwo)
            throw new IllegalArgumentException("Invalid amount of parentheses.");

        return infix;
    }
    //Validates the input string; removing whitespace, and evaluating key conditions to generate a valid infix expression.
    //Cleans up the user's input expression and passes it to initializeOperationsList().

    private String removeElem(String input, String elem) {
        String[] remove = input.split(elem);
        input = "";
        for (String element : remove)
            input += element;
        return input;
    }
    //Removes a string element from an input string and returns the cleansed string.

    private void inCaseOfSingleNum(String input) throws IllegalArgumentException {
        char[] infix = input.toCharArray();
        boolean areThereChars = false;
        for (char ch : infix) //Checks to see if the input string was just one single number.
            if (!Character.isDigit(ch) && ch != '.' && ch != '(' && ch != ')')
                areThereChars = true;

        if (!areThereChars) {
            input = input.replace("(", "");
            input = input.replace(")", "");
            int decimalCount = 0;
            for (char ch : infix) {
                if (ch == '.')
                    decimalCount++;
                if (decimalCount > 1)
                    throw new IllegalArgumentException("Invalid decimal number.");
            }
            throw new IllegalArgumentException(input);
        }
    }
    //Determines if the input string/expression was just one single number.
    //If it is a single number, removes parentheses (if there are) from single input number.
    //It then prints out the input number to the screen.

    private void initializeOperationsList(String input) {
        mOperations = new ArrayList<String>();
        mOperations.add(input + System.getProperty("line.separator")
                + System.getProperty("line.separator"));
        mOperations.add("Order of operations:" + System.getProperty("line.separator"));
    }
    //Initializes the operations list with the starting expression inputted by the user.

    private char[] process(char[] infix) {
        infix = Arrays.copyOf(infix, infix.length + 1);
        infix[infix.length - 1] = ')';
        return infix;
    }
    //Processes the input char array, adding a parenthesis at the end according to the infix-postfix algorithm.

    private void toList() throws IllegalArgumentException {
        mInfix = new ArrayList();
        String numString = "";
        BigDecimal number;
        int decimalCount = 0;
        for (char ch : mValidInput) {
            if (Character.isDigit(ch) || ch == '.') {
                //Checks in case that a number has more than one period character (invalid)
                numString += ch;
                if (ch == '.')
                    decimalCount++;
                if (decimalCount > 1)
                    throw new IllegalArgumentException("Invalid decimal number.");
            } else {
                if (numString != "") {
                    number = new BigDecimal(numString);
                    mInfix.add(number);
                    decimalCount = 0;
                }
                mInfix.add(ch);
                numString = "";
            }
        }
    }
    //Parses the elements of the valid input char array, and stores them as number and character elements in an object list.

    private void toPostfix() throws IllegalArgumentException {
        mPostfix = new ArrayList();
        mCharStack = new Stack<Character>();
        mCharStack.push('(');
        for (Object obj : mInfix) {
            if (obj instanceof BigDecimal)
                mPostfix.add(obj);
            else if (obj.equals('('))
                mCharStack.add((Character) obj);
            else if (obj.equals(')') && mCharStack.peek() != '(') {
                do {
                    mPostfix.add(mCharStack.pop());
                } while (mCharStack.peek() != '(');
                mCharStack.pop();
            } else if (obj.equals(')') && mCharStack.peek() == '(') {
                mCharStack.pop();
            } else if (!(obj instanceof BigDecimal)) {
                if (Character.isDigit(mCharStack.peek()))
                    mCharStack.push((Character) obj);
                else {
                    while (evaluateOperators((Character) obj, mCharStack.peek()))
                        mPostfix.add(mCharStack.pop());
                    mCharStack.push((Character) obj);
                }
            }
        }
    }
    //Converts the infix expression into a postfix expression and stores it in mPostfix.
    //Uses methods evaluateOperators and operatorScore.

    private boolean evaluateOperators(char op1, char op2) throws IllegalArgumentException {
        return operatorScore(op1) <= operatorScore(op2);
    }
    //Uses the operatorScore of two arithmetic operators to determine if the second operator is greater than the first.

    private int operatorScore(char op) throws IllegalArgumentException {
        switch (op) {
            case '*':
                return 2;
            case '/':
                return 2;
            case '+':
                return 1;
            case '-':
                return 1;
            case ')':
                return 0;
            case '(':
                return 0;
            default:
                throw new IllegalArgumentException("Error evaluating precedence of stack operators: " + op
                        + " Current postfix expression: " + mPostfix + " Current stack values: " + mCharStack);
        }
    }
    //Assigns and returns a score to each arithmetic operator, so that their precedence may be compared easily.

    public BigDecimal getResult() throws IllegalArgumentException {
        BigDecimal last, nextToLast, product, scaleOfNum;
        List<Integer> scaleOfNums = new ArrayList<Integer>(); //List of the scale of the numbers in our operations.
        mNumStack = new Stack<BigDecimal>();

        for (Object obj : mPostfix) {
            if (obj instanceof BigDecimal) {
                mNumStack.push((BigDecimal) obj);
                scaleOfNum = (BigDecimal) obj;

                if (!scaleOfNum.equals(new BigDecimal(0)))
                    scaleOfNums.add(scaleOfNum.scale());
            } else {
                last = mNumStack.pop();
                nextToLast = mNumStack.pop();
                product = evaluateArithmetic((Character) obj, nextToLast, last);

                mOperations.add(nextToLast + " " + (Character) obj + " " +
                        last + " = " + product + System.getProperty("line.separator"));
                mNumStack.push(product);
            }
        }
        int scaleResult = scaleOfNums.get(scaleOfNums.indexOf(Collections.min(scaleOfNums))) + 1;
        //Determines the number of significant units of the result.
        return mNumStack.pop().setScale(scaleResult, RoundingMode.HALF_UP);
        //Scale = # of decimal units
        //Precision = # of significant units
    }
    //Calculates the result of the input generated by our class constructor.
    //Converts our postfix expression into a result.
    //Uses method evaluateArithmetic.
    //Passes String values to list mOperations.

    private BigDecimal evaluateArithmetic(char op, BigDecimal num1, BigDecimal num2) throws IllegalArgumentException {
        switch (op) {
            case '*':
                return num1.multiply(num2, MathContext.DECIMAL64);
            case '/':
                if (num2.equals(new BigDecimal(0)))
                    throw new IllegalArgumentException("Division by zero is forbidden.");
                return num1.divide(num2, 16, RoundingMode.HALF_UP);
            case '+':
                return num1.add(num2, MathContext.DECIMAL64);
            case '-':
                return num1.subtract(num2, MathContext.DECIMAL64);
            default:
                throw new IllegalArgumentException("Error evaluating arithmetic operation: " + num1 + op + num2);
        }
    }

    //Determines what mathematical operation to execute based on the operator passed as argument.
    //Uses two numbers as input.
    public String getOperations() {
        String operationStatement = "";
        for (String str : mOperations)
            operationStatement += str;
        return operationStatement;
    }
    //Returns a formatted string containing all of the operations performed by this program in solving the infix expression.
}