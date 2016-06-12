/*
 * Copyright (C) 2016 Meine Toonen
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

import static nl.meine.ibb.stripes.Block.encode;

/**
 *
 * @author Meine Toonen
 */
public enum Command {
    
    BLOCK_START(encode(4009, 4001)),    
    START_DRAWING ( encode(4001, 4001)),
    STOP_DRAWING ( encode(4002, 4000)),
    PEN_LIFT ( encode(4003, 0)),
    PEN_DOWN ( encode(4004, 0)),
    COORDINATE(-1);

    private final int value;
    
    private int x, y;

    Command(int value){
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }
    
    
    public static Command valueOf(int val){
        for (Command com : Command.values()) {
            if(com.value == val){
                return com;
            }
        }
        return COORDINATE;
        
    }
}
