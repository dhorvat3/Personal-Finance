package com.hr.foi.personalfinance;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class RegisterTest
{
    private Solo solo;

    @Rule
    public ActivityTestRule<RegisterActivity> activityTestRule =
            new ActivityTestRule<>(RegisterActivity.class);

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
    public void testRegister() throws Exception
    {
        EditText ime = (EditText) solo.getView(R.id.name);
        EditText prezime = (EditText) solo.getView(R.id.surname);
        EditText email = (EditText) solo.getView(R.id.email);
        EditText korisnickoIme = (EditText) solo.getView(R.id.username);
        EditText lozinka = (EditText) solo.getView(R.id.pass);

        Button register = (Button) solo.getView(R.id.register);

        solo.assertCurrentActivity("Krivi activity", RegisterActivity.class);

        solo.enterText(ime, "TestIme");
        solo.enterText(prezime, "TestPrezime");
        solo.enterText(email, "TestEmail");
        solo.enterText(korisnickoIme, "TestKorisnickoIme");
        solo.enterText(lozinka, "TestLozinka");

        solo.clickOnView(register);
        assertNotNull("Ime prazno", ime);
        assertNotNull("Prezime prazno", prezime);
        assertNotNull("Email prazan", email);
        assertNotNull("Korisnicko ime prazno", korisnickoIme);
        assertNotNull("Lozinka prazna", lozinka);
    }
}
