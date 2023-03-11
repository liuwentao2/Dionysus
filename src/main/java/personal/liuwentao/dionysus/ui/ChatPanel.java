package personal.liuwentao.dionysus.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import okhttp3.Request;
import okhttp3.Response;
import personal.liuwentao.dionysus.sse.OpenAiService;
import personal.liuwentao.dionysus.sse.ServerSentEvent;
import personal.liuwentao.dionysus.entity.SseChatCompletionResult;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ChatPanel extends JPanel implements ActionListener {
    private JTextArea chatTextArea;
    private JTextField messageField;
    private JButton sendButton;

    public ChatPanel() {
        setLayout(new BorderLayout());

        // 创建聊天区域
        chatTextArea = new JTextArea(20, 15);
        chatTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // 创建输入区域
        messageField = new JTextField(15);
        sendButton = new JButton("Send");
        JPanel inputPanel = new JPanel();
        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        add(inputPanel, BorderLayout.SOUTH);

        // 绑定发送按钮的点击事件
        sendButton.addActionListener(this);
        messageField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {

            }
            public void removeUpdate(DocumentEvent e) {

            }
            public void insertUpdate(DocumentEvent e) {
                if (messageField.getText().trim().equals("/")) {
                    SwingUtilities.invokeLater(() -> messageField.setText("/comment"));
                }
            }
        });
        messageField.addKeyListener(new MyKeyListener());

    }

    class MyKeyListener implements KeyListener{

        ArrayList<String> promote = new ArrayList();

        MyKeyListener(){
            promote.add("/comment");
            promote.add("/log");
            promote.add("/git");
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER) {
                String message = messageField.getText().trim();
                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(ChatPanel.this, "Message cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // TODO: 在这里处理发送消息的逻辑
                chatTextArea.append("Me: " + message + "\n");
                SwingUtilities.invokeLater(() -> messageField.setText(""));
                getResult(message);
                return;
            }
            int index = promote.indexOf(messageField.getText().trim());
            if (index < 0) {
                return;
            }
            switch (keyCode) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_LEFT:
                    final int newIndex;
                    if (index != 2){
                        newIndex = index + 1;
                    }else {
                        newIndex = 0;
                    }
                    SwingUtilities.invokeLater(() -> messageField.setText(promote.get(newIndex)));
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_RIGHT:
                    final int newIndex1;
                    if (index != 0){
                        newIndex1 = index - 1;
                    }else {
                        newIndex1 = 2;
                    }
                    SwingUtilities.invokeLater(() -> messageField.setText(promote.get(newIndex1)));
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String message = messageField.getText().trim();
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Message cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // TODO: 在这里处理发送消息的逻辑
            chatTextArea.append("Me: " + message + "\n");
            messageField.setText("");
            getResult(message);
        }
    }

    public void getResult(String input){
        OpenAiService service = new OpenAiService("YOUR_TOKEN");
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("user", input));
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .maxTokens(1000)
                .messages(messages)
                .n(1)
                .build();
        ServerSentEvent.Listener listener = new ServerSentEvent.Listener(){

            @Override
            public void onOpen(ServerSentEvent sse, Response response) {
                chatTextArea.append("chatGPT: " + "\n");
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    SseChatCompletionResult result = objectMapper.readValue(message, SseChatCompletionResult.class);
                    chatTextArea.append(result.getChoices().get(0).getDelta().getContent());
                } catch (JsonProcessingException e) {
                    e.getMessage();
                }

            }

            @Override
            public void onComment(ServerSentEvent sse, String comment) {
            }

            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                return false;
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
                return false;
            }

            @Override
            public void onClosed(ServerSentEvent sse) {
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                return null;
            }
        };
        try {
            ServerSentEvent sse = service.createChatCompletionStream(completionRequest, listener);
//            sse.request();
        }catch (Exception e) {
            //ignore
        }
    }

}