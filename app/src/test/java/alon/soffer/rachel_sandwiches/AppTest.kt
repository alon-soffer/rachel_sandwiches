package alon.soffer.rachel_sandwiches

import android.content.ComponentName
import android.os.Looper
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = [28])
class AppTest : TestCase() {

    @Test
    fun when_newOrderUploadsToDB_then_waitingActivityIsLunched() {
        // setup
        val newOrderActivityActivityController = Robolectric.buildActivity(NewOrderActivity::class.java)
        newOrderActivityActivityController.create().visible()
        val activityUnderTest = newOrderActivityActivityController.get()
        val shadow = shadowOf(activityUnderTest)
        activityUnderTest.onDbSuccess("")

        // verify
        val component = shadow.nextStartedActivity.component
        assertEquals(ComponentName(activityUnderTest, WaitingActivity::class.java), component)
        assertTrue(activityUnderTest.isFinishing)
    }


    @Test
    fun when_orderMarkedDone_then_goToNewOrderAndNoCurrentOrderInApp() {
        // setup
        val readyActivityController = Robolectric.buildActivity(ReadyActivity::class.java)
        readyActivityController.create().visible()
        val activityUnderTest = readyActivityController.get()
        val shadow = shadowOf(activityUnderTest)
        val order = SandwichOrder()
        order.status = RachelApplication.READY
        activityUnderTest.onStatusChangedToDone(order)

        // verify
        val component = shadow.nextStartedActivity.component
        assertEquals(ComponentName(activityUnderTest, NewOrderActivity::class.java), component)
        assertTrue(activityUnderTest.isFinishing)
    }

    @Test
    fun when_appOpensWithOrderInProgress_then_InProgressActivityOpens(){
        val welcomeActivityController = Robolectric.buildActivity(WelcomeActivity::class.java)
        welcomeActivityController.create().visible()
        val activityUnderTest = welcomeActivityController.get()

        val shadow = shadowOf(activityUnderTest)
        val order = SandwichOrder()
        order.status = RachelApplication.IN_PROGRESS
        activityUnderTest.onGotOrderFromDb(order)

        // verify
        val component = shadow.nextStartedActivity.component
        assertEquals(ComponentName(activityUnderTest, InProgressActivity::class.java), component)
        assertTrue(activityUnderTest.isFinishing)
    }
}