Running the program:
	1. Navigate to the project directory containing the edu and lib directories in a cmd window.
	2. compile the java files: "javac -cp .;lib\* edu\ucalgary\ensf409\*.java".
	3. Run "Runner": "java -cp .;lib\* edu.ucalgary.ensf409.Runner".

Running the tests:
	1. Navigate to the project directory containing the edu and lib directories in a cmd window.
	2. compile the java files: "javac -cp .;lib\* edu\ucalgary\ensf409\*.java".
	3. Run "inventory.sql" file in the project directory using the MySQL command line client.
		a. open the mysql command line client.
		b. run the script: "sourcePath\inventory.sql".
	4. Run "RunnerTest": "java -cp .;lib\* org.junit.runner.JUnitCore edu.ucalgary.ensf409.RunnerTest".
	5. Repeat step 3 to restore the database.
	6. Run "MySQLServiceTest": "java -cp .;lib\* org.junit.runner.JUnitCore edu.ucalgary.ensf409.MySQLServiceTest".
	7. Run "CombinationFinderTest": "java -cp .;lib\* org.junit.runner.JUnitCore edu.ucalgary.ensf409.CombinationFinderTest".