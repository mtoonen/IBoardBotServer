/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.meine.ibb.stripes;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
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
public class ServerActionBeanTest {
    
    public ServerActionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     * Test of main method, of class ServerActionBean.
     */
    @Test
    public void testMain() {
       // String s = "abc";
        //String s = "40014001";
       // int s= 4001; //111110100001
                       //111110100001
                      //111110100001
                      //111110100001
                      //111110100001
        //String s = "FA1FA1"; //1000110100000111000110001101000001110001
                     //111110100001 111110100001
        String s = "FA1FA1";// FA1";
        /*"FA9001";
        "FA1FA1";
        "FA3000";*/
        ServerActionBean sab = new ServerActionBean();
        byte[] val = sab.commandToByte(s);
        System.out.println(val);
        
    }
    
}
