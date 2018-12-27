package com.example.demo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class TestMain  {
	public static void main(String[] args)  {
		 String eml = "C://Users/sunil.singh/Downloads/test_sample_message.eml";

	        MailParser parser = new MailParser();
	        parser.parse(eml);
		 }
}
