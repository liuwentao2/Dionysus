package personal.liuwentao.dionysus.entity;

import com.theokanning.openai.completion.chat.ChatMessageRole;

public class Delta {
    String role;
    String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
