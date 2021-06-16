package alon.soffer.rachel_sandwiches;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest extends TestCase {

    private ActivityController<NewOrderActivity> newOrderActivityActivityController;
    private ActivityController<WaitingActivity> waitingActivityActivityController;
    private ActivityController<InProgressActivity> inProgressActivityActivityController;
    private ActivityController<ReadyActivity> readyActivityActivityController;


    @Before
    public void setup(){
        newOrderActivityActivityController = Robolectric.buildActivity(NewOrderActivity.class);
        waitingActivityActivityController = Robolectric.buildActivity(WaitingActivity.class);
        inProgressActivityActivityController = Robolectric.buildActivity(InProgressActivity.class);
        readyActivityActivityController = Robolectric.buildActivity(ReadyActivity.class);
    }

    @Test
    public void when_orderButtonClicked_then_waitingActivityIsLunched() {
        // setup
        newOrderActivityActivityController.create().visible();
        NewOrderActivity activityUnderTest = newOrderActivityActivityController.get();

        View orderButton = activityUnderTest.findViewById(R.id.makeOrderButton);
        orderButton.performClick();

        // verify
        WaitingActivity waitingActivity =
        assertTrue(userInput.isEmpty());
    }

}

