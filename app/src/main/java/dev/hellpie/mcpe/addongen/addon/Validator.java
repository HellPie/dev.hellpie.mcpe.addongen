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

import java.util.regex.Pattern;

import dev.hellpie.mcpe.addongen.R;

public class Validator {

    public static boolean validateAddonName(@NonNull String name, Context caller) {
        return ((name.length() <= 50) || alertAndReturn(R.string.name_too_long, caller));
    }

    public static boolean validatePackageId(@NonNull String packageId, Context caller) {
        Pattern javaPkg = Pattern.compile("^[a-z][a-z0-9_]*(\\.[a-z][a-z0-9]+)+[a-z0-9_]$");
        return javaPkg.matcher(packageId).matches() || alertAndReturn(R.string.package_not_valid, caller);
    }

    public static boolean validateMCPEVersion(@NonNull String version, @NonNull Context caller) {
        Pattern mcpeVer = Pattern.compile("^\\d+\\.\\d+\\.\\d+(\\.b\\d+)?$");

        if (!mcpeVer.matcher(version).matches()) {
            return alertAndReturn(R.string.version_not_valid, caller);
        }

        int latest = 0;
        int given = 0;

        String[] latest_split = caller.getString(R.string.mcpe_version_latest).split("\\.");
        String[] given_split = version.split("\\.");

        for (int i = 0; i < latest_split.length; i++) {
            latest += Integer.parseInt(latest_split[i]) * 10 ^ i;
        }

        for (int i = 0; i < given_split.length; i++) {
            given += Integer.parseInt(given_split[i]) * 10 ^ i;
        }

        if (latest > given) {
            return alertAndReturn(R.string.version_obsolete, caller);
        } else if (latest < given) {
            return alertAndReturn(R.string.version_unreleased, caller);
        } else {
            return true;
        }
    }

    private static boolean alertAndReturn(@StringRes int message, Context caller) {
        if (caller != null) {
            Toast.makeText(caller, caller.getString(message), Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
