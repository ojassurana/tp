# User Guide

## Introduction

Travel Diary is a desktop app for managing trips and travel memories, for use via a Command Line Interface (CLI). Travel Diary helps you log and organize trips, photos, and experiences more efficiently than traditional travel journaling apps.

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 17 or above installed.
1. Down the latest version of `Duke` from [here](http://link.to/duke).

## Features

{Give detailed description of each feature}


### Viewing Help: `help`
Displays the help menu with available commands.
- **Format:** `help`

### Exiting the Application: `bye`
Closes the application.
- **Format:** `bye`

### Returning to the Main Menu: `menu`
Returns to the main menu from the trip state.
- **Format:** `menu`


### Adding a todo: `todo`
Adds a new item to the list of todo items.

Format: `todo n/TODO_NAME d/DEADLINE`

* The `DEADLINE` can be in a natural language format.
* The `TODO_NAME` cannot contain punctuation.

Example of usage:

`todo n/Write the rest of the User Guide d/next week`

`todo n/Refactor the User Guide to remove passive voice d/13/04/2020`

## Data Management
- Data is automatically saved after any modification.
- Data is stored in a JSON file at `[JAR location]/data/travel_diary.json`.

## FAQ

**Q**: How do I transfer my data to another computer?

**A**: {your answer here}

## Command Summary
| Action | Format |
|--------|--------|
| Help | `help` |
| Exit | `bye` |
| Return to Menu | `menu` |