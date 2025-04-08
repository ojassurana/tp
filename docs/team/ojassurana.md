[User Guide](../UserGuide.md) | [Developer Guide](../DeveloperGuide.md) | [About Us](../AboutUs.md) | [View on GitHub](https://github.com/ojassurana/tp)

# Ojas Surana - Project Portfolio Page

## Project: Travel Diary

Travel Diary is a desktop app for managing trips and travel memories, optimized for use via a Command Line Interface (CLI). It helps travelers log and organize trips, photos, and experiences more efficiently than traditional travel journaling apps.

* **Code contributed**: [RepoSense link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=ojassurana&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

* **New Feature**: Designed and implemented the Album class for photo management within trips.
  * What it does: Provides an organizational structure for storing, displaying, and managing photos within a trip.
  * Justification: This feature forms the core functionality of the app, allowing users to track their memories through photos.
  * Highlights: Album intelligently sorts photos by datetime and calculates distances between consecutive photos to track journey progression.

* **New Feature**: Developed offline geo-tagging system for photos.
  * What it does: Extracts GPS coordinates from photo EXIF data and converts them to human-readable city/country names without internet connection.
  * Justification: Allows users to identify photo locations even when traveling without internet access.
  * Highlights: Implemented a KD-tree algorithm with a dataset of cities with population above 1000 for efficient offline reverse geocoding.
  * Challenges & Implementation:
    * Created a spatial search solution using KD-tree data structure (O(log n) time complexity) to overcome the limitation of requiring online services like Google Maps.
    * The KD-tree partitions coordinate space to quickly identify the nearest city to GPS coordinates without exhaustive searching.
    * Addressed technical challenges including EXIF metadata extraction, dataset size optimization, and seamless Photo class integration.
  * Limitations:
    * May return incorrect city names for photos taken near city borders, as it identifies the nearest city center by coordinates.
    * Future implementation would use city boundary data to improve location accuracy.

* **New Feature**: Built the command architecture using Command pattern.
  * What it does: Created an extensible command system that handles all user interactions with proper state management.
  * Justification: This modular design provides clean separation of concerns and makes adding new commands straightforward.
  * Highlights: Implemented a finite state machine to manage application state (Trip Page vs. Photo Page) and proper command routing.

* **New Feature**: Architected the Parser system for processing user input.
  * What it does: Parses user commands with complex tag-based syntax (e.g., `add_photo f#FILENAME n#PHOTONAME c#CAPTION`) into structured data for Command classes.
  * Justification: A robust parser is essential for correctly interpreting user inputs and providing appropriate feedback, especially with a tag-based command system.
  * Highlights: 
    * Implemented flexible parsing logic that handles various command formats and special characters.
    * Created a tag-based parameter extraction system that correctly handles escaped delimiters and special characters.
    * Designed error handling for malformed commands to provide clear feedback to users.
    * Integrated the parser with the command factory to seamlessly convert user input into executable commands.

* **New Feature**: Developed the UI component for user interaction.
  * What it does: Provides a clean, consistent interface for displaying information and collecting user input through the CLI.
  * Justification: A well-designed UI is crucial for usability, especially in a CLI application where visual cues are limited.
  * Highlights:
    * Implemented state-aware UI that adapts display based on the current application context (Trip Page vs. Photo Page).
    * Created a consistent formatting system with separators and padding for improved readability.
    * Added ASCII art logo capability to enhance the visual appeal of the application.
    * Designed a modular approach with dedicated methods for different UI elements, making future UI enhancements straightforward.

* **New Feature**: Designed the application's Finite State Machine (FSM) architecture.
  * What it does: Controls the application flow between Trip mode and Photo mode, ensuring commands are context-appropriate.
  * Justification: A state machine approach creates an intuitive navigation model for users, with context-specific commands and views.
  * Highlights:
    * Created a centralized FSM state value to track whether users are at the main menu (Trip Page) or inside a trip (Photo Page).
    * Integrated state transitions with all commands, allowing commands to update the application state as needed.
    * Ensured proper command routing based on current state, presenting only relevant commands to users.
    * Implemented state persistence during command execution to maintain application context.

* **Project management**:
  * Managed GitHub releases: **v1.0**, **v2.0**, **v2.1**
  * Tracked issues from the PE-D and worked on them: cosmetic and fundamental
  * Facilitated pull request reviews and issue triaging

* **Enhancements to existing features**:
  * Improved Photo class to extract and utilize EXIF metadata (commit 2e0622a)
  * Added validation for image file formats to ensure only JPG files with GPS data are accepted (commits c22ec9f, 2c4628b)
  * Enhanced UI text and error messages for better user experience (commits 9c705c3, c44de02)
  * Refactored code to ensure cross-platform compatibility (commits 13c506f, b9bf0ed)

* **Documentation**:
  * User Guide:
    * Added documentation for photo management features
    * Added important image format requirements and GPS data prerequisites
    * Added explanations for trip date range calculations and location tracking
  * Developer Guide:
    * Did everything in the Ui Component section
    * Did everything in the Trip Manager component

* **Community**:
  * Reviewed PRs from team members (#35, #37, #39, #41 and others)
  * Helped teammates debug issues with Git and testing frameworks
  * Fixed numerous Gradle compatibility issues affecting different OS environments
