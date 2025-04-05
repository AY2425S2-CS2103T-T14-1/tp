---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---


# WhoAreYouAgain? Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is adapted from AB3.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-T14-1/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-T14-1/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.
1. Please do note that this explanation is **intentionally** simplified (i.e. no elaboration on `executeConfirmed`) for the ease of reading and reference. 

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-T14-1/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="600" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPrefs` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* stores a `CommandHistory` object that stores the past commands of the user. This is exposed to the outside as a `ReadOnlyCommandHistory` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="600" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-T14-1/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" />

The `Storage` component,
* can save address book data, command history data, and user preference data in JSON format, and read them back into corresponding objects.
* inherits from `AddressBookStorage`, `CommandHistoryStorage` and `UserPrefStorage`, which means it can be treated as any one (if only the functionality of only one is needed).
* provides read-only interfaces (`ReadOnlyAddressBook` and `ReadOnlyCommandHistory`) for controlled data access.
* is implemented by concrete classes `JsonAddressBookStorage` and `JsonCommandHistoryStorage` which extend the abstract generic `JsonStorage<T, S extends JsonSerializable<T>>` for type-safe serialization.
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`).

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Current\] Undo/Redo feature

#### Current Implementation

The current undo/redo mechanism is facilitated by `CommandTracker`. It tracks the `Undoable` commands i.e. commands that extend `Undoable` in the form of 2 separate stacks: `undoStack` and `redoStack`. 
Additionally, it supports these operations:

* `CommandTracker#getInstance` - Returns the singleton instance of ```CommandTracker```
* `CommandTracker#push` - Pushes a new `UndoableCommmand` onto the undo stack and clears the redo stack
* `CommandTracker#popUndo` - Pops and returns the most recent command from the undo stack
* `CommandTracker#getRedo` - Pops and returns the most recent command from the redo stack
* `CommandTracker#canUndo` - Returns true if there are commands to undo
* `CommandTracker#canRedo` - Returns true if there are commands to redo
* `CommandTracker#clear` - Clears both the undo and redo stacks

Step 1: Initial Application Launch

- State:
  - The application is launched.
  - The address book contains preloaded contacts.

-  Behaviour:
    - No undo or redo operations are available at this point since no changes have been made.

Step 2: Adding a Person

- Action:
  - The user executes the `add` command to add a new person.
  - ```add n/John Doe p/98765432 e/johndoe@example.com```

- State:
    - The address book now contains the new person, John Doe.

-  Behaviour:
    - The `add` command is executed and is reflected in the new address book state (with the newly added person). 
    - ```CommandTracker.getInstance().push()``` is called to push this command to the undo stack.
        - This is allowed because the ```AddCommand``` extends ```UndoableCommand```.
    - `undo` is now available, but `redo` is not (since no action has been undone yet).

Step 3: Deleting a Person

- Action:
    - The user executes the ```delete``` command to delete the previously added person.
    - ```delete <index>``` where `index` is the location John Doe is stored.

- State:
    - The address book now no longer contains John Doe.

- Behaviour:
    - The ```delete command``` is executed and is reflected in the new address book state (without the deleted person).
    - ```CommandTracker.getInstance().push()``` is called to push this command to the undo stack.
        - This is allowed because the ```DeleteCommand``` extends ```UndoableCommand```.
    - `undo` is still available to restore the deleted person, but `redo` is still not available since delete has not been undone yet.

Step 4: Undoing the DeleteCommand

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram" />

- Action:
    - The user decides to undo the delete command using the ```undo``` command.

- State:
    - The address book is reverted to the state before the delete command, with John Doe restored.

- Behaviour:
  - The ```undo``` command is called.
  - ```CommandTracker.canUndo()``` is called to see if the command can be undone.
      - Since it can, ```CommandTracker.getInstance().popUndo()``` is called to pop the command from the undo stack.
      - Subsequently, this command is also pushed to the redo stack.
  - The ```DeleteCommand#redo``` method is invoked on the last ```delete``` command and restores the deleted person.
  - `undo` is still available (for the intial ```add``` command) and `redo` is available since delete has been undone.

Step 5: Redoing the DeleteCommand

<puml src="diagrams/RedoSequenceDiagram.puml" alt="RedoSequenceDiagram" />

- Action:
    - The user decides to redo the delete command using the ```redo``` command.

- State:
    - The address book is reverted to the state after the delete command, with John Doe deleted.

- Behaviour:
    - The ```redo``` command is called.
    - ```CommandTracker.canRedo()``` is called to see if the command can be redone.
        - Since it can, ```CommandTracker.getInstance().popRedo()``` is called to pop the command from the redo stack.
    - The ```DeleteCommand#redo``` method is invoked on the last ```delete``` command and deletes the person again.
    - `undo` is still available (for the intial ```add``` command) and `redo` is no longer available since delete has been redone.

**Note:** At this point, the user should be able to notice that the `undo` and `redo` commands can be called in a circular manner i.e. ```undo -> redo -> undo -> ...``` 

#### Planned Extensions
1. Ensure undo / redo restore order in any state
   1. This applies to the states after list and find have been executed followed by an `UndoableCommand`
1. Implement undo / redo for `clear`

### \[Proposed\] Possible Undo/Redo Implementation
The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagramProposed-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Individuals with large extended families who struggle to keep track of relationships
* Users who frequently attend family gatherings and want to avoid awkward moments of forgetting names and relationships
* Individuals who want an organized way to store family details such as birthdays, nicknames, and notable events
* Users looking for an easy-to-use digital family directory
* Users who prefer typing to mouse interactions
* Users who are reasonably comfortable using CLI apps

**Value proposition**:

**WhoAreYouAgain?** is a family directory app designed to help users remember family members, track important dates,
and avoid social faux pas during family gatherings. It provides a structured way to manage extended family information,
ensuring that users never forget key relationships, birthdays, or inside jokes. The app simplifies family
record-keeping while adding a fun and practical element to family interactions.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I want to …​                                                                                              | So that I can…​                                            |
|----------|---------|-----------------------------------------------------------------------------------------------------------|------------------------------------------------------------|
| `* * *`  | user    | add a family member to my address book                                                                    | keep track of relatives                                    |
| `* * *`  | user    | delete a family member                                                                                    | remove outdated or incorrect entries                       |
| `* * *`  | user    | search for a family member by name                                                                        | quickly find their details                                 |
| `* * *`  | user    | add a profile picture for each family member                                                              | visually recognize them                                    |
| `* * *`  | user    | store a family member’s contact details (phone, email, address)                                           | reach out to them easily                                   |
| `* * *`  | user    | record a family member’s relationship to me in a friendly way (e.g., “Mom’s Brother” instead of just “Uncle”) | recall how we are connected                                |
| `* * *`  | user    | add nicknames to a family member’s profile                                                                | remember what they are called in my family                 |
| `* * *`  | user    | add notes about a family member (fun facts, shared memories and inside jokes)                             | have a reference for conversation starters                 |
| `* *  `  | user    | sort my family members by birthday                                                                        | remember whose birthday is coming soon to buy them gifts   |
| `* *  `  | user    | undo and redo changes in my address book                                                                  | make mistakes comfortably when using my address book       |




# WhoAreYouAgain? - Use Cases  

## Use Case 1: Add Nickname to Family Member’s Profile  
**System**: WhoAreYouAgain?  
**Use case**: UC01 - Add Nickname  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User selects a family member’s profile.  
2. System displays the family member’s details.  
3. User chooses to add/edit a nickname.  
4. System requests nickname input.  
5. User enters a nickname and confirms.  
6. System saves the nickname and updates the profile.  
   **Use case ends.**  

### Extensions:  
- **4a. User leaves the nickname field empty.**  
  - 4a1. System displays a warning that the nickname cannot be empty.  
  - 4a2. User enters a valid nickname.  
  - Use case resumes from step 5.  

- **5a. User cancels the action before confirmation.**  
  - 5a1. System discards changes.  
  - **Use case ends.**  

---

## Use Case 2: View a List of Upcoming Birthdays  
**System**: WhoAreYouAgain?  
**Use case**: UC02 - View Upcoming Birthdays  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User navigates to the birthdays section.  
2. System retrieves and displays a list of upcoming birthdays.  
   **Use case ends.**  

### Extensions:  
- **2a. There are no upcoming birthdays.**  
  - 2a1. System displays a message: “No upcoming birthdays.”  
  - **Use case ends.**  

---

## Use Case 3: Search for a Family Member  
**System**: WhoAreYouAgain?  
**Use case**: UC03 - Search Family Member  
**Actor**: User  

### Main Success Scenario (MSS):  
1. User opens the family tree view.  
2. User enters a family member’s name in the search bar.  
3. System searches and highlights the matching family member in the tree.  
   **Use case ends.**  

### Extensions:  
- **3a. No matching family member is found.**  
  - 3a1. System displays a message: “No results found.”  
  - **Use case ends.**  

---

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 200 family members with a response time of less than 5 seconds.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. The application should handle unexpected inputs gracefully with informative error messages.
5. The application should not utilise a database management system.
6. All family data should be stored locally.
7. All family data should be stored in a human editable text file.
8. The software should function without an installer.
9. The software architecture must primarily follow object-oriented programming principles. 
10. External libraries must be free, open-source, require no user installation.
11. The GUI must work well at 1920×1080 resolution and higher with 100-125% scaling, and remain usable at 1280×720 with 150% scaling. 
12. The product JAR file must not exceed 100MB, and documentation PDFs must not exceed 15MB each. 
13. The entire application must be packaged as a single JAR file.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Average typing speed**: 40 words per minute (wpm)
* **JAR file**: Java Archive file that packages multiple Java class files and associated metadata into a single file
* **Human editable text file**: A file format such as JSON or txt that can be read and modified using common text editors
* **JSON**: JavaScript Object Notation, a lightweight data interchange format that uses human-readable text to store and transmit data objects consisting of attribute-value pairs and arrays
* **Database management system**: Software for creating and managing databases, providing systematic access to stored data (e.g., MySQL, PostgreSQL, MongoDB)
* **GUI:** Graphical User Interface, a visual way of interacting with a computer program using graphical elements such as windows, icons, and buttons

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file or run `java -jar <file_name>` in the folder. <br>
       Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding a person

1. Adding a person with ALL fields

    1. Prerequisites: No persons / Multiple persons in the list.

    1. Test case: `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 b/30-12-2001 r/Father nn/Johnny no/Allergic to peanuts t/friends t/owesMoney`<br>
       Expected: Person with given details is added to the list.

    1. Test case: `add n/John Doe`<br>
       Expected: Person is added as only name is a compulsory field.

    1. Other incorrect add commands to try: `add`, `add 3`, `add x/` (where x is an unknown field)<br>
       Expected: Error details shown in the status message. Status bar remains the same.

### Editing a person

1. Edit some fields of a person

    1. Prerequisites: At least one person in the list.

    1. Test case: `edit 1 p/91234567 e/johndoe@example.com`<br>
       Expected: Person at index 1 is edited with new details.

    1. Test case: `edit 1`<br>
       Expected: Error details shown in the status message. Status bar remains the same.

    1. Other incorrect edit commands to try: `edit x` (where x is an unknown index), `edit 1 y/` (where y is an unknown field)<br>
       Expected: Similar to previous.
    
### Deleting a person

1. Deleting a person

    1. Prerequisites: List all persons using the `list` command and at least one person in the list.

    1. Test case: `delete 1`<br>
       Expected: Confirmation message pops up. First contact is deleted from the list when user enters `y`. Details of the deleted contact shown in the status message.
   
    1. Test case: `delete 1`<br>
       Expected: Confirmation message pops up. Contact is not deleted from the list when user enters `n`. Deletion abortion message is shown.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

### Listing persons

1. List persons

    1. Prerequisites: At least one person in the list.

    1. Test case: `list asc`<br>
       Expected: Persons listed in order of soonest upcoming birthday.

    1. Test case: `list desc`<br>
       Expected: Persons listed with furthest birthdays first.

    1. Other incorrect delete commands to try: `list 1`, `list y/` (where y is an unknown field)<br>
       Expected: Error details shown in the status message. Status bar remains the same.

### Clear persons

1. Clear persons

    1. Prerequisites: At least one person in the list.

    1. Test case: `clear`<br>
       Expected: Confirmation message pops up. All contacts cleared when user enters `y`.

    1. Test case: `clear`<br>
       Expected: Confirmation message pops up. No action occurs when user enters `n`.

    1. Test case: `clear 1`<br>
       Expected: Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `clear name`, `clear 1`<br>
       Expected: Similar to previous.

### Finding persons

1. Finding a person / persons

    1. Prerequisites: At least one person in the list.

    1. Test case: `find Alice`<br>
       Expected: Persons with "Alice" in their name are listed.

    1. Test case: `find ALICE`<br>
       Expected: Persons with "Alice" in their name are listed.

    1. Test case: `find Aliss`<br>
       Expected: Shows no exact matches but displays similar entries.

### Undo and Redo

1. Undo the last command

    1. Prerequisites: At least one undoable command (e.g., add, edit, delete) has been executed.

    1. Test case: `undo`<br>
       Expected: Last change is reverted. Successful undo message shown.

2. Redo the last undone command

   1. Test case: `redo` <br>
      Expected: The previously undone change is re-applied. Successful redo message shown.

3. Attempting undo/redo when nothing to undo/redo or after undoable command

    1. Test case: undo or redo repeatedly after no more history <br>
    Expected: Error message "Nothing to undo!" or "Nothing to redo!" displayed.

### Command History

1. Navigating command history

    1. Prerequisites: At least two different commands (e.g., list, find Alice) have been executed.

    1. Test case: Press Up arrow key after entering a command<br>
    Expected: Previous command appears in command box.

    2. Test case: Press Down arrow key after pressing Up arrow key multiple times<br>
    Expected: More recent command appears in command box.

    3. Test case: Enter both valid and invalid commands, then use Up/Down keys<br>
    Expected: All commands (valid and invalid) are accessible in history. Invalid commands don't clear the command box.

    4. Other scenarios to try: Navigate to oldest command and press Up again, navigate to most recent command and press Down<br>
    Expected: No change when pressing Up at the oldest command. The command box is cleared after pressing Down on the most recent command.

### Help and Exit

1. Help command

    1. Test case: `help` <br>
    Expected: Help window appears.

2. Exit command

    1. Test case: `exit` <br>
    Expected: Application closes.

### Saving data

1. Dealing with missing/corrupted data files

    1. Load the application and run some commands.

    1. Go into the folder where the jar file is stored and delete the savedata folder.

    1. Re-run the application.

    1. Address book is now in original default state with preloaded contacts for testing.

2. Transferring data

    1. Go into the folder where the jar file is stored and delete the savedata folder.

    1. Replace it with your incoming savedata folder.

    1. Re-run the application.

    1. Address book contains contacts from the new savedata folder.

1. Modifying data

    1. Go into the folder where the jar file is stored and modify the savedata folder.

    1. You may add/edit/delete people here manually.

    1. Re-run the application.

    1. Address book contains contacts from the modified savedata folder.
