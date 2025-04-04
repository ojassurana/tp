# Travel Diary - User Guide

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

#### Format:

```sh
add_trip n#NAME d#DESCRIPTION
```

#### Examples:

```sh
add_trip n#2025 Great Barrier Reef d#Summer break with family
add_trip n#2025 Kyoto Tour d#Winter exchange
```

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
trip
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
trip [ID]
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

#### Format:

```sh
add_photo f#FILENAME n#PHOTONAME c#CAPTION
```

#### Examples:

```sh
add_photo f#desktop/bali.jpg n#Dog c#Wow
add_photo f#desktop/Phuket.jpg n#Cat c#Amazing Fun
```

### Selecting a Photo

Select an existing photo to view content and display its details.

#### Format:

```sh
select [ID]
```

#### Example Output:

```
Caption: #summer holiday
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

---

## Navigation

### Returning to Main Menu

To return to the main menu:

```sh
menu
```

