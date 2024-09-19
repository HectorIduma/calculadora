package calculaexpreciones;

import java.util.*;

public class Calculaexpreciones {
    public static void main(String[] args) {
        String inputString;
        Scanner keyb = new Scanner(System.in);

        while (true) {
            System.out.println("Introduce una expresion en notacion infija:");
            System.out.print("> ");
            inputString = keyb.nextLine();
            if (inputString.equalsIgnoreCase("quit")) {
                break;
            }

            List<String> tokens = getTokens(inputString);
            System.out.println("Tokens infijos: " + toString(tokens));

            List<String> postfix = toPostfix(tokens);
            System.out.println("Notacion postfija: " + toString(postfix));

            double result = evaluatePostfix(postfix);
            System.out.println("Resultado: " + result);
        }
    }

    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
               token.equals("*") || token.equals("/") || token.equals("^");
    }

    public static boolean isOperand(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String toString(List<String> list) {
        StringBuilder output = new StringBuilder();
        for (String token : list) {
            output.append(token).append(" ");
        }
        return output.toString();
    }

    public static ArrayList<String> toPostfix(List<String> input) {
        Stack<String> stack = new Stack<>();
        ArrayList<String> output = new ArrayList<>();
        String t;

        for (String token : input) {
            if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!(t = stack.pop()).equals("(")) {
                    output.add(t);
                }
            } else if (isOperand(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && getPrec(stack.peek()) >= getPrec(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }
        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }
        return output;
    }

    public static int getPrec(String token) {
        switch (token) {
            case "^": return 3;
            case "*": 
            case "/": return 2;
            case "+": 
            case "-": return 1;
            default: return 0;
        }
    }

    public static List<String> getTokens(String input) {
        StringTokenizer st = new StringTokenizer(input, " ()+-*/^", true);
        ArrayList<String> tokenList = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (!token.isEmpty()) {
                tokenList.add(token);
            }
        }
        return tokenList;
    }

    public static double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isOperand(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                    case "^": stack.push(Math.pow(a, b)); break;
                }
            }
        }
        return stack.pop();
    }
}
