# Travel Diary - User Guide

## Introduction

Travel Diary is a desktop app for managing trips and travel memories, designed for use via a Command Line Interface (CLI). Travel Diary helps you log and organize trips, photos, and experiences more efficiently than traditional travel journaling apps.

Travel Diary consists of two main pages:

1. **Trip Page**: The main page where users can manage trips.
2. **Photo Page**: A secondary page accessed after selecting a trip, where users can manage photos.



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
add_trip n#NAME d#DESCRIPTION l#LOCATION
```

#### Examples:

```sh
add_trip n#2025 Great Barrier Reef d#Summer break with family l#Australia
add_trip n#2025 Kyoto Tour d#Winter exchange l#Japan, Kyoto
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

Shows a list of all trips and prints each trip’s name.

#### Format:

```sh
trip
```

#### Example Output:

```
Trip to Bali
Trip to SG
Summer break with family
```

### Selecting a Trip

Select an existing trip to perform operations on it.

#### Format:

```sh
trip [ID]
```

#### Examples:

```sh
trip 1
trip 2
```

---

## Managing Photos

### Adding a Photo

After selecting a trip, users can add a new photo to the trip.

#### Format:

```sh
add_photo f#FILENAME n#PHOTONAME c#CAPTION l#LOCATION
```

#### Examples:

```sh
add_photo f#desktop/bali.jpg n#Dog c#Wow l#Nusa Penida
add_photo f#desktop/Phuket.jpg n#Cat c#Amazing Fun l#Phi Phi
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

Shows a list of all photos in the selected trip’s folder.

#### Format:

```sh
list
```

#### Example Output:

```
Lunch in Bali
Bali temple
Beach photo
```

---

## Navigation

### Returning to Main Menu

To return to the main menu:

```sh
menu
```

