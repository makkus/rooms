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

package rooms.model.lights;

/**
 * Project: rooms
 * <p>
 * Written by: Markus Binsteiner
 * Date: 6/02/14
 * Time: 1:36 PM
 */
public enum Cmd {
    ON,
    OFF,
    BRIGHTNESS_UP,
    BRIGHTNESS_DOWN,
    WARMTH_UP,
    WARMTH_DOWN,
    NIGHTMODE,
    FULL,
    IGNORE,
    SET_COLOUR
}
