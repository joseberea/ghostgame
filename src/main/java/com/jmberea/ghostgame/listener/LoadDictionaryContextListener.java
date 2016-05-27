package com.jmberea.ghostgame.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vo.NodeVO;

/**
 * Application Lifecycle Listener implementation class
 * LoadDictionaryContextListener
 *
 */
public class LoadDictionaryContextListener implements ServletContextListener {

	private static final Logger logger = LogManager.getLogger(ServletContextListener.class);

	private static final String DICTIONARY_CTX_NAME = "dictionary_";
	private static final String DICTIONARY_FILE_NAME = "WORD.LST";

	/**
	 * Default constructor.
	 */
	public LoadDictionaryContextListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		logger.debug("Destroying context .....");
		try {
			ServletContext servletContext = event.getServletContext();

			servletContext.removeAttribute(DICTIONARY_CTX_NAME);
		} catch (Exception e) {
			logger.error("Error destroying context: " + e);
		}
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		logger.debug("Initializing context .....");
		try {
			ServletContext servletContext = event.getServletContext();
			Map<Character, Map<Character, NodeVO>> dictionary_map = readFile();
			servletContext.setAttribute(DICTIONARY_CTX_NAME, dictionary_map);
		} catch (IOException ioException) {
			logger.error("Error reading file:  " + ioException.toString());
		} catch (Exception e) {
			logger.error("Error initializing context: " + e);
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private Map<Character, Map<Character, NodeVO>> readFile() throws IOException {
		Map<Character, Map<Character, NodeVO>> dictionary_map = new HashMap<Character, Map<Character, NodeVO>>();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/" + DICTIONARY_FILE_NAME)));
		String line = null;
		while ((line = reader.readLine()) != null) {
			Character c = line.charAt(0);
			if (!dictionary_map.containsKey(c)) {
				dictionary_map.put(c, new HashMap<Character, NodeVO>());
			}
			fillNode(dictionary_map.get(c), line.substring(1), line.length()); // Minimum line
																// size = 2
		}
		return dictionary_map;
	}

	private void fillNode(Map<Character, NodeVO> map, String substring, int lineLength) {
		NodeVO node = new NodeVO();
		Map<Character, NodeVO> children = null;
		Character c = null;
		boolean possibleWinnerBranch = lineLength % 2 != 0;
		if (!substring.isEmpty()) {
			c = substring.charAt(0);
			if (!map.containsKey(c)) {
				children = new HashMap<Character, NodeVO>();
				if(substring.length() > 1) {
					fillNode(children, substring.substring(1), lineLength);
				} else {
					node.setLeaf(true);
				}
				node.setMaxLength(lineLength);
				node.setChildren(children);
				if(possibleWinnerBranch) {
					node.setCanWin(true);
				} else {
					node.setCanLose(true);
				}
				map.put(c, node);
			} else {
				// Node already exists
				node = map.get(c);
				if(lineLength > node.getMaxLength()) {
					node.setMaxLength(lineLength);
				}
				if(possibleWinnerBranch) {
					node.setCanWin(true);
				} else {
					node.setCanLose(true);
				}
				if(substring.length() > 1) {
					fillNode(node.getChildren(), substring.substring(1), lineLength);
				} else {
					node.setLeaf(true);
				}
			}
		}
	}

}
