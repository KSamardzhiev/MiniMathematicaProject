import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class Test {

	public static void main(String[] args) {
		
		String input = "(3+5.3) * 2.7 - ln(22) / pow(2.2, -1.7)";
		String input1 = "";
        input = input1.replace(" ", "").replace("(-", "(0-").replace(",-", ",0-"); // добавя се 0, за да се избегнем проблема с отрицателните числа
        Queue<Character> output = new LinkedList<Character>();
        output = ShuntingYard(input1);
        double result = PolishNotation(output).pop();
        System.out.println(result);
	}

	private static Stack<Double> PolishNotation(Queue<Character> output) {
		Stack<Double> exp = new Stack<Double>();
        while (output.size() > 0)
        {
            char symbol = output.poll();
            if (Character.isDigit(symbol))
            {
                String num = "";
                while (Character.isDigit(symbol) || symbol == '.')
                {
                    num += symbol;
                    symbol = output.poll();
                }
                double number = Double.parseDouble(num);
                exp.push(number);
            }
            if (symbol != ' ' && symbol != ',')
            {
                exp.push(CalculateExpression(symbol, exp));
            }
           
        }
        return exp;
	}

	private static Double CalculateExpression(char oper, Stack<Double> exp) {
		 double expression = 0;
	        double lastNumber = exp.pop();
	        switch (oper)
	        {
	            case 's': expression = Math.sqrt(lastNumber); // изчислява корен квадратен
	                break;
	            case 'p': expression = Math.pow(exp.pop(), lastNumber); // изчислява повдигане на степен
	                break;
	            case 'l': expression = Math.log(lastNumber); // изчислява логаритъм
	                break;
	            case '*': expression = exp.pop() * lastNumber; // умножение
	                break;
	            case '/': expression = exp.pop() / lastNumber; // деление
	                break;
	            case '+': expression = exp.pop() + lastNumber; // събиране
	                break;
	            case '-': expression = exp.pop() - lastNumber; // изваждане
	                break;
	            default:
	                break;
	        }
	        return expression;
	}

	private static Queue<Character> ShuntingYard(String input) {
		Queue<Character> output = new LinkedList<Character>(); // опашка
        Stack<Character> stack = new Stack<Character>(); // стек за операторите
        for (int i = 0; i < input.length(); i++)
        {
            char symbol = input.charAt(i);
            if (symbol == '.' || symbol == ',' || Character.isDigit(symbol))
            {
                output.add(symbol);
            }
            else
            {
                int preced = GetPrecedence(symbol);
                if (preced > 0)
                {
                    if (stack.isEmpty() || GetPrecedence(stack.peek()) < preced)
                    {
                        stack.push(symbol);
                        output.add(' ');
                    }
                    else
                    {
                        while (!stack.isEmpty() && preced <= GetPrecedence(stack.peek()))
                        {
                            output.add(stack.pop());
                        }
                        stack.push(symbol);
                    }
                }
                else
                {
                    if (symbol == '(')
                    {
                        stack.push(symbol);
                    }
                    else if (symbol == ')')
                    {
                        while (stack.peek() != '(')
                        {
                            output.add(stack.pop());
                        }
                        stack.pop();
                        if (!stack.isEmpty())
                        {
                        	if(Character.isLetter(stack.peek()))
                        	{
                            output.add(stack.pop());
                        	}
                        }
                    }
                }
            }
        }
        while (!stack.isEmpty())
        {
            output.add(stack.pop());
        }
        return output;
	}

	private static int GetPrecedence(char oper) {
		switch (oper)
        {
            case 's':
            case 'p':
            case 'l':
                return 3;
            case '*':
            case '/':
                return 2;
            case '+':
            case '-':
                return 1;
            default:
                break;
        }
        return 0;
	}
}
