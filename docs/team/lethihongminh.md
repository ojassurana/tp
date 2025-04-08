# Le Thi Hong Minh - Project Portfolio Page

## Overview

**Travel Diary** is a desktop application for managing trips and travel memories, designed for use through a Command Line Interface (CLI). It enables users to efficiently log and organize trips, photos, and personal experiences — providing a more structured and customizable alternative to traditional travel journaling apps.

---

## Contributions

### 🌟 New Features

#### 1. Storage System
- **What it does**: Implements persistent data storage by saving and loading trip data (trip, album, photos and metadata) from txt files, ensuring that user data is retained across sessions.
- **Justification**: Enables continuity of user data between uses of the application, which is essential for real-world usability.
- **Highlights**:
    - Designed the file structure and implemented read/write logic.
    - Includes exception handling and validation to ensure data integrity.

#### 2. Trip Class
- **What it does**: Represents the core data model for a trip, including its name, description, and associated album and photo.
- **Highlights**:
    - Includes logic for formatting trip details and tracking photo timelines.
    - Interfaces with the album class to provide cohesive trip representation.

#### 3. TripManager Class
- **What it does**: Manages a collection of `Trip` objects, including logic for adding, removing, and listing trips.
- **Highlights**:
    - Coordinates interactions between the model and storage components.
    - Encapsulates business logic such as duplicate detection and validations.

#### 4. Location Class
- **What it does**: Models individual locations visited during a trip, enhancing trip detail granularity.
- **Highlights**:
    - Integrates with trip, album and photo classes for location-based photo tracking.

---

### 💻 Code Contribution

- [📊 View my contributions on RepoSense](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=lethihongminh&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

---

### 🚀 Project Management

- Managed GitHub releases: **v1.0**, **v2.0**, **v2.1**
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
    - **Design: Architecture**
    - **Design: How the architecture components interact with each other**
    - **Design: Component Details: Main components**
    - **Design: Storage Components**
    - **Implementation details for storage**
    - **Getting started and setup instructions**
    - **Manual testing steps**
    - **Appendices: Requirements (User Stories)**

---

### 🤝 Community Involvement

- Reviewed PRs with constructive, non-trivial feedback: [#22](#)
- Reported and suggested improvements for other teams: [29](#)
---
