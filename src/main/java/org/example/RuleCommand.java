package org.example;

import org.apache.logging.log4j.Logger;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.exception.MissingPermissionsException;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;

public class RuleCommand implements MessageCreateListener
{

	Logger logger;

	public RuleCommand(Logger logger)
	{
		this.logger = logger;
	}

	/*
	 * This command can be used to display information about the user who used the command.
	 * It's a good example for the MessageAuthor, MessageBuilder and ExceptionLogger class.
	 */
	@Override
	public void onMessageCreate(MessageCreateEvent event)
	{
		if (event.getServer().isPresent())
		{
			String msg = event.getMessageContent();
			if (msg.equalsIgnoreCase("!rules") && event.getMessageAuthor().isUser())
			{
				event.getChannel().sendMessage("This bot is used for a round-robin dice roll gambling game. Define an order of players before rolling. The first player will start by using !dice and choosing any number they want (for example, 1000000). A lower number than the one used in the !dice command will be given. The next player in line uses !dice n where n is equal to the result of the previous roll. Players will roll in order until a player reaches 1 and wins.")
					.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
			}
		}
	}
}