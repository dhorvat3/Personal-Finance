package com.hr.foi.personalfinance;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.RobotiumUtils;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.Assert.assertNotNull;
import com.robotium.solo.Solo;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class LogInTest
{
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception
    {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                activityTestRule.getActivity());
    }

    @After
    public void tearDown() throws Exception
    {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    @Test
    public void testLogin() throws Exception
    {
        EditText korisnickoIme = (EditText) solo.getView(R.id.korime);
        EditText lozinka = (EditText) solo.getView(R.id.lozinka);

        Button logIn = (Button) solo.getView(R.id.login);

        solo.assertCurrentActivity("Krivi activity", LoginActivity.class);

        solo.enterText(korisnickoIme, "admin");
        solo.enterText(lozinka, "admin");
        solo.clickOnView(logIn);
        assertNotNull("Korisnicko ime prazno", korisnickoIme);
        assertNotNull("Lozinka prazna", lozinka);
    }
}
