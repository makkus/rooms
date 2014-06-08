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

package rooms.actions;

import com.google.common.collect.Maps;
import rooms.model.lights.limitless.LimitlessLEDControllerV2;
import rooms.model.lights.limitless.whiteV2.LightWhiteV2;
import rooms.types.Bridge;
import rooms.types.Light;
import rooms.types.LightState;
import rx.Observable;
import things.thing.Thing;
import things.thing.ThingControl;
import things.types.TypeRegistry;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Project: things
 * <p>
 * Written by: Markus Binsteiner
 * Date: 12/05/14
 * Time: 10:44 PM
 */
public class LightUtil {

    private List<Thing<Bridge>> bridges;
    private Map<String, LightWhiteV2> lights;
    private ThingControl tc;
    private TypeRegistry tr;


    public LightUtil() {
    }

    public Thing<LightState> createLightStateThing(Thing<Light> light) {

        LightState ls = createState(light.getKey());

        Thing<LightState> lightState = new Thing();
        lightState.setId("light:" + light.getId());
        lightState.setThingType(tr.getType(LightState.class));
        lightState.setValueIsPopulated(false);
        lightState.setValue(ls);
        lightState.setKey(light.getKey());
        return lightState;
    }

    public LightState createState(String lightName) {
        LightWhiteV2 l = getLights().get(lightName);
        LightState ls = new LightState();
        ls.setBrightness(l.getBrightness());
        ls.setOn(l.isOn());
        ls.setWarmth(l.getWarmth());

        return ls;
    }

    public synchronized List<Thing<Bridge>> getBridges() {
        if ( bridges == null ) {
            bridges = tc.findThingsForType(Bridge.class);
        }
        return bridges;
    }

    public synchronized Map<String, LightWhiteV2> getLights() {
        if ( lights == null ) {
            lights = Maps.newHashMap();
            for ( Thing<Bridge> bridgeThing : getBridges() ) {

                Bridge bridge = tc.getValue(bridgeThing);
                LimitlessLEDControllerV2 c = new LimitlessLEDControllerV2(bridge.getHost(), bridge.getPort());

                List<Thing<Light>> lightThings = tc.getChildrenForType(Observable.just(bridgeThing), Light.class, true);
                for ( Thing<Light> tempLight : lightThings ) {
                    Light ll = tc.getValue(tempLight);
                    LightWhiteV2 white = new LightWhiteV2(tempLight.getKey(), c, ll.getLightGroup());
                    lights.put(white.getName(), white);
                }

            }
        }
        return lights;
    }

    @Inject
    public void setThingControl(ThingControl tc) {
        this.tc = tc;
//        new Thread(() -> getLights()).start();
    }

    @Inject
    public void setTypeRegistry(TypeRegistry tr) {
        this.tr = tr;
    }

}
