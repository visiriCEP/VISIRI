package org.cse.visiri.app;

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
       // StreamDefinition.Attribute attribute=new StreamDefinition.Attribute();



        inputStreamDef.addAttribute("sid", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("x", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("y", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("z", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("v", StreamDefinition.Type.INTEGER);
        inputStreamDef.addAttribute("a", StreamDefinition.Type.INTEGER);

        StreamDefinition outputStreamdef=inputStreamDef;
        List<StreamDefinition> streamDefinitionList=new ArrayList<StreamDefinition>();
        streamDefinitionList.add(inputStreamDef);
       // Query query1=new Query()

    }
}
