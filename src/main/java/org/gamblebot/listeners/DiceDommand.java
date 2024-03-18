package org.gamblebot.listeners;

import java.math.BigInteger;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gamblebot.Main;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.exception.MissingPermissionsException;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;

public class DiceDommand implements MessageCreateListener
{

	private static final Logger logger = LogManager.getLogger(DiceDommand.class);

	@Override
	public void onMessageCreate(MessageCreateEvent event)
	{
		if (event.getServer().isPresent())
		{
			String msg = event.getMessageContent();

			if (msg.startsWith("!dice") && event.getMessageAuthor().isUser())
			{
				long serverID = event.getServer().get().getId();
				String[] splitMessage = msg.split(" ");
				if (splitMessage.length == 1)
				{
					event.getChannel().sendMessage("Command is !dice # & !dice next")
						.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
				}
				else if (splitMessage.length == 2)
				{
					String nextPart = splitMessage[1];
					if (nextPart.equalsIgnoreCase("next"))
					{
						if (Main.getLastFromServer(serverID) != null)
						{
							BigInteger randomBigInt = new BigInteger("1").add(nextRandomBigInteger(Main.getLastFromServer(serverID)));

							Main.storeLastFromServer(serverID, randomBigInt);

							event.getChannel().sendMessage(String.valueOf(randomBigInt))
								.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
						}
						else
						{
							event.getChannel().sendMessage("Couldn't find previous number")
								.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
						}
					}
					else
					{
						try
						{
							BigInteger bigInteger = new BigInteger(nextPart);

							BigInteger randomBigInt = new BigInteger("1").add(nextRandomBigInteger(bigInteger));

							Main.storeLastFromServer(serverID, randomBigInt);

							event.getChannel().sendMessage(String.valueOf(randomBigInt))
								.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
						}
						catch (Exception e)
						{
							event.getChannel().sendMessage("Command is !dice ###")
								.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
						}
					}
				}
				else
				{
					event.getChannel().sendMessage("Command is !dice # & !dice next")
						.exceptionally(ExceptionLogger.get(MissingPermissionsException.class));
				}
			}
		}
	}

	public BigInteger nextRandomBigInteger(BigInteger n)
	{
		Random rand = new Random();
		BigInteger result = new BigInteger(n.bitLength(), rand);
		while (result.compareTo(n) >= 0)
		{
			result = new BigInteger(n.bitLength(), rand);
		}
		return result;
	}
}
