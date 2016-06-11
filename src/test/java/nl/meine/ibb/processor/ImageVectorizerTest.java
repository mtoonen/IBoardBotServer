/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author meine
 */
public class ImageVectorizerTest {
    
    public ImageVectorizerTest() {
    }
    
    private ImageVectorizer instance = new ImageVectorizer();
    private final String twolinessvgstring = "<svg width=\"50\" height=\"50\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" ><path fill=\"rgb(0,0,0)\" stroke=\"rgb(0,0,0)\" stroke-width=\"1\" opacity=\"0.0\" d=\"M 0.5 0.0 L 50.0 0.5 L 49.5 50.0 L 0.0 49.5 L 0.5 0.0 Z\" /><path fill=\"rgb(0,0,0)\" stroke=\"rgb(0,0,0)\" stroke-width=\"1\" opacity=\"1.0\" d=\"M 6.5 10.0 L 45.0 10.5 L 44.5 12.0 L 6.0 11.5 L 6.5 10.0 Z\" /><path fill=\"rgb(0,0,0)\" stroke=\"rgb(0,0,0)\" stroke-width=\"1\" opacity=\"1.0\" d=\"M 21.5 22.0 L 23.0 22.5 L 22.5 43.0 L 21.0 42.5 L 21.5 22.0 Z\" /></svg>";
    
    private int xOffset = 1;
    private int yOffset = 1;
    
    private static Block b;
    
    
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
        b.addPosition(0.5, 0.0);
        b.down();
        b.addPosition(50.0,0.5);
        b.addPosition(49.5,50.0);
        b.addPosition(0.0,49.5);
        b.addPosition(0.5,0.0);
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
        b.addPosition(6.5, 10.0);
        b.down();
        b.addPosition(45.0,10.5);
        b.addPosition(44.5,12.0);
        b.addPosition(6.0,11.5);
        b.addPosition(6.5,10.0);
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
        
        b.addPosition(21.5, 22.0);
        b.down();
        b.addPosition(23.0, 22.5);
        b.addPosition(22.5, 43.0);
        b.addPosition(21.0, 42.5);
        b.addPosition(21.5, 22.0);
        b.up();
        b.finish();
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
     * Test of process method, of class ImageVectorizer.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        File input = null;
        List<Block> expResult = Collections.singletonList(b);
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
     * Test of svgToBlockList method, of class ImageVectorizer.
     */
    @Test
    public void testSvgToBlockList() {
        System.out.println("svgToBlockList");
        String svg = "";
        int xOffset = 0;
        int yOffset = 0;
        ImageVectorizer instance = new ImageVectorizer();
        List<Block> expResult = null;
        List<Block> result = instance.svgToBlockList(svg, xOffset, yOffset);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        result.finish();
        
        Block test = new Block();
        instance.parsePath(d, test);
        test.finish();
        assertEquals(result, test);
        // TODO review the generated test code and remove the default call to fail.
    }
}
