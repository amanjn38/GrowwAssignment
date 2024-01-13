Star Wars Characters App

This Android application utilizes the MVVM (Model-View-ViewModel) architecture, Single Activity Multiple Fragments pattern, and Room Database to display Star Wars characters, their movies, and provide offline support.
The app incorporates pagination for smooth scrolling, and a clean UI design. It also includes filtering and sorting options, with a bottom sheet for convenient user interaction.


Features

    Display Characters:
        The app presents a grid view of all Star Wars characters.
        Pagination is implemented for efficient loading of character data.

    Filtering and Sorting:
        Users can filter characters based on height, mass, and eye color.
        Sorting options include name, height, mass, created, and edited.
        Filter and sort options are accessible through a bottom sheet for a seamless user experience.

    Character Details:
        Clicking on a character displays a list of movies that character has participated in.
        Pagination is implemented for a grid view of movies.

    Offline Support:
        Room Database is employed to provide offline support, caching character and movie data locally.

    Single Activity Multiple Fragments:
        The app follows a Single Activity Multiple Fragments architecture for better maintainability and scalability.

    Dependency Injection:
        Dagger is used for dependency injection, enhancing code modularity.

    Unit Tests:
        Unit tests are included for ViewModel and Repository to ensure code reliability.


Usage

    Explore Characters:
        Launch the app to view a paginated GridView of Star Wars characters.
        Scroll through the list to discover various characters.

    Filter and Sort:
        Tap on the filter and sort icon to open a bottom sheet.
        Select desired filtering and sorting options to refine the character list.

    View Movies:
        Click on any character to view a list of movies they have been a part of.
        Movies are displayed in a paginated Grid.

    Offline Support:
        The app supports offline mode using Room Database.
        Cached data is utilized when the device is offline.

Installation

1.Clone the repository:

2.Open in Android Studio:

    Open Android Studio and select "Open an existing Android Studio project."
    Navigate to the cloned repository and open the project.

3.Run the App:

    Connect an Android device or use an emulator.
    Click on the "Run" button in Android Studio to install and run the app.

Project Structure
|-- app
    |-- src
        |-- main
            |-- java/com/finance/growwassignment
                |-- api
                |   |-- ApiService
                |-- db
                    |   |-- CharacterDao
                    |   |-- MovieDao
                    |   |-- RemoteKeysDao
                |-- di
                    |   |-- AppModule
                |-- models
                    |   |-- CharacterRemoteKeys
                    |   |-- CharacterResponse
                    |   |-- Filter
                    |   |-- MoviesResult
                    |   |-- Movies
                    |   |-- Result
                |-- paging
                    |   |-- CharacterMovieAdapter
                    |   |-- CharacterMoviePagingSource
                    |   |-- CharacterPagingAdapter
                    |   |-- CharacterPagingSource
                    |   |-- CharacterRemoteMediator
                    |   |-- LoaderAdapter
                |-- repository
                    |   |-- CharacterRepository
                    |   |-- MovieRepository
                |-- ui.screens
                    |   |-- CharacterFragment
                    |   |-- FilmsFragment
                    |   |-- MainActivity
                |-- utilities
                    |   |-- CustomButtonWithText
                    |   |-- CustomIncrementorButton
                    |   |-- DateTypeConverter
                    |   |-- FilterType
                    |   |-- GridSpacingItemDecoration
                    |   |-- OnItemClickListener
                    |   |-- GridSpacingItemDecoration
                    |   |-- StringListConverter
                |-- viewmodels
                    |   |-- CharacterViewModel
                    |   |-- MovieViewModel
                |-- CharacterDatabase
                |-- GrowwAssignmentApp
                |-- MovieDatabase
                |-- build.gradle
                    |-- ...

Dependencies

    Android Architecture Components
    Dagger
    Retrofit
    Room Database


Contribution

Contributions are welcome! Follow these steps:

    Fork the repository.
    Create a new branch.
    Make changes.
    Create a pull request.

Contact

For questions or feedback, contact amanjn38@gmail.com