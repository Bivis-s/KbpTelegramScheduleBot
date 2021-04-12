package bot.utils;

import bot.db.objects.Note;
import by.bivis.kbp.parser.objects.News;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

@Log4j2
public class TelegramBotUtils {
    private TelegramBotUtils() {
    }

    public static String escapeAllChars(String text) {
        return Pattern.compile("[{}()\\[\\].+*?^$\\\\|`-]").matcher(text).replaceAll("\\\\$0");
    }

    private static SendMessage createMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        return sendMessage;
    }

    public static SendMessage createMessage(long chatId, String text) {
        SendMessage sendMessage = createMessage(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    public static SendPhoto createSendPhoto(long chatId, String caption, InputFile photo) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setCaption(caption);
        sendPhoto.setParseMode(ParseMode.HTML);
        sendPhoto.setPhoto(photo);
        return sendPhoto;
    }

    private static InputFile createNewsInputFile(News news) {
        InputFile inputFile = new InputFile();
        if (news.getArticleLink() != null) {
            log.info("Set photo url to news: " + news.getImgLink());
            inputFile.setMedia(new File("src/main/resources/logo-kbp.png"));
        }
        return inputFile;
    }

    // countOfNews should be between 2 and 10
    public static SendPhoto createNewsMediaGroup(long chatId, List<News> news, int countOfNews) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < countOfNews; i++) {
            sb
                    .append(i + 1)
                    .append(". <a href = '")
                    .append(news.get(i).getArticleLink())
                    .append("'>").append(news.get(i).getTitle())
                    .append("</a>\n")
                    .append("-----\n");
        }
        return createSendPhoto(chatId, sb.toString(), createNewsInputFile(news.get(0)));
    }

    public static SendMessage createMessage(long chatId, String text, ReplyKeyboardMarkup replyMarkup) {
        SendMessage sendMessage = createMessage(chatId, text);
        sendMessage.setReplyMarkup(replyMarkup);
        return sendMessage;
    }

    public static String createNotesMessageText(List<Note> notes) {
        StringBuilder sb = new StringBuilder();
        for (Note note : notes) {
            sb.append(note.getValue()).append("\n\n");
        }
        return sb.toString();
    }

    public static String createNotifyMessageText(List<String> notifies) {
        StringBuilder sb = new StringBuilder();
        if (notifies.size() > 0) {
            sb.append("Установленные рассылки:\n");
        }
        for (String notify : notifies) {
            sb.append(notify).append("\n");
        }
        return sb.toString();
    }
}
