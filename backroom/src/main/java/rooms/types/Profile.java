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

import com.google.common.collect.Maps;
import things.model.types.Value;
import things.model.types.attributes.UniqueKey;

import javax.persistence.Transient;
import java.util.Map;

/**
 * Project: things-to-build
 * <p>
 * Written by: Markus Binsteiner
 * Date: 18/04/14
 * Time: 11:16 PM
 */
@UniqueKey
@Value(typeName = "profile")
public class Profile {

    @Transient
    private Map<String, Light> lights = Maps.newHashMap();

    public Profile() {
    }

    public void addLight(String name, Light l2) {
        getLights().put(name, l2);
    }

    public Map<String, Light> getLights() {
        return lights;
    }

    public void setLights(Map<String, Light> lights) {
        this.lights = lights;
    }
}
