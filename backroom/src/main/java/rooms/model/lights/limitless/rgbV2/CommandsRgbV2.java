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

package rooms.model.lights.limitless.rgbV2;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import rooms.model.lights.Cmd;
import rooms.model.lights.limitless.LimitlessControllerCommand;

import java.util.List;
import java.util.Map;

/**
 * All possible commands for whiteV2 LimitlessLED lamps.
 * <p>
 * Written by: Markus Binsteiner
 * Date: 29/05/13
 * Time: 10:29 PM
 */
public enum CommandsRgbV2 implements LimitlessControllerCommand {
    ALL_ON(Cmd.ON, 34),
    ALL_OFF(Cmd.OFF, 33),
    BRIGHTNESS_UP(Cmd.BRIGHTNESS_UP, 35),
    BRIGHTNESS_DOWN(Cmd.BRIGHTNESS_DOWN, 36),
    DISCO_NEXT(Cmd.IGNORE, 39),
    DISCO_LAST(Cmd.IGNORE, 40),
    DISCO_SLOWER(Cmd.IGNORE, 38),
    DISCO_FASTER(Cmd.IGNORE, 37),
    SET_COLOUR(Cmd.SET_COLOUR, 32);

    // ------------------------------ FIELDS ------------------------------
    private static final byte END = (byte) 85;
    private static final byte NO_CONF = (byte) 0;
    private static final byte START = (byte) 32;

    public static CommandsRgbV2 lookup(Cmd cmd) {
        for ( CommandsRgbV2 commandsRGB : CommandsRgbV2.values() ) {
            if ( commandsRGB.cmd == cmd ) {
                return commandsRGB;
            }
        }
        throw new RuntimeException("Can't find group/cmd combination.");
    }

    public final byte byte_to_send;

    // -------------------------- STATIC METHODS --------------------------
    public final Cmd cmd;

    private CommandsRgbV2(Cmd cmd, int byte_to_send) {
        this.cmd = cmd;
        this.byte_to_send = (byte) byte_to_send;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public List<byte[]> getCommand(Map<String, String> options) {

        byte[] sendData = null;
        String colour = null;
        if ( options != null ) {
            colour = options.get("colour");
        }

        if ( Strings.isNullOrEmpty(colour) ) {
            sendData = new byte[]{byte_to_send, NO_CONF, END};
        } else {
            byte colourCode = (byte) Integer.parseInt(colour);
            sendData = new byte[]{byte_to_send, colourCode, END};
        }

        List<byte[]> result = Lists.newArrayList();
        result.add(sendData);
        return result;
    }
}

