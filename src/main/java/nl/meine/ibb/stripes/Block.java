/*
 * Copyright (C) 2016 Meine Toonen, Gertjan Al
 *
 * This program is free software: you can redistribute it and/or modify
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
package nl.meine.ibb.stripes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Block {

    private int BLOCK_START = hex(4009, 4001);
    private int START_DRAWING = hex(4001, 4001);
    private int STOP_DRAWING = hex(4002, 4000);
    private int PEN_LIFT = hex(4003, 0);
    private int PEN_DOWN = hex(4004, 0);

    private List<Integer> commands = new ArrayList<>();
    
    private boolean isDown = false;

    public Block(int id) {
        commands.add(BLOCK_START);
        commands.add(hex(4009, id));
        commands.add(START_DRAWING);
    }

    public Block() {
        this(0);
    }

    public void up() {
        if(isDown){
            commands.add(PEN_LIFT);
            isDown = false;
        }
    }

    public void down() {
        if(!isDown){
            commands.add(PEN_DOWN);
            isDown = true;
        }
    }

    public static int hex(int c1, int c2) {
        return (c1 << 12) + c2;
    }

    public void addPosition(int x, int y) {
        commands.add(hex(x * 10, y * 10));
    }

    public void addPosition(double x, double y) {
        commands.add(hex((int)x * 10, (int)y * 10));
    }

    public void finish() {
        up();
        addPosition(0, 0);
        commands.add(STOP_DRAWING);
    }

    /*
C1 = 4009, C2 = 4001 : Block start. Message start sync bytes. HEX: FA9FA1
C1 = 4009, C2 < 4000 : Block number C2
C1 = 4001, C2 = 4001 : Start drawing. HEX: FA1FA1
C1 = 4002, C2 = xxxx :  Stop drawing. HEX: FA2000
C1 = 4003, C2 = xxxx : Pen lift
C1 = 4004, C2 = xxxx : Pen down (draw)
C1 = 4005, C2 = xxxx : Enable Eraser
C2 = 4006, C2 = seg   : Wait C2 seconds (max 30 seconds).
     */
    private static String pad(String hex) {
        return ("000000" + hex).substring(hex.length());
    }

    public void write(OutputStream out) throws IOException {
        for (int command : commands) {
            ByteBuffer buffer = ByteBuffer.allocate(4).putInt(command);
            byte[] buf = new byte[3];
            buffer.rewind();
            buf[0] = buffer.get(1);
            buf[1] = buffer.get(2);
            buf[2] = buffer.get(3);
            out.write(buf);
        }
    }

    public List<Integer> getCommands(){
        return commands;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int command : commands) {
            builder.append(" ");
            String value = Integer.toHexString(command);
            builder.append(pad(value));
        }

        return builder.toString().substring(1);
    }
    
    @Override
    public boolean equals(Object otherObj){
        Block other = (Block)otherObj;
        List<Integer> otherCommands = other.getCommands();
        if(otherCommands.size() != commands.size()){
            return false;
        }
        for (int i = 0; i < commands.size(); i++) {
            int com = commands.get(i);
            int otherCom = otherCommands.get(i);
            if(com != otherCom){
                return false;
            }
        }
        return true;
    }
}
