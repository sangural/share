package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.james.mime4j.message.BinaryBody;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.message.TextBody;
import org.apache.james.mime4j.parser.Field;

public class MailParser {

    private StringBuffer txtBody;
    private StringBuffer htmlBody;
    private ArrayList<BodyPart> attachments;

    public void parse(String fileName) {
        FileInputStream file = null;

        txtBody = new StringBuffer();
        htmlBody = new StringBuffer();
        attachments = new ArrayList<BodyPart>();

        try {
           
            file = new FileInputStream(fileName);
          
            Message mimeMsg = new Message(file);          
            System.out.println("To: " + mimeMsg.getTo());
            System.out.println("From: " + mimeMsg.getFrom());
            System.out.println("Subject: " + mimeMsg.getSubject());

         
            Field priorityFld = mimeMsg.getHeader().getField("X-Priority");
           
            if (priorityFld != null) {
                System.out.println("Priority: " + priorityFld.getBody());
            }

           
            if (mimeMsg.isMultipart()) {
                Multipart multipart = (Multipart) mimeMsg.getBody();
                parseBodyParts(multipart);
            } else {
                
                String text = getTxtPart(mimeMsg);
                txtBody.append(text);
            }

 
            System.out.println("Text body: " + txtBody.toString());
            System.out.println("Html body: " + htmlBody.toString());

            for (BodyPart attach : attachments) {
                String attName = attach.getFilename();
               
                FileOutputStream fos = new FileOutputStream(attName);
                try {
                    
                    BinaryBody bb = (BinaryBody) attach.getBody();
                    bb.writeTo(fos);
                } finally {
                    fos.close();
                }
            }

        } catch (IOException ex) {
            ex.fillInStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    private void parseBodyParts(Multipart multipart) throws IOException {
        for (BodyPart part : multipart.getBodyParts()) {
            if (part.isMimeType("text/plain")) {
                String txt = getTxtPart(part);
                txtBody.append(txt);
            } else if (part.isMimeType("text/html")) {
                String html = getTxtPart(part);
                htmlBody.append(html);
            } else if (part.getDispositionType() != null && !part.getDispositionType().equals("")) {
               
                attachments.add(part);
            }

           
            if (part.isMultipart()) {
                parseBodyParts((Multipart) part.getBody());
            }
        }
    }

    private String getTxtPart(Entity part) throws IOException {
       
        TextBody tb = (TextBody) part.getBody();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tb.writeTo(baos);
        return new String(baos.toByteArray());
    }

}