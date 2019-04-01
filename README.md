# javalibrarylocaldb
A Java UI library application. CRUD functions supported by local database file

## Using the Application

### Run the Application
This application was written in IntelliJ and includes with it an SQLite database file named “library.db”. This file is required for the application to run. To recreate this table, you can use the SQL provided in the file’s names “createtables.txt” and “mockdata.txt”. To run the application double click the file named “Final Project.jar”.

### Add a Record
To add a record to the database, fill in the field on the right side of the application, specifically the title, author, ISBN, and choose an option in the genre drop down field. After all the fields are filled in, click the “Save” button. If there is a book in the database with the same ISBN, then that record in the database will be updated. Other wise a new book will be added to the database. New authors will be added to the database if they do not exist in the database already.

### Update a Record
To update a book from the database, click on an item from the list on the left. The information from that record will fill the fields on the right. You can then update these fields, afterwards when pressing the “Save” button the record in the database will be updated based on the new information provided.

### Delete a Record
To update a book from the database, click on an item from the list on the left. When the information is filled on the right, pressing the “Delete” button will update the status of the record in the database and remove it from the main list on the left.

### Undelete a record
To undelete a record in the database, click the “Undelete” button in the top right corner. This will open a new window like the main window, except this window will be titled “Undelete Books | Grand Rapids Pubic Library”. The items in the list on the left will be books which have been deleted but not purged yet. Clicking on one of these items will fill the fields on the right with the item’s information. When the information is filled in, clicking on the “Undelete” button will set the book back to active, and remove it from the undelete list. It will now show up in the regular window when the list is reset.

### Purge a record
To purge a record, you must go to the same undelete window as described in the undelete section. If, after you have opened the undelete window and have clicked on an item from the list on the left, you press the “Purge” button this record will be completely removed from the database and cannot be recovered.
