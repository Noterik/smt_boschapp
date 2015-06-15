/* 
* BoschApplication.java
* 
* Copyright (c) 2012 Noterik B.V.
* 
* This file is part of Lou, related to the Noterik Springfield project.
* It was created as a example of how to use the multiscreen toolkit
*
* Bosch app is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Bosch app is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Bosch app.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.springfield.lou.application.types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springfield.fs.Fs;
import org.springfield.fs.FsNode;
import org.springfield.lou.application.*;
import org.springfield.lou.screen.*;

public class BoschApplication extends Html5Application{
	
	private List<Screen> mainScreens;
	private List<Screen> secondScreens;
	private String charactersUrl = "/domain/espace/user/bosch/character";
	private ArrayList<String> colors = new ArrayList<String>( Arrays.asList("maroon", "red", "orange","yellow","olive","purple","fuchsia","lime","green","navy","blue","aqua","teal","silver","gray"));
	private double ratio = 0.7873d;
	private Random charRnd;
	private HashMap<String, FsNode> foundqueue;
	private List<FsNode> queue;
	private int characterCount;
	private int foundCharacters;
	
	
	
 	public BoschApplication(String id) {
		super(id); 
		mainScreens = new ArrayList<Screen>();
		secondScreens = new ArrayList<Screen>();
		charRnd = new Random();
		List<FsNode> characters = Fs.getNodes(charactersUrl, 1);
		//characterCount = characters.size();
		characterCount = 4;
		foundqueue = new HashMap<String, FsNode>();
		queue = new ArrayList<FsNode>();
		foundCharacters = 0;
	}
	
 	/*
 	 * This method is called when a browser window opens the application
 	 * @see org.springfield.lou.application.Html5Application#onNewScreen(org.springfield.lou.screen.Screen)
 	 */
    public void onNewScreen(Screen s) {
        
        String role = s.getParameter("role");
        if(role==null) role = "";
        if(role.equals("main")) {
        	loadStyleSheet(s, "generic"); //Loading the genereic style from css folder
        	Capabilities caps = s.getCapabilities();
        	int screenwidth = Integer.parseInt(caps.getCapability("screenwidth"));
        	int screenheight = Integer.parseInt(caps.getCapability("screenheight"));
        	s.setProperty("width", ""+screenwidth);
        	s.setProperty("height", ""+screenheight);
        	s.setRole("main");
        	mainScreens.clear();
        	mainScreens.add(s);
        	initMainScreen(s);
        } else {
        	loadStyleSheet(s, "mobile"); //Loading the mobile style from css folder
        	s.setRole("character");
        	secondScreens.add(s);
        	initSecondScreen(s);
        }
    }
    
    private void initMainScreen(Screen s) {
    	System.out.println("BOSCH Main screen");
    	s.setContent("wrap", "<div id=\"opacity\"></div>");
    	s.setDiv("opacity", "style:opacity:0.7");
    	//s.setContent("titlebar", "Please mark all the characters: " + characterCount);
    	
    }
    
    private void initSecondScreen(Screen s) {
    	System.out.println("BOSCH Second screen");
    	/*
    	List<FsNode> characters = Fs.getNodes(charactersUrl, 1);
        
    	if(characters.size()>0) {
    		FsNode character = characters.get(charRnd.nextInt(characters.size()));
    	}
    	*/
    	s.setProperty("color", colors.get(new Random().nextInt(colors.size())));
    	
    	s.setContent("wrap", " ");
    	//s.setDiv("wrap", "style:background:#777");
    	s.setDiv("wrap", "bind:mousedown", "onTouch", this);
    	String screenId = s.getId();
	   	String id = screenId.substring(screenId.lastIndexOf("/")+1);
	   	String divId = "touch"+id;
	   	
	   	setContentMainScreens("wrap", "<div id=\"opacity\"></div><div id=\""+divId+"\"></div>");
	   	setDivMainScreens("opacity", "style:opacity:0.7");
	   	setDivMainScreens(divId, "style:visibility:hidden");
	   	setDivMainScreens(divId, "style:position:absolute");
	   	setDivMainScreens(divId, "style:width:140px");
	   	setDivMainScreens(divId, "style:height:140px");
	   	setDivMainScreens(divId, "style:border-radius:70px");
	   	//setDivMainScreens(divId, "style:background:" + s.getProperty("color"));
	   	
	   	
	   	s.setContent("wrap", "<div id=\""+divId+"\"></div>");
	   	s.setDiv(divId, "draggable:{containment: 'parent'}");
	   	s.setDiv(divId,"bind:mousemove","onDrag",this);
	   	s.setDiv(divId, "bind:mouseup", "onRelease", this);
	   	s.setDiv(divId, "style:visibility:hidden");
	 	s.setDiv(divId, "style:position:absolute");
	   	s.setDiv(divId, "style:width:140px");
	   	s.setDiv(divId, "style:height:140px");
	   	s.setDiv(divId, "style:border-radius:70px");
	   	s.setDiv(divId, "style:background:" + s.getProperty("color"));
    }
    
    public void onRelease(Screen s, String c) {
    	System.out.println("BOSCH onRelease: " + c);
    	s.setProperties(c);
    	
    	Capabilities caps = s.getCapabilities();
    	
    	int mainscreenwidth = Integer.parseInt((String) mainScreens.get(0).getProperty("width"));
    	
    	int screenwidth = Integer.parseInt(caps.getCapability("screenwidth"));
    	int screenheight = Integer.parseInt(caps.getCapability("screenheight"));
    	System.out.println(screenwidth + "x" + screenheight);
    	Double ox = Double.parseDouble((String)s.getProperty("clientX"));
 	   	int x = ox.intValue();
 	   	x-=70; // Make center of the circle
 	   	double multipl2 = (double)100/(screenwidth-140);
 	   	double xper = x*multipl2;
 	    Double oy = Double.parseDouble((String)s.getProperty("clientY"));
	   	int y = oy.intValue();
	   	y-=70; // make center of the circle
	   	double multipl = (double)100/(screenheight-140);
	   	double yper = y*multipl;
	   	
	   	double mposx = (double) (xper*1139)/100;
	   	
	   	double xoffset = (double) (mainscreenwidth - 1139)/2;
	   	mposx = mposx - xoffset;
	   	//mposx = mposx;
	   	double mposy = (double) (yper*897)/100;
    	
    	
	   	//System.out.println("RELEASE: top: " + mposy + "px; left: " + mposx + "px");
	   	System.out.println("RELEASE: top: " + yper + "px; left: " + xper + "px");
	   	
	   	String screenId = s.getId();
	   	String id = screenId.substring(screenId.lastIndexOf("/")+1);
	   	String divId = "touch"+id;
	   	
	   	List<FsNode> characters = Fs.getNodes(charactersUrl, 1);
	   	System.out.println("onRelease: CHARACTERS COUNT: " + characters.size());
	   	for(FsNode ch : characters) {
	   		String cx = ch.getProperty("positionX");
	   		String cy = ch.getProperty("positionY");
	   		String characterName = ch.getProperty("name");
	   		Double dx = Double.parseDouble(cx);
	   		double charx = dx.doubleValue();
	   		Double dy = Double.parseDouble(cy);
	   		double chary = dy.doubleValue();
	   		//maid/ crepy2/flutelady/angelman
	   		String characterId = ch.getId();
	   		
	   		if(((dx-5) < xper && xper < (dx+5)) && ((dy-5) < yper && yper < (dy+5))) { // We got a match
	   			System.out.println("HIT CHARACTER: " + characterName);
	   			
	   			if(!foundqueue.containsKey(ch.getId())) {
		   			s.setProperty("microphone", "true");
		   			s.setContent(divId, "<input type=\"file\" accept=\"audio/*;capture=microphone\" id=\"micbutton\" >");
		   			foundqueue.put(ch.getId(), ch);
		   			queue.add(ch);
		   			foundCharacters++;
		   			
		   			String audioUrl = ch.getPath();
					audioUrl = audioUrl.replace("/character/character","/character");
					audioUrl = audioUrl + "/audio";
					
					System.out.println("animation: AUDIO URL:" + audioUrl);
					List<FsNode> audios = Fs.getNodes(audioUrl, 1);
					System.out.println("animation: AUDIO CNT:" + audios.size());
					FsNode audioNode = audios.get(new Random().nextInt(audios.size()));
					if(audioNode!=null) {
						
						String filename = audioNode.getProperty("file");
						filename = filename.replace(".mp3", "");
						setDivMainScreens("wrap", "sound:"+filename);
						System.out.println("sound:"+filename);
					}
		   			
		   			if(queue.size()==characterCount) {
		   				//setContentMainScreens("titlebar", "");
		   				try{
					   		Thread.sleep(10000);
					   	} catch (Exception e) {}
		   				beginAnimation();
		   			} else {
		   				//setContentMainScreens("titlebar", "Please mark all the characters: " + (characterCount - foundCharacters));
		   			}
	   			}
	   		}
	   		
	   		
	   	}
	   	
    }
    
    public void beginAnimation() {
    	
    	Screen mainScreen = mainScreens.get(0);
    	for(Screen ss : secondScreens) {
    		String screenId = ss.getId();
    	   	String id = screenId.substring(screenId.lastIndexOf("/")+1);
    	   	String divId = "touch"+id;
    	   	mainScreen.removeContent(divId);
    	}
    	
    	mainScreen.removeContent("opacity");
    	
    	int counter = 0;
    	for(FsNode ch : queue) {
    		counter++;
	    	String audioUrl = ch.getPath();
			audioUrl = audioUrl.replace("/character/character","/character");
			audioUrl = audioUrl + "/audio";
			
			System.out.println("animation: AUDIO URL:" + audioUrl);
			List<FsNode> audios = Fs.getNodes(audioUrl, 1);
			System.out.println("animation: AUDIO CNT:" + audios.size());
			FsNode audioNode = audios.get(new Random().nextInt(audios.size()));
			if(audioNode!=null) {
				
				String filename = audioNode.getProperty("file");
				filename = filename.replace(".mp3", "");
				setDivMainScreens("wrap", "sound:"+filename);
				System.out.println("sound:"+filename);
				
				/*
				String divId = "touch_main";
			   	
			   	setContentMainScreens(divId, " ");
			   	setDivMainScreens(divId, "style:visibility:hidden");
			   	setDivMainScreens(divId, "style:position:absolute");
			   	setDivMainScreens(divId, "style:width:140px");
			   	setDivMainScreens(divId, "style:height:140px");
			   	setDivMainScreens(divId, "style:border-radius:70px");
			   	
			   	int mainscreenwidth = Integer.parseInt((String) mainScreens.get(0).getProperty("width"));
			   	int mainscreenheight = Integer.parseInt((String) mainScreens.get(0).getProperty("height"));
			   	
			   	String ox = (String) ch.getProperty("positionX");
			   	double xper = Double.parseDouble(ox);
			   	String oy = (String) ch.getProperty("positionY");
			   	double yper = Double.parseDouble(oy);
			   	
			   	int mposx = (int) xper*mainscreenwidth / 100;
			   	//mposx = mposx;
			   	int mposy = (int) yper*mainscreenheight / 100;
			   	mposy = mposy -24;
			   	
			   
			   	setDivMainScreens(divId, "style:visibility:visible");
			   	setDivMainScreens(divId, "style:top:" + yper + "%");
			   	setDivMainScreens(divId, "style:left:" + xper + "%");
			   	setDivMainScreens(divId, "style:background: url(/eddie/apps/bosch/img/baccus.jpg)");
			   	setDivMainScreens(divId, "style:background-position:-"+mposx+"px -"+mposy+"px");
			   	setDivMainScreens(divId, "style:background-size: "+mainscreenwidth+"px "+(mainscreenwidth*ratio)+"px");
			   	setDivMainScreens(divId, "style:box-shadow:0px 0px 27px 11px #ffffff");*/
			   	
			   	try{
			   		Thread.sleep(10000);
			   	} catch (Exception e) {}
				
			}
    	}
    	
    	/*
    	double opacity = 0.7;
    	double opacity_incr = (double) 0.7 / characterCount;
    	int counter = 0;
    	for(FsNode ch : queue) {
    		counter++;
	    	String audioUrl = ch.getPath();
			audioUrl = audioUrl.replace("/character/character","/character");
			audioUrl = audioUrl + "/audio";
			
			System.out.println("onRelease: AUDIO URL:" + audioUrl);
			List<FsNode> audios = Fs.getNodes(audioUrl, 1);
			System.out.println("onRelease: AUDIO CNT:" + audios.size());
			FsNode audioNode = audios.get(new Random().nextInt(audios.size()));
			if(audioNode!=null) {
				
				String filename = audioNode.getProperty("file");
				filename = filename.replace(".mp3", "");
				setDivMainScreens("wrap", "sound:"+filename);
				System.out.println("sound:"+filename);
				
				
				String divId = "touch_main";
			   	
			   	setContentMainScreens(divId, " ");
			   	setDivMainScreens(divId, "style:visibility:hidden");
			   	setDivMainScreens(divId, "style:position:absolute");
			   	setDivMainScreens(divId, "style:width:140px");
			   	setDivMainScreens(divId, "style:height:140px");
			   	setDivMainScreens(divId, "style:border-radius:70px");
			   	
			   	int mainscreenwidth = Integer.parseInt((String) mainScreens.get(0).getProperty("width"));
			   	int mainscreenheight = Integer.parseInt((String) mainScreens.get(0).getProperty("height"));
			   	
			   	String ox = (String) ch.getProperty("positionX");
			   	double xper = Double.parseDouble(ox);
			   	String oy = (String) ch.getProperty("positionY");
			   	double yper = Double.parseDouble(oy);
			   	
			   	int mposx = (int) xper*mainscreenwidth / 100;
			   	//mposx = mposx;
			   	int mposy = (int) yper*mainscreenheight / 100;
			   	mposy = mposy -24;
			   	
			   
			   	setDivMainScreens(divId, "style:visibility:visible");
			   	setDivMainScreens(divId, "style:top:" + yper + "%");
			   	setDivMainScreens(divId, "style:left:" + xper + "%");
			   	setDivMainScreens(divId, "style:background: url(/eddie/apps/bosch/img/baccus.jpg)");
			   	setDivMainScreens(divId, "style:background-position:-"+mposx+"px -"+mposy+"px");
			   	setDivMainScreens(divId, "style:background-size: "+mainscreenwidth+"px "+(mainscreenwidth*ratio)+"px");
			   	setDivMainScreens(divId, "style:box-shadow:0px 0px 27px 11px #ffffff");
			   	
			   	try{
			   		Thread.sleep(10000);
			   	} catch (Exception e) {}
				
			}
			
			opacity = opacity-(counter*opacity_incr);
			setDivMainScreens("opacity", "style:opacity:" + opacity);
    	}
    	
    	setDivMainScreens("opacity", "style:opacity:0");*/
    	foundCharacters = 0;
		queue.clear();
		foundqueue.clear();
    }
    
    public void onTouch(Screen s, String c) {
    	System.out.println("BOSCH onTouch: " + c);
    	s.setProperties(c);
    	Capabilities caps = s.getCapabilities();
    	
    	
    	int screenwidth = Integer.parseInt(caps.getCapability("screenwidth"));
    	int screenheight = Integer.parseInt(caps.getCapability("screenheight"));
    	System.out.println(screenwidth + "x" + screenheight);
    	Double ox = Double.parseDouble((String)s.getProperty("clientX"));
 	   	int x = ox.intValue();
 	   	x-=70; // Make center of the circle
 	   	double multipl2 = (double)100/(screenwidth-140);
 	   	double xper = x*multipl2;
 	    Double oy = Double.parseDouble((String)s.getProperty("clientY"));
	   	int y = oy.intValue();
	   	y-=70; // make center of the circle
	   	double multipl = (double)100/(screenheight-140);
	   	double yper = y*multipl;
	   	System.out.println("top: " + yper + "%; left: " + xper + "%");
 	   	
	   	String screenId = s.getId();
	   	String id = screenId.substring(screenId.lastIndexOf("/")+1);
	   	String divId = "touch"+id;
	   	//setContentMainScreens(divId, " ");
	   	
	   	int mainscreenwidth = Integer.parseInt((String) mainScreens.get(0).getProperty("width"));
	   	int mainscreenheight = Integer.parseInt((String) mainScreens.get(0).getProperty("height"));
	   	
	   	double mposx = (double) (xper*0.877)*1139/100;
	   	//mposx = mposx;
	   	double mposy = (double) (yper*0.843)*897/100;
	   	//mposy = mposy -24;
	   	System.out.println("MPOSX: " + mposx + "; MPOSY: " + mposy);
	   	setDivMainScreens(divId, "style:visibility:visible");
	   	setDivMainScreens(divId, "style:top:" + (yper*0.843) + "%");
	   	setDivMainScreens(divId, "style:left:" + (xper*0.877) + "%");
	   	setDivMainScreens(divId, "style:background: url(/eddie/apps/bosch/img/baccus.jpg)");
	   	setDivMainScreens(divId, "style:background-position:-"+mposx+"px -"+mposy+"px");
	   	setDivMainScreens(divId, "style:background-size: 1139px 897px");
	   	setDivMainScreens(divId, "style:box-shadow:0px 0px 27px 11px #ffffff");
	   	
	   	//s.setContent(divId, " ");
	   	s.setDiv(divId, "style:visibility:visible");
	   	s.setDiv(divId, "style:top:" + yper + "%");
	   	s.setDiv(divId, "style:left:" + xper + "%");
	   	if(s.getProperty("microphone")!=null && s.getProperty("microphone").equals("true")) {
	   		s.removeContent("micbutton");
	   		s.setProperty("microphone", "false");
	   	}
    }
    
    public void onDrag(Screen s, String c) {
    	s.setProperties(c);
    	String screenId = s.getId();
	   	String id = screenId.substring(screenId.lastIndexOf("/")+1);
	   	String divId = "touch"+id;
    	
	   	double xper = getMobilePerX(s);
	   	xper = (xper*0.877);
	   	double yper = getMobilePerY(s);
	   	yper = (yper*0.843);
	   	int mainscreenwidth = Integer.parseInt((String) mainScreens.get(0).getProperty("width"));
	   	int mainscreenheight = Integer.parseInt((String) mainScreens.get(0).getProperty("height"));
	   	
	   	double mposx = (double) xper*1139/100;
	   	double mposy = (double) yper*897/100;
     	   
	   	System.out.println("onDrag: X:" + xper + " Y:" + yper);
    	setDivMainScreens(divId,"style:top: "+(yper)+"%"); // compensate for the block height 
    	setDivMainScreens(divId,"style:left: "+(xper)+"%"); // compensate for the block width 
    	setDivMainScreens(divId, "style:background: url(/eddie/apps/bosch/img/baccus.jpg)");
	   	setDivMainScreens(divId, "style:background-position:-"+mposx+"px -"+mposy+"px");
	   	//setDivMainScreens(divId, "style:background-size: "+mainscreenwidth+"px "+(mainscreenwidth*ratio)+"px");
	   	setDivMainScreens(divId, "style:background-size: 1139px 897px");
	   	setDivMainScreens(divId, "style:box-shadow:0px 0px 27px 11px #ffffff");
    }
    
    public void setContentMainScreens(String t,String c) {
		Iterator<String> iterator = getScreenManager().getScreens().keySet().iterator();	
		while (iterator.hasNext()) {
			String next = (String) iterator.next();
			Screen screen = getScreenManager().get(next);
			String role = screen.getRole();
			
			
			if (role.equals("main")) { // ok its a main screen
				screen.setContent(t, c);
			}
		}
	}
    
    public void setDivMainScreens(String t,String c) {
		Iterator<String> iterator = getScreenManager().getScreens().keySet().iterator();	
		while (iterator.hasNext()) {
			String next = (String) iterator.next();
			Screen screen = getScreenManager().get(next);
			String role = screen.getRole();
			
			if (role.equals("main")) { // ok its a main screen
				screen.setDiv(t, c);
			}
		}
	}
    
    public double getMobilePerX(Screen s) {
        Capabilities caps = s.getCapabilities();
        try {
     	   Double ol = Double.parseDouble((String)s.getProperty("elementOffsetLeft"));
     	   int x = ol.intValue();
     	  
     	   Double ow = Double.parseDouble((String)s.getProperty("elementWidth"));
     	   int bw = ow.intValue();
     	   int screenwidth = 600;
     	   try {
     		   screenwidth = Integer.parseInt(caps.getCapability("screenwidth"));
     	   } catch(Exception e) {}
     	   
     	   
     	   double multipl2 = (double)100/(screenwidth-bw);
     	   double xper = x*multipl2;
     	   return xper;
        } catch(Exception e) {
     	   return 0;
        }
     }
     
     public double getMobilePerY(Screen s) {
        Capabilities caps = s.getCapabilities();
        try {
     	   Double ot = Double.parseDouble((String)s.getProperty("elementOffsetTop"));
     	   int y = ot.intValue();
     	  
     	   Double oh = Double.parseDouble((String)s.getProperty("elementHeight"));
     	   int bh = oh.intValue();
     	   
     	   int screenheight = 600;
    	   try {
    		   screenheight = Integer.parseInt(caps.getCapability("screenheight"));
    	   } catch(Exception e) {}
     	   
     	   double multipl = (double)100/(screenheight-bh);
     	   double yper = y*multipl;
   
     	   
     	   return yper;
         } catch(Exception e) { 
         	return 0;
         } 
     }
    
    
}