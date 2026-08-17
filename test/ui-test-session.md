# UI test session

## 1. exit-greeting - PASS

Aim: Verify that the application starts and exits politely when the user enters `bye`.

### Command

```text
javac -d build/classes src/main/java/*.java src/main/java/errors/*.java && java -cp build/classes Hu9o
```

### Input sent to stdin

```text
bye
```

### Program output

```text
_________________________________
 _   _           ___ 
| | | | | | | | / _ \   ___  
| |_| | | | | || (_) | / _ \ 
|  _  | | |_| | \__,| | (_) |
|_| |_|  \___/   /_/   \___/ 
_________________________________

Woof! I'm Hu9o!
What can I do for you?

> _________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

Exit code: `0`

## 2. task-lifecycle - PASS

Aim: Verify adding todo, deadline, and event tasks; listing tasks; marking and unmarking a task; and handling an unknown command.

### Command

```text
javac -d build/classes src/main/java/*.java src/main/java/errors/*.java && java -cp build/classes Hu9o
```

### Input sent to stdin

```text
todo read book
deadline submit assignment /by Friday
event project meeting /from 2pm /to 4pm
list
mark 2
unmark 2
list
unknown
bye
```

### Program output

```text
_________________________________
 _   _           ___ 
| | | | | | | | / _ \   ___  
| |_| | | | | || (_) | / _ \ 
|  _  | | |_| | \__,| | (_) |
|_| |_|  \___/   /_/   \___/ 
_________________________________

Woof! I'm Hu9o!
What can I do for you?

> _________________________________

Got it. I've added this task:
	[T][ ] read book
Now you have 1 tasks in the list.
_________________________________

> _________________________________

Got it. I've added this task:
	[D][ ] submit assignment (by: Friday)
Now you have 2 tasks in the list.
_________________________________

> _________________________________

Got it. I've added this task:
	[E][ ] project meeting (from: 2pm to: 4pm)
Now you have 3 tasks in the list.
_________________________________

> _________________________________

1. [T][ ] read book
2. [D][ ] submit assignment (by: Friday)
3. [E][ ] project meeting (from: 2pm to: 4pm)
_________________________________

> _________________________________

Nice! I've marked this task as done:
	[D][X] submit assignment (by: Friday)
_________________________________

> _________________________________

Ok, I've marked this task as not done yet:
	[D][ ] submit assignment (by: Friday)
_________________________________

> _________________________________

1. [T][ ] read book
2. [D][ ] submit assignment (by: Friday)
3. [E][ ] project meeting (from: 2pm to: 4pm)
_________________________________

> _________________________________

Unknown command
_________________________________

> _________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

Exit code: `0`

## 3. command-errors - PASS

Aim: Verify that each invalid command form is handled by its specific Hu9oException without ending the program.

### Command

```text
javac -d build/classes src/main/java/*.java src/main/java/errors/*.java && java -cp build/classes Hu9o
```

### Input sent to stdin

```text
todo
mark 0
unmark 2
deadline report /at Friday
event study /from 2pm /until 4pm
unknown
bye
```

### Program output

```text
_________________________________
 _   _           ___ 
| | | | | | | | / _ \   ___  
| |_| | | | | || (_) | / _ \ 
|  _  | | |_| | \__,| | (_) |
|_| |_|  \___/   /_/   \___/ 
_________________________________

Woof! I'm Hu9o!
What can I do for you?

> _________________________________

Invalid todo format. Use: todo DESCRIPTION
_________________________________

> _________________________________

Invalid mark index
_________________________________

> _________________________________

Invalid unmark index
_________________________________

> _________________________________

Invalid deadline format. Use: deadline DESCRIPTION /by DATE
_________________________________

> _________________________________

Invalid event format. Use: event DESCRIPTION /from START /to END
_________________________________

> _________________________________

Unknown command
_________________________________

> _________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

Exit code: `0`

All 3 test case(s) passed.
