package com.cintel.frame.shell;

import java.io.OutputStream;
import java.util.Map;

/**
 * 
 * @version $Id: ShellCmdExecutor.java 20137 2010-07-23 05:38:27Z anlina $
 * @history 
 *          1.0.0 2009-12-17 wangshuda created
 */
public interface ShellCmdExecutor {
	public int execute(String commandKey, String[] args, OutputStream out);
	
	public int execute(Command command, OutputStream out);
	
	public boolean execute(Command command);	
	
	public boolean execute(String cmdKey, String[] cmdArgs);	
	
	public String execute(String[] cmdArgs ,String cmdKey );
	
	public Map<String, Command> getCommandMap();
}
