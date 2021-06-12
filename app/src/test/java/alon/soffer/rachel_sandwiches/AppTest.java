package alon.soffer.rachel_sandwiches;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


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

    private ActivityController<MainActivity> activityController;
    private TodoItemsHolder mockHolder;

    @Before
    public void setup(){
        mockHolder = Mockito.mock(TodoItemsHolder.class);
        // when asking the `mockHolder` to get the current items, return an empty list
        Mockito.when(mockHolder.getCurrentItems())
                .thenReturn(new ArrayList<>());

        activityController = Robolectric.buildActivity(MainActivity.class);

        // let the activity use our `mockHolder` as the TodoItemsHolder
        MainActivity activityUnderTest = activityController.get();
        activityUnderTest.holder = mockHolder;
    }

    @Test
    public void when_activityIsLaunched_then_theEditTextStartsEmpty() {
        // setup
        activityController.create().visible();
        MainActivity activityUnderTest = activityController.get();
        EditText editText = activityUnderTest.findViewById(R.id.editTextInsertTask);
        String userInput = editText.getText().toString();
        // verify
        assertTrue(userInput.isEmpty());
    }

}

