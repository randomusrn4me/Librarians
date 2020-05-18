# Library Management Software
Developers: Berényi Ervin, Kátai Patrik, Kovács Máté, Molnár István

Our goal was to create a software for libraries to manage their day-to-day operations, such as maintaining a catalog of books, issuing to readers, who may view, search and handle their corner of the library through an intuitive user interface.
Development tools used: JDK 11.0.2, IntelliJ IDEA, Apache Maven, JavaFX + Gluon SceneBuilder, H2 Database (SQL),  Trello development table (Kanban), Git for VCS

## User Manual for Librarians
Running the program brings you to the login screen. Here you must type in the username you set and the password that you received. After pressing login, you will be brought to the main screen.
Please consider changing your password. You can do that in the List Users section, where you must look for your username, right click it, then follow the instructions!
In the top left corner you should see your username. Next to that in the Options menu you can find the option to extend all issues by 1, 2 or 4 weeks, if necessary. In the Help menu you can find basic information about the software.

### On the right side of the window you see a column of icons:
* Add user: this is where you can register a new reader or admin. You must fill out the fields with the information provided to you by the person you want to register. Please give the person a registration form, which contains all the information and rules they must follow filling out this form. Once the person has filled out all the fields, you must register them into the system using the add user option. If the fields' parameters are met, the program should give you a randomly generated password. Please write it down on a paper and give it to the user. Advise the user to change it as soon as possible once they log in. 
* Add book: This button brings up the add book window. Here you must fill out all the fields the program asks you to. Every book should have a unique ID, which you must write inside the book, and the same ID should be given in to the database. The ID must start with the letter B and be followed by numbers. 
* List users: This window opens up a list of all registered users with their user details. Right clicking a user you can edit their details (except for the username), change their password and delete their account, unless they are still issued any books.
* List books: With this option you can list all the books registered in the database. Right clicking one you can edit its details (except for the ID). You can check who the book has been issued to and you can also select multiple books and delete them together, but you cannot delete books that are issued to users. 
* Search books: Here you can search for books based on multiple criteria. The matches will be displayed in the list at the bottom.
* Logout: clicking this button you will be logged out of the software and brought back to the login screen.

### The Issue and Renew/Return operations:
In the middle of the window there are two boxes, one for selecting a book by its ID and one for selecting a user by their username. Type in the information, press enter in each field and basic information will be displayed. You can now issue the book to the user with the Issue button, if it is available. Upon issue, users have 3 weeks to return the books or they can renew them if they wish to. 
To Renew or Return books, the user must be entered and by clicking on the Renew/Return button , you will open a list of all the selected user's issued books. In this list if you right click on a book, you can either return it or renew it. You can also select multiple books with the shift key, or if they are not beside each other on the list, ctrl + left click. 

### Issue rules:
Users may only be issued books if they do not have any overdue. In that case, you may either renew their books (if they have been renewed less than 3 times) or implore them to return their overdue books if they wish to borrow more. Users with overdue books may be subject to fines.

### Renew rules:
You may only renew books if their due date is within 7 days. 
Renewing a book extends the due date by an additional 2 weeks. Each book may only be renewed a maximum of 3 times.
