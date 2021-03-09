# Task manager library 

With Task Manager we refer to a software
component that is designed for handling multiple
processes inside an operating system.

We want the Task Manager to expose the following functionality:
 - Add a process
 - List running processes
 - Kill/KillGroup/KillAll
 
## Usage 
````kotlin
  val tm = TaskManager(3, TaskManagerStoreStrategy.Priority)
  tm.add(Process("1", ProcessPriority.Medium))
  tm.add(Process("2", ProcessPriority.High)) 
  
  val list = tm.list()
  tm.kill("PID1") 
  
````
