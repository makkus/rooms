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

import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import rooms.model.lights.limitless.whiteV2.LightWhiteV2;
import rooms.types.Light;
import rooms.types.LightState;
import rx.Observable;
import things.thing.Thing;
import things.thing.ThingAction;
import things.thing.ThingControl;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * Project: things
 * <p>
 * Written by: Markus Binsteiner
 * Date: 12/05/14
 * Time: 10:42 PM
 */
public class LightAction implements ThingAction {

    private LightUtil lightUtil;
    @Autowired
    public SimpMessagingTemplate simpMessagingTemplate;
    private ThingControl tc;

    public LightAction() {

    }

    @Override
    public Observable<? extends Thing<?>> execute(String command, Observable<? extends Thing<?>> things, Map<String, String> parameters) {

        Observable<Thing<Light>> lights = tc.filterThingsOfType(Light.class, things);

        Observable<? extends Thing<?>> result = null;

        switch ( command ) {
            case "toggle":
                result = lights.map(l -> toggleLight(l));
                break;
            case "turn_on":
                result = lights.map(l -> switchOnLight(l, true));
                break;
            case "turn_off":
                result = lights.map(l -> switchOnLight(l, false));
                break;
            case "set":
                result = lights.map(l -> setLight(l, parameters));
                break;
            default:
                result = Observable.empty();
        }

        return result.doOnNext(o -> {
            simpMessagingTemplate.convertAndSend("/topic/light_change", o);
        });
    }

    @Override
    public Set<String> getSupportedActionNames() {
        return ImmutableSet.<String>of("set_light", "toggle", "turn_on", "turn_off", "set");
    }

    private Thing<LightState> setLight(Thing<Light> light, Map<String, String> params) {
        LightWhiteV2 l = lightUtil.getLights().get(light.getKey());
        l.set(params);
        return lightUtil.createLightStateThing(light);
    }

    @Inject
    public void setLightUtil(LightUtil lu) {
        this.lightUtil = lu;
    }

    @Inject
    public void setThingControl(ThingControl tc) {
        this.tc = tc;
    }

    private Thing<LightState> switchOnLight(Thing<Light> light, boolean on) {
        LightWhiteV2 l = lightUtil.getLights().get(light.getKey());
        l.setOn(on);
        return lightUtil.createLightStateThing(light);
    }

    private Thing<LightState> toggleLight(Thing<Light> light) {
        LightWhiteV2 l = lightUtil.getLights().get(light.getKey());
        l.toggle();
        return lightUtil.createLightStateThing(light);
    }

}
