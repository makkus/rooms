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

import com.google.common.collect.Maps;
import rooms.types.Group;

import java.util.Map;

/**
 * Project: rooms
 * <p>
 * Written by: Markus Binsteiner
 * Date: 6/02/14
 * Time: 9:41 PM
 */
public class LimitlessCommand {
// ------------------------------ FIELDS ------------------------------

    private LimitlessControllerCommand command;
    private Group group;

    private Map<String, String> options = Maps.newConcurrentMap();

// --------------------------- CONSTRUCTORS ---------------------------

    public LimitlessCommand(LimitlessControllerCommand command, Group group, Map<String, String> options) {
        this.command = command;
        this.group = group;
        this.options = options;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public void addOption(String key, String value) {
        getOptions().put(key, value);
    }

    public LimitlessControllerCommand getCommand() {
        return command;
    }

    public Group getGroup() {
        return group;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void removeOption(String key) {
        getOptions().remove(key);
    }

    public void setCommand(LimitlessControllerCommand command) {
        this.command = command;
    }

// -------------------------- OTHER METHODS --------------------------

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "LimitlessCommand{" +
                "command=" + command +
                ", group=" + group +
                ", options=" + options +
                '}';
    }
}
