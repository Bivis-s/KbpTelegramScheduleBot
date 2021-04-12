package bot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class KeyboardGenerator {
    /**
     * Creates a row list with a specified number of columns
     * <p>
     * For example:
     * We have a buttons "home", "info", "help", "start", "close"
     * Columns count = 2
     * <p>
     * This method will create row list like this:
     * <p>
     * "home", "info"
     * "help", "start"
     * "close"
     *
     * @param columnCount count of keyboard columns
     * @param strings     names of keyboard buttons
     * @return List<KeyboardRow>
     */
    private static List<KeyboardRow> createKeyboardRowList(int columnCount, List<String> strings) {
        // init Button Queue from string CharSequence
        Queue<KeyboardButton> buttonQueue = new ArrayDeque<>();
        for (String string : strings) {
            buttonQueue.offer(new KeyboardButton(string));
        }
        List<KeyboardRow> rows = new ArrayList<>();

        // allocate all Buttons in Queue to rows
        boolean state = true;
        while (state) {
            KeyboardRow row = new KeyboardRow();
            for (int j = 0; j < columnCount; j++) {
                if (buttonQueue.peek() != null) {
                    row.add(buttonQueue.poll());
                } else {
                    state = false;
                }
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * Creates a inline row list with a specified number of columns
     * <p>
     * For example:
     * We have a buttons "home", "info", "help", "start", "close"
     * Columns count = 2
     * <p>
     * This method will create row list like this:
     * <p>
     * "home", "info"
     * "help", "start"
     * "close"
     * <p>
     * Has callback data contains same String from {@code strings}
     *
     * @param columnCount count of keyboard columns
     * @param strings     names of keyboard buttons
     * @return List<List < InlineKeyboardButton>>
     */
    private static List<List<InlineKeyboardButton>> createInlineKeyboardButtonMatrix(int columnCount, List<String> strings) {
        // init Button Queue from string CharSequence
        Queue<InlineKeyboardButton> buttonQueue = new ArrayDeque<>();
        for (String string : strings) {
            InlineKeyboardButton button = new InlineKeyboardButton(string);
            button.setCallbackData(string);
            buttonQueue.offer(button);
        }
        List<List<InlineKeyboardButton>> rowMatrix = new ArrayList<>();

        // allocate all Buttons in Queue to rows
        boolean state = true;
        while (state) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < columnCount; j++) {
                if (buttonQueue.peek() != null) {
                    row.add(buttonQueue.poll());
                } else {
                    state = false;
                }
            }
            rowMatrix.add(row);
        }
        return rowMatrix;
    }

    /**
     * Creates a telegram reply keyboard (under text field in chat) with a specified number of columns
     *
     * @param columnCount count of keyboard columns
     * @param strings     names of keyboard buttons
     * @return ReplyKeyboardMarkup obj which might me setted to telegrambots Message
     */
    public static ReplyKeyboardMarkup createReplyKeyboardMarkup(int columnCount, List<String> strings) {
        // init ReplyKeyboardMarkup
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(createKeyboardRowList(columnCount, strings));
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(true);
        keyboard.setSelective(true);
        return keyboard;
    }

    /**
     * Creates a telegram inline keyboard (under message in chat) with a specified number of columns
     *
     * @param columnCount count of keyboard columns
     * @param strings     names of keyboard buttons
     * @return ReplyKeyboardMarkup obj which might me setted to telegrambots Message
     */
    public static InlineKeyboardMarkup createInlineKeyboardMarkup(int columnCount, List<String> strings) {
        return new InlineKeyboardMarkup(createInlineKeyboardButtonMatrix(columnCount, strings));
    }

    /**
     * Creates a link-button under message with Source Name and Source url
     *
     * @param sourceName Button Text
     * @param url        Button redirect url
     * @return InlineKeyboardMarkup Button
     */
    public static InlineKeyboardMarkup createInlineSourceLink(String sourceName, String url) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(sourceName);
        button.setUrl(url);
        buttonRow.add(button);
        rows.add(buttonRow);
        return new InlineKeyboardMarkup(rows);
    }
}
