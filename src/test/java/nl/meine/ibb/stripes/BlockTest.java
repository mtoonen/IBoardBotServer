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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class BlockTest {

    @Test
    public void test() {
//		Block block = new Block(0);
//		block.addPosition(10, 10);
//		block.addPosition(12, 12);
//block.finish();
//		System.out.println(block);

        // 4009 4001   [FA9FA1]  
        int d = Block.encode(4009, 4001);
        System.out.println(Long.toHexString(d));
    }

    @Test
    public void testStart() {
        int val = Block.encode(4009, 4001);
        System.out.println(val);
        assertEquals("fa9fa1", Long.toHexString(val));
    }

    @Test
    public void testBlock() {
        Block block = new Block(0);
        block.addPosition(0, 100);
        block.addPosition(100, 0);
        block.finish();
        System.out.println(block);
        //assertEquals("fa9fa1", Long.toHexString(Block.hex(4009, 4001)));
    }
    
    @Test
    public void testDecode1(){
        int input = 409700;
        String expected =  "100,100";
        String real = Block.decode(input);
        assertEquals(expected, real);
    }
    
    @Test
    public void testDecode2(){
        int input = 819300;
        String expected =  "200,100";
        String real = Block.decode(input);
        assertEquals(expected, real);
    }
    
    @Test
    public void testHumanReadableString(){
        Block block = new Block(0);
        block.down();
        block.addPosition(10, 10);
        block.finish();
        String result = block.toHumanReadableString();
        String exp = "BLOCK_START ID(0) START_DRAWING PEN_DOWN COORDINATE(10,10) PEN_LIFT COORDINATE(0,0) STOP_DRAWING";
        assertEquals(exp, result);
    }

    @Test
    public void testStream() throws IOException {
        Block block = new Block(1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        block.write(os);
        byte[] s = os.toByteArray();
        
        System.out.println(Arrays.toString(s));
        System.out.println();
        System.out.println(bytesToHex(s));
        assertEquals(block.toString().replaceAll(" ", "").toUpperCase(), bytesToHex(s));
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
