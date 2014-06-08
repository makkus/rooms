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

package rooms.queries;

import rooms.types.LightState;
import things.exceptions.QueryRuntimeException;
import things.thing.Thing;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Project: things-to-build
 * <p>
 * Written by: Markus Binsteiner
 * Date: 2/05/14
 * Time: 7:46 PM
 */
public class LightStateQuery {

//    private LightUtil lightUtil;

    private Thing<LightState> getState(Thing light) {

//        LightWhiteV2 l = lightUtil.getLights().get(light.getKey());
//        LightState ls = new LightState();
//        ls.setBrightness(l.getBrightness());
//        ls.setOn(l.isOn());
//        ls.setWarmth(l.getWarmth());

//        Thing t = new Thing(light.getKey(), ls);
        return null;
    }

    public List<Thing<LightState>> query(List<Thing> things, Map<String, String> queryParams) throws QueryRuntimeException {

        List<Thing<LightState>> states = things.stream().map(t -> getState(t)).collect(Collectors.toList());
        return states;
    }
}
