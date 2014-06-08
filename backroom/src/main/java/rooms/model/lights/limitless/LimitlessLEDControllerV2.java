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

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rooms.model.lights.Cmd;
import rooms.model.lights.limitless.rgbV2.CommandsRgbV2;
import rooms.model.lights.limitless.whiteV2.CommandsWhiteV2;
import rooms.types.Group;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Project: rooms
 * <p>
 * Written by: Markus Binsteiner
 * Date: 6/02/14
 * Time: 12:04 PM
 */
public class LimitlessLEDControllerV2 {
// ------------------------------ FIELDS ------------------------------

    private final static int COMMAND_WAIT_TIME = 400;
    private static final int DEFAULT_PORT = 50000;
    private static final Logger myLogger = LoggerFactory.getLogger(LimitlessLEDControllerV2.class);
    final private InetAddress bridgeAddr;
    private final List<Group> groups = Lists.newArrayList(Group.GROUP_1, Group.GROUP_2, Group.GROUP_3, Group.GROUP_4, Group.GROUP_ALL, Group.GROUP_COLOUR);
    final private String hostname;
    private Group lastGroup;
    final private int port;
    private final Map<Group, Queue<LimitlessCommand>> queues = Maps.newLinkedHashMap();
    private int waitTime = 30;
    private final Thread sendCommandDaemonThread = new Thread() {
        public void run() {
            Iterator<Group> groupIterator = Iterators.cycle(groups);

            Group lastGroup = null;

            while ( groupIterator.hasNext() ) {
                Group group = groupIterator.next();


                Queue<LimitlessCommand> queue = queues.get(group);

                LimitlessCommand cmd = queue.poll();
                if ( cmd != null ) {

                    myLogger.debug("Executing command for group {}", group);

                    try {
                        if ( (group != Group.GROUP_COLOUR
                                || lastGroup == null)
                                && group != lastGroup
                                && !cmd.equals(Cmd.ON)
                                && !cmd.equals(Cmd.OFF) ) {
                            // change to group
                            CommandsWhiteV2 temp = CommandsWhiteV2.lookup(group, Cmd.ON);
                            List<byte[]> tempSeq = temp.getCommand(null);
                            for ( byte[] t : tempSeq ) {
                                send(t);
                                try {
                                    Thread.sleep(waitTime);
                                } catch (InterruptedException e) {
                                }
                            }

                        }

                        // send the actual command
                        int repeat = 1;
                        try {
                            repeat = Integer.parseInt(cmd.getOptions().get("repeat"));
                        } catch (Exception e) {
                        }
                        for ( int j = 0; j < repeat; j++ ) {
                            List<byte[]> seq = cmd.getCommand().getCommand(cmd.getOptions());
                            for ( byte[] c : seq ) {
                                send(c);
                                try {
                                    Thread.sleep(waitTime);
                                } catch (InterruptedException e) {
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lastGroup = group;
                    }
                }
//                try {
//                    Thread.sleep(COMMAND_WAIT_TIME);
//                } catch (InterruptedException e) {
//                }

            }
        }
    };

    public LimitlessLEDControllerV2(String hostname) {
        this(hostname, DEFAULT_PORT);
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public LimitlessLEDControllerV2(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        try {
            bridgeAddr = InetAddress.getByName(hostname);
        } catch (Exception e) {
            throw new RuntimeException("Can't get ip address for bride '" + hostname + ";");
        }

        // initialize queues
        for ( Group g : groups ) {
            Queue<LimitlessCommand> temp = Lists.newLinkedList();
            queues.put(g, temp);
        }

        // start queue-draining thread
        sendCommandDaemonThread.setDaemon(true);
        sendCommandDaemonThread.start();
    }

    public String getHostname() {
        return hostname;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public InetAddress getIp() {
        return bridgeAddr;
    }

    public int getPort() {
        return port;
    }

// -------------------------- OTHER METHODS --------------------------

    public void queue(Group group, LimitlessControllerCommand cmd) {
        queue(group, cmd, (Map) null);
    }

    public synchronized void queue(Group group, LimitlessControllerCommand cmd, Integer times) {

        Map<String, String> options = Maps.newHashMap();
        options.put("repeat", times.toString());

        queue(group, cmd, options);
    }

    private synchronized void queue(Group group, LimitlessControllerCommand cmd, Map<String, String> options) {

        LimitlessCommand command = new LimitlessCommand(cmd, group, options);
        queues.get(group).add(command);
    }

    private synchronized void send(byte[] sendData) {
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getIp(), getPort());
            clientSocket.send(sendPacket);
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            clientSocket.close();
        }
    }

    public void sendRGB(Cmd cmd) {
        sendRGB(cmd, 1);
    }

    public void sendRGB(Cmd cmd, Integer colourCode) {

        Map<String, String> options = Maps.newHashMap();
        options.put("colour", colourCode.toString());

        LimitlessControllerCommand command = CommandsRgbV2.lookup(cmd);

        queue(Group.GROUP_COLOUR, command, options);
    }

    public void sendWhite(Group group, Cmd cmd, Integer times) {
        CommandsWhiteV2 command = CommandsWhiteV2.lookup(group, cmd);
        queue(group, command, times);
    }

    public void sendWhite(Group group, Cmd cmd) {
        sendWhite(group, cmd, 1);
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
