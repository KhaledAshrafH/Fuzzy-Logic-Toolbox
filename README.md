# Fuzzy Logic Toolbox

It's a Java-based application that provides functions and tools for designing and simulating fuzzy logic systems. It offers a user-friendly interface for creating and testing fuzzy logic systems by allowing users to define and configure input variables, output variables, membership functions, rules, and defuzzification methods.

## Features

The Fuzzy Logic Toolbox offers the following key features:

### 1. Create a New Fuzzy System

Users can create a new fuzzy logic system by providing a name and a brief description. This allows users to define the purpose and context of the system they are building.

### 2. Add Variables

Users can define input and output variables for the fuzzy logic system. Each variable has a name, type (input or output), and a range of possible values. Adding variables allows users to specify the parameters that affect the system's behavior.

### 3. Add Fuzzy Sets to Variables

Users can define fuzzy sets for each input and output variable. Fuzzy sets are used to represent linguistic terms, such as "low," "medium," and "high." The toolbox supports triangular and trapezoidal membership functions, which define the shape and boundaries of the fuzzy sets.

### 4. Add Rules

Users can define the rules that govern the behavior of the fuzzy logic system. Rules are specified in the format of "IF \[condition\] THEN \[conclusion\]." By adding rules, users can express the logical relationships between input variables and determine the appropriate output values based on the system's inputs.

### 5. Run Simulation on Crisp Values

Users can input crisp (precise) values for the input variables and simulate the fuzzy logic system. The simulation includes fuzzification, where crisp values are mapped to fuzzy sets based on their membership degrees, inference, where fuzzy rules are applied to determine the output fuzzy sets, and defuzzification, where the output fuzzy sets are converted back to crisp values using various defuzzification methods (e.g., weighted average).

### 6. Error Handling

The toolbox includes error handling mechanisms to alert users if there are missing or invalid inputs. This helps users identify and correct any issues in their fuzzy logic system design.

### 7. File I/O

The toolbox supports reading from and writing to file, allowing users to save and load fuzzy logic systems for future use. This feature enables users to easily store and share their fuzzy logic systems with others.

## Installation and Setup

To use the Fuzzy Logic Toolbox, follow these steps:

1. Download the Fuzzy Logic Toolbox repository from [GitHub](https://github.com/KhaledAshrafH/Fuzzy-Logic-Toolbox).

2. Make sure you have Java Development Kit (JDK) installed on your system.

3. Open the project in your preferred Java Integrated Development Environment (IDE).

4. Build the project to generate the executable JAR file.

5. Run the JAR file to launch the Fuzzy Logic Toolbox application.

## Usage Example

Here's an example usage scenario to demonstrate the functionality of the Fuzzy Logic Toolbox:

```
Fuzzy Logic Toolbox
===================
1- Create a new fuzzy system
2- Quit
1

Enter the system’s name and a brief description:
------------------------------------------------
Project Risk Estimation
The problem is to estimate the risk level of a project based on the project
funding and the technical experience of the project’s team members.

Main Menu:
==========
1- Add variables.
2- Add fuzzy sets to an existing variable.
3- Add rules.
4- Run the simulation on crisp values.
1

Enter the variable’s name, type (IN/OUT) and range ([lower, upper]):
(Press x to finish)
--------------------------------------------------------------------
proj_funding IN [0, 100]
exp_level IN [0, 60]
risk OUT [0, 100]
x

Main Menu:
==========
1- Add variables.
2- Add fuzzy sets to an existing variable.
3- Add rules.
4- Run the simulation on crisp values.
4

CAN’T START THE SIMULATION! Please add the fuzzy sets and rules first

Main Menu:
==========
1- Add variables.
2- Add fuzzy sets to an existing variable.
3- Add rules.
4- Run the simulation on crisp values.
2

Enter the variable’s name:
--------------------------
exp_level
Enter the fuzzy set name, type (TRI/TRAP) and values: (Press x to finish)
-----------------------------------------------------
beginner TRI 0 15 30
intermediate TRI 15 30 45
expert TRI 30 60 60
x

Main Menu:
==========
1- Add variables.
2- Add fuzzy sets to an existing variable.
3- Add rules.
4- Run the simulation on crisp values.
2

Enter the variable’s name:
--------------------------
proj_funding
Enter the fuzzy set name, type (TRI/TRAP) and values: (Press x to finish)
-----------------------------------------------------
very_low TRAP 0 0 10 30
low TRAP 10 30 40 60
medium TRAP 40 60 70 90
high TRAP 70 90 100 100
x

Main Menu:
==========
1- Add variables.
2- Add fuzzy sets to an existing variable.
3- Add rules.
4- Run the simulation on crisp values.
2

Enter the variable’s name:
--------------------------
risk
Enter the fuzzy set name, type (TRI/TRAP) and values: (Press x to finish)
-----------------------------------------------------
low TRI 0 25 50
normal TRI 25 50 75
high TRI 50 100 100
x

Main Menu:
==========
1- Add variables.
2- Add fuzzy sets to an existing variable.
3- Add rules.
4- Run the simulation on crisp values.
3

Enter the rules in this format: (Press x to finish)
IN_variable set operator IN_variable set => OUT_variable set
------------------------------------------------------------
proj_funding high or exp_level expert => risk low
proj_funding medium and exp_level intermediate => risk normal
proj_funding medium and exp_level beginner => risk normal
proj_funding low and exp_level beginner => risk high
proj_funding very_low and_not exp_level expert => risk high
x

Main Menu:
==========
1- Add variables.
2- Add fuzzy sets to an existing variable.
3- Add rules.
4- Run the simulation on crisp values.
4

Enter the crisp values:
-----------------------
proj_funding: 50
exp_level: 40
Running the simulation…
Fuzzification => done
Inference => done
Defuzzification => done
The predicted risk is normal (37.5)

Main Menu:
==========
1- Add variables.
2- Add fuzzy sets to an existing variable.
3- Add rules.
4- Run the simulation on crisp values.
Close

Fuzzy Logic Toolbox
===================
1- Create a new fuzzy system
2- Quit
2
```

## Contributing

Contributions to the Fuzzy Logic Toolbox are welcome. If you would like to contribute, please follow these steps:

1. Fork the repository on GitHub.

2. Create a new branch for your feature or bug fix.

3. Make the necessary changes in your branch.

4. Test your changes thoroughly.

5. Submit a pull request to the main repository.

6. Provide a clear description of your changes and the problem they solve.

7. Be responsive to any feedback or questions regarding your pull request.

## Team
This project was created by a team of three computer science students at Faculty of Computers and Artificial Intelligence Cairo University. The team members are:

- [Khaled Ashraf](https://github.com/KhaledAshrafH).
- [Ahmed Sayed](https://github.com/AhmedSayed117).
- [Ebarhim Muhammed](https://github.com/EbrahimHeggy).

## Acknowledgments

This Project is based on Soft Computing course at Faculty of Computers and Artificial Intelligence Cairo University. We would like to thank Dr. Samar Hesham for his guidance and support throughout this course.

## License

The Fuzzy Logic Toolbox is released under the [MIT License](LICENSE.md). You are free to use, modify, and distribute the toolbox in accordance with the terms of this license.
