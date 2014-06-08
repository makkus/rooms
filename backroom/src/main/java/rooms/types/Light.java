/*
 * Rooms
 *
 * Copyright (c) 2014, Markus Binsteiner. All rights reserved.
 *
 * This file is part of Things.
 *
 * Rooms is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package rooms.types;


import org.hibernate.validator.constraints.NotEmpty;
import things.model.types.Value;
import things.model.types.attributes.UniqueKey;

import javax.validation.constraints.NotNull;

/**
 * Project: things-to-build
 * <p>
 * Written by: Markus Binsteiner
 * Date: 13/04/14
 * Time: 12:09 AM
 */
@Value(typeName = "light")
@UniqueKey
public class Light {

    @NotNull
    private Group lightGroup;
    @NotEmpty
    private String lightType;


    public Light(Group lightGroup, String type) {
        this.lightGroup = lightGroup;
        this.lightType = type;
    }

    public Light() {
    }

    public Group getLightGroup() {
        return lightGroup;
    }

    public String getLightType() {
        return lightType;
    }

    public void setLightGroup(Group lightGroup) {
        this.lightGroup = lightGroup;
    }

    public void setLightType(String type) {
        this.lightType = type;
    }

    @Override
    public String toString() {
        return "Light{" +
                "lightGroup=" + lightGroup +
                ", type='" + lightType +
                '}';
    }
}
