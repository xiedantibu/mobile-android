
package com.trovebox.android.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Instrumentation;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import com.actionbarsherlock.app.ActionBar;
import com.trovebox.android.app.AlbumsFragment;
import com.trovebox.android.app.GalleryFragment;
import com.trovebox.android.app.HomeFragment;
import com.trovebox.android.app.MainActivity;
import com.trovebox.android.app.SyncFragment;
import com.trovebox.android.app.TagsFragment;

public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity>
{

    private MainActivity activity;
    private Fragment fragment;
    Instrumentation instrumentation;

    public MainActivityTest()
    {
        super("com.trovebox.android.app", MainActivity.class);
    }

    /**
     * @see android.test.ActivityInstrumentationTestCase2#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        activity = this.getActivity();
        instrumentation = getInstrumentation();
    }

    public void testPreconditions()
    {
        fragment = activity.getSupportFragmentManager().findFragmentById(
                android.R.id.content);
        assertNotNull(fragment);
        assertTrue(fragment instanceof HomeFragment);
    }

    public void testTabSelection() throws InterruptedException
    {

        testSingleTabSelection(MainActivity.GALLERY_INDEX,
                GalleryFragment.class);
        testSingleTabSelection(MainActivity.SYNC_INDEX,
                SyncFragment.class);
        testSingleTabSelection(MainActivity.ALBUMS_INDEX,
                AlbumsFragment.class);
        testSingleTabSelection(MainActivity.TAGS_INDEX,
                TagsFragment.class);
        testSingleTabSelection(MainActivity.HOME_INDEX,
                HomeFragment.class);

    }

    private void testSingleTabSelection(
            final int index, Class<?> fragmentClass)
            throws InterruptedException
    {
        final ActionBar actionBar = activity.getSupportActionBar();
        assertNotNull(actionBar);
        Fragment fragment;
        activity.runOnUiThread(new Runnable()
        {

            @Override
            public void run()
            {
                actionBar.selectTab(actionBar
                        .getTabAt(index));
            }
        });
        instrumentation.waitForIdleSync();
        CountDownLatch signal = new CountDownLatch(1);
        signal.await(2, TimeUnit.SECONDS);

        fragment = activity.getSupportFragmentManager()
                .findFragmentById(
                        android.R.id.content);
        assertTrue(fragmentClass.isInstance(fragment));
    }
}
