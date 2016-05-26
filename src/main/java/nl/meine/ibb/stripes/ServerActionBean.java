/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.meine.ibb.stripes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 *
 * @author meine
 */
@UrlBinding("/action/server")
public class ServerActionBean implements ActionBean {

    int[][] commands = {
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
    };

    public ServerActionBean() {
    }

    private ActionBeanContext context;

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    @DefaultHandler
    public Resolution poll() {
        StreamingResolution res = new StreamingResolution("plain/text") {
            @Override
            public void stream(HttpServletResponse response) throws Exception {

                OutputStream out = response.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out);
                for (int[] command : commands) {
                    osw.write(convert(command));
                }
            }
        };
        return res;
    }

    public String convert(int[] command) {
        String s = Integer.toBinaryString(command[0]);
        s += Integer.toBinaryString(command[1]);
        return s;
    }

}
