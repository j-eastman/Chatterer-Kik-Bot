# Chatterer-Kik-Bot

This can be used on an existing java web server to implement a kik bot.

## Dependencies
Add this to your pom.xml file or download the jar manually
```
<dependency>
  <groupId>org.json</groupId>
  <artifactId>json</artifactId> 
  <version>20160810</version>
 </dependency>
 ```
## Implementation
The Bot class is abstract so you will need to create your own class that extends it.
Example:
```
import chatterer.kik.Bot;
public class MyBot extends Bot {
  public MyBot(String username, String apikey, String webhoook){
    super(username,apikey,webhook);
  }
  
  @Override
  public void onTextMessage(Message message){
    message.reply("Hello!");
  }
  
  @Override
  public void onStartChatting(Message message){
    this.send("New User: " + message.from, "YOUR_USERNAME");
    message.reply("Welcome!");
   }
   ...
   ```
 ## Authenticating Messages
  Kik sends a HMAC-Sha1 hash using your bot's api key as the key and the message request body as the encoded string. Verifying messages on your servlet may look something like this:
  ```
  if (!bot.verifyMessage(requestBody.toString(), signature)) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request.");
		}
  ```
 ## Receiving Messages
 In order to recieve messages from kik's servers, you must have a webhook set up. Incoming messages from kik can be parsed like so:
 ```
JSONObject first = new JSONObject(json); //Construct JSONObject using string input from server
JSONArray messages = first.getJSONArray("messages");  
for (int i = 0; i < messages.length(); i++) {		//Messages can be sent in batches so you must go through them all	
	Message message = new Message(messages.getJSONObject(i), bot); //Create new message using the json line and your bot
	message.setTypeTime(1000); //Set time (int milliseconds) app will say "is Typing..." before message is received
```
After the message is created, you can use the Bot.processMessage() class which automatically calls the proper response class based on the message type 
```
boolean responded = bot.processMessage(message);
			if (responded) {
				// Bot replied to message successfully
				System.out.println("Bot responded.");
			} else {
				// For some reason, the bot never responded, can put default,
				// catchall response here
				System.out.println("Bot did not respond!");
			}
```
## Sending Messages
Text based messages can be sent in three different ways.
  ### To the User Directly
  You can send messages directly to a user using the Bot class.
  ```
  bot.send("Message to be sent", "Username of recipient");
  ```
  ### Reply to User's Message With Single Response
  You can easily reply to a user's message using the `Message.reply(String)` command.
  ```
  message.reply("Message to send");
  ```
  ### Reply to User's Message With Several Responses
  You can send multiple messages at once using the `Message.addReply(String)` command followed by `Message.reply()`
  ```
  message.addReply("Multiple");
  message.addReply("Messages");
  message.addReply("Sent");
  message.reply();
  ```
  This method can also be used to send video and picture messages.
  ```
  message.addPicReply("image_url");
  message.addVideoReply("video_url");
  message.reply();
  ```
Additional descriptions are available in the attached Javadocs
