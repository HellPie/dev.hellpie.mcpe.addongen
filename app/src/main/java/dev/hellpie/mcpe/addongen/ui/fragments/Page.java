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

package dev.hellpie.mcpe.addongen.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.View;

import dev.hellpie.mcpe.addongen.R;

public abstract class Page extends Activity implements View.OnClickListener {

    public static final String PAGE_NAME = "dev.hellpie.mcpe.addongen.ui.fragments.Page.PAGE_NAME";

    @StringRes
    private final int name;

    public Page(@StringRes int name) {
        this.name = name;
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        findViewById(R.id.bottom_bar_back).setOnClickListener(this);
        findViewById(R.id.bottom_bar_next).setOnClickListener(this);

        onPageInit(findViewById(R.id.container));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected abstract void onPageInit(View page);
    protected abstract void onPageSave();

    protected abstract boolean onBackPage();
    protected abstract boolean onNextPage();

    @LayoutRes
    protected abstract int getInflatableLayout();

    public abstract String getKey();
}
