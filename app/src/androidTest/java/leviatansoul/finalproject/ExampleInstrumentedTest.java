package leviatansoul.finalproject;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private FavStorage fav;

    @Before
    public void init(){
        fav = new FavStorage();
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("leviatansoul.finalproject", appContext.getPackageName());
      /*  fav.insertFav("299");
        String[] s = fav.getFavs();
        System.out.println("PRUEBA: "+s.toString());*/

    }
}
