# Instructor notes: StateFlow pitfalls

Reference only. **No runnable code for these pitfalls is in this project.** The throwaway demo functions (`togglePrayerBad` and a read-then-write version of `incrementTasbih`) are classroom-only and were left out of the source on purpose.

## Pitfall 1: changing a list in place

**Setup shown in class.** The state class is temporarily changed so that `donePrayers` is a `MutableList<String>` (with a `mutableListOf()` default). A "bad" ViewModel function adds to that list directly and is called from the Tracker checkbox. Students predict what happens first.

**What happens.** Nothing changes on screen, even though the list really did change.

**Why.** `_uiState.value` is still the *same object*, so `StateFlow` sees an equal value and does not emit. Compose never hears about the change. Writing `_uiState.value = _uiState.value.copy(donePrayers = sameList)` fails for the same reason: the list inside is the same instance, so `equals` returns `true`. (General rule: `StateFlow` skips equal values.)

**Fix.** Go back to a read-only `List<String>` and build a **new** list inside `update { }`, for example `state.donePrayers + name` or `state.donePrayers - name`, as `SalatKuViewModel.togglePrayer` does.

**Rule to say out loud.** State classes use `val` and `List`, never `var` and `MutableList`.

## Pitfall 2: read, then write, without `update`

**The shape of the mistake.** The function reads `_uiState.value` into a local variable, then assigns `_uiState.value = current.copy(...)` computed from that local.

**Why it is wrong.** With one tap on one thread it works. If two updates run at nearly the same time, both can read the same old value and one overwrites the other (a lost update). `update { }` does read, change and write as one safe step.

**Rule to say out loud.** Use `update { }` every time the new value depends on the old one. In this project every state change in `SalatKuViewModel` goes through `update { }`.

## Related traps (not asked for, kept for completeness)

- **Coroutines in `init`.** Do not start work in `init { viewModelScope.launch { ... } }`. It is hard to test, you do not control when it starts, and it can read properties declared below the `init` block before they are initialised. Write a named function (e.g. `loadPrayerTimes()`) and call it on purpose.
- **Process death.** A ViewModel survives rotation, not process death. `rememberSaveable` or `SavedStateHandle` solve that. This baseline only promises rotation.
- **`stateIn`.** For streams (repository, database), not for counting a list. `doneCount` stays a computed property on `SalatKuUiState`.

## How to demonstrate in class

Do the demos in a scratch branch, or undo them afterwards. Starting from this project, the demo for Pitfall 1 needs the state class change and a temporary extra ViewModel function, and the Tracker call site must be pointed at it. Because the ViewModel's functions are private (Single Event Entry Point feature), the demo function also has to be reachable (for example through a temporary `onEvent` branch). Reset to the committed state when done.
