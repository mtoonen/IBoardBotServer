/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.meine.ibb.stripes;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

/**
 *
 * @author meine
 */
@UrlBinding("/action/server")
public class ServerActionBean implements ActionBean {

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
    String[] commands = {
        "FA9FA1",
        "FA9001",
        "FA1FA1",
        "FA3000",
        "000000",
        "3E83E8",
        "FA4000",
        "5DC3E8",
        "FA3000",
        "000000",
        "FA2000"
    };
    
    String aapCommand = "fa9f a1fa 9092 8342 58fa 4000 7e92 587e\n" +
"92d9 7fa2 d97f a267 8342 6783 4258 8342\n" +
"58fa 3000 fa30 0089 8258 fa40 0084 d258\n" +
"84d2 d985 f2d9 85f2 6789 8267 8982 5889\n" +
"8258 fa30 00fa 3000 8ca2 58fa 4000 8b52\n" +
"588b 5271 8ca2 718c a258 8ca2 58fa 3000\n" +
"fa30 008f b258 fa40 008e 6258 8e62 718f\n" +
"b271 8fb2 588f b258 fa30 00fa 3000 92d2\n" +
"58fa 4000 9182 5891 8271 92d2 7192 d258\n" +
"92d2 58fa 3000 fa30 0095 e258 fa40 0094\n" +
"9258 9492 7195 e271 95e2 5895 e258 fa30\n" +
"00fa 3000 fa30 0011 b18d fa40 000f 612c\n" +
"0e81 2c0c 318d 0d51 8d0e f142 10a1 8d11\n" +
"b18d 11b1 8dfa 3000 fa30 0016 9144 fa40\n" +
"0016 915f 15a1 5d15 215c 14c1 5b14 6158\n" +
"1411 5413 f152 13e1 5013 d149 13e1 4214\n" +
"013f 1421 3c14 413b 1471 3914 f138 1571\n" +
"3915 d13b 1631 3f16 9144 1691 44fa 3000\n" +
"fa30 0016 9136 fa40 0016 4132 15d1 2e15\n" +
"512a 1501 2914 9129 1441 2a13 e12b 1391\n" +
"2e13 5132 1311 3612 e13c 12d1 4212 c148\n" +
"12d1 5113 0159 1331 5c13 615f 13e1 6414\n" +
"7167 1511 6a16 916c 1691 6f16 9174 1671\n" +
"7816 517b 1621 7d15 f17f 15b1 8015 2181\n" +
"14b1 8014 417f 13d1 7d13 517a 1341 7a13\n" +
"418b 1411 8e14 918f 1521 8f15 b18f 1621\n" +
"8e16 918c 16f1 8817 3183 1771 7e17 9177\n" +
"1791 6e17 912c 1691 2c16 9136 1691 36fa\n" +
"3000 fa30 001d e12c fa40 001c d12c 1cd1\n" +
"631c c170 1cc1 751c a179 1c81 7c1c 517e\n" +
"1c01 801b b180 1b51 7f1a e17d 1a81 791a\n" +
"2174 1a21 2c19 112c 1911 8d1a 218d 1a21\n" +
"821a 9188 1b01 8c1b 718f 1bf1 901c 618f\n" +
"1cc1 8d1d 118a 1d61 861d 9181 1db1 7b1d\n" +
"d173 1de1 6b1d e12c 1de1 2cfa 3000 fa30\n" +
"0021 4174 fa40 001f f174 1ff1 8d21 418d\n" +
"2141 7421 4174 fa30 00fa 3000 2141 2cfa\n" +
"4000 1ff1 2c1f f145 2141 4521 412c 2141\n" +
"2cfa 3000 fa30 00fa 3000 2cf1 2cfa 4000\n" +
"2bf1 2c2b f163 2be1 702b d175 2bb1 792b\n" +
"917c 2b61 7e2b 2180 2ac1 802a 617f 2a01\n" +
"7d29 9179 2931 7429 312c 2831 2c28 318d\n" +
"2931 8d29 3182 29a1 882a 118c 2a91 8f2b\n" +
"0190 2b71 8f2b d18d 2c21 8a2c 7186 2ca1\n" +
"812c d17b 2ce1 732c f16b 2cf1 2c2c f12c\n" +
"fa30 00fa 3000 3371 5cfa 4000 3361 5133\n" +
"4147 3301 3e32 b136 3251 3131 d12c 3151\n" +
"2a30 b129 3021 2a2f 912c 2f21 312e c137";

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

    // <editor-fold desc="Getters and Setters" default-state="collapsed">
    public void setNUM(int NUM) {
        this.NUM = NUM;
    }

    // </editor-fold>
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
        System.out.println("Polled: Status:" + STATUS + " num: " + NUM);
       
    /*   if(ID_IWBB.equalsIgnoreCase("5CCF7F0D7F90")){
            return null;
        }*/
        StreamingResolution res = new StreamingResolution("plain/text") {
            @Override
            public void stream(HttpServletResponse response) throws Exception {


                OutputStream out = response.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out);
                String command = aapCommand;
               /* for (int i = 0; i < commands.length; i++) {
                    
                    String com = commands[i];
                    System.out.println("Command: " + com);
                    command += com;
                }
                String sendString = "";
                for (int i = 0; i < command.length(); i++) {
                    if (i != 0) {
                        if (i % 4 == 0) {
                            sendString += " ";
                        }
                        if (i % 32 == 0) {
                            sendString += "\n";
                        }
                    }
                    sendString += command.charAt(i);
                                    
                }*/
                osw.write(command);
                osw.flush();
                osw.close();
                out.close();
                //for (int[] command : commands) {
               //     osw.write(convert(commands[0]));
                //}
            }
        };
        res.setAttachment(false);
        
        return res;
    }

    public String convert(int[] command) {
        String s = Integer.toBinaryString(command[0]);
        s += Integer.toBinaryString(command[1]);
        return s;
    }

}
