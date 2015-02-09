/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cse.visiri.app.util;


import au.com.bytecode.opencsv.CSVReader;
import org.cse.visiri.util.Event;
import org.cse.visiri.util.StreamDefinition;

import java.io.FileReader;
import java.io.IOException;



public class CsvEventReader {

    CSVReader reader;
    String [] columns ;
    StreamDefinition sd;

    public CsvEventReader(String filename,StreamDefinition sd) throws  Exception
    {
        reader = new CSVReader( new FileReader(filename));
        columns = reader.readNext();
        this.sd = sd;
    }

    public Event getNextEvent() throws Exception {
        String[] row = reader.readNext();
        if(row == null)
        {
            return  null;
        }

        Object [] data = new Object[row.length];
        for(int i=0 ; i < row.length; i++)
        {
            Object obj = null;
            String col= row[i];

            //handle empty
            if(col.equals("-"))
            {
                return getNextEvent();
            }

            switch (sd.getAttributeList().get(i).getType())
            {
                case BOOLEAN:
                    obj = (Boolean) col.equals("1");
                    break;
                case FLOAT:
                    obj = Float.parseFloat(col);
                    break;
                case INTEGER:
                    obj = Integer.parseInt(col);
                    break;
                case STRING:
                    obj = col;
                    break;
                case DOUBLE:
                    obj = Double.parseDouble(col);
                    break;
                case LONG:
                    obj = Long.parseLong(col);
                    break;
            }

            data[i] = obj;
        }

        Event evt = new Event();
        evt.setStreamId(sd.getStreamId());
        evt.setData(data);

        return evt;
    }

    public void close()
    {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
