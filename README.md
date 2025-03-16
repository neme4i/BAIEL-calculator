Project Overview
This project implements a command-line calculator in Java that processes mathematical expressions with support for basic arithmetic operations, nested brackets, and mathematical functions. The calculator maintains a history of past calculations and provides a simple text-based interface for user interaction.
Design Choices
Architecture

Object-Oriented Approach: The program uses a class-based design with methods organized by functionality.
Console-Based Interface: Simple text input/output for accessibility and ease of use.
Recursive Expression Evaluation: The calculator evaluates complex expressions by breaking them down recursively.

Core Components

Main Class: Serves as both the entry point and the calculator implementation.
User Interface: Handles input/output through the console.
Expression Parser: Breaks down and evaluates mathematical expressions.
History Management: Tracks and displays calculation history.

Algorithms and Data Structures
Data Structures

ArrayList (pastCalculations): Stores the history of calculations.
StringBuilder: Used for parsing and manipulating expression strings.
Lists for Numbers and Operators: Temporarily store operands and operators during calculation.

Algorithms

Expression Evaluation: Follows mathematical order of operations (PEMDAS):

Evaluate functions (power, sqrt, abs, round)
Resolve parentheses/brackets
Perform multiplication and division
Perform addition and subtraction


Function Handling:

Uses recursive bracket matching to find function arguments
Supports nested functions and complex expressions as arguments


Parentheses Resolution:

Uses a recursive approach to find the innermost brackets first
Resolves brackets from inside out



Implementation Challenges

Operator Precedence: Ensuring correct order of operations (PEMDAS) required careful implementation of multiple parsing passes.
Negative Numbers: Special handling was needed to differentiate between subtraction operations and negative number indicators.
Nested Functions/Brackets: Finding matching brackets in nested expressions required tracking bracket count.
Error Handling: The calculator implements exception handling to provide meaningful error messages for invalid inputs.

Input/Output Approach

The calculator uses standard console I/O through the Scanner class rather than file-based input/output.
User input is collected through the command line.
Results and history are displayed directly in the console.

Potential Improvements

Code Separation: The current implementation could benefit from splitting into multiple classes (e.g., separate Calculator, Parser, and UI classes).
Additional Functions: The calculator could be extended with more mathematical functions.
Validation: More robust input validation could be implemented.
Performance Optimization: The parsing algorithm could be optimized for handling very complex expressions.
Variable Support: Adding support for variables would enhance functionality.
![image](https://github.com/user-attachments/assets/059bdad0-7381-4cbb-b413-72a2772d9217)
