## Muzz Chat

A simple chat screen between two users, built with Jetpack Compose and Room.

## Assumptions
A few assumptions were made throughout this exercise:
- It didn't matter when switching between users that it appeared that they were the main user. When switching we assume that the current user is still 'Connor', and when switching we are just inputting the text. Everything appears from 'Connor's perspective.
- The back button had no use, so it wasn't included in the app.
- No networking logic was included as requested in 'Not Required', so the only fetching was from Room.
- Messages exactly 1 hour do not get a section; messages with a gap of more than 1 hour do
- Messages exactly 20s apart get a normal gap; only messages under 20s apart get the smaller gap

## Decisions
- I've kept the project intentionally simple using the principles of KISS. Only programming the essential features of a chat app and keeping the code abstracted with clear comments.
- MVVM was chosen as the design pattern, mainly because this is what I'm used to and for a simple app like this it was ideal. An even simpler design pattern could've been implemented with just MV, but separating code makes it easier to understand and keep logic separated for cleaner UIs and for understanding the code better as it's separated into its core components.
- The decision to implement a UIState was chosen in order to split up the view logic, when displaying loading, errors and messages, rather than having separate MutableStateFlow(false), this way it's a lot cleaner and can handle the different states.
- Additionally, the decision was made to make all viewModel properties read-only from the view so it can only change from inside the viewModel
- Instead of fetching the messages and storing it in the viewModel, I decided to use flows inside of Room so the messages auto update. This is ideal because after each message we don't have to append the array and/or fetch the messages again, it happens automatically inside of Room @Query Flow.
- I created a separate MessageSeparation object that holds the logic of message separation and when to show sections, matching the logic required in the project. I decided to make it a separate object so it was testable and reusable.
- try catch was added as a decision, despite not specified in the document, it's essential to prevent issues relating to out of storage errors that can come around as a result of Room - a broken app is no good, so it's better safe than sorry.
- I took the decision that when switching between Contacts it would always show Connor as the main user, any message from Muzz would show like it was a response to Connor. This was to save on complexity and time.
- As a chat app, I made the decision as well to hide the keyboard when scrolling, this is pretty standard across all chat apps and although it wasn't mentioned in the spec, it is necessary as part of a chat feature.

## Tests
- Tests check the logic in MessageSeparation to make sure the messages have the correct separation and sections are shown correctly. Claude and I worked on the tests together, including the mock data, to make sure every rule in the brief was covered. I reviewed and ran them, and fixed the bugs they found (such as reversed `Duration.between` arguments and the wrong neighbouring message being used).

## Good techniques
- As part of my daily app development, I use collectAsStateWithLifecycle(), derivedStateOf and remember in the views, this is so that data stays within the lifecycle of the view and stops collecting in the background, derivedStateOf prevents recompositions so it only recomposes when values change and remember keeps values across recomposition. 
- I used sealed interfaces for managing UIState, this was so I can use when, if I decide to add another state, then it will shout at me to add that state to the view.
- Abstracting code into components and separating them into folders with similar functions, this is for simplicity's sake.

## Limitations
- For the errors it would be good to provide the user with a better error, like if it threw an error due to storage, we could tell them they're out of storage in a user-friendly way.
- Once the chat is there, it stays, there is no way of deleting the chat and starting from scratch. If given more time it would be useful to be able to refresh it.
- As mentioned in decisions, I made switching contacts keep Connor as the main contact, this means switching to Muzz still appears like it's Connor's chat, although this was intentional, with more time it would be beneficial that when switching it shows the chat from the individual contact's perspective.
- Given more time I would also test the viewModel with a fake dao to check data was fetched correctly and create UI tests to confirm components worked as intended and to discover any potential bugs.
- Supporting those with disabilities is important when implementing accessibility features, this would be ideal in a submitted app, but for simplicity, this feature was ignored.

## Use of AI
- Claude code was used to help me build tests for the logic, as well as building mock data for the previews and seed data - to save me time building it myself whilst I worked on the logic and structure of the app.
- Claude assisted in building certain UI components and built the colour theme to match the theme in the example UI.
- Claude assisted in the README.md, including structure.