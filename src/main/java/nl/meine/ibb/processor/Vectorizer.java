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
public abstract class Vectorizer {
    private static final Log log = LogFactory.getLog(Vectorizer.class);

    protected byte[][] palette;

    public void init() {
        palette = new byte[8][4];
        for (int colorcnt = 0; colorcnt < 8; colorcnt++) {
            palette[colorcnt][0] = (byte) (-128 + colorcnt * 32); // R
            palette[colorcnt][1] = (byte) (-128 + colorcnt * 32); // G
            palette[colorcnt][2] = (byte) (-128 + colorcnt * 32); // B
            palette[colorcnt][3] = (byte) 255;             // A
        }
    }

    public abstract List<Block> process(File input, int width, int height, double resolution);

    public abstract String fileToSvg(File input) throws Exception;

    protected HashMap<String, Float> getDefaultOptions() {
        HashMap<String, Float> options = new HashMap<>();

        // Tracing
        options.put("ltres", 1f);
        options.put("qtres", 1f);
        options.put("pathomit", 8f);

        // Color quantization
        options.put("colorsampling", 1f); // 1f means true ; 0f means false: starting with generated palette
        options.put("numberofcolors", 16f);
        options.put("mincolorratio", 0.02f);
        options.put("colorquantcycles", 3f);

        // SVG rendering
        options.put("scale", 1f);
        options.put("simplifytolerance", 0f);
        options.put("roundcoords", 1f); // 1f means rounded to 1 decimal places, like 7.3 ; 3f means rounded to 3 places, like 7.356 ; etc.
        options.put("lcpr", 0f);
        options.put("qcpr", 0f);
        options.put("desc", 0f); // 1f means true ; 0f means false: SVG descriptions deactivated
        options.put("viewbox", 0f); // 1f means true ; 0f means false: fixed width and height

        // Selective Gauss Blur
        options.put("blurradius", 0f); // 0f means deactivated; 1f .. 5f : blur with this radius
        options.put("blurdelta", 20f); // smaller than this RGB difference will be blurred
        return options;
    }
    
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
                    throw new IllegalArgumentException("SVG command '"+commandType +"' not supported.");
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
