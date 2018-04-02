package org.xm.api.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OldServerKiller {
	private static final Logger log = LoggerFactory.getLogger(OldServerKiller.class);
	private static Set<Integer> ports = new HashSet<Integer>();
    
	public static void init(Integer...args){
		for(int port : args){
			start(port);
		}
	}
	
    private static void start(int port){
    	ports.add(port);
        Runtime runtime = Runtime.getRuntime();
        try {
            //查找进程号
            Process p = runtime.exec("cmd /c netstat -ano | findstr \""+port+"\"");
            InputStream inputStream = p.getInputStream();
            List<String> read = read(inputStream, "UTF-8");
            if(read.size() > 0){
            	for (String string : read) {
                    System.out.println(string);
                }
                kill(read);
            }
            else{
            	log.info("port[" + port + "] is avilable!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    /**
     * 验证此行是否为指定的端口，因为 findstr命令会是把包含的找出来，例如查找80端口，但是会把8099查找出来
     * @param str
     * @return
     */
    private static boolean validPort(String str){
        Pattern pattern = Pattern.compile("^ *[a-zA-Z]+ +\\S+");
        Matcher matcher = pattern.matcher(str);

        matcher.find();
        String find = matcher.group();
        int spstart = find.lastIndexOf(":");
        find = find.substring(spstart + 1);
        
        int port = 0;
        try {
            port = Integer.parseInt(find);
        } catch (NumberFormatException e) {
            return false;
        }
        if(ports.contains(port)){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * 更换为一个Set，去掉重复的pid值
     * @param data
     */
    private static void kill(List<String> data){
        Set<Integer> pids = new HashSet<Integer>();
        for (String line : data) {
            int offset = line.lastIndexOf(" ");
            String spid = line.substring(offset);
            spid = spid.replaceAll(" ", "");
            int pid = 0;
            try {
                pid = Integer.parseInt(spid);
            } catch (NumberFormatException e) {
            }
            pids.add(pid);
        }
        killWithPid(pids);
    }
    
    /**
     * 一次性杀除所有的端口
     * @param pids
     */
    private static void killWithPid(Set<Integer> pids){
        for (Integer pid : pids) {
            try {
                Process process = Runtime.getRuntime().exec("taskkill /F /pid "+pid+"");
                InputStream inputStream = process.getInputStream();
                String txt = readTxt(inputStream, "GBK");
                System.out.println(txt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    private static List<String> read(InputStream in,String charset) throws IOException{
        List<String> data = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        String line;
        while((line = reader.readLine()) != null){
            boolean validPort = validPort(line);
            if(validPort){
                data.add(line);
            }
        }
        reader.close();
        return data;
    }
    private static String readTxt(InputStream in,String charset) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        StringBuffer sb = new StringBuffer();
        String line;
        while((line = reader.readLine()) != null){
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
