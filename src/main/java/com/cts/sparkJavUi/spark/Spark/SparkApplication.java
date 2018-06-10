package com.cts.sparkJavUi.spark.Spark;
import static spark.Spark.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import spark.utils.IOUtils;

public class SparkApplication {
    public static void main(String[] args) {
    	//refer to your resources files here
    	staticFiles.location("/public");
    	//mapping index to index.html
        get("/index", (req, res) -> {
        	res.type("text/html"); 
        	res.redirect("html/index.html");
        	return "";
        	});
        //Post request handler
        post("/api/submit", (req,res) ->
        {
        	//this line is absolutely necessary, without this the form-data & multipart file html page wont get any values
        	req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
        	
        	//Get the uploaded file 
        	Part uploadedFile = null;
			try {
				uploadedFile = req.raw().getPart("myFile");
			} catch (IOException | ServletException e) {
				e.printStackTrace();
			}
			
			//Read the uploaded file as a inputeStream
			try(InputStream inStream = uploadedFile.getInputStream()){
    			StringWriter writer = new StringWriter();
    			IOUtils.copy(inStream, writer);
    			String theString = writer.toString();
    			//Printing content
    			System.out.println("Content from Uploaded file: " + theString);
    		} catch (IOException e) {
				e.printStackTrace();
			}
        	//get all the form attributes.
        	Map<String, String[]> parameterMap = req.raw().getParameterMap();
        	Set<String> parameterSet = parameterMap.keySet();
        	parameterSet.stream().forEach(each -> {
        		String join = String.join(",",parameterMap.get(each));
        		System.out.println("This is the key from parameter map: " + each);
        		System.out.println("This is the value from parameter map: " + join);
        		});
        	return "Successfully uploaded the file";
        });
    }
}