[User Guide](../UserGuide.md) | [Developer Guide](../DeveloperGuide.md) | [About Us](../AboutUs.md) | [View on GitHub](https://github.com/ojassurana/tp)

# Ojas Surana - Project Portfolio Page

## Project: Travel Diary

Travel Diary is a desktop app for managing trips and travel memories, optimized for use via a Command Line Interface (CLI). It helps travelers log and organize trips, photos, and experiences more efficiently than traditional travel journaling apps.

Given below are my contributions to the project.

* **New Feature**: Designed and implemented the Album class for photo management within trips.
  * What it does: Provides an organizational structure for storing, displaying, and managing photos within a trip.
  * Justification: This feature forms the core functionality of the app, allowing users to track their memories through photos.
  * Highlights: Album intelligently sorts photos by datetime and calculates distances between consecutive photos to track journey progression.

* **New Feature**: Developed offline geo-location system for photos.
  * What it does: Extracts GPS coordinates from photo EXIF data and converts them to human-readable city/country names without internet connection.
  * Justification: Allows users to identify photo locations even when traveling without internet access.
  * Highlights: Implemented a KD-tree algorithm with a dataset of cities with population above 1000 for efficient offline reverse geocoding.

* **New Feature**: Built the command architecture using Command pattern.
  * What it does: Created an extensible command system that handles all user interactions with proper state management.
  * Justification: This modular design provides clean separation of concerns and makes adding new commands straightforward.
  * Highlights: Implemented a finite state machine to manage application state (Trip Page vs. Photo Page) and proper command routing.

* **Code contributed**: [RepoSense link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=ojassurana&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

* **Project management**:
  * Set up initial repository structure and project architecture
  * Managed pull requests and code reviews (12+ PRs merged)
  * Debugged cross-platform compatibility issues to ensure consistent functionality

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
    * Created UML diagrams for Trip class and UI components
    * Documented album component design and interactions
    * Added and updated Glossary section with domain-specific terminology

* **Community**:
  * Reviewed PRs from team members (#35, #37, #39, #41 and others)
  * Helped teammates debug issues with Git and testing frameworks
  * Fixed numerous Gradle compatibility issues affecting different OS environments

* **Testing**:
  * Created comprehensive test suite for command classes (commits 2b5cacd, f07c9c9)
  * Implemented tests for Album class functionality (commit 892087c)
  * Fixed test compatibility issues to ensure consistent test execution across platforms