# Parser

## Fields

- lines - Array of all the lines in the file
- lineIndex - Current line index
- currentLine - Processed line info
- COMMAND_LOOKUP - HashMap with known commands and their type
- COMMAND_TYPE - Command types available

## Methods

- hasMoreLines()


- advance()
    1. Check that there are more lines
    2. Increment lineIndex
    3. Trim, and then split line by spaces
    4. Lookup command type from HashMap
    5. Make an array with the arguments of the command
    6. Initialise instance of Line with the two things above and command name
    7. Set to currentLine


- commandType()


- arg1()
    1. If the commandType is C_ARITHMETIC:
       1. Return current line's command
    2. Else if the commandType is C_RETURN:
       1. Raise exception
    3. Otherwise:
       1. Return current line's getArg with index 0

- arg2()
  1. Declare allowed commands array (C_PUSH, C_POP, C_FUNCTION, C_CALL)
  2. If in allowed commands
     1. Return current line's getArg with index 1
  3. Otherwise, raise exception





## How to represent the line?

1. Array with all the line components?
2. Class with the line info

Option 2 is better because the array can only hold data in a single 
data type, so storing a constant for the command type is not possible, for 
example, and it would have to be worked out each time you call `commandType`.

## Line subclass

### Fields

- commandType - Command type
- command
- args - Arguments for command

### Methods

- getCommandType()
- getCommand()
- getArg(int index)

## Other functionalities

**We must implement a check to ensure the label is within that function.**
Implementing a check to ensure the label exists and that only the function
which contains the label can access it could be done with a check to see if you
go up or down along the code lines, the label declaration comes before a function
declaration.  
For that, you could have a Hashmap with the function's declaration location
and the label's declaration location. Although for that, a first and second
pass would be necessary.  
The next option would be to "peek" at the next or
previous lines and see which comes first. For something with hundreds of
lines, that would take a long time.  
Or, we could make a HashMap with the functions and label declarations and
their instruction numbers. If it hasn't been declared yet, we add it to a
queue. When the Parser finishes, before closing it we check in the queue
whether it has been declared and passes the requirement - if not, we throw
an error, terminate the program and delete the file. This ensures we have a 

## TEST 

1. Iterate through all the lines
2. Display index and whether there are more lines
3. Display their info in the following format:

LINE: Raw line  
COMMAND TYPE: Line's command type  
COMMAND: Line's command  
ARGUMENT 1: Argument 1 (displays None if exception thrown)  
ARGUMENT 2: Argument 2 (displays None if exception thrown)  

4. Terminate when there are no more lines

*Note: Argument 1 is the same as command when line type is C_ARITHMETIC*


# CodeWriter

## Commands and corresponding Assembly code

- push (segment) (index) 

Push and pop vary for each memory segment. In general, you'll work out the 
address, store it in the D register, then increase the stack pointer. 
All pieces of code below detail how the address is worked out and the 
value is stored in the D register. At the end, we have the code to truly 
push it on the stack and increase the stack pointer.

For local, argument, this and that:
```
// Work out address of 'segment index'
@segment
D=M
@index
D=D+A
// Go to 'segment index' and store its value on D register
A=D
D=M
```

For pointer:
```
// if it's pointer 0
@THIS
D=M
// if it's pointer 1
@THAT
D=M
```

For temp:
```
// A check must be made first to ensure 0 <= index <= 7
@5
D=A
@index
D=D+A
A=D
D=M
```

For constant:
```
@index
D=A
```

For static:
```
@filename.index
D=M
```

Ending for all of these pieces of code:
```
// Store D register's value on top of stack
@SP
A=M
M=D
// Increase stack pointer
@SP
M=M+1
```

- pop segment index

Not valid when constant is the segment.

For local, argument, temp, this and that
```
// Work out address of 'segment index'
@segment
D=M
@index
D=D+A
// Store it in R13 as a temporary variable
@R13
M=D
// Get top of stack on D register
@SP
M=M-1
A=M
D=M
M=0
// Go to adress of 'segment' index
@R13
A=M
// Set it to the D register's value
M=D
```

For static and pointer
```
// Get top of stack on D register
@SP
M=M-1
A=M
D=M
M=0
// Go to adress
// is THIS and THAT for pointer 0 and pointer 1, respectively.
@filename.index
// Set it to the D register's value
M=D
```

- add, sub, and, or
```
// Pop top of stack into R13
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
// Pop top of stack into D register
@SP
M=M-1
A=M
D=M
M=0
// Do operation D op R13 and store on D register
@R13
D=D(op)M
// Store it on top of the stack
@SP
A=M
M=D
// Increase stack pointer
@SP
M=M+1
```

- neg and not
```
// Pop top of stack into D register
@SP
M=M-1
A=M
D=M
M=0
// Do operation on D register's value
D=(! or -)D
// Store it back on the stack
M=D
// Increase stack pointer
@SP
M=M+1
```

- eq, gt, lt
```
// Pop top of stack into R13
@SP
M=M-1
A=M
D=M
M=0
@R13
M=D
// Pop top of stack into D register
@SP
M=M-1
A=M
D=M
M=0
// Do operation D - R13 and store on D register
@R13
D=D-M
// If it's more than 0, then we jump to the code that sets RAM[SP] to -1
// x is the index for that comparison (each time we make a new comparison, 
we have to generate this code again. To not cause confusion and make it jump 
into a completely different part of the program, we must make an index each time 
we generate it)
@push_true_x
D;J(GT, LT, or EQ)
@SP
A=M
M=0
@continue_x
0;JMP
(push_true_x)
@SP
A=M
M=-1
(continue_x)
// Increase stack pointer
@SP
M=M+1
```

In theory, you could have a bunch of code after the infinite loop which 
would be labelled elementary routines, such as setting the top of the stack to 
-1, 0 or the D register's value. When these routines are finished, you'd then 
have to jump back to the program from where you left off.  
They would be after the infinite loop as they would be inaccessible when 
the program ends.

At the time of writing, it was considered a potentially dangerous thing to 
do as the computer or other program might treat the infinite loop as the end 
of the code, not the end of the elementary routines section which is after 
the infinite loop.

- label (labelName)
```
(filename.functionName$labelName)
```

- goto (labelName)
```
@filename.functionName$labelName
0;JMP
```

- if-goto (labelName)
```
// Pop top of stack into D register
@SP
M=M-1
A=M
D=M
M=0
@filename.functionName$labelName
D;JNE
```

- function (f) (nVars)
```
(filename.f)
// Counter for number of iterations
@R13
M=0
// Store nVars in R14
@nVars
D=A
@R14
M=D
// LOOP
(filename.f.setup)
[code equaivalent to push constant 0]
@R13
M=M+1
D=M
@R14
D=D-M
@filename.f.setup
D;JLT
```

- call (f) (nArgs)
```
// Push return address
@filename.f$ret.index
D=A
[push d register value on stack]
// Push LCL
@LCL
D=M
[push d register value on stack]
// Push ARG
@ARG
D=M
[push d register value on stack]
// Push THIS
@THIS
D=M
[push d register value on stack]
// Push THAT
@THAT
D=M
[push d register value on stack]
// ARG = SP-5-nArgs
@SP
D=M
@5
D=D-A
@nArgs
D=D-A
@ARG
M=D
// LCL = SP
@SP
D=M
@LCL
M=D
// goto f
// if there's a dot, meaning a filename is specified,
// this address is specifiedFile.f, otherwise it's filename.f,
// where filename is the current file's name
@filename.f
0;JMP
(filename.f$ret.index)
```

- return 
```
// frame = LCL
// frame is stored in R13
@LCL
D=M
@R13
M=D
// retAddr = *(frame-5)
// retAddr is stored in R14
@5
D=D-A
A=D
D=M
@R14
M=D
// *ARG = pop()
[pop top of stack to D register]
@ARG
A=M
M=D
// SP = ARG+1
@ARG
D=M+1
@SP
M=D
// THAT = *(frame-1)
@R13
D=M-1
A=D
D=M
@THAT
M=D
// THIS = *(frame-2)
@R13
D=M
@2
D=D-A
A=D
D=M
@THIS
M=D
// ARG = *(frame-3)
@R13
D=M
@3
D=D-A
A=D
D=M
@ARG
M=D
// LCL = *(frame-4)
@R13
D=M
@4
D=D-A
A=D
D=M
@LCL
M=D
// goto retAddr
@R14
A=M
0;JMP
```

Where we can use the pattern for
(VAR) = *(frame-x)
```
@R13
D=M
@x
D=D-A
A=D
D=M
@VAR
M=D
```

## Fields

- comparisonCounter - initially 0, increases as more comparisons are written.

## Methods

Most of these methods will probably be System.out.printf with the code 
templates above, filled in by the segment and index parts.

We will have HashMaps to convert the command name to the corresponding symbol.

