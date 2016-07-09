# OpCalc
A JavaFX order-of-operations calculator for arithmetic expressions. 

![OpCalc Image](/OpCalc Image.png?raw=true)

My first original JavaFX program. Built over the span of a weekend in March 2016 after spending two weeks with Java,
OpCalc is a robust calculator with support for a very large magnitude of values for solving basic arithmetic expressions.
It supports addition, subtraction, multiplication, and division, along with parentheses for proper evaluation of expressions. It utilizes the BigDecimal class for boxing numbers, so working with very large integers and decimals is not a problem. 

OpCalc essentially uses an implementation of the shunting-yard algorithm (infix-to-postfix conversion) in order to both display to the user the proper order of operations for arithmetic expressions and the correct result. It does not aim to be an extensive calculator, but merely a simple, useful tool for arithmetic expressions and for visualizing the problem solving process in real time.

Read more about it [here](https://teamtreehouse.com/community/community-code-challenge-build-your-own-calculator-app-with-the-java-skills-youve-learned).

# Usage tip

To use negative numbers in your calculations, wrap your number with a parenthesis and subtract it to 0:

-5 -> (0-5)
