package com.cintel.frame.cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cintel.frame.util.StringUtils;

@SuppressWarnings("serial")
public class OperateCmdServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        
        StringBuffer strBuf = new StringBuffer();
        String line = null;

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            strBuf.append(line);
        }
        
        String str = strBuf.toString().trim();
        while(str.indexOf("  ") != -1){
            str = str.replaceAll("  "," ");
        }
        String [] strArr = str.split(" ");
        List<String> listCommand = new ArrayList();
        if(strArr != null){
             listCommand = Arrays.asList(strArr);
        }
        String path = request.getParameter("path");
        if(!StringUtils.hasText(path)){
             path = request.getSession().getServletContext().getRealPath("/"); 
        }
        ProcessBuilder builder = new ProcessBuilder(listCommand);//windows 命令格式：cmd /c dir ; linux  格式：ls -al
        builder.directory(new File(path));
        Process process = builder.start();
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "GBK");
        BufferedReader br = new BufferedReader(isr);
        String responseStr = null;
        while ((responseStr = br.readLine()) != null) {
            out.println(responseStr);
        }
        out.flush();
        out.close();
    }
}
