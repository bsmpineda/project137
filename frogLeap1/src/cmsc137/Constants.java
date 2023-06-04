package cmsc137;

/**
 * Important game constants
 * @author Joseph Anthony C. Hermocill
 *
 */
public interface Constants {
	public static final String APP_NAME="Circle Wars 0.01";
	
	/**
	 * Game states.
	 */
	public static final int GAME_START=0;
	public static final int IN_PROGRESS=1;
	public final int GAME_END=2;
	public final int WAITING_FOR_PLAYERS=3;
	public final int END_GAME=4;
	public final String RESET_TOKEN = "2hDzVjY7c#6T9gNw@R4k!LxQaS8p1m$XbF3e^ZvU6yM1q&JnI0oP5lK2";
	
	/**
	 * Game port
	 */
	public static final int PORT=4444;
}
