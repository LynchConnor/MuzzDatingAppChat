# Muzz Chat – Android Exercise

A single-screen chat between two fixed users, built with Jetpack Compose and Room. Messages persist locally, update the UI through a `Flow`, and are grouped by the spacing and section rules from the brief.

## Running it

1. Open the project in Android Studio and run the `app` configuration on an emulator or a device (API 35+).
2. Run the unit tests with `./gradlew :app:testDebugUnitTest`.

On first launch the database is seeded with a sample conversation. To reseed it, clear the app's data:

```
adb shell pm clear com.muzz.datingappchat
```

## How the brief is met

| Requirement | Implementation |
|---|---|
| Newest messages at the bottom | `LazyColumn` with `reverseLayout = true` and a newest-first list, so the list starts at the bottom and stays anchored to the input. It scrolls to the newest message when one arrives. |
| Sent vs received bubbles | My messages are pink and right-aligned. The other user's are grey and left-aligned. |
| Small separation | `MessageSeparation.messageSeparation` gives a 4dp gap below a message when the **next** message is from the same sender and is sent **less than 20s** later. Otherwise the gap is 12dp. |
| Section headers | `MessageSeparation.showsSectionHeader` shows a header ("**Thursday** 11:59") above a message when there is **no previous message**, or the previous one was sent **more than an hour** earlier. |
| Two-way messaging | The ⋯ menu (or tapping the avatar) switches who you are sending as, so both sides of the conversation can be written from one device. |
| No network | All messages are created on the device. A sample conversation is inserted on first launch when the database is empty. |
| Persistence (bonus) | Room stores messages. The DAO returns a `Flow`, so a sent message appears in the list without any manual refresh. |

## Architecture

A single module using MVVM and unidirectional data flow:

```
Room (MessagesDao: Flow<List<Message>>)
        │
ChatViewModel  ── seeds on first launch, sends messages, holds the current sender
        │ StateFlow
ChatView       ── wires the ViewModel to the screen
        │
ChatScreen     ── stateless Compose UI (previewable with sample data)
```

- **`shared/`**: the data layer. It holds the `Message` entity, `MessagesDao`, the Room database and a `LocalDateTime` converter.
- **`main/viewModel/`**: `ChatViewModel`. Database work runs in `viewModelScope` off the main thread, so it isn't repeated on recomposition and survives configuration changes.
- **`main/view/`**: the Compose UI. `ChatScreen` takes plain data and callbacks and owns no state, which makes it easy to preview and reason about.
- **`main/MessageSeparation.kt`**: the brief's two layout rules as pure functions with no Android UI dependencies, so they can be unit tested directly.

I deliberately left out a repository layer, use cases, dependency injection and multiple modules. With one screen and one data source, they would add files without adding clarity. See [With more time](#with-more-time) for when I would add them.

## Key decisions

- **Each rule uses the neighbour it describes.** The list is newest first, so for item `index` the previous (older) message is `index + 1` and the next (newer) message is `index - 1`. The header rule uses the previous message and the spacing rule uses the next one. That way each function reads like the sentence in the brief, and the first message gets its header without a special case.
- **Rules are pure functions.** Keeping the time maths out of composables means the edge cases (exactly 20s, exactly 1h) are covered by fast JVM tests rather than by eye.
- **Boundaries follow the brief's wording.** "Less than 20 seconds" means 20s exactly is a normal gap. "More than an hour" means exactly 1h has no header.
- **The header gap is measured from the previous message, not the last header.** Several messages 50 minutes apart never get a header, even if the conversation covers hours.
- **Seed data doubles as a test fixture.** `MockMessages.kt` is written to show every rule on screen: a 19s pair, exactly 20s, a sender change, exactly 1h, 1h 1s, a chain of 50-minute gaps, a new day and a group of three. Each entry has a comment stating the expected result.
- **The section header follows the device's 12/24-hour setting.**
- **Muzz styling.** The colours are taken from the Muzz brand pink, with light and dark themes. Dynamic colour is turned off so the app looks the same on every device.

## Assumptions

- There are exactly two fixed users and one conversation. "Me" is `Contact.Connor`, and the other user is `Contact.Muzz`.
- Times are stored as `LocalDateTime` in device local time. Time zones and changes to the device clock are out of scope.
- Messages are text only. Empty or whitespace-only messages are not sent.
- The back button, read receipts and typing indicators in the design screenshot weren't part of the brief, so I left them out.
- The section header shows the weekday name, as in the brief's example ("Thursday 11:59").

## Language techniques

- **Sealed interface** `ChatUIState` with an exhaustive `when` in the UI.
- **`StateFlow` / `Flow`** from Room through to `collectAsStateWithLifecycle`, so collection stops when the app is in the background.
- **Enum with behaviour**: `Contact` has its `id` and a `toggle()`.
- **Kotlin idioms**: `?:` early returns, `getOrNull` for list neighbours, destructuring (`val (container, content) = …`), default and named arguments, and the `@Volatile` singleton database.
- **Compose**: `remember`/`derivedStateOf` to avoid needless recomposition, stable `key`s and `animateItem()` for list animations, and `@PreviewLightDark` previews.

## Testing

`MessageSeparationTest` covers both layout rules:

- **Spacing**: 15s and 19s are small, 20s and 21s are normal, a different sender is always normal, and the other user's messages group too. A whole conversation is also checked in on-screen order.
- **Headers**: the first message always has one, exactly 1h has none, 1h 1s has one, and the gap is measured from the previous message rather than the last header.

## Limitations

- **Two-way messaging is a manual switch**, not a simulated reply from the other user.
- **No message ordering in SQL.** `fetchMessages()` relies on insertion order. That works because messages are only ever appended, but an `ORDER BY messageSent` would make it explicit.
- **The ViewModel isn't the single source of the message list.** `ChatView` collects the DAO `Flow` directly. Combining messages and sender into one `StateFlow` in the ViewModel would be cleaner and easier to test.
- **Errors are only logged.** If an insert fails (for example, when storage is full), the user isn't told.
- **No paging.** The whole conversation is loaded into memory, which is fine for a demo but not for long chat histories.
- **No UI or instrumented tests.** Only the pure rules are tested.

## With more time

- Move message collection into the ViewModel (`combine(messages, contact).stateIn(…)`) and test it with a fake DAO and an injected `Clock`.
- Add a repository in front of Room. That's where a real backend would plug in to sync with the server, with Room staying the source of truth for the UI.
- Add Hilt once there is more than one screen or dependency to inject.
- Add a simulated reply from the other user, plus a typing indicator.
- Add delivery and read ticks, message failure and retry states, and timestamps on long press.
- Add Paging 3 for long conversations.
- Add Compose UI tests for sending a message and for the header and spacing layout, and screenshot tests for light and dark themes.
- Add accessibility work: content descriptions that read the sender and time for each bubble, and testing with large font sizes and TalkBack.
- Add a CI workflow on GitHub Actions that runs the tests and lint on every push.

## Use of AI

I used Claude (Claude Code) as a pair programmer during this exercise. I used it to plan the approach, review my code, find bugs (such as reversed `Duration.between` arguments and off-by-one neighbour indices), write the seed data, and help write the unit tests and this README. I made the design decisions and wrote, read and tested the code myself, and I can explain any part of it.
