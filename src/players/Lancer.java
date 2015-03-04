package players;

import controller.Player;

public class Lancer extends Player {

	@Override
	public String getCmd() {
		return "C:\\Node\\node.exe Lancer.js";
	}

}
