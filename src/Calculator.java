import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


public class Calculator {

	public static List<Character> ArithmeticOperations = Arrays.asList('+','-','*','/'); 
	public static List<String> functions = Arrays.asList("ln", "pow", "sqrt","sin","cos","tg","cotg","root","log");
	
	public static String TrimInput(String input)
	{
		StringBuilder rezult = new StringBuilder();
		
		for(int i = 0 ; i < input.length(); i++)
		{
			if(input.charAt(i) != ' ')
			{
				rezult.append(input.charAt(i));
			}
		}
		
		return rezult.toString();	
	}

	public static List<String> SeparateTokens(String input)
	{
		LinkedList<String>  result = new LinkedList<String>();
		StringBuilder number = new StringBuilder();
		
		input = input.toLowerCase();
		
		for(int i = 0; i< input.length(); i++)
		{
			
			if(input.charAt(i) == '-' && (i == 0 || input.charAt(i-1) == ',' || input.charAt(i-1) == '('))
			{
				number.append("-");
			}
			else if(Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')
			{
				number.append(input.charAt(i));
			}
			else if(!Character.isDigit(input.charAt(i)) && input.charAt(i)!='.' && number.length()!=0)
			{
				result.add(number.toString());
				number.setLength(0);
				i--;
			}
			else if((i+1)<input.length() && input.charAt(i) == 'p' && input.charAt(i+1) == 'i')
			{
				double pi = 180;
				result.add(String.valueOf(pi));
			}
			else if(i<input.length() && input.charAt(i) == 'e')
			{
				result.add(String.valueOf(Math.E));
			}
			else if(input.charAt(i) =='(')
			{
				result.add("(");
			}
			else if(input.charAt(i) == ')')
			{
				result.add(")");
			}
			else if(ArithmeticOperations.contains(input.charAt(i)))
			{
				result.add(Character.toString(input.charAt(i)));
			}
			else if ((i+1)<input.length() && input.charAt(i) == 'l' && input.charAt(i+1) == 'n')
			{
				result.add("ln");
				i++;
			}
			else if ((i+2) < input.length() && input.charAt(i) == 'p' && input.charAt(i+1) == 'o' && input.charAt(i+2) == 'w')
			{
				result.add("pow");
				i+=2;
			}
			else if ((i+2) < input.length() && input.charAt(i) == 'l' && input.charAt(i+1) == 'o' && input.charAt(i+2) == 'g')
			{
				result.add("log");
				i+=2;
			}
			else if ((i+3) < input.length() && input.charAt(i) == 's' && input.charAt(i+1) == 'q' && input.charAt(i+2) == 'r' && input.charAt(i+3) == 't')
			{
				result.add("sqrt");
				i+=3;
			}
			else if ((i+1)<input.length() && input.charAt(i) == 't' && input.charAt(i+1) == 'g')
			{
				result.add("tg");
				i++;
			}
			else if ((i+2) < input.length() && input.charAt(i) == 's' && input.charAt(i+1) == 'i' && input.charAt(i+2) == 'n')
			{
				result.add("sin");
				i+=2;
			}
			else if ((i+2) < input.length() && input.charAt(i) == 'c' && input.charAt(i+1) == 'o' && input.charAt(i+2) == 's')
			{
				result.add("cos");
				i+=2;
			}
			else if ((i+3) < input.length() && input.charAt(i) == 'c' && input.charAt(i+1) == 'o' && input.charAt(i+2) == 't' && input.charAt(i+3) == 'g')
			{
				result.add("cotg");
				i+=3;
			}
			else if ((i+3) < input.length() && input.charAt(i) == 'r' && input.charAt(i+1) == 'o' && input.charAt(i+2) == 'o' && input.charAt(i+3) == 't')
			{
				result.add("root");
				i+=3;
			}
		}
		
		if(number.length() != 0)
		{
			result.add(number.toString());
		}
		
		return result;
	}
	
	public static boolean tryParseDouble(String input)
	{
		try  
	     {  
	        Double.parseDouble(input);  
	         return true;  
	      } catch(NumberFormatException nfe)  
	      {  
	          return false;  
	      }  
	}
	
	public static int Precedence(Character opr)
	{
		if(opr == '+' || opr == '-')
		{
			int low = 1;
			return low;
		}
		else 
		{
			int high = 2;
			return high;
		}
	}
	
	public static Queue<String> ConvertToRPN(List<String> tokens) throws Exception
	{
		Stack<String> stack = new Stack<String>();
		Queue<String> queue = new LinkedList<String>();
		
		for(int i = 0; i<tokens.size(); i++)
		{
			String currentToken = tokens.get(i);
			
			if(tryParseDouble(currentToken))
			{
				queue.add(currentToken);
			}
			else if(functions.contains(currentToken))
			{
				stack.push(currentToken);
			}
			else if(currentToken == ",")
			{
				if(!stack.contains("("))
				{
					throw new Exception("Invalid bracket");
				}
				
				while(stack.peek()!="(")
				{
					queue.add(stack.pop());
				}
			}
			else if(ArithmeticOperations.contains(currentToken.charAt(0)))
			{
				while(!stack.isEmpty() && ArithmeticOperations.contains(stack.peek().charAt(0)) && (Precedence(currentToken.charAt(0)) <= Precedence(stack.peek().charAt(0))))
				{
					queue.add(stack.pop());
				}
				stack.push(currentToken);
			}
			else if(currentToken == "(")
			{
				stack.push(currentToken);
			}
			else if(currentToken == ")")
			{
				if(!stack.contains("(") )
				{
					throw new Exception("Invalid brackets");
				}
				
				while(stack.peek()!="(")
				{
					queue.add(stack.pop());
				}
				stack.pop();
				
				if(!stack.isEmpty() && functions.contains(stack.peek()))
				{
					queue.add(stack.pop());
				}
			}
		}
		
		while(!stack.isEmpty())
		{
			queue.add(stack.pop());
		}
		
		return queue;	
	}
	
	public static double GetResult(Queue<String> queue) throws Exception
	{
		Stack<Double> stack = new Stack<Double>();
		
		while(!queue.isEmpty())
		{
			String currentToken = queue.poll();
			
			if(tryParseDouble(currentToken))
			{
				stack.push(Double.parseDouble(currentToken));
			}
			else if(ArithmeticOperations.contains(currentToken.charAt(0)) || functions.contains(currentToken))
			{
				if(currentToken.charAt(0) == '+')
				{
					if(stack.size() < 2)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double firstValue = stack.pop();
						double secondValue = stack.pop();
						double result = firstValue+secondValue;
						
						stack.push(result);
					}
				}
				else if(currentToken.charAt(0) == '-')
				{
					if(stack.size() < 2)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double firstValue = stack.pop();
						double secondValue = stack.pop();
						
						stack.push(secondValue - firstValue);
					}
				}
				else if(currentToken.charAt(0) == '*')
				{
					if(stack.size() < 2)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double firstValue = stack.pop();
						double secondValue = stack.pop();
						
						stack.push(firstValue*secondValue);
					}
				}
				else if(currentToken.charAt(0) == '/')
				{
					if(stack.size() < 2)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double firstValue = stack.pop();
						double secondValue = stack.pop();
						
						stack.push(secondValue/firstValue);
					}
				}
				else if(currentToken == "pow") // pow(number, power) 
				{
					if(stack.size() < 2)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double firstValue = stack.pop();
						double secondValue = stack.pop();
						
						stack.push(Math.pow(secondValue, firstValue));
					}
				}
				else if(currentToken == "sqrt")
				{
					if(stack.size() < 1)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double value = stack.pop();
						
						stack.push(Math.sqrt(value));
					}
				}
				else if(currentToken == "ln") // log(base E, number) / one argument
				{
					if(stack.size() < 1)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double value = stack.pop();
						
						stack.push(Math.log(value)); //base E
					}
				}
				else if(currentToken == "log") // log(base,number) / two arguments
				{
					if(stack.size() < 2)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double firstValue = stack.pop();
						double secondValue = stack.pop();
						
						stack.push(Math.log(firstValue)/Math.log(secondValue));
					}
				}
				else if(currentToken == "sin")
				{
					if(stack.size() < 1)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double value = stack.pop();
						if(Math.round(value % 1) * 100 == 0)
						{
							stack.push(Math.sin(Math.toRadians(value)));
						}
						else
						{
							stack.push(Math.sin(value));
						}				
					}
				}
				else if(currentToken == "cos")
				{
					if(stack.size() < 1)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double value = stack.pop();
						if(Math.round(value % 1) * 100 == 0)
						{
							stack.push(Math.cos(Math.toRadians(value)));
						}
						else
						{
							stack.push(Math.cos(value));
						}
					}
				}
				else if(currentToken == "tg")
				{
					if(stack.size() < 1)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double value = stack.pop();
						
						if(Math.round(value % 1) * 100 == 0)
						{
							stack.push(Math.tan(Math.toRadians(value)));
						}
						else
						{
							stack.push(Math.tan(value));
						}
					}
				}
				else if(currentToken == "cotg")
				{
					if(stack.size() < 1)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double value = stack.pop();
						
						if(Math.round(value % 1) * 100 == 0)
						{
							stack.push(1/Math.tan(Math.toRadians(value)));
						}
						else
						{
							stack.push(1/Math.tan(value));
						}
					}
				}
				else if(currentToken == "root")
				{
					if(stack.size() < 2)
					{
						throw new Exception("Invalid input");
					}
					else
					{
						double firstValue = stack.pop();
						double secondValue = stack.pop();
						
						stack.push(Math.pow(secondValue, 1/firstValue));
					}
				}
			}
		}
		
		if(stack.size() == 1)
		{
			return stack.pop();	
		}
		else
		{
			throw new Exception("Something is wrong!");
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		String expr = "5 + sin(pi)/pow(2,10) - log(e, pow(e, sqrt(4)))";

		System.out.println(TrimInput(expr));
		
		Queue<String> test = ConvertToRPN(SeparateTokens(TrimInput(expr)));
	
		System.out.println(GetResult(test));
		
	}

}
