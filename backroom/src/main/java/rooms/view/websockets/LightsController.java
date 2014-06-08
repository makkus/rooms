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

package rooms.view.websockets;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import rooms.types.LightState;

/**
 * Created by markus on 27/05/14.
 */
@Controller
public class LightsController {


    @MessageMapping("/hello")
    @SendTo("/topic/light_change")
    public LightState stateChanged(LightState ls) {
        System.out.println("CHANGE: " + ls);
        return ls;
    }
}
