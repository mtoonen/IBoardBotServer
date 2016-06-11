/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.meine.ibb.processor;

import jankovicsandras.imagetracer.ImageTracer;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.meine.ibb.stripes.Block;

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
        return Collections.singletonList(new Block());
    }


}
