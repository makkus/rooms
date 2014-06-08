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

package rooms.readers;

import rooms.actions.LightUtil;
import rooms.types.Light;
import rx.Observable;
import things.thing.AbstractSimpleThingReader;
import things.thing.Thing;
import things.thing.ThingControl;
import things.thing.ThingReader;

import javax.inject.Inject;

/**
 * Project: things
 * <p>
 * Written by: Markus Binsteiner
 * Date: 13/05/14
 * Time: 7:40 PM
 */
public class LightStateReader extends AbstractSimpleThingReader implements ThingReader {

    private LightUtil lightUtil;
    private ThingControl tc;

    public LightStateReader() {
    }


    @Override
    public Observable<? extends Thing<?>> findAllThings() {
        return tc.observeThingsForType(Light.class, false).map(l -> lightUtil.createLightStateThing(l));
    }


    @Override
    public <V> V readValue(Thing<V> light) {

        return (V) lightUtil.createState(light.getKey());

    }

    @Inject
    public void setLightUtil(LightUtil lightUtil) {
        this.lightUtil = lightUtil;
    }

    @Inject
    public void setThingControl(ThingControl tc) {
        this.tc = tc;
    }
}
