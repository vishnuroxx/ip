# UI test plan

Add each new console test using the required Aim, Command, Input, and Expected output blocks. Commands run from the project root.

The compile step excludes `src/main/java/hu9o/gui/` (the JavaFX GUI) with `-not -path "*/gui/*"`, since those files need the JavaFX libraries on the classpath and the console entry point `hu9o.Hu9o` does not depend on them.

## Test case: exit-greeting

Aim:
```text
Verify that the application starts and exits politely when the user enters `bye`.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/exit-greeting/data && : > build/ui-tests/exit-greeting/data/taskData.txt && (cd build/ui-tests/exit-greeting && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
bye
```

Expected output:
```text
_________________________________
 _   _           ___ 
| | | | | | | | / _ \   ___  
| |_| | | | | || (_) | / _ \ 
|  _  | | |_| | \__,| | (_) |
|_| |_|  \___/   /_/   \___/ 
_________________________________

Give me a second....Loading tasks...
Successful! Use list to view the tasks.
Woof! I'm Hu9o!
What can I do for you?

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

## Test case: load-saved-tasks

Aim:
```text
Verify that Hu9o loads saved todo, deadline, and event tasks, including their completion statuses.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/load-saved-tasks/data && printf 'T| |read book|\nD|X|submit assignment|12/08/26 3 PM|\nE| |project meeting|12/08/26 2 PM|12/08/26 4 PM|\n' > build/ui-tests/load-saved-tasks/data/taskData.txt && (cd build/ui-tests/load-saved-tasks && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
list
bye
```

Expected output:
```text
_________________________________
 _   _           ___ 
| | | | | | | | / _ \   ___  
| |_| | | | | || (_) | / _ \ 
|  _  | | |_| | \__,| | (_) |
|_| |_|  \___/   /_/   \___/ 
_________________________________

Give me a second....Loading tasks...
Successful! Use list to view the tasks.
Woof! I'm Hu9o!
What can I do for you?

> _________________________________

1. [T][ ] read book
2. [D][X] submit assignment (by: 12 Aug, 3 PM)
3. [E][ ] project meeting (from: 12 Aug, 2 PM to: 12 Aug, 4 PM)
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

## Test case: dump-tasks-on-exit

Aim:
```text
Verify that tasks created during a session are written to the data file when Hu9o exits.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/dump-tasks-on-exit/data && : > build/ui-tests/dump-tasks-on-exit/data/taskData.txt && ((cd build/ui-tests/dump-tasks-on-exit && java -cp ../../classes hu9o.Hu9o) && cat build/ui-tests/dump-tasks-on-exit/data/taskData.txt)
```

Input:
```text
todo read book
deadline submit assignment /by 12/08/26 3 PM
event project meeting /from 12/08/26 2 PM /to 12/08/26 4 PM
mark 2
bye
```

Expected output:
```text
_________________________________
 _   _           ___ 
| | | | | | | | / _ \   ___  
| |_| | | | | || (_) | / _ \ 
|  _  | | |_| | \__,| | (_) |
|_| |_|  \___/   /_/   \___/ 
_________________________________

Give me a second....Loading tasks...
Successful! Use list to view the tasks.
Woof! I'm Hu9o!
What can I do for you?

> _________________________________

Got it. I've added this task:
	[T][ ] read book
Now you have 1 tasks in the list.
_________________________________

> _________________________________

Got it. I've added this task:
	[D][ ] submit assignment (by: 12 Aug, 3 PM)
Now you have 2 tasks in the list.
_________________________________

> _________________________________

Got it. I've added this task:
	[E][ ] project meeting (from: 12 Aug, 2 PM to: 12 Aug, 4 PM)
Now you have 3 tasks in the list.
_________________________________

> _________________________________

Nice! I've marked this task as done:
	[D][X] submit assignment (by: 12 Aug, 3 PM)
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

T| |read book|
D|X|submit assignment|12/08/26 3 PM|
E| |project meeting|12/08/26 2 PM|12/08/26 4 PM|
```
## Test case: task-lifecycle

Aim:
```text
Verify adding todo, deadline, and event tasks; listing tasks; marking and unmarking a task; deleting a task; and handling an unknown command.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/task-lifecycle/data && : > build/ui-tests/task-lifecycle/data/taskData.txt && (cd build/ui-tests/task-lifecycle && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
todo read book
deadline submit assignment /by 12/08/26 3 PM
event project meeting /from 12/08/26 2 PM /to 12/08/26 4 PM
list
mark 2
unmark 2
delete 2
list
unknown
bye
```

Expected output:
```text
_________________________________
 _   _           ___ 
| | | | | | | | / _ \   ___  
| |_| | | | | || (_) | / _ \ 
|  _  | | |_| | \__,| | (_) |
|_| |_|  \___/   /_/   \___/ 
_________________________________

Give me a second....Loading tasks...
Successful! Use list to view the tasks.
Woof! I'm Hu9o!
What can I do for you?

> _________________________________

Got it. I've added this task:
	[T][ ] read book
Now you have 1 tasks in the list.
_________________________________

> _________________________________

Got it. I've added this task:
	[D][ ] submit assignment (by: 12 Aug, 3 PM)
Now you have 2 tasks in the list.
_________________________________

> _________________________________

Got it. I've added this task:
	[E][ ] project meeting (from: 12 Aug, 2 PM to: 12 Aug, 4 PM)
Now you have 3 tasks in the list.
_________________________________

> _________________________________

1. [T][ ] read book
2. [D][ ] submit assignment (by: 12 Aug, 3 PM)
3. [E][ ] project meeting (from: 12 Aug, 2 PM to: 12 Aug, 4 PM)
_________________________________

> _________________________________

Nice! I've marked this task as done:
	[D][X] submit assignment (by: 12 Aug, 3 PM)
_________________________________

> _________________________________

Ok, I've marked this task as not done yet:
	[D][ ] submit assignment (by: 12 Aug, 3 PM)
_________________________________

> _________________________________

Got it. Deleted the following task:
	[D][ ] submit assignment (by: 12 Aug, 3 PM)
_________________________________

> _________________________________

1. [T][ ] read book
2. [E][ ] project meeting (from: 12 Aug, 2 PM to: 12 Aug, 4 PM)
_________________________________

> _________________________________

Unknown command. Use todo, list, find, deadline, event, mark, unmark, delete or bye
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```
## Test case: command-errors

Aim:
```text
Verify that each invalid command form is handled by its specific Hu9oException without ending the program.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/command-errors/data && : > build/ui-tests/command-errors/data/taskData.txt && (cd build/ui-tests/command-errors && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
todo
mark 0
unmark 2
delete 0
deadline report /at Friday
event study /from 2pm /until 4pm
unknown
bye
```

Expected output:
```text
_________________________________
 _   _           ___ 
| | | | | | | | / _ \   ___  
| |_| | | | | || (_) | / _ \ 
|  _  | | |_| | \__,| | (_) |
|_| |_|  \___/   /_/   \___/ 
_________________________________

Give me a second....Loading tasks...
Successful! Use list to view the tasks.
Woof! I'm Hu9o!
What can I do for you?

> _________________________________

Invalid todo format. Use: todo DESCRIPTION
_________________________________

> _________________________________

Invalid index. Check the list of items using "list"
_________________________________

> _________________________________

Invalid index. Check the list of items using "list"
_________________________________

> _________________________________

Invalid index for deleting. Check the list of items using "list"
_________________________________

> _________________________________

Invalid deadline format. Use: deadline DESCRIPTION /by DATE
_________________________________

> _________________________________

Invalid event format. Use: event DESCRIPTION /from START /to END
_________________________________

> _________________________________

Unknown command. Use todo, list, find, deadline, event, mark, unmark, delete or bye
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

## Test case: find-tasks

Aim:
```text
Verify that `find KEYWORD` lists the loaded tasks whose description contains the keyword (case-insensitively), reports when nothing matches, and rejects a find command with no keyword.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/find-tasks/data && printf 'T|X|read book|\nD|X|return book|06/06/26 3 PM|\nT| |wash car|\n' > build/ui-tests/find-tasks/data/taskData.txt && (cd build/ui-tests/find-tasks && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
find book
find plants
find
bye
```

Expected output:
```text
_________________________________
 _   _           ___ 
| | | | | | | | / _ \   ___  
| |_| | | | | || (_) | / _ \ 
|  _  | | |_| | \__,| | (_) |
|_| |_|  \___/   /_/   \___/ 
_________________________________

Give me a second....Loading tasks...
Successful! Use list to view the tasks.
Woof! I'm Hu9o!
What can I do for you?

> _________________________________

Here are the matching tasks in your list:
1. [T][X] read book
2. [D][X] return book (by: 06 Jun, 3 PM)
_________________________________

> _________________________________

No matching tasks found.
_________________________________

> _________________________________

Invalid find format. Use: find KEYWORD
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```
