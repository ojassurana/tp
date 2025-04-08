# Travel Diary - User Guide

## Table of Contents
1. [Introduction](#introduction)
2. [Additional Features](#additional-features)
   - [Command Overview](#command-overview)
   - [Error Handling](#error-handling)
   - [Data Storage](#data-storage)
3. [Quick Start](#quick-start)
4. [Viewing Help](#viewing-help-help)
5. [Exiting the Application](#exiting-the-application)
6. [Managing Trips](#managing-trips)
   - [Adding a Trip](#adding-a-trip)
   - [Deleting a Trip](#deleting-a-trip)
   - [Listing All Trips](#listing-all-trips)
   - [Selecting a Trip](#selecting-a-trip)
7. [Managing Photos](#managing-photos)
   - [Adding a Photo](#adding-a-photo)
   - [Selecting a Photo](#selecting-a-photo)
   - [Deleting a Photo](#deleting-a-photo)
   - [Listing All Photos](#listing-all-photos)
   - [Closing a Photo: `close`](#closing-a-photo-close)
8. [Navigation](#navigation)
   - [Returning to Main Menu](#returning-to-main-menu)
9. [Storage](#storage)
   - [File Location](#file-location)
   - [Editing the Data File](#editing-the-data-file)
10. [Command Summary](#command-summary)
    - [General Commands](#general-commands)
    - [Managing Trips](#managing-trips-1)
    - [Managing Photos](#managing-photos-1)

---

## Introduction

Travel Diary is a desktop app for managing trips and travel memories, designed for use via a Command Line Interface (CLI). Travel Diary helps you log and organize trips, photos, and experiences more efficiently than traditional travel journaling apps.

Travel Diary consists of two main pages:

1. **Trip Page**: The main page where users can manage trips.
2. **Photo Page**: A secondary page accessed after selecting a trip, where users can manage photos.

## Additional Features

### Command Overview
The application uses a command pattern to execute various actions. Commands like `AddPhotoCommand`, `AddTripCommand`, and `HelpCommand` are available to perform specific tasks. Use the `help` command to view all available commands.

### Error Handling
The application includes robust error handling with custom exceptions such as `MissingTagsException` and `StorageException`. If you encounter an error, refer to the error message for guidance on resolving the issue.

### Data Storage
All data is managed by the `Storage` class, ensuring that your trips and photos are saved and loaded efficiently. Make sure to save your work frequently to avoid data loss.

---

## Quick Start
1. Ensure you have Java 17 or later installed on your computer.
2. Download the latest `.jar` file for Travel Diary.
3. Open a terminal, navigate to the folder containing the `.jar` file, and run the application using the following command:
   ```
   java -jar Travel_Diary.jar
   ```
4. Start using the commands listed below.

## Viewing Help: `help`
Displays information about available commands in current page.
```sh 
help
```

## Exiting the Application

To exit Travel Diary, use the following command:

```sh
bye
```

---

## Managing Trips 

These are the command available when user is at the trip page. 

### Adding a Trip

Adds a new trip to the collection.

!!! CAUTION: please refrain from entering `#` for NAME and DESCRIPTION 

#### Format:

```sh
add_trip n#NAME d#DESCRIPTION
```

#### Examples:

```sh
add_trip n#2025 Great Barrier Reef d#Summer break with family
add_trip n#2025 Kyoto Tour d#Winter exchange
```

> **Note:** Trip date ranges are automatically determined based on the dates of photos added to the trip. The earliest photo date becomes the trip start date, and the latest photo date becomes the trip end date. You cannot manually set these dates when creating a trip.

### Deleting a Trip

Deletes a trip from the collection.

#### Format:

```sh
delete [ID]
```

#### Examples:

```sh
delete 3
delete 2
```

### Listing All Trips

Shows a list of all trips and prints each trip's name.

#### Format:

```sh
list
```

#### Example Output:

```
*********
Trip Page
*********
Enter: list
1: Hong Kong Trip
		hk with my frens~!!~ (2024-11-15 3:00AM - 2024-12-21 12:19PM)
```

### Selecting a Trip

Select an existing trip to perform operations on it.

#### Format:

```sh
select [ID]
```

#### Examples:

```sh
select 1
select 2
```

---

## Managing Photos

### Adding a Photo

After selecting a trip, users can add a new photo to the trip.

!!! CAUTION: please refrain from entering `#` for FILENAME, DESCRIPTION
#### Format:

```sh
add_photo f#FILENAME n#PHOTONAME c#CAPTION
```

#### Examples:

```sh
add_photo f#desktop/bali.jpg n#Dog c#Wow
add_photo f#desktop/Phuket.jpg n#Cat c#Amazing Fun
```

> **Note:** This program only accepts .jpg images. Photos must be taken from a phone with internet and make sure your gps is working. If you are using a messaging app like telegram to transfer the photo please transfer it as a `FILE !!!`

> **Important:** Only images with GPS data can be uploaded. You can use [Online EXIF Viewer](https://onlineexifviewer.com/) to check if your images contain GPS data before uploading them.

### Selecting a Photo

Select an existing photo to view content and display its details.

#### Format:

```sh
select [ID]
```

#### Example Output:

```
Caption: summer holiday
Location: Bali beach (Nusa Dua)
Photo Name: Beach photo
```

### Deleting a Photo

Deletes a photo from the selected trip.

#### Format:

```sh
delete [ID]
```

#### Examples:

```sh
delete 3
delete 2
```

### Listing All Photos

Shows a list of all photos in the selected trip's folder.

#### Format:

```sh
list
```

#### Example Output:

```
	Here are all your photos:

	1) AIRPORT (Tung Chung, Hong Kong, China) 2024-11-14 7:00PM 
		Just landed in HK!
				|	11.0 km
	2) Olaf (Hong Kong Disneyland Resort, Hong Kong, China) 2024-11-15 9:16PM 
		i saw olaf?
				|	2584.0 km
	3) SINGAPORE RAIN! (Singapore, Singapore) 2024-12-20 8:19PM 
		raining in singapore.
				|	0.0 km
	4) Mirror (Singapore, Singapore) 2024-12-21 4:19AM 
		mirror mirror on the wall
```
### Closing a Photo: `close`
Closes the currently opened photo.
- **Format:**:
```sh
close
```
---

## Navigation

### Returning to Main Menu

To return to the main menu:

```sh
menu
```

## Storage
Trip data is automatically saved to a local file after every change. No manual saving is required.

### File Location
Data is stored in `/data/travel_diary.txt` within the application directory.

### Editing the Data File
Advanced users can modify the JSON file directly.  
⚠️ **Warning**: Editing the file incorrectly may cause data loss or unexpected behavior.

## Command Summary

### General Commands

> These commands are available on any page.

| Action            | Command     | Description                        |
|------------------|-------------|------------------------------------|
| View Help         | `help`      | Shows available commands           |
| Exit Application  | `bye`       | Closes the program                 |
| Return to Menu    | `menu`      | Goes back to the main menu         |

---

### Trip Management Commands

> Available on the **Trip Page**

| Action         | Command Format                         | Example                                                |
|----------------|----------------------------------------|--------------------------------------------------------|
| Add Trip       | `add_trip n#NAME d#DESCRIPTION`        | `add_trip n#Japan d#Autumn leaves in Kyoto`           |
| List Trips     | `list`                                 |                                                        |
| Select Trip    | `select INDEX`                         | `select 1`                                             |
| Delete Trip    | `delete INDEX`                         | `delete 2`                                             |

---

### Photo Management Commands

> Available **after selecting a trip**

| Action          | Command Format                                          | Example                                                      |
|-----------------|---------------------------------------------------------|--------------------------------------------------------------|
| Add Photo       | `add_photo f#FILE_PATH n#PHOTO_NAME c#CAPTION`          | `add_photo f#img/hanoi.jpg n#Old Quarter c#Crowded and fun!` |
| List Photos     | `list`                                                  |                                                              |
| Select Photo    | `select INDEX`                                          | `select 3`                                                   |
| Delete Photo    | `delete INDEX`                                          | `delete 2`                                                   |
| Close Photo     | `close`                                                 |                                                              |
