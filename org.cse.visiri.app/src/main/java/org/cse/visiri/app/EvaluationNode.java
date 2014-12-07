package org.cse.visiri.app;

import org.cse.visiri.communication.Environment;
import org.cse.visiri.core.Node;
import org.cse.visiri.util.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by visiri on 12/7/14.
 */
public class EvaluationNode {

    public static void main(String[] args){
        Environment environment;
        Evaluation evaluation=new Evaluation();
        Node node=new Node();

        node.initialize();

        Scanner sc = new Scanner(System.in);
        // System.out.println("");
        int command =   1;//sc.nextInt();

        if(command != 1)
        {
            return;
        }

        List<Query> debsQueryList=evaluation.getDEBSQueries();
        List<Query> stockQueryList=evaluation.getStockQueries();

        List<Query> queryList= new ArrayList<Query>();
        queryList.addAll(debsQueryList);
        queryList.addAll(stockQueryList);


    }
}
