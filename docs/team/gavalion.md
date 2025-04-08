# Gavalion - Project Portfolio Page

## Overview

Travel Diary is a desktop app for managing trips and travel memories, for use via a Command Line Interface (CLI). Travel Diary helps you log and organize trips, photos, and experiences more efficiently than traditional travel journaling apps.


### Summary of Contributions

#### Code Contribution
[View my contributions](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=gavalion&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

#### Enhancements Implemented
- **Parser**: Parse the input from the user
- **Exception**: Defined exceptions for parser and some for photos.
- **PhotoExtractMetadata**: Extract metadata from photos.
- **UI**: Improved UI to see where the user at.
- **CommandFactory**: Help to integrate CommandFactory and Parser along with other commands
- **Commands**: Collaborate to create most commands and make sure it corrects. 

# Bryan Salim - Project Portfolio Page

## Overview

**Travel Diary** is a desktop application for managing trips and travel memories, designed for use through a Command Line Interface (CLI). It enables users to efficiently log and organize trips, photos, and personal experiences — providing a more structured and customizable alternative to traditional travel journaling apps.

---

## Contributions

### 🌟 New Features

#### 1. Parser
- **What it does**: Parse user input from string to hashmap<String, String> based on the command and tags
- **Justification**: To send data to the commandFactory easier and made the code more modular.
- **Highlights**:
    - Designed the parsing structure and create a skeletal code for others so that they can create command easier.
    - Includes exception handling for invalid tags, duplicated tags and missing tags 
    - Reject invalid inputs due to wrong command name
- **Technologies**: Java I/O, JSON serialization

#### 2. Trip Class
- **What it does**: Represents the core data model for a trip, including its name, description, and associated photo album.
- **Highlights**:
    - Added missing compulsory parameter error to detect missing input.

#### 3. TripManager Class
- **What it does**: Manages a collection of `Trip` objects, including logic for adding, removing, and listing trips.
- **Highlights**:
    - Add mechanism where the trip can not be added to trip manager due to duplicated trip name

#### 4. Photo Class
- **What it does**: Represents the core data model for a photo, including its photoname, caption, and filepath.
- **Highlights**:
    - Make sure that the user does not have any missing input

#### 5. PhotoMetadataExtractor Class
- **What it does**: Extract photo gps latitude, longitude and date when the photo was taken.
- **Highlights**:
    - Create the constructor to extract the photo metadata.
    - Create exceptions to reject photos with missing metadata values, eg. missing gps data.

#### 6. TravelDairy Class
- **What it does**: Runs the main software function.
- **Highlights**:
    - Collaborated with `Ojassurana` to create the main function of the code based on his previous IP.
    - Design the logic on how to track the fsm value.

#### 7. Commands and commandFactory Classes
- **What it does**: Execute commands based on parser output and connects them to trip, tripmanager and photos.
- **Highlights**:
    - Collaborated with `Ojassurana` to crea
    - Create exceptions to reject photos with missing metadata values, eg. missing gps data.

---

### 💻 Code Contribution

- [📊 View my contributions on RepoSense](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=lethihongminh&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

---

### 🚀 Project Management

- Managed GitHub releases: **v1.0**, **v2.0**, **v2.1**
- Integrated the trip, tripmanager, parser (which back then was combined with command), photo and album developed in **v1.0**
- Hunt bugs and solve them in **v2.1**, mainly on storage
- Coordinated team milestones and documentation updates
- Facilitated pull request reviews and issue triaging

---

### 🛠️ Enhancements to Existing Features

- Extended the `help` command to provide an interactive and detailed guide to all available commands
- Refined UI feedback for invalid commands and enhanced CLI responsiveness

---

### 📚 Documentation

#### User Guide
- Added:
    - **Quick Start** section
    - Comprehensive **Command Summary**
    - Detailed usage documentation for the `help` and `close` commands

#### Developer Guide
- Documented:
    - **Architecture**
    - **Main components (Storage, Model, UI, Logic)**
    - **Interactions between components**
    - **Implementation details for storage**
    - **Getting started and setup instructions**
    - **Manual testing steps**
    - **Appendices: Requirements, User Stories**

---

### 🤝 Community Involvement

- Reviewed PRs with constructive, non-trivial feedback: [#12](#), [#32](#), [#19](#), [#42](#)
- Reported and suggested improvements for other teams: [1](#), [2](#), [3](#)
---
