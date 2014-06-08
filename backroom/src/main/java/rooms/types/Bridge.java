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

import things.model.types.Value;
import things.model.types.attributes.UniqueKey;

/**
 * Project: things-to-build
 * <p>
 * Written by: Markus Binsteiner
 * Date: 13/04/14
 * Time: 12:06 AM
 */
@Value(typeName = "bridge")
@UniqueKey(unique = true)
public class Bridge {

    public static final int DEFAULT_PORT = 8899;
    private String host;
    private int port;
    private String version = "2.0";
    private int waitTime = 30;

    public Bridge() {

    }

    public Bridge(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Bridge(String host) {
        this(host, DEFAULT_PORT);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getVersion() {
        return version;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
