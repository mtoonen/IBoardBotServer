/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.meine.ibb.processor;

import java.io.File;
import java.util.List;
import nl.meine.ibb.stripes.Block;

/**
 *
 * @author meine
 */
public abstract class Vectorizer {
    
    public abstract void init();
    
    public abstract List<Block> process(File input, int xOffset, int yOffset);
    
    public abstract String fileToSvg(File input)throws Exception;
    
    public abstract List<Block> svgToBlockList(String svg, int xOffset, int yOffset);
    
}
