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

Unknown command. Tasks: todo, list, find, deadline, event, mark, unmark, delete, bye. Contacts: person, people, findperson, deleteperson, link, connections, select, deselect.
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

Unknown command. Tasks: todo, list, find, deadline, event, mark, unmark, delete, bye. Contacts: person, people, findperson, deleteperson, link, connections, select, deselect.
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
## Test case: person-lifecycle

Aim:
```text
Verify adding, listing, finding, linking, viewing connections, and deleting people in the contact network, and that an unrecognized command still produces the standard unknown-command error.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/person-lifecycle/data && : > build/ui-tests/person-lifecycle/data/taskData.txt && (cd build/ui-tests/person-lifecycle && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
person Alice Tan /phone 91112222 /email alice@example.com
person Bob Lee /phone 93334444 /email bob@example.com
people
findperson Bob
link Alice Tan /with Bob Lee
connections Alice Tan
deleteperson 2
people
frobnicate
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

Got it. I've added this person:
	Alice Tan (Phone: 91112222, Email: alice@example.com)
Now you have 1 people in the network.
_________________________________

> _________________________________

Got it. I've added this person:
	Bob Lee (Phone: 93334444, Email: bob@example.com)
Now you have 2 people in the network.
_________________________________

> _________________________________

1. Alice Tan (Phone: 91112222, Email: alice@example.com)
2. Bob Lee (Phone: 93334444, Email: bob@example.com)
_________________________________

> _________________________________

Here are the matching people in your network:
1. Bob Lee (Phone: 93334444, Email: bob@example.com)
_________________________________

> _________________________________

Got it. Linked Alice Tan and Bob Lee.
_________________________________

> _________________________________

Alice Tan is connected to:
1. Bob Lee
_________________________________

> _________________________________

Got it. Deleted the following person:
	Bob Lee (Phone: 93334444, Email: bob@example.com)
_________________________________

> _________________________________

1. Alice Tan (Phone: 91112222, Email: alice@example.com)
_________________________________

> _________________________________

Unknown command. Tasks: todo, list, find, deadline, event, mark, unmark, delete, bye. Contacts: person, people, findperson, deleteperson, link, connections, select, deselect.
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

## Test case: person-task-session

Aim:
```text
Verify that selecting a person redirects todo/deadline/event/list/mark/unmark/delete to their own task list, and that deselecting reverts to the global list, which stays untouched throughout.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/person-task-session/data && : > build/ui-tests/person-task-session/data/taskData.txt && (cd build/ui-tests/person-task-session && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
person Carol Ng /phone 95556666 /email carol@example.com
select Carol Ng
todo buy milk
deadline submit report /by 12/08/26 3 PM
event team meeting /from 12/08/26 2 PM /to 12/08/26 4 PM
list
mark 1
unmark 1
delete 2
list
deselect
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

Got it. I've added this person:
	Carol Ng (Phone: 95556666, Email: carol@example.com)
Now you have 1 people in the network.
_________________________________

> _________________________________

Now viewing Carol Ng's list. todo/list/mark/... apply to them until you deselect.
_________________________________

> _________________________________

Got it. I've added this task:
	[T][ ] buy milk
Now you have 1 tasks in the list.
_________________________________

> _________________________________

Got it. I've added this task:
	[D][ ] submit report (by: 12 Aug, 3 PM)
Now you have 2 tasks in the list.
_________________________________

> _________________________________

Got it. I've added this task:
	[E][ ] team meeting (from: 12 Aug, 2 PM to: 12 Aug, 4 PM)
Now you have 3 tasks in the list.
_________________________________

> _________________________________

1. [T][ ] buy milk
2. [D][ ] submit report (by: 12 Aug, 3 PM)
3. [E][ ] team meeting (from: 12 Aug, 2 PM to: 12 Aug, 4 PM)
_________________________________

> _________________________________

Nice! I've marked this task as done:
	[T][X] buy milk
_________________________________

> _________________________________

Ok, I've marked this task as not done yet:
	[T][ ] buy milk
_________________________________

> _________________________________

Got it. Deleted the following task:
	[D][ ] submit report (by: 12 Aug, 3 PM)
_________________________________

> _________________________________

1. [T][ ] buy milk
2. [E][ ] team meeting (from: 12 Aug, 2 PM to: 12 Aug, 4 PM)
_________________________________

> _________________________________

Back to your own list.
_________________________________

> _________________________________

_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

## Test case: contact-save-format

Aim:
```text
Verify that people, a link between them, and a person's own task are written to data/contactData.txt, data/contactLinks.txt, and data/contacts/1.txt in the expected pipe-delimited format on exit.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/contact-save-format/data && : > build/ui-tests/contact-save-format/data/taskData.txt && ((cd build/ui-tests/contact-save-format && java -cp ../../classes hu9o.Hu9o) && cat build/ui-tests/contact-save-format/data/contactData.txt && cat build/ui-tests/contact-save-format/data/contactLinks.txt && cat build/ui-tests/contact-save-format/data/contacts/1.txt)
```

Input:
```text
person Dan Ho /phone 91230000 /email dan@example.com
person Eve Koh /phone 98760000 /email eve@example.com /dob 5/5/95 /notes vegetarian
link Dan Ho /with Eve Koh
select Dan Ho
todo call supplier
deselect
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

Got it. I've added this person:
	Dan Ho (Phone: 91230000, Email: dan@example.com)
Now you have 1 people in the network.
_________________________________

> _________________________________

Got it. I've added this person:
	Eve Koh (Phone: 98760000, Email: eve@example.com, DOB: 5/5/95, Notes: vegetarian)
Now you have 2 people in the network.
_________________________________

> _________________________________

Got it. Linked Dan Ho and Eve Koh.
_________________________________

> _________________________________

Now viewing Dan Ho's list. todo/list/mark/... apply to them until you deselect.
_________________________________

> _________________________________

Got it. I've added this task:
	[T][ ] call supplier
Now you have 1 tasks in the list.
_________________________________

> _________________________________

Back to your own list.
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

Dan Ho|91230000|dan@example.com||
Eve Koh|98760000|eve@example.com|5/5/95|vegetarian
Dan Ho|Eve Koh
T| |call supplier|
```

## Test case: contact-load-from-seed

Aim:
```text
Verify that Hu9o loads pre-saved people (with and without biodata), a link between them, and one person's own saved tasks, all correctly on start-up.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/contact-load-from-seed/data/contacts && : > build/ui-tests/contact-load-from-seed/data/taskData.txt && printf 'Frank Sim|91110000|frank@example.com||\nGrace Yeo|92220000|grace@example.com|3/3/92|allergic to nuts\n' > build/ui-tests/contact-load-from-seed/data/contactData.txt && printf 'Frank Sim|Grace Yeo\n' > build/ui-tests/contact-load-from-seed/data/contactLinks.txt && printf 'T|X|call Grace|\nD| |renew passport|20/12/26 5 PM|\n' > build/ui-tests/contact-load-from-seed/data/contacts/1.txt && (cd build/ui-tests/contact-load-from-seed && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
people
select Frank Sim
list
deselect
connections Grace Yeo
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

1. Frank Sim (Phone: 91110000, Email: frank@example.com)
2. Grace Yeo (Phone: 92220000, Email: grace@example.com, DOB: 3/3/92, Notes: allergic to nuts)
_________________________________

> _________________________________

Now viewing Frank Sim's list. todo/list/mark/... apply to them until you deselect.
_________________________________

> _________________________________

1. [T][X] call Grace
2. [D][ ] renew passport (by: 20 Dec, 5 PM)
_________________________________

> _________________________________

Back to your own list.
_________________________________

> _________________________________

Grace Yeo is connected to:
1. Frank Sim
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

## Test case: contact-command-errors

Aim:
```text
Verify that each invalid contact command form is handled by its specific Hu9oException without ending the program: bad person format, a duplicate-free unknown select/link target, a self-link, an out-of-range deleteperson index, and empty findperson/connections keywords.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/contact-command-errors/data && : > build/ui-tests/contact-command-errors/data/taskData.txt && (cd build/ui-tests/contact-command-errors && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
person Henry Goh
person Henry Goh /phone 91112222 /email henry@example.com
select Nobody Here
link Henry Goh /with Nobody Here
link Henry Goh /with Henry Goh
deleteperson 0
findperson
connections
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

Invalid person format. Use: person NAME /phone PHONE /email EMAIL [/dob DOB] [/notes NOTES]
_________________________________

> _________________________________

Got it. I've added this person:
	Henry Goh (Phone: 91112222, Email: henry@example.com)
Now you have 1 people in the network.
_________________________________

> _________________________________

No person with that name. Check the list of people using "people"
_________________________________

> _________________________________

No person with that name. Check the list of people using "people"
_________________________________

> _________________________________

Cannot link a person to themselves.
_________________________________

> _________________________________

Invalid index for deleting. Check the list of people using "people"
_________________________________

> _________________________________

Invalid findperson format. Use: findperson KEYWORD
_________________________________

> _________________________________

Invalid connections format. Use: connections NAME
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```

## Test case: global-list-unaffected-by-contacts

Aim:
```text
Verify that adding a person does not change the behaviour or output of any existing task command: the rest of this input is byte-for-byte the same task-lifecycle sequence as the task-lifecycle test case, just preceded by one person command.
```

Command:
```shell
javac -d build/classes $(find src/main/java -name "*.java" -not -path "*/gui/*") && mkdir -p build/ui-tests/global-list-unaffected-by-contacts/data && : > build/ui-tests/global-list-unaffected-by-contacts/data/taskData.txt && (cd build/ui-tests/global-list-unaffected-by-contacts && java -cp ../../classes hu9o.Hu9o)
```

Input:
```text
person Ivy Chua /phone 91112222 /email ivy@example.com
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

Got it. I've added this person:
	Ivy Chua (Phone: 91112222, Email: ivy@example.com)
Now you have 1 people in the network.
_________________________________

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

Unknown command. Tasks: todo, list, find, deadline, event, mark, unmark, delete, bye. Contacts: person, people, findperson, deleteperson, link, connections, select, deselect.
_________________________________

> 
 Saving data...
 Successfully saved data
_________________________________

Bye. Hope to see you again soon! (wags tail)
_________________________________

```
