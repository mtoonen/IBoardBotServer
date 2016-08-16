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
    
    @Override
    public List<Block> svgToBlockList(String svg, int width, int height, double resolution) {
        List<Block> blocks = new ArrayList<>();
        try {
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse(new InputSource(new StringReader(svg)));

            String widthString = "/svg/@width";
            String heightString = "/svg/@height";
            String pathString = "//path";
            
            //Now we can instantiate the XPath processor and compile the expression:

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            
            XPathExpression pathExpression = xpath.compile(pathString);
            XPathExpression widthExpression = xpath.compile(widthString);
            XPathExpression heightExpression = xpath.compile(heightString);
            
            //Since the expected result is a node-set (two strings), we evaluate the expression on the SVG document using XPathConstants.NODESET as the second parameter:

            NodeList svgPaths = (NodeList) pathExpression.evaluate(doc, XPathConstants.NODESET);
            
            double w = (double)widthExpression.evaluate(doc, XPathConstants.NUMBER);
            double h = (double)heightExpression.evaluate(doc, XPathConstants.NUMBER);
            
            double widthRatio = (width / resolution * 10) / w;
            double heightRatio = (height / resolution * 10) / h;
            
            //From there you can extract the first set of path data using:
            Block b = new Block();
            blocks.add(b);
            for (int i = 0; i < svgPaths.getLength(); i++) {
                Node path = svgPaths.item(i);
                NamedNodeMap nnm = path.getAttributes();
                String opacity = nnm.getNamedItem("opacity").getNodeValue();
                if (!opacity.equalsIgnoreCase("0.0")) {
                    String d = nnm.getNamedItem("d").getNodeValue();

                    parsePath(d, b, widthRatio, heightRatio);
                }
            }
        } catch (SAXException ex) {
            log.error("Cannot parse svg document:",ex);
        } catch (IOException ex) {
            log.error("Cannot open file: ",ex);
        } catch (XPathExpressionException ex) {
            log.error( "Cannot retrieve paths via xpath", ex);
        } catch (ParserConfigurationException ex) {
            log.error("Cannot parse svg document", ex);
        }
        return blocks;
    }
    
    void parsePath(String d, Block b, double widthRatio, double heightRatio){
        //d="M 6.5 10.0 L 45.0 10.5 L 44.5 12.0 L 6.0 11.5 L 6.5 10.0 Z"
        d = d.substring(0, d.length() - 3);
        String[] tokens = d.split(" ");
        for (int i = 0; i < tokens.length; i++) {
            String commandType = tokens[i];
            switch (commandType) {
                case "Z":
                    continue;
                case "M":
                    b.up();
                    break;
                case "L":
                    b.down();
                    break;
                default:
                    log.debug("Complete path: " + d);
                    log.error("Cannot parse commandtype >" + commandType + "<.");
                    break;
            }
            i++;
            String xString = tokens[i];
            i++;
            String yString = tokens[i];

            double x = Double.parseDouble(xString) * widthRatio;
            double y = Double.parseDouble(yString) * heightRatio;
            b.addPosition(x, y);
        }
        b.up();
    }
}