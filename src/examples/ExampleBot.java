package examples;

import chatterer.kik.Bot;
import chatterer.kik.Keyboard;
import chatterer.kik.Message;

class ExampleBot extends Bot {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExampleBot(String username, String apikey, String webhook) {
		super(username, apikey, webhook);

	}

	@Override
	public Keyboard getDefaultKeyboard(String from) {
		// Set the default suggested keyboard for your bot
		return new Keyboard(new String[] { "Sample", "Responses" });
	}

	@Override
	public void onTextMessage(Message message) {
		message.reply("Hey there");

	}

	@Override
	public void onStartChattingMessage(Message message) {
		// TODO: Add user to database so you can message again
		this.send("New user: " + message.from, "<YOUR_USERNAME>");
		message.reply("Welcome!");

	}

	@Override
	public void onPictureMessage(Message message) {
		message.reply("Cool picture!");

	}

	@Override
	public void onVideoMessage(Message message) {
		message.reply("Cool video!");

	}

	@Override
	public void onLinkMessage(Message message) {
		message.reply("Cool link!");

	}

	@Override
	public void onFriendPickerMessage(Message message) {

	}

	@Override
	public void onStickerMessage(Message message) {

	}

	@Override
	public void onDeliveryReceiptMessage(Message message) {

	}

	@Override
	public void onIsTypingMessage(Message message) {

	}

	@Override
	public void onScanDataMessage(Message message) {

	}

	@Override
	public void onReadReceiptMessage(Message message) {

	}

}
