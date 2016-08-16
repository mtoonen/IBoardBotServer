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

import jankovicsandras.imagetracer.ImageTracer;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import nl.meine.ibb.stripes.Block;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Meine Toonen
 */
public class ImageVectorizer extends Vectorizer {

    private static final Log log = LogFactory.getLog(ImageVectorizer.class);
    
    private double resolution; // resolution in mm/coord
    private int boardWidthCm;    // width in cm
    private int boardHeightCm;   // height in cm
    
    
    public ImageVectorizer() {
        this(36, 12, 0.1);        // Standard IBB size glass
    }
    
    public ImageVectorizer(int boardWidth, int boardHeight, double resolution){
        this.boardHeightCm = boardHeight;
        this.boardWidthCm = boardWidth;
        this.resolution = resolution;
        
       // width = Math.round(this.boardHeightCm / resolution) * 10; 
        init();
    }

 

    @Override
    public List<Block> process(File input, int width, int height, double resolution) {
        List<Block> pointlist = null;
        try {
            String svg = fileToSvg(input);
            pointlist = svgToBlockList(svg, width, height, resolution);
        } catch (Exception ex) {
            log.error("cannot process: ", ex);
        }
        return pointlist;
    }

    @Override
    public String fileToSvg(File input) throws Exception{
        HashMap<String, Float> options = getDefaultOptions();
        String svg = ImageTracer.imageToSVG(input.getAbsolutePath(),options,palette);
        return svg;
    }
}