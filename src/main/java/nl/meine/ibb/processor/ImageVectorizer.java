/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.meine.ibb.processor;

import jankovicsandras.imagetracer.ImageTracer;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import nl.meine.ibb.stripes.Block;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author meine
 */
public class ImageVectorizer extends Vectorizer {

    private byte[][] palette;

    public ImageVectorizer() {
        init();
    }

    @Override
    public void init() {
        palette = new byte[8][4];
        for (int colorcnt = 0; colorcnt < 8; colorcnt++) {
            palette[colorcnt][0] = (byte) (-128 + colorcnt * 32); // R
            palette[colorcnt][1] = (byte) (-128 + colorcnt * 32); // G
            palette[colorcnt][2] = (byte) (-128 + colorcnt * 32); // B
            palette[colorcnt][3] = (byte) 255;             // A
        }
    }

    @Override
    public List<Block> process(File input, int xOffset, int yOffset) {
        List<Block> pointlist = null;
        try {
            String svg = fileToSvg(input);
            pointlist = svgToBlockList(svg, xOffset, yOffset);
        } catch (Exception ex) {
            Logger.getLogger(ImageVectorizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pointlist;
    }

    
    
    /* For imagemagick
    // bmp --> png
    // jpg --> png
    // gif --> svg
    // flatten png 
        // png -> svg
     */

    @Override
    public String fileToSvg(File input) throws Exception{
        HashMap<String, Float> options = getDefaultOptions();
        String svg = ImageTracer.imageToSVG(input.getAbsolutePath(),options,palette);
   
        return svg;
    }
    
    private HashMap<String, Float> getDefaultOptions() {
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

    @Override
    public List<Block> svgToBlockList(String svg, int xOffset, int yOffset) {
        List<Block> blocks = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse(new InputSource(new StringReader(svg)));

            String xpathExpression = "//path/@d";
            //Now we can instantiate the XPath processor and compile the expression:

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            XPathExpression expression = xpath.compile(xpathExpression);
            //Since the expected result is a node-set (two strings), we evaluate the expression on the SVG document using XPathConstants.NODESET as the second parameter:

            NodeList svgPaths = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
            //From there you can extract the first set of path data using:
            Block b = new Block();
            blocks.add(b);
            for (int i = 0; i < svgPaths.getLength(); i++) {
                String d = svgPaths.item(i).getNodeValue();
                parsePath(d, b, xOffset, yOffset);
            }


        } catch (SAXException ex) {
            Logger.getLogger(ImageVectorizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageVectorizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(ImageVectorizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ImageVectorizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return blocks;
    }
    
    void parsePath(String d, Block b, int xOffset, int yOffset){
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
                //error
            }
            i++;
            String xString = tokens[i];
            i++;
            String yString = tokens[i];

            double x = Double.parseDouble(xString) * xOffset;
            double y = Double.parseDouble(yString) * yOffset;
            b.addPosition(x, y);
        }
    }
    
    


}
