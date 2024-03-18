package org.gamblebot;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gamblebot.listeners.DiceDommand;
import org.gamblebot.listeners.RuleCommand;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main
{

	private static final Logger logger = LogManager.getLogger(Main.class);

	public static HashMap<Long, BigInteger> storedNumbers = new HashMap<>();

	public static String DISCORD_TOKEN = "";

	static
	{
		Map<String, String> env_var = System.getenv();

		for (String envName : env_var.keySet())
		{
			if (envName.equals("BOT_TOKEN"))
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
			DiscordApiBuilder builder = new DiscordApiBuilder();
			builder.setAllIntents();
			builder.setToken(DISCORD_TOKEN);
			builder.setTrustAllCertificates(false);
			builder.setWaitForServersOnStartup(false);
			builder.setWaitForUsersOnStartup(false);

			builder.addServerBecomesAvailableListener(event -> {
				DiscordApi thisServerApi = event.getApi();
				thisServerApi.addMessageCreateListener(new DiceDommand());
				thisServerApi.addMessageCreateListener(new RuleCommand());
			});

			DiscordApi api = builder.login().join();
			logger.info("You can invite me by using the following url: " + api.createBotInvite());

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
