/*
 * Copyright 2016 Diego Rossi (@_HellPie)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hellpie.mcpe.addongen.ui.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import dev.hellpie.mcpe.addongen.R;
import dev.hellpie.mcpe.addongen.ui.fragments.WelcomePage;

public class MainActivity extends Activity {

    private static MainActivity self = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, new WelcomePage());
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);

        /*self = this;
        lockBottomBar(true);

        ((TextView) findViewById(R.id.header_text)).setText(getString(R.string.app_name));
*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                transaction.commit();
            }
        }, 1000);

        hideNavBar();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            hideNavBar();
                        }
                    }
                }
        );

  //      unlockBottomBar();
    }

    private void hideNavBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    public static void lockBottomBar(boolean both) {
        self.findViewById(R.id.bottom_bar_back).setEnabled(false);
        if(both) self.findViewById(R.id.bottom_bar_next).setEnabled(false);
    }

    public static void unlockBottomBar() {
        self.findViewById(R.id.bottom_bar_back).setEnabled(true);
        self.findViewById(R.id.bottom_bar_next).setEnabled(true);
    }
}
