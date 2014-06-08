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

package rooms.model.lights.limitless;

import java.util.Map;

/**
 * Project: rooms
 * <p>
 * Written by: Markus Binsteiner
 * Date: 6/02/14
 * Time: 11:40 PM
 */
public interface Light {

    void decreaseBrightness(int steps);

    void decreaseWarmth(int steps);

    String getName();

    void increaseBrightness(int steps);

    void increaseWarmth(int steps);

    Boolean isOn();

    void set(Map<String, String> properties);

    void setBrightness(int absBrightness);

    void setColor(int color);

    void setOn(Boolean on);

    void setWarmth(int absWarmth);


}
