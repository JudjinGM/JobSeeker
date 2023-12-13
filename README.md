# Job Seeker
An Android application in Kotlin for job search, leveraging the HeadHunter API for enhanced functionality. The app allows users to search for vacancies, set job filters, view details of individual job postings, and add preferred vacancies to a 'Favorites' list with local storage. Additionally, I implemented a feature that utilizes the Yandex Geocoder for location-based filtering.

Developed as graduation project on Yandex.Practicum.

## Device requirements
Compatible from Android 8 (Android API 26)

## App features

### Search vacancies
The user can search for job vacancies using any non-empty set of words in the search query. 

The search results constitute a list containing brief information about the job positions.  

<img src="https://github.com/JudjinGM/JobSeeker/blob/dev/info/search_screen.gif" width=30% height=30%>

### Filter vacancies for search
By using the filter settings, the user can refine certain search parameters conducted on the 'Search' screen. 

The filter allows specifying:

• Workplace - the region or locality mentioned in the vacancy as the working location.

• Industry - the field of activity of the organization posting the vacancy.

• Salary Level - the salary range corresponding to the one specified in the vacancy.

• Ability to hide vacancies for which the salary is not specified.


<img src="https://github.com/JudjinGM/JobSeeker/blob/dev/info/filter_screen.gif" width=30% height=30%>

### Favorite vacancies
Add vacancies to favorites. Explore your favorite vacancies:

<img src="https://github.com/JudjinGM/JobSeeker/blob/dev/info/favorites_screen.gif" width=30% height=30%>

### Vacancies
Explore vacancies detail information:

<img src="https://github.com/JudjinGM/JobSeeker/blob/dev/info/vacancy_screen.gif" width=30% height=30%>

### Simular vacancies
Explore simular vacancies:

<img src="https://github.com/JudjinGM/JobSeeker/blob/dev/info/simular_vacancies.gif" width=30% height=30%>

### Dark theme support

Job Seeker app supports dark theme as well as light theme:

<img src="https://github.com/JudjinGM/JobSeeker/blob/dev/info/dark_theme.gif" width=30% height=30%>

## Technology stack
Kotlin, MVVM with Clean Architecture, Kotlin Coroutines, Koin, Navigation Component, Retrofit2 (for interaction with HeadHunter API, Yandex Geocoder API), Gson, Room, Glide, Firebase-Crashlytics, KPermissions (handling permissions).
