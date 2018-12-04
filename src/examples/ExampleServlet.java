package examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import chatterer.kik.Bot;
import chatterer.kik.Message;

@WebServlet("/bot-endpoint")
public class ExampleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Bot yourBot1 = new ExampleBot("<your-bot-username-1>", "<your-bot-api-key-1>", "<your-bot-webhook-1>");
	static Bot yourBot2 = new ExampleBot("<your-bot-username-2>", "<your-bot-api-key-2>", "<your-bot-webhook-2>");

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
		String botName = req.getHeader("X-Kik-Username");
		String signature = req.getHeader("X-Kik-Signature");
		StringBuilder requestBody = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null && !line.equals("")) {
			requestBody.append(line);
		}

		// If you have more than one bot on the same endpoint, diffentiate like
		// this
		Bot bot = null;
		if (yourBot1.equals(botName)) {
			bot = yourBot1;
		} else if (yourBot2.equals(botName)) {
			bot = yourBot2;
		} else {
			System.out.println("Unkown bot!");
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Bot username not recognized!");
			return;
		}

		// Verify authenticity of request, send unauthorized response back if
		// necessary
		if (!bot.verifyMessage(requestBody.toString(), signature)) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request.");
			return;
		}

		JSONObject first = new JSONObject(requestBody.toString());
		JSONArray messages = first.getJSONArray("messages");

		// Messages can sometimes come in bunches
		for (int i = 0; i < messages.length(); i++) {
			Message message = new Message(messages.getJSONObject(i), bot);

			// Replace smart punctuation which can mess with string matching
			message.body = removeSmartPunctuation(message.body);
			message.setTypeTime(1000);

			// Bot processes incoming message here
			boolean responded = bot.processMessage(message);
			if (responded) {
				// Bot replied to message successfully
				System.out.println("Bot responded.");
			} else {
				// For some reason, the bot never responded, can put default,
				// catchall response here
				System.out.println("Bot did not respond!");
			}
		}
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	/**
	 * Remove smart punctuation from given string
	 * 
	 * @param s
	 *            String to escape
	 * @return escaped string
	 */
	public static String removeSmartPunctuation(String s) {
		char lsquo = 8216; // '
		char rsquo = 8217; // '
		char ldquo = 8220; // "
		char rdquo = 8221; // "
		try {
			s = s.replaceAll(String.valueOf(lsquo), "'").replaceAll(String.valueOf(rsquo), "'");
			s = s.replaceAll(String.valueOf(ldquo), "\"").replaceAll(String.valueOf(rdquo), "\"");
			return s;
		} catch (NullPointerException e) {
			return null;
		}
	}
}
