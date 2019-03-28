# Motiv8

###

Motiv8 is an application that wants to help keep you on track to complete your tasks/goals through periodically giving you inspiring quotes! Utilize the task list to help you keep track of your tasks and how important they are!

# Overview
  
#### Adding a Task

Users can Navigate to the Tasks Tab and Create a new Task using the Floating Action Button. Once save is clicked, the Task is saved locally to Sqlite Database. This database is then used to populate the Task Fragment when there are items within.

<img src="./images/add_task.png" width="300">

<h4 style="color:red"> *A title, description, and priority is required for every task.</h4>

#### Viewing Your Tasks

<img src="./images/task_tab.png" width="300"><br>
The tasks are retrieved from the Database using Room and are then populated into the Task Fragment using a RecyclerView. 

### Timer
Once the "Start Timer" Button is hit, the counter will begin to count down. Use this screen when you're ready to start an activity. There is no stop timer button, so once you start, you have to finish (Unless a new time is selected on the clock)
<div>
    <img  src="./images/timer.png" width="300">
</div>


#### Services
The Service and Broadcast Receiver are used in conjunction with each other to provide the user with quotes at their desired frequency. Once the "Start Timer" button has been hit, The Service is then started. This service then starts the Broadcast Receiver. The Broadcast Receiver is responsible for pushing notifications.

#### Settings
The Settings Activity allows users to change their preferences of their desired type of quote as well as quote frequency. The Retrofit API call is based on the category value, while the AlarmReceiver repeating frequency is set by the frequency.

<img src="./images/quote_settings.png" width="300">

### Notification Example
<img src="./images/notification.png" width="300">

## Structure

#### Activities

<ol>
    <li>Main Activity</li>
    <ol>
        <li>Timer Fragment</li>
        <li>Task Fragment</li>
    </ol>
    <li>Settings Activity</li>
    <li>Add Task Activity</li>
</ol>

#### Services
<ol>
    <li>NotificationService</li>
    <li>AlarmReceiver</li>
</ol>

#### Other Components/Libraries
<ol>
    <li><a href="https://developer.android.com/topic/libraries/architecture/room">Room (Content Provider)</li></a>
    <li><a href="https://square.github.io/retrofit/">Retrofit</li></a>
    <li><a href="https://square.github.io/picasso/">Picasso</li></a>
    <li><a href="http://jakewharton.github.io/butterknife">Butterknife for View Binding</li></a>
</ol>

## Architecture
Motiv8 utilizes the MVVM architecture  to assist with the seperation of class responsibilities.

### Model
The data is represented by two classes, Task and Quote. A Task repository was created to hold the database operations for Adding, Deleting, & Updating. The fields for each are as follows

### Task
<ul>
    <li>ID (For room)</li>
    <li>Title</li>
    <li>Description</li>
    <li>Priority</li>
    <li>Time</li>
</ul>

### Quote
Modeled after data retrieved from [Inspirational Quotes API](https://rapidapi.com/HealThruWords/api/universal-inspirational-quotes)

Fields 
<ul>
    <li>Author</li>
    <li>Title</li>
    <li>Link to Quote (Loaded with Picasso)</li>
</ul>

### ViewModel
The TaskViewModel calls the associated database operation from the Task Repository based on user interaction

### View
The Timer Fragment receives the List of tasks from the ViewModel and populates the RecyclerView. The View allows users to Add, Delete, or Update their tasks. Once one of these actions is taken, the associated operation is called in the ViewModel.

### The inspiration for this project mostly stems from my own journey and struggles with time management as well as keeping myself motivated. I believe anything is within the realm of reach, we just need some motivation to get there. Hopefully this can serve as just that! Enjoy :), feedback is welcomed!