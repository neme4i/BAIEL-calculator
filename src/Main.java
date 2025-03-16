import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private List<String> pastCalculations;
    private Scanner userInput;

    public Main() {
        pastCalculations = new ArrayList<>();
        userInput = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main calc = new Main();
        calc.start();
    }

    public void start() {
        System.out.println("Welcome to the Calculator!");

        boolean keepGoing = true;
        while (keepGoing) {
            try {
                System.out.print("Please enter your math problem: ");
                String problem = userInput.nextLine().trim();

                if (problem.equalsIgnoreCase("history")) {
                    showPastCalculations();
                    continue;
                }

                double answer = solve(problem);
                System.out.println("Result: " + answer);

                pastCalculations.add(problem + " = " + answer);

                System.out.print("Do you want to solve another problem? (y/n): ");
                String choice = userInput.nextLine().trim().toLowerCase();
                keepGoing = choice.equals("y") || choice.equals("yes");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Thank you for using the Calculator!");
        userInput.close();
    }

    private void showPastCalculations() {
        if (pastCalculations.isEmpty()) {
            System.out.println("No calculations yet.");
            return;
        }

        System.out.println("--- Your Past Calculations ---");
        for (int i = 0; i < pastCalculations.size(); i++) {
            System.out.println((i + 1) + ". " + pastCalculations.get(i));
        }
        System.out.println("------------------------");
    }

    private double solve(String problem) {
        problem = problem.replaceAll("\\s+", "");

        problem = handleAllFunctions(problem);

        return doBasicMath(problem);
    }

    private String handleAllFunctions(String problem) {
        problem = handleOneFunction(problem, "power");
        problem = handleOneFunction(problem, "sqrt");
        problem = handleOneFunction(problem, "abs");
        problem = handleOneFunction(problem, "round");

        return problem;
    }

    private String handleOneFunction(String problem, String funcName) {
        while (problem.contains(funcName + "(")) {
            int start = problem.indexOf(funcName + "(");
            int end = findEndBracket(problem, start + funcName.length());

            if (end == -1) {
                throw new IllegalArgumentException("Missing closing bracket for " + funcName);
            }

            String fullFunction = problem.substring(start, end + 1);
            double result = calculateFunction(fullFunction);

            problem = problem.substring(0, start) + result + problem.substring(end + 1);
        }
        return problem;
    }

    private int findEndBracket(String text, int openPos) {
        int count = 1;
        for (int i = openPos + 1; i < text.length(); i++) {
            if (text.charAt(i) == '(') {
                count++;
            } else if (text.charAt(i) == ')') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private double calculateFunction(String fullFunction) {
        if (fullFunction.startsWith("power(")) {
            String inside = fullFunction.substring(6, fullFunction.length() - 1);
            String[] parts = inside.split(",");

            if (parts.length != 2) {
                throw new IllegalArgumentException("power needs two numbers");
            }

            double num1 = solve(parts[0]);
            double num2 = solve(parts[1]);

            return Math.pow(num1, num2);
        }
        else if (fullFunction.startsWith("sqrt(")) {
            String inside = fullFunction.substring(5, fullFunction.length() - 1);
            double num = solve(inside);

            if (num < 0) {
                throw new IllegalArgumentException("Can't take square root of negative number");
            }

            return Math.sqrt(num);
        }
        else if (fullFunction.startsWith("abs(")) {
            String inside = fullFunction.substring(4, fullFunction.length() - 1);
            double num = solve(inside);
            return Math.abs(num);
        }
        else if (fullFunction.startsWith("round(")) {
            String inside = fullFunction.substring(6, fullFunction.length() - 1);
            double num = solve(inside);
            return Math.round(num);
        }

        throw new IllegalArgumentException("Unknown function: " + fullFunction);
    }

    private double doBasicMath(String problem) {
        problem = solveBrackets(problem);
        problem = solveMultiplyAndDivide(problem);
        return solveAddAndSubtract(problem);
    }

    private String solveBrackets(String problem) {
        while (problem.contains("(")) {
            int start = problem.lastIndexOf("(");
            int end = problem.indexOf(")", start);

            if (end == -1) {
                throw new IllegalArgumentException("Missing closing bracket");
            }

            String insideBrackets = problem.substring(start + 1, end);
            double result = doBasicMath(insideBrackets);

            problem = problem.substring(0, start) + result + problem.substring(end + 1);
        }
        return problem;
    }

    private String solveMultiplyAndDivide(String problem) {
        if (problem.startsWith("-")) {
            problem = "0" + problem;
        }

        problem = problem.replace("--", "+");

        List<Double> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();

        StringBuilder currentNum = new StringBuilder();
        for (int i = 0; i < problem.length(); i++) {
            char c = problem.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                currentNum.append(c);
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                if (currentNum.length() > 0) {
                    numbers.add(Double.parseDouble(currentNum.toString()));
                    currentNum = new StringBuilder();
                }

                if (c == '-' && (i == 0 || "+-*/%)".indexOf(problem.charAt(i-1)) >= 0)) {
                    currentNum.append(c);
                } else {
                    operators.add(c);
                }
            }
        }

        if (currentNum.length() > 0) {
            numbers.add(Double.parseDouble(currentNum.toString()));
        }

        for (int i = 0; i < operators.size(); i++) {
            char op = operators.get(i);

            if (op == '*' || op == '/' || op == '%') {
                double left = numbers.get(i);
                double right = numbers.get(i + 1);
                double result;

                if (op == '*') {
                    result = left * right;
                } else if (op == '/') {
                    if (right == 0) {
                        throw new ArithmeticException("Can't divide by zero");
                    }
                    result = left / right;
                } else {
                    if (right == 0) {
                        throw new ArithmeticException("Can't do modulo by zero");
                    }
                    result = left % right;
                }

                numbers.set(i, result);
                numbers.remove(i + 1);
                operators.remove(i);
                i--;
            }
        }

        StringBuilder newProblem = new StringBuilder();
        newProblem.append(numbers.get(0));

        for (int i = 0; i < operators.size(); i++) {
            newProblem.append(operators.get(i));
            newProblem.append(numbers.get(i + 1));
        }

        return newProblem.toString();
    }

    private double solveAddAndSubtract(String problem) {
        if (problem.startsWith("-")) {
            problem = "0" + problem;
        }

        problem = problem.replace("--", "+");

        List<Double> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();

        StringBuilder currentNum = new StringBuilder();
        for (int i = 0; i < problem.length(); i++) {
            char c = problem.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                currentNum.append(c);
            } else if (c == '+' || c == '-') {
                if (currentNum.length() > 0) {
                    numbers.add(Double.parseDouble(currentNum.toString()));
                    currentNum = new StringBuilder();
                }

                if (c == '-' && (i == 0 || problem.charAt(i-1) == '+' || problem.charAt(i-1) == '-')) {
                    currentNum.append(c);
                } else {
                    operators.add(c);
                }
            }
        }

        if (currentNum.length() > 0) {
            numbers.add(Double.parseDouble(currentNum.toString()));
        }

        double result = numbers.get(0);
        for (int i = 0; i < operators.size(); i++) {
            char op = operators.get(i);
            double nextNum = numbers.get(i + 1);

            if (op == '+') {
                result += nextNum;
            } else if (op == '-') {
                result -= nextNum;
            }
        }

        return result;
    }
}