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

import android.support.annotation.NonNull;

public final class AddonData {

    private String addonName;
    private String packgeID;
    private String mcpeVersion;

    public String getAddonName() {
        return addonName;
    }

    public void setAddonName(@NonNull String addonName) {
        this.addonName = addonName;
    }

    public String getPackgeID() {
        return packgeID;
    }

    public void setPackgeID(@NonNull String packgeID) {
        this.packgeID = packgeID;
    }

    public String getMcpeVersion() {
        return mcpeVersion;
    }

    public void setMcpeVersion(@NonNull String mcpeVersion) {
        this.mcpeVersion = mcpeVersion;
    }
}
