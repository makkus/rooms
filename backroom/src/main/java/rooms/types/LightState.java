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


import org.hibernate.validator.constraints.Range;
import things.model.types.Value;

import java.io.Serializable;

/**
 * Project: things-to-build
 * <p>
 * Written by: Markus Binsteiner
 * Date: 2/05/14
 * Time: 7:34 PM
 */
@Value(typeName = "lightState")
public class LightState implements Serializable {

    @Range(min = 0, max = 10)
    private Integer brightness = null;
    @Range(min = 0, max = 255)
    private Integer color = null;
    private Boolean on = null;
    @Range(min = 0, max = 10)
    private Integer warmth = null;

    public Integer getBrightness() {
        return brightness;
    }

    public Integer getColor() {
        return color;
    }

    public Integer getWarmth() {
        return warmth;
    }

    public Boolean isOn() {
        return on;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public void setWarmth(Integer warmth) {
        this.warmth = warmth;
    }

    @Override
    public String toString() {
        return "LightState{" +
                "brightness=" + brightness +
                ", color=" + color +
                ", warmth=" + warmth +
                ", on=" + on +
                '}';
    }
}
