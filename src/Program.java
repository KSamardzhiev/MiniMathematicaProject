import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class Program {
	  // Метод за определяне на приоритета
    public int GetPrecedence(char oper)
    {
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

    // Метод за извършване на изчисленията
    public double CalculateExpression(char oper, Stack<Double> exp)
    {
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

    public Queue<Character> ShuntingYard(String input) // Използва се алгоритъма Shunting Yard, като в опашка се вкарват числата и операторите
    {
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
                    if (stack.capacity() == 0 || GetPrecedence(stack.peek()) < preced)
                    {
                        stack.push(symbol);
                        output.add(' ');
                    }
                    else
                    {
                        while (stack.capacity() > 0 && preced <= GetPrecedence(stack.peek()))
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
                        if (stack.capacity() > 0 && Character.isLetter(stack.peek()))
                        {
                            output.add(stack.pop());
                        }
                    }
                }
            }
        }
        while (stack.capacity() > 0)
        {
            output.add(stack.pop());
        }
        return output;
    }

   public Stack<Double> PolishNotation(Queue<Character> output) // Използва се алгоритъма Polish Notation, за да се извърши изчислението в готовата опашка
    {
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
}
