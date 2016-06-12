/*
 * Copyright (C) 2016 Meine Toonen, Gertjan Al
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.meine.ibb.stripes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Block {
  
    private List<Long> commands = new ArrayList<>();
    
    private boolean isDown = false;

    public Block(int id) {
        commands.add(Command.BLOCK_START.getValue());
        commands.add(encode(4009, id));
        commands.add(Command.START_DRAWING.getValue());
    }

    public Block() {
        this(0);
    }

    public void up() {
        if(isDown){
            commands.add(Command.PEN_LIFT.getValue());
            isDown = false;
        }
    }

    public void down() {
        if(!isDown){
            commands.add(Command.PEN_DOWN.getValue());
            isDown = true;
        }
    }

    public static long encode(long c1, long c2) {
        return (c1 << 12) + c2;
    }
    
    public static String decode (long num){
        long first = num >> 12;
        long second = num - ( first << 12);
        String s = first + "," + second;
        return s;
    }

    public void addPosition(long x, long y) {
        commands.add(encode(x , y));
    }

    public void addPosition(double x, double y) {
        addPosition(Math.round(x), Math.round(y));
    }

    public void finish() {
        up();
        addPosition(0, 0);
        commands.add(Command.STOP_DRAWING.getValue());
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
        for (long command : commands) {
            ByteBuffer buffer = ByteBuffer.allocate(8).putLong(command);
            byte[] buf = new byte[3];
            buffer.rewind();
            buf[0] = buffer.get(5);
            buf[1] = buffer.get(6);
            buf[2] = buffer.get(7);
            out.write(buf);
        }
    }

    public List<Long> getCommands(){
        return commands;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (long command : commands) {
            builder.append(" ");
            String value = Long.toHexString(command);
            builder.append(pad(value));
        }

        return builder.toString().substring(1);
    }
    
    public String toHumanReadableString(){
        StringBuilder builder = new StringBuilder();
        for (Long command : commands) {
            builder.append(" ");
            
            Command com = Command.valueOf(command);
            
            if(!com.equals(Command.COORDINATE)){
                builder.append(com.toString());
                
            }else{
                String comString = Block.decode(command);
                
                String s = comString.length() > 4 ? comString.substring(0, 4) : comString;

                if (s.equalsIgnoreCase("4009")) {
                    String id = comString.substring(5);
                    builder.append("ID(");
                    builder.append(id);
                    builder.append(")");
                    //id
                } else {
                    builder.append("COORDINATE(");
                    builder.append(comString);
                    builder.append(")");
                }
            }
            
        }
        return builder.toString().substring(1);
        
    }
    
    @Override
    public boolean equals(Object otherObj){
        Block other = (Block)otherObj;
        List<Long> otherCommands = other.getCommands();
        if(otherCommands.size() != commands.size()){
            return false;
        }
        for (int i = 0; i < commands.size(); i++) {
            long com = commands.get(i);
            long otherCom = otherCommands.get(i);
            if(com != otherCom){
                return false;
            }
        }
        return true;
    }
}
