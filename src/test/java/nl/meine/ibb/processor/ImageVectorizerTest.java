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
package nl.meine.ibb.processor;

import java.awt.Point;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.meine.ibb.stripes.Block;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Meine Toonen
 */
public class ImageVectorizerTest {
    
    public ImageVectorizerTest() {
    }
    
    private ImageVectorizer instance = new ImageVectorizer();
    private final String twolinessvgstring = "<svg width=\"50\" height=\"50\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" ><path fill=\"rgb(0,0,0)\" stroke=\"rgb(0,0,0)\" stroke-width=\"1\" opacity=\"0.0\" d=\"M 0.5 0.0 L 50.0 0.5 L 49.5 50.0 L 0.0 49.5 L 0.5 0.0 Z\" /><path fill=\"rgb(0,0,0)\" stroke=\"rgb(0,0,0)\" stroke-width=\"1\" opacity=\"1.0\" d=\"M 6.5 10.0 L 45.0 10.5 L 44.5 12.0 L 6.0 11.5 L 6.5 10.0 Z\" /><path fill=\"rgb(0,0,0)\" stroke=\"rgb(0,0,0)\" stroke-width=\"1\" opacity=\"1.0\" d=\"M 21.5 22.0 L 23.0 22.5 L 22.5 43.0 L 21.0 42.5 L 21.5 22.0 Z\" /></svg>";
    
    private int xOffset = 1;
    private int yOffset = 1;
    
    private int xOffset2 = 10;
    private int yOffset2 = 20;
    
    private static Block b;
    private static Block bOffset;
    
    
    @BeforeClass
    public static void setUpClass() {
                      /*
        
        M 0.5 0.0 L 50.0 0.5 L 49.5 50.0 L 0.0 49.5 L 0.5 0.0 Z
        UP 
        MOVE 0.5 0.0 
        DOWN
        MOVE 50.0 0.5
        MOVE 49.5 50.0
        MOVE 0.0 49.5
        MOVE 0.5 0.0
        UP
        
        M
        */
        b = new Block();
        b.up();
        b.addPosition(1, 0.0);
        b.down();
        b.addPosition(50.0,1);
        b.addPosition(50,50.0);
        b.addPosition(0.0,50);
        b.addPosition(1,0.0);
        b.up();
        /*
        M 6.5 10.0 L 45.0 10.5 L 44.5 12.0 L 6.0 11.5 L 6.5 10.0 Z
        
        6.5, 10.0
        b.down();
        45.0,10.5
        44.5,12.0
        6.0,11.5
        6.5,10.0
        */
        b.addPosition(7, 10.0);
        b.down();
        b.addPosition(45.0,10.5);
        b.addPosition(45,12.0);
        b.addPosition(6.0,12);
        b.addPosition(7,10.0);
        b.up();
        
        
        /*
        M 21.5 22.0 L 23.0 22.5 L 22.5 43.0 L 21.0 42.5 L 21.5 22.0 Z
        
        21.5,22.0 
        L 
        23.0,22.5 
        L 
        22.5,43.0 
        L 
        21.0,42.5
        L 
        21.5,22.0 
        Z
        */
        
        b.addPosition(22, 22.0);
        b.down();
        b.addPosition(23.0, 23);
        b.addPosition(23, 43.0);
        b.addPosition(21.0, 43);
        b.addPosition(22, 22.0);
        b.up();

        bOffset= new Block();
        bOffset.up();
        bOffset.addPosition(5, 0.0);
        bOffset.down();
        bOffset.addPosition(500.0, 10);
        bOffset.addPosition(495, 1000.0);
        bOffset.addPosition(0.0, 990);
        bOffset.addPosition(5, 0.0);
        bOffset.up();
        /*
M 6.5 10.0 L 45.0 10.5 L 44.5 12.0 L 6.0 11.5 L 6.5 10.0 Z

6.5, 10.0
bOffset.down();
45.0,10.5
44.5,12.0
6.0,11.5
6.5,10.0
         */
        bOffset.addPosition(65, 200.0);
        bOffset.down();
        bOffset.addPosition(450, 210);
        bOffset.addPosition(445, 240);
        bOffset.addPosition(60, 230);
        bOffset.addPosition(65, 200.0);
        bOffset.up();


        /*
M 21.5 22.0 L 23.0 22.5 L 22.5 43.0 L 21.0 42.5 L 21.5 22.0 Z

21.5,22.0 
L 
23.0,22.5 
L 
22.5,43.0 
L 
21.0,42.5
L 
21.5,22.0 
Z
         */
        bOffset.addPosition(22, 440.0);
        bOffset.down();
        bOffset.addPosition(230, 450);
        bOffset.addPosition(225, 860);
        bOffset.addPosition(210, 850);
        bOffset.addPosition(215, 440.0);
        bOffset.up();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of init method, of class ImageVectorizer.
     */
    @Test
    public void testInit() {
        System.out.println("init");
       // instance.init();
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of process method, of class ImageVectorizer.
     */
    @Test
    public void testProcess2Lines() throws URISyntaxException {
        System.out.println("process");        
        
        File input = new File(this.getClass().getResource("2lines.png").getFile());
        
        List<Block> expResult = new ArrayList<>();
        expResult.add(b);
        List<Block> result = instance.process(input,xOffset,yOffset);
        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of svgToPointlist method, of class ImageVectorizer.
     */
    @Test
    public void testSvgToBlocklist() {
        System.out.println("svgToPointlist");
        
  
        List<Block> expResult = Collections.singletonList(b);
        List<Block> result = instance.svgToBlockList(twolinessvgstring,xOffset,yOffset);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of fileToSvg method, of class ImageVectorizer.
     */
    @Test
    public void testFileToSvg() throws Exception {
        System.out.println("fileToSvg");
        File input = new File(this.getClass().getResource("2lines.png").getFile());
        String result = instance.fileToSvg(input);
        assertEquals(twolinessvgstring, result);        
    }

    /**
     * Test of parsePath method, of class ImageVectorizer.
     */
    @Test
    public void testParsePath() {
        System.out.println("parsePath");
        String d = "M 6.5 10.0 L 45.0 10.5 L 44.5 12.0 L 6.0 11.5 L 6.5 10.0 Z";
        Block result = new Block();
        result.addPosition(6.5, 10.0);
        result.down();
        result.addPosition(45.0,10.5);
        result.addPosition(44.5,12.0);
        result.addPosition(6.0,11.5);
        result.addPosition(6.5,10.0);
        result.up();
        
        Block test = new Block();
        instance.parsePath(d, test, xOffset, yOffset);
        assertEquals(result, test);
        // TODO review the generated test code and remove the default call to fail.
    }
    
     @Test
    public void testParsePathOffset() {
        System.out.println("parsePath");
        String d = "M 6.5 10.0 L 45.0 10.5 L 44.5 12.0 L 6.0 11.5 L 6.5 10.0 Z";
        Block result = new Block();
        result.addPosition(65, 200.0);
        result.down();
        result.addPosition(450,210);
        result.addPosition(445,240);
        result.addPosition(60,230);
        result.addPosition(65,200.0);
        result.up();
        
        Block test = new Block();
        instance.parsePath(d, test, xOffset2,yOffset2);
        assertEquals(result, test);
        // TODO review the generated test code and remove the default call to fail.
    }
}
