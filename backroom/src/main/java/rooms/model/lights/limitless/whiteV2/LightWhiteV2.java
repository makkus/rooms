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

package rooms.model.lights.limitless.whiteV2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rooms.model.lights.Cmd;
import rooms.model.lights.limitless.Light;
import rooms.model.lights.limitless.LimitlessLEDControllerV2;
import rooms.types.Group;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

/**
 * Project: rooms
 * <p>
 * Written by: Markus Binsteiner
 * Date: 6/02/14
 * Time: 12:04 PM
 */
public class LightWhiteV2 implements Light {
// ------------------------------ FIELDS ------------------------------

    private static final Integer BRIGHTNESS_MAX = 10;
    private static final Integer BRIGHTNESS_MIN = 0;
    public static final int CALIBRATION_THRESHOLD = 11;
    private static final Integer WARMTH_MAX = 10;
    private static final Integer WARMTH_MIN = 0;
    private static final Logger myLogger = LoggerFactory.getLogger(LightWhiteV2.class);
    private Integer brightness = null;
    private Integer brightness_shadow = 0;
    private final LimitlessLEDControllerV2 controller;
    private boolean default_calibrate_brightness_full = false;
    private boolean default_calibrate_warmth_full = false;
    private final Group group;
    private String name;
    private Boolean on = null;
    private Integer warmth = null;
    private Integer warmth_shadow = 0;

// --------------------------- CONSTRUCTORS ---------------------------

    public LightWhiteV2(String name, LimitlessLEDControllerV2 controller, Group group) {
        this.name = name;
        this.controller = controller;
        this.group = group;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    private void calibrate() {
        Integer old_brightness = brightness;
        Integer old_warmth = warmth;
        setOn(true);
        calibrateBrightness(default_calibrate_brightness_full);
        calibrateWarmth(default_calibrate_warmth_full);

        if ( old_brightness == null ) {
            if ( default_calibrate_brightness_full ) {
                old_brightness = BRIGHTNESS_MAX;
            } else {
                old_brightness = BRIGHTNESS_MIN;
            }
        }

        if ( old_warmth == null ) {
            if ( default_calibrate_warmth_full ) {
                old_warmth = WARMTH_MAX;
            } else {
                old_warmth = WARMTH_MIN;
            }
        }
        setBrightnessAndWarmth(old_brightness, old_warmth);
    }

    private void calibrateBrightness(boolean full) {
        setOn(true);
        if ( full ) {
            send(Cmd.BRIGHTNESS_UP, CALIBRATION_THRESHOLD);
            this.brightness = BRIGHTNESS_MAX;
        } else {
            send(Cmd.BRIGHTNESS_DOWN, CALIBRATION_THRESHOLD);
            this.brightness = BRIGHTNESS_MIN;
        }
        brightness_shadow = 0;
    }

// ------------------------ CANONICAL METHODS ------------------------

    private void calibrateWarmth(boolean full) {
        setOn(true);
        if ( full ) {
            send(Cmd.WARMTH_UP, CALIBRATION_THRESHOLD);
            this.warmth = WARMTH_MAX;
        } else {
            send(Cmd.WARMTH_DOWN, CALIBRATION_THRESHOLD);
            this.warmth = WARMTH_MIN;
        }
        warmth_shadow = 0;
    }

// -------------------------- OTHER METHODS --------------------------

    public void changeBrightness(int steps) {
        if ( steps == 0 ) {
            return;
        }

        setOn(true);

        if ( brightness != null && (
                (steps < 0 && brightness == BRIGHTNESS_MIN) || (steps > 0 && brightness == BRIGHTNESS_MAX))
                ) {
            return;
        }

        if ( steps > 0 ) {
            controller.sendWhite(this.group, Cmd.BRIGHTNESS_UP, steps);
        } else {
            controller.sendWhite(this.group, Cmd.BRIGHTNESS_DOWN, Math.abs(steps));
        }
        if ( brightness != null ) {
            brightness = brightness + steps;
        } else {
            brightness_shadow = brightness_shadow + steps;
            if ( Math.abs(brightness_shadow) >= CALIBRATION_THRESHOLD ) {
                if ( brightness_shadow > 0 ) {
                    brightness = BRIGHTNESS_MAX;
                } else {
                    brightness = BRIGHTNESS_MIN;
                }
                brightness_shadow = 0;
            }
        }
        myLogger.debug("Changed brightness ({} steps), new state: {}", steps, debugState());
    }

    public void changeWarmth(int steps) {
        if ( steps == 0 ) {
            return;
        }

        if ( warmth != null &&
                ((steps < 0 && warmth == WARMTH_MIN) || (steps > 0 && warmth == WARMTH_MAX))
                ) {
            return;
        }

        if ( steps > 0 ) {
            controller.sendWhite(this.group, Cmd.WARMTH_UP, steps);
        } else {
            controller.sendWhite(this.group, Cmd.WARMTH_DOWN, Math.abs(steps));
        }
        if ( warmth != null ) {
            warmth = warmth + steps;
        } else {
            warmth_shadow = warmth_shadow + steps;
            if ( Math.abs(warmth_shadow) >= CALIBRATION_THRESHOLD ) {
                if ( warmth_shadow > 0 ) {
                    warmth = WARMTH_MAX;
                } else {
                    warmth_shadow = WARMTH_MIN;
                }
                warmth_shadow = 0;
            }
        }
        myLogger.debug("Changed warmth ({} steps), new state: {}", steps, debugState());
    }

    private String debugState() {
        String onString = null;
        if ( on != null ) {
            if ( on ) {
                onString = "on";
            } else {
                onString = "off";
            }
        }
        return "state: " + onString + ", brightness " + brightness + " (shadow: " + brightness_shadow + "), warmth: " + warmth + " shadow: " + warmth_shadow + ")";
    }

    public void decreaseBrightness() {
        decreaseBrightness(1);
    }

    public void decreaseBrightness(int steps) {
        changeBrightness(-steps);
    }

    public void decreaseWarmth() {
        decreaseWarmth(1);
    }

    public void decreaseWarmth(int steps) {
        changeWarmth(-steps);
    }

    public Integer getBrightness() {
        return brightness;
    }

    @Override
    public String getName() {
        return name;
    }

    public Integer getWarmth() {
        return warmth;
    }

    public void increaseBrightness() {
        increaseBrightness(1);
    }

    public void increaseBrightness(int steps) {
        changeBrightness(steps);
    }

    public void increaseWarmth() {
        increaseWarmth(1);
    }

    public void increaseWarmth(int steps) {
        changeWarmth(steps);
    }

    @Override
    public Boolean isOn() {
        return this.on;
    }

    private void send(Cmd command) {
        controller.sendWhite(this.group, command);
    }

    private void send(Cmd command, int times) {
        controller.sendWhite(this.group, command, times);
    }

    @Override
    public void set(Map<String, String> properties) {
        for ( String key : properties.keySet() ) {
            switch ( key ) {
                case "brightness":
                    setBrightness(Integer.parseInt(properties.get(key)));
                    break;
                case "warmth":
                    setWarmth(Integer.parseInt(properties.get(key)));
                    break;
                case "on":
                    setOn(Boolean.parseBoolean(properties.get(key)));
                    break;
                default:
                    return;
            }
        }
    }

    public void setBrightness(int absBrightness) {
        if ( absBrightness < 0 ) {
            return;
        }
        setOn(true);
        if ( this.brightness == null ) {
            calibrateBrightness(absBrightness > BRIGHTNESS_MAX / 2);
        }

        int delta_brightness = absBrightness - this.brightness;

        myLogger.debug("Changing brightness (" + delta_brightness + " steps)");

        changeBrightness(delta_brightness);
    }

    public void setBrightnessAndWarmth(int brightness, int warmth) {
        setOn(true);
        if ( this.brightness == null ) {
            calibrateBrightness(brightness > BRIGHTNESS_MAX / 2);
        }
        if ( this.warmth == null ) {
            calibrateWarmth(warmth > WARMTH_MAX / 2);
        }

        int delta_brightness = brightness - this.brightness;
        int delta_warmth = warmth - this.warmth;

        myLogger.debug("Changing brightness (" + delta_brightness + " steps), warmth (" + delta_warmth + " steps)");

        boolean brightness_increase = delta_brightness > 0;
        boolean warmth_increase = delta_warmth > 0;

        // make the transition a bit smoother, instead of changing brightness & warmth after each other
        int maxSteps = Math.max(delta_brightness, delta_warmth);

        for ( int i = 0; i < maxSteps; i++ ) {
            if ( Math.abs(delta_warmth) > 0 ) {
                if ( warmth_increase ) {
                    increaseWarmth();
                    delta_warmth = delta_warmth - 1;
                } else {
                    decreaseWarmth();
                    delta_warmth = delta_warmth + 1;
                }
            }
            if ( Math.abs(delta_brightness) > 0 ) {
                if ( brightness_increase ) {
                    increaseBrightness();
                    delta_brightness = delta_brightness - 1;
                } else {
                    decreaseBrightness();
                    delta_brightness = delta_brightness + 1;
                }
            }
        }

        changeBrightness(delta_brightness);
        changeWarmth(delta_warmth);
    }

    @Override
    public void setColor(int color) {
        throw new NotImplementedException();
    }

    public void setNightMode() {
        send(Cmd.NIGHTMODE);
    }

    public void setOn(Boolean on) {
        if ( on == null ) {
            return;
        }
        if ( this.on == null ) {
            if ( on ) {
                send(Cmd.ON);
                this.on = true;
            } else {
                send(Cmd.OFF);
                this.on = false;
            }
        } else {
            if ( !this.on.equals(on) ) {
                if ( on ) {
                    send(Cmd.ON);
                    this.on = true;
                } else {
                    send(Cmd.OFF);
                    this.on = false;
                }
            }
        }
    }

    public void setWarmth(int absWarmth) {
        if ( absWarmth < 0 ) {
            return;
        }
        setOn(true);
        if ( this.warmth == null ) {
            calibrateWarmth(absWarmth > WARMTH_MAX / 2);
        }

        int delta_warmth = absWarmth - this.warmth;

        myLogger.debug("Changing warmth (" + delta_warmth + " steps)");

        changeWarmth(delta_warmth);
    }

    public String toString() {
        String brightness_string = brightness.toString();
        if ( brightness == null ) {
            brightness_string = "n/a";
        }
        String warmth_string = warmth.toString();
        if ( warmth == null ) {
            warmth_string = "n/a";
        }

        String result = name + " - (state: " + on + ", brightness: " + brightness_string + ", warmth: " + warmth_string + ")";
        return result;
    }

    public void toggle() {
        if ( isOn() == null || !isOn() ) {
            setOn(true);
        } else {
            setOn(false);
        }
    }
}
