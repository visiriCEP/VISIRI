package org.cse.visiri.app;

import org.cse.visiri.util.Configuration;
import org.cse.visiri.util.Query;
import org.cse.visiri.util.StreamDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by visiri on 12/6/14.
 */
public class Evaluation {

    public static void main(String[] args){

        StreamDefinition inputStreamDef=new StreamDefinition();
        inputStreamDef.setStreamId("players");
        List<StreamDefinition.Attribute> attributeList=new ArrayList<StreamDefinition.Attribute>();

        inputStreamDef.addAttribute("sid", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("x", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("y", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("z", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("v", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("a", StreamDefinition.Type.INTEGER);

        List<StreamDefinition> inputStreamDefinitionList=new ArrayList<StreamDefinition>();
        inputStreamDefinitionList.add(inputStreamDef);

        StreamDefinition outputStreamdef1=new StreamDefinition();
        outputStreamdef1.setStreamId("stopplayer");
        outputStreamdef1.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef1.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q1="from players[v<=1]" +
                "select sid,x,y,z,v,a" +
                "insert into stopplayer";
        Query query1=new Query(q1,inputStreamDefinitionList,outputStreamdef1,"1", Configuration.ENGINE_TYPE_SIDDHI);

        StreamDefinition outputStreamdef2=new StreamDefinition();
        outputStreamdef2.setStreamId("trotplayer");
        outputStreamdef2.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef2.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q2="from players[v>1 and v<=11] " +
                "select sid,x,y,z,v,a " +
                "insert into trotplayer";

        Query query2=new Query(q2,inputStreamDefinitionList,outputStreamdef2,"2", Configuration.ENGINE_TYPE_SIDDHI);

        StreamDefinition outputStreamdef3=new StreamDefinition();
        outputStreamdef3.setStreamId("lowplayer");
        outputStreamdef3.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef3.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q3="from players[v>11 and v<=14] " +
                "select sid,x,y,z,v,a " +
                "insert into lowplayer";

        Query query3=new Query(q3,inputStreamDefinitionList,outputStreamdef3,"3", Configuration.ENGINE_TYPE_SIDDHI);

        StreamDefinition outputStreamdef4=new StreamDefinition();
        outputStreamdef4.setStreamId("mediumplayer");
        outputStreamdef4.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef4.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q4="from players[v>14 and v<=17]" +
                "select sid,x,y,z,v,a" +
                "insert into mediumplayer";

        Query query4=new Query(q4,inputStreamDefinitionList,outputStreamdef4,"4", Configuration.ENGINE_TYPE_SIDDHI);


        StreamDefinition outputStreamdef5=new StreamDefinition();
        outputStreamdef5.setStreamId("highplayer");
        outputStreamdef5.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef5.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q5="from players[v>17 and v<=24]" +
                "select sid,x,y,z,v,a" +
                "insert into highplayer";

        Query query5=new Query(q5,inputStreamDefinitionList,outputStreamdef5,"5", Configuration.ENGINE_TYPE_SIDDHI);

        StreamDefinition outputStreamdef6=new StreamDefinition();
        outputStreamdef6.setStreamId("sprintplayer");
        outputStreamdef6.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("x", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("y", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("z", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("v", StreamDefinition.Type.INTEGER);
        outputStreamdef6.addAttribute("a", StreamDefinition.Type.INTEGER);

        String q6="from players[v>24]" +
                "select sid,x,y,z,v,a" +
                "insert into sprintplayer";

        Query query6=new Query(q6,inputStreamDefinitionList,outputStreamdef6,"6", Configuration.ENGINE_TYPE_SIDDHI);
        StreamDefinition outputStreamdefAvgSpeedPlayer=new StreamDefinition();
        outputStreamdefAvgSpeedPlayer.setStreamId("avgspeedplayers");
        outputStreamdefAvgSpeedPlayer.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdefAvgSpeedPlayer.addAttribute("avgV", StreamDefinition.Type.INTEGER);

        String q15="from players#window.length(50) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers; ";
        Query query15=new Query(q15,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"15", Configuration.ENGINE_TYPE_SIDDHI);

        String q7="from players#window.length(100) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers; ";
        Query query7=new Query(q7,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"7", Configuration.ENGINE_TYPE_SIDDHI);

        String q8="from players#window.length(200) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers;";
        Query query8=new Query(q8,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"8", Configuration.ENGINE_TYPE_SIDDHI);


        StreamDefinition outputStreamdefHighSpeedPlayer=new StreamDefinition();
        outputStreamdefHighSpeedPlayer.setStreamId("highspeedplayers");
        outputStreamdefHighSpeedPlayer.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdefHighSpeedPlayer.addAttribute("avgV", StreamDefinition.Type.INTEGER);

        String q9="from players#window.length(50) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>17 " +
                "insert into highspeedplayers;";
        Query query9=new Query(q9,inputStreamDefinitionList,outputStreamdefHighSpeedPlayer,"9", Configuration.ENGINE_TYPE_SIDDHI);

        String q10="from players#window.length(100) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>17 " +
                "insert into highspeedplayers;";
        Query query10=new Query(q10,inputStreamDefinitionList,outputStreamdefHighSpeedPlayer,"10", Configuration.ENGINE_TYPE_SIDDHI);

        String q11="from players#window.length(200) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>17 " +
                "insert into highspeedplayers;";
        Query query11=new Query(q11,inputStreamDefinitionList,outputStreamdefHighSpeedPlayer,"11", Configuration.ENGINE_TYPE_SIDDHI);
        //////////////////////////////////////////////////////////// 11 end

        String q12="from players#window.time(1sec) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers;";
        Query query12=new Query(q12,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"12", Configuration.ENGINE_TYPE_SIDDHI);

        String q13="from players#window.time(5sec) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers;";
        Query query13=new Query(q13,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"13", Configuration.ENGINE_TYPE_SIDDHI);

        String q14="from players#window.time(10sec) " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers;";
        Query query14=new Query(q14,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"14", Configuration.ENGINE_TYPE_SIDDHI);



    }
}
