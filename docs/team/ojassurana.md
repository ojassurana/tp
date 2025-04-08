[User Guide](../UserGuide.md) | [Developer Guide](../DeveloperGuide.md) | [About Us](../AboutUs.md) | [View on GitHub](https://github.com/ojassurana/tp)

# Ojas Surana - Project Portfolio Page

## Project: Travel Diary

Travel Diary is a desktop app for managing trips and travel memories, optimized for use via a Command Line Interface (CLI). It helps travelers log and organize trips, photos, and experiences more efficiently than traditional travel journaling apps.

* **Code contributed**: [RepoSense link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=ojassurana&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

* **New Feature**: Designed and implemented the Album class for photo management.
  * What it does: Organizes photos within trips with metadata support.
  * Justification: Core functionality enabling users to track travel memories via photos.
  * Highlights: Sorts photos chronologically and calculates distances between consecutive photos.

* **New Feature**: Developed offline geo-tagging system for photos.
  * What it does: Converts GPS coordinates to city/country names without internet.
  * Justification: Essential for travelers identifying photo locations without connectivity.
  * Highlights: Used KD-tree algorithm with population data for efficient reverse geocoding.
  * Limitations: May be inaccurate near city borders; future version would use boundary data.

* **New Feature**: Built the command architecture using Command pattern.
  * What it does: Processes user interactions with proper state management.
  * Justification: Enables clean separation of concerns and extends functionality easily.
  * Highlights: Implements FSM to manage Trip Page vs. Photo Page contexts.

* **New Feature**: Architected the Parser system for processing user input.
  * What it does: Parses user commands with complex tag-based syntax (e.g., `add_photo f#FILENAME n#PHOTONAME c#CAPTION`) into structured data for Command classes.
  * Justification: A robust parser is essential for correctly interpreting user inputs and providing appropriate feedback, especially with a tag-based command system.
  * Highlights: 
    * Implemented flexible parsing logic that handles various command formats and special characters.
    * Created a tag-based parameter extraction system that correctly handles escaped delimiters and special characters.
    * Designed error handling for malformed commands to provide clear feedback to users.
    * Integrated the parser with the command factory to seamlessly convert user input into executable commands.

* **New Feature**: Developed the UI component for user interaction.
  * What it does: Displays information based on application context.
  * Justification: Critical for usability in text-based interfaces.
  * Highlights: Adapts display based on current application state.

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
  * Improved Photo class to use EXIF metadata (commit 2e0622a)
  * Added validation for JPG files with GPS data (commits c22ec9f, 2c4628b)
  * Enhanced error messages for better user experience (commits 9c705c3, c44de02)

* **Documentation**:
  * User Guide: Added photo management, format requirements, and location tracking
  * Developer Guide: Did everything in the Ui Component section and Trip Manager component

* **Community**:
  * Reviewed PRs from team members (#35, #37, #39, #41)
  * Helped teammates with Git and testing frameworks
  * Fixed Gradle compatibility issues across platforms
