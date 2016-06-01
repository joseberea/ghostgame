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

import com.jmberea.ghostgame.util.Const;
import com.jmberea.ghostgame.vo.NodeVO;

/**
 * LoadDictionaryContextListener:
 * This is the listener that handles the dictionary map with the given dictionary.
 * The listener reads a each line of the file and puts the fist character of the line if the
 * 	maps does not contains it.
 * 
 * Assuming a word is larger than two characters, the listener calls to the fillNode method
 * 	for each remaining letter.
 * 
 * The fillNode method determines if the node is new or if the dictionary map already
 * 	has it. In case the dictionary tree contains the given character the method the node,
 * 	or includes it in the tree in other case.
 * 
 * The possibilities of a node is to be a "can win" branch o a "can lose" one.
 * 	- Can win branch: Determines that The Ghost can win if it chooses it. We assume that The
 * 		Ghost has winner possibilities if the word length is not a multiple of two because the
 * 		last character is in a odd position (corresponding to human turns). 
 * 
 *  - Can lose branch: Determines that The Ghost can win if it chooses it. We assume that The
 * 		Ghost has winner possibilities if the word length is a multiple of two because the
 * 		last character is in a even position (corresponding to The Ghost turns) and the current 
 * 		string length is at least three (because the game determines a word only bigger than three). 
 */
public class LoadDictionaryContextListener implements ServletContextListener {

	private static final Logger logger = LogManager.getLogger(ServletContextListener.class);

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

			servletContext.removeAttribute(Const.DICTIONARY_CTX_NAME);
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
			servletContext.setAttribute(Const.DICTIONARY_CTX_NAME, dictionary_map);
		} catch (IOException ioException) {
			logger.error("Error reading file:  " + ioException.toString());
		} catch (Exception e) {
			logger.error("Error initializing context: " + e);
		}
	}

	/**
	 * Reads the given dictionary file
	 * @return Returns the dictionary tree with the game strategy
	 * @throws IOException When an IO error occurs while reading the file
	 */
	private Map<Character, Map<Character, NodeVO>> readFile() throws IOException {
		logger.debug("Start reading file");
		Map<Character, Map<Character, NodeVO>> dictionary_map = new HashMap<Character, Map<Character, NodeVO>>();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/" + DICTIONARY_FILE_NAME)));
		String line = null;
		
		while ((line = reader.readLine()) != null) {
			logger.debug("File line: " + line);
			Character c = line.charAt(0);
			if (!dictionary_map.containsKey(c)) {
				dictionary_map.put(c, new HashMap<Character, NodeVO>());
			}
			fillNode(dictionary_map.get(c), line.substring(1), line.length()); 
		}
		logger.debug("End reading file");
		return dictionary_map;
	}

	/**
	 * Fill a node of the tree and prepares the child nodes letter by letter (making a substring)
	 * @param map  The current branch map
	 * @param substring The current substring
	 * @param lineLength The length of the file line
	 */
	private void fillNode(Map<Character, NodeVO> map, String substring, int lineLength) {
		Character c = null;
		boolean possibleWinnerBranch = lineLength % 2 != 0;
		if (!substring.isEmpty()) {
			c = substring.charAt(0);
			logger.debug(" - Character: " + c);
			if (!map.containsKey(c)) {
				setNewNode(substring, lineLength, possibleWinnerBranch, c, map);
			} else {
				updateNode(substring, lineLength, possibleWinnerBranch, c, map);
			}
		}
	}

	/**
	 * Adds a new node to the tree
	 * @param substring The current substring
	 * @param lineLength lineLength The length of the file line
	 * @param possibleWinnerBranch Boolean that contains if the node is a possible winner (odd line length)
	 * @param c The current character of the string
	 * @param map The current branch map
	 */
	private void setNewNode(String substring, Integer lineLength, Boolean possibleWinnerBranch, Character c,
			Map<Character, NodeVO> map) {
		NodeVO node = new NodeVO();
		Map<Character, NodeVO> children = null;
		children = new HashMap<Character, NodeVO>();
		if (substring.length() > 1) {
			fillNode(children, substring.substring(1), lineLength);
		} else {
			node.setLeaf(true);
		}
		node.setMaxLength(lineLength);
		node.setChildren(children);
		if (lineLength > Const.WORD_LIMIT-1) {
			if (possibleWinnerBranch) {
				node.setCanWin(true);
			} else {
				node.setCanLose(true);
			}
		}
		map.put(c, node);
	}

	/**
	 * Modifies a node (length and booleans flags) and calls to fill the next child node
	 * @param substring The current substring
	 * @param lineLength lineLength The length of the file line
	 * @param possibleWinnerBranch Boolean that contains if the node is a possible winner (odd line length)
	 * @param c The current character of the string
	 * @param map The current branch map
	 */
	private void updateNode(String substring, Integer lineLength, Boolean possibleWinnerBranch, Character c,
			Map<Character, NodeVO> map) {
		NodeVO node = map.get(c);
		if (lineLength > node.getMaxLength()) {
			node.setMaxLength(lineLength);
		}
		if (lineLength > Const.WORD_LIMIT-1) {
			if (possibleWinnerBranch) {
				node.setCanWin(true);
			} else {
				node.setCanLose(true);
			}
		}
		if (substring.length() > 1) {
			fillNode(node.getChildren(), substring.substring(1), lineLength);
		} else {
			node.setLeaf(true);
		}
	}

}
