package org.example;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main
{

	private static final Logger logger = LogManager.getLogger(Main.class);

	public static HashMap<Long, BigInteger> storedNumbers = new HashMap<>();

	public static String DISCORD_TOKEN ="";
	static
	{
		Map<String, String> env_var=System.getenv();

		for(String envName:env_var.keySet()){
			if(envName.equals("BOT_TOKEN"))
			{
				DISCORD_TOKEN = env_var.get(envName);
			}
		}
	}
	/**
	 * The entrance point of our program.
	 *
	 * @param args The arguments for the program. The first element should be the bot's token.
	 */
	public static void main(String[] args) throws Exception
	{
		if (DISCORD_TOKEN.equals(""))
		{
			logger.error("Failed to start Discord bot. No Discord token supplied");
		}
		else
		{
			DiscordApi api = new DiscordApiBuilder().setToken(DISCORD_TOKEN).setWaitForServersOnStartup(false).setAllIntents().login().join();
			logger.info("You can invite me by using the following url: " + api.createBotInvite());

			api.updateActivity("It's gamba time");
			api.addMessageCreateListener(new DiceDommand(logger));
			api.addMessageCreateListener(new RuleCommand(logger));
		}
	}

	public static BigInteger getLastFromServer(Long server)
	{
		return storedNumbers.getOrDefault(server, null);
	}

	public static void storeLastFromServer(Long server, BigInteger value)
	{
		storedNumbers.put(server, value);
	}
}