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



        ///////////////////////////////////////////////////////////////////////////6 to 11

        StreamDefinition outputStreamdefAvgSpeedPlayer=new StreamDefinition();
        outputStreamdefAvgSpeedPlayer.setStreamId("avgspeedplayers");
        outputStreamdefAvgSpeedPlayer.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdefAvgSpeedPlayer.addAttribute("avgV", StreamDefinition.Type.INTEGER);

        String q6="from players#window.length[50] " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers; ";
        Query query6=new Query(q6,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"6", Configuration.ENGINE_TYPE_SIDDHI);

        String q7="from players#window.length[100] " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers; ";
        Query query7=new Query(q7,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"7", Configuration.ENGINE_TYPE_SIDDHI);

        String q8="from players#window.length[200] " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>12 " +
                "insert into avgspeedplayers;";
        Query query8=new Query(q8,inputStreamDefinitionList,outputStreamdefAvgSpeedPlayer,"8", Configuration.ENGINE_TYPE_SIDDHI);


        StreamDefinition outputStreamdefHighSpeedPlayer=new StreamDefinition();
        outputStreamdefHighSpeedPlayer.setStreamId("highspeedplayers");
        outputStreamdefHighSpeedPlayer.addAttribute("sid", StreamDefinition.Type.INTEGER);
        outputStreamdefHighSpeedPlayer.addAttribute("avgV", StreamDefinition.Type.INTEGER);

        String q9="from players#window.length[50] " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>17 " +
                "insert into highspeedplayers;";
        Query query9=new Query(q9,inputStreamDefinitionList,outputStreamdefHighSpeedPlayer,"9", Configuration.ENGINE_TYPE_SIDDHI);

        String q10="from players#window.length[100] " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>17 " +
                "insert into highspeedplayers;";
        Query query10=new Query(q10,inputStreamDefinitionList,outputStreamdefHighSpeedPlayer,"10", Configuration.ENGINE_TYPE_SIDDHI);

        String q11="from players#window.length[200] " +
                "select sid,avg(v) as avgV " +
                "group by sid having avgV>17 " +
                "insert into highspeedplayers;";
        Query query11=new Query(q11,inputStreamDefinitionList,outputStreamdefHighSpeedPlayer,"11", Configuration.ENGINE_TYPE_SIDDHI);
        //////////////////////////////////////////////////////////// 11 end


    }
}
