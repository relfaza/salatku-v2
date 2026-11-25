package com.example.shalatku

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Test

// Plain JVM tests: the ViewModel has no Context, so no emulator is needed.
// M13: state is read from vm.uiState.value, and every action goes through onEvent (CP3).
class SalatKuViewModelTest {

    private fun SalatKuViewModel.tap(times: Int) =
        repeat(times) { onEvent(SalatKuEvent.TasbihIncremented) }

    private val SalatKuViewModel.state: SalatKuUiState get() = uiState.value

    @Test
    fun startsEmpty() {
        val vm = SalatKuViewModel()
        assertEquals(SalatKuUiState(), vm.state)
        assertEquals(0, vm.state.tasbihCount)
        assertEquals(0, vm.state.completedRounds)
        assertEquals(33, vm.state.roundSize)
        assertTrue(vm.state.donePrayers.isEmpty())
        assertEquals(0, vm.state.doneCount)
        assertEquals(0, vm.state.selectedDay)
    }

    @Test
    fun incrementTasbihCountsUp() {
        val vm = SalatKuViewModel()
        vm.tap(12)
        assertEquals(12, vm.state.tasbihCount)
        assertEquals(0, vm.state.completedRounds)
    }

    @Test
    fun roundIsNotCompleteAt32Taps() {
        val vm = SalatKuViewModel()
        vm.tap(32)
        assertEquals(32, vm.state.tasbihCount)
        assertEquals(0, vm.state.completedRounds)
    }

    @Test
    fun the33rdTapCompletesARoundAndStartsANewOne() {
        val vm = SalatKuViewModel()
        vm.tap(33)
        assertEquals(0, vm.state.tasbihCount)
        assertEquals(1, vm.state.completedRounds)
    }

    @Test
    fun countContinuesInTheNewRound() {
        val vm = SalatKuViewModel()
        vm.tap(33 + 5)
        assertEquals(5, vm.state.tasbihCount)
        assertEquals(1, vm.state.completedRounds)
    }

    @Test
    fun roundsAccumulate() {
        val vm = SalatKuViewModel()
        vm.tap(33 * 3 + 1)
        assertEquals(1, vm.state.tasbihCount)
        assertEquals(3, vm.state.completedRounds)
    }

    @Test
    fun resetTasbihClearsCountAndRounds() {
        val vm = SalatKuViewModel()
        vm.tap(40)
        vm.onEvent(SalatKuEvent.TasbihReset)
        assertEquals(0, vm.state.tasbihCount)
        assertEquals(0, vm.state.completedRounds)
    }

    @Test
    fun resetTasbihLeavesPrayersAndDayAlone() {
        val vm = SalatKuViewModel()
        vm.onEvent(SalatKuEvent.PrayerToggled("Subuh"))
        vm.onEvent(SalatKuEvent.DaySelected(3))
        vm.tap(5)
        vm.onEvent(SalatKuEvent.TasbihReset)
        assertEquals(listOf("Subuh"), vm.state.donePrayers)
        assertEquals(3, vm.state.selectedDay)
    }

    @Test
    fun togglePrayerTicksThenUnticks() {
        val vm = SalatKuViewModel()
        vm.onEvent(SalatKuEvent.PrayerToggled("Subuh"))
        assertEquals(listOf("Subuh"), vm.state.donePrayers)
        vm.onEvent(SalatKuEvent.PrayerToggled("Subuh"))
        assertTrue(vm.state.donePrayers.isEmpty())
    }

    @Test
    fun togglePrayerKeepsTickOrderAndNeverDuplicates() {
        val vm = SalatKuViewModel()
        vm.onEvent(SalatKuEvent.PrayerToggled("Dzuhur"))
        vm.onEvent(SalatKuEvent.PrayerToggled("Subuh"))
        vm.onEvent(SalatKuEvent.PrayerToggled("Isya"))
        assertEquals(listOf("Dzuhur", "Subuh", "Isya"), vm.state.donePrayers)
        vm.onEvent(SalatKuEvent.PrayerToggled("Subuh"))
        assertEquals(listOf("Dzuhur", "Isya"), vm.state.donePrayers)
    }

    @Test
    fun selectDayStoresTheIndex() {
        val vm = SalatKuViewModel()
        vm.onEvent(SalatKuEvent.DaySelected(4))
        assertEquals(4, vm.state.selectedDay)
        vm.onEvent(SalatKuEvent.DaySelected(0))
        assertEquals(0, vm.state.selectedDay)
    }

    @Test
    fun separateInstancesDoNotShareState() {
        // This is why the app must use viewModel() and never the constructor in a composable.
        val a = SalatKuViewModel()
        val b = SalatKuViewModel()
        a.tap(7)
        a.onEvent(SalatKuEvent.PrayerToggled("Ashar"))
        a.onEvent(SalatKuEvent.DaySelected(2))
        assertNotSame(a, b)
        assertEquals(0, b.state.tasbihCount)
        assertTrue(b.state.donePrayers.isEmpty())
        assertEquals(0, b.state.selectedDay)
    }

    // --- HW12 Part A ---

    @Test
    fun clearPrayersEmptiesTheListAndLeavesTheRestAlone() {
        val vm = SalatKuViewModel()
        vm.onEvent(SalatKuEvent.PrayerToggled("Subuh"))
        vm.onEvent(SalatKuEvent.PrayerToggled("Isya"))
        vm.onEvent(SalatKuEvent.DaySelected(2))
        vm.tap(4)
        vm.onEvent(SalatKuEvent.PrayersCleared)
        assertTrue(vm.state.donePrayers.isEmpty())
        assertEquals(2, vm.state.selectedDay)
        assertEquals(4, vm.state.tasbihCount)
    }

    // --- HW12 Part B ---

    @Test
    fun setRoundSizeResetsCountButKeepsCompletedRounds() {
        val vm = SalatKuViewModel()
        vm.tap(33 + 10)
        vm.onEvent(SalatKuEvent.RoundSizeSelected(99))
        assertEquals(99, vm.state.roundSize)
        assertEquals(0, vm.state.tasbihCount)
        assertEquals(1, vm.state.completedRounds)
    }

    @Test
    fun aRoundEndsAtTheChosenRoundSize() {
        val vm = SalatKuViewModel()
        vm.onEvent(SalatKuEvent.RoundSizeSelected(100))
        vm.tap(99)
        assertEquals(99, vm.state.tasbihCount)
        assertEquals(0, vm.state.completedRounds)
        vm.tap(1)
        assertEquals(0, vm.state.tasbihCount)
        assertEquals(1, vm.state.completedRounds)
    }

    // --- M13 CP2: derived state ---

    @Test
    fun doneCountFollowsDonePrayers() {
        val vm = SalatKuViewModel()
        vm.onEvent(SalatKuEvent.PrayerToggled("Subuh"))
        vm.onEvent(SalatKuEvent.PrayerToggled("Dzuhur"))
        assertEquals(2, vm.state.doneCount)
        vm.onEvent(SalatKuEvent.PrayersCleared)
        assertEquals(0, vm.state.doneCount)
    }
}
