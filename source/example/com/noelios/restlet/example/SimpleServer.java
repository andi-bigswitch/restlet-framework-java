/*
 * Copyright 2005-2006 Jerome LOUVEL
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * http://www.opensource.org/licenses/cddl1.txt
 * If applicable, add the following below this CDDL
 * HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package com.noelios.restlet.example;

import java.util.List;

import org.restlet.AbstractRestlet;
import org.restlet.Call;
import org.restlet.Restlet;
import org.restlet.component.RestletContainer;
import org.restlet.connector.DefaultServer;
import org.restlet.data.Form;
import org.restlet.data.MediaTypes;
import org.restlet.data.Parameter;
import org.restlet.data.Protocols;

import com.noelios.restlet.HostMaplet;
import com.noelios.restlet.data.StringRepresentation;

/**
 * Simple HTTP server invoked by the simple client.
 */
public class SimpleServer
{
   public static void main(String[] args)
   {
      try
      {
         // Create a new Restlet container
         RestletContainer myContainer = new RestletContainer("My container");

         // Create the HTTP server connector, then add it as a server
         // connector to the Restlet container. Note that the container
         // is the call restlet.
         myContainer.addServer(new DefaultServer(Protocols.HTTP, "My connector", myContainer, 9876));

         // Attach a host Maplet as the root handler
         HostMaplet rootMaplet = new HostMaplet(myContainer, 9876);
         myContainer.attach(rootMaplet);

         // Prepare and attach a test Restlet
         Restlet testRestlet = new AbstractRestlet(myContainer)
         {
            public void handlePut(Call call)
            {
               System.out.println("Handling the call...");
               System.out.println("Trying to get the input as a form...");
               Form form = call.getInputAsForm();

               System.out.println("Trying to getParameters...");
               List<Parameter> params = form.getParameters();
               StringBuffer sb = new StringBuffer("foo");
               for(Parameter p : params)
               {
                  System.out.println(p);

                  sb.append("field name = ");
                  sb.append(p.getName());
                  sb.append("value = ");
                  sb.append(p.getValue());
                  sb.append("\n");
                  System.out.println(sb.toString());
               }

               call.setOutput(new StringRepresentation(sb.toString(), MediaTypes.TEXT_PLAIN));
            }
         };
         rootMaplet.attach("/test", testRestlet);

         // Now, start the container
         myContainer.start();
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }
}
