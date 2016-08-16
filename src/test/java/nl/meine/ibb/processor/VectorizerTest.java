
package nl.meine.ibb.processor;

import java.io.File;
import java.util.List;
import nl.meine.ibb.stripes.Block;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

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

/**
 *
 * @author Meine Toonen
 */
public class VectorizerTest {
    
    private int width = 1;
    private int height = 1;
    
    private int width2 = 10;
    private int height2 = 20;
    
    private Vectorizer instance;
    
    @Before
    public void setupClass(){
        instance = new Vectorizer() {
            @Override
            public List<Block> process(File input, int width, int height, double resolution) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String fileToSvg(File input) throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
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
        instance.parsePath(d, test, width, height);
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
        instance.parsePath(d, test, width2,height2);
        assertEquals(result, test);
    }
    
     /**
     * Test of parsePath method, of class ImageVectorizer.
     */
    @Test
    public void testPathToRelativeCoords() {
        System.out.println("parsePath");
        String d = "M 6.5 10.0 L 45.0 10.5 L 44.5 12.0 L 6.0 11.5 L 6.5 10.0 Z";
        
        int imgWidth = 50;
        int imgHeight = 50;

        int boardWidth = 36; // 36 /0.1 * 10 
        int boardHeight = 12; // 12/0.1*10
        double resolution = 0.1;
        double widthRatio = (boardWidth / resolution * 10) / imgWidth;
        double heightRatio = (boardHeight / resolution * 10) / imgHeight;

        ImageVectorizer instance = new ImageVectorizer(boardWidth, boardHeight, resolution);
        Block result = new Block();
        result.addPosition(6.5 * widthRatio, 10.0 * heightRatio);
        result.down();
        result.addPosition(45.0 * widthRatio,10.5* heightRatio);
        result.addPosition(44.5 * widthRatio,12.0* heightRatio);
        result.addPosition(6.0 * widthRatio,11.5* heightRatio);
        result.addPosition(6.5 * widthRatio,10.0* heightRatio);
        result.up();
        
        Block test = new Block();
        instance.parsePath(d, test, widthRatio, heightRatio);
        assertEquals(result.toHumanReadableString(), test.toHumanReadableString());
    }
}
