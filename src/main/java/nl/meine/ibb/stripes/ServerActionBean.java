
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.meine.ibb.stripes;

import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import nl.meine.ibb.processor.ImageVectorizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author meine
 */
@UrlBinding("/action/server")
public class ServerActionBean implements ActionBean {

    private static final Log log = LogFactory.getLog(ServerActionBean.class);
    
    public ServerActionBean() {
    }

    private ActionBeanContext context;
    
    @Validate
    private String STATUS;

    @Validate
    private String ID_IWBB;

    @Validate
    private int NUM;

    /* int[][] commands = {
        {4009, 4001},//   [FA9FA1]    # Nuevo dibujo
        {4009, 0001},//   [FA9001]    # Block number (this number is calculated by the server)
        {4001, 4001},//   [FA1FA1]    # Start drawing (new draw)
        {4003, 0000},//   [FA3000]     # pen lift
        {0000, 0000},//   [000000]      # Move to  X = 0, Y = 0
        {1000, 1000},//   [3E83E8]     # Move to X = 100mm, Y = 100mm
        {4004, 0000},//   [FA4000]     # pen down (draw)
        {1500, 1000},//   [5DC3E8]    # Move to  X = 150mm, Y = 100mm
        {4003, 0000},//   [FA3000]     # Pen lift
        {0000, 0000},//   [000000]      # Move to 0,0
        {4002, 0000}//   [FA2000]     # Stop drawing (Finish).
    };*/
  
 
    // <editor-fold desc="Getters and Setters" defaultstate="collapsed">
    public void setNUM(int NUM) {
        this.NUM = NUM;
    }
   public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getID_IWBB() {
        return ID_IWBB;
    }

    public void setID_IWBB(String ID_IWBB) {
        this.ID_IWBB = ID_IWBB;
    }

    public int getNUM() {
        return NUM;
    }

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }
    // </editor-fold>

    @DefaultHandler
    public Resolution poll() {
        System.out.println("Polled: Status:" + STATUS + " num: " + NUM);
        final Block b = new Block(1);
        b.addPosition(1000,100);
        b.down();
        b.addPosition(10500, 150);
        b.up();
        b.finish();

        StreamingResolution res = new StreamingResolution("application/octet-stream") {
            @Override
            public void stream(HttpServletResponse response) throws Exception {
                OutputStream out = response.getOutputStream();
                b.write(out);
                out.close();
            }
        };
        res.setAttachment(true);
        res.setFilename(ID_IWBB + ".g092");
        return res;
    }
}
