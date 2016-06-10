/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.meine.ibb.processor;

import java.io.File;

/**
 *
 * @author meine
 */
public abstract class Vectorizer {
    
    public abstract void init();
    public abstract File process(File input);
    
}
