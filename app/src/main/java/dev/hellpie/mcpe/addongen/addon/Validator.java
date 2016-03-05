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

package dev.hellpie.mcpe.addongen.addon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

import dev.hellpie.mcpe.addongen.R;

public class Validator {

    public static boolean validateAddonName(@NonNull String name, Context caller) {
        if (name.length() > 50) {
            alertAndReturn(R.string.senpai_too_long)
        }
    }

    private static boolean alertAndReturn(@StringRes int message, Context caller) {
        if (caller != null) {
            Toast.makeText(caller, caller.getString(message), Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
