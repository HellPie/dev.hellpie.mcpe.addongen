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

import android.view.View;

import dev.hellpie.mcpe.addongen.R;

public class WelcomePage extends Page {

    public WelcomePage(String name) {
        super(name);
    }

    @Override
    protected void onPageInit(View page) {

    }

    @Override
    protected void onPageSave() {

    }

    @Override
    protected boolean onBackPage() {
        return false;
    }

    @Override
    protected boolean onNextPage() {
        return false;
    }

    @Override
    protected final int getInflatableLayout() {
        return R.layout.page_welcome;
    }

    @Override
    protected String getTitleId() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
