package org.example;

import java.math.BigInteger;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.exception.MissingPermissionsException;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;

public class DiceDommand implements MessageCreateListener
{

	Logger logger;

	public DiceDommand(Logger logger)
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
			if (msg.startsWith("!dice") && event.getMessageAuthor().isUser())
			{
				String[] splitMessage = msg.split(" ");
				if (splitMessage.length == 1)
				{
					event.getChannel().sendMessage("Command is !dice #")
						.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
				}
				else if (splitMessage.length == 2)
				{
					try
					{
						BigInteger bigInteger = new BigInteger(splitMessage[1]);

						event.getChannel().sendMessage(String.valueOf( new BigInteger("1").add(nextRandomBigInteger(bigInteger))))
							.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
					}
					catch (Exception e)
					{
						event.getChannel().sendMessage("Command is !dice ###")
							.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
					}
				}
				else
				{
					event.getChannel().sendMessage("Command is !dice #")
						.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
				}
			}
		}
	}

	public BigInteger nextRandomBigInteger(BigInteger n) {
		Random rand = new Random();
		BigInteger result = new BigInteger(n.bitLength(), rand);
		while( result.compareTo(n) >= 0 ) {
			result = new BigInteger(n.bitLength(), rand);
		}
		return result;
	}
}