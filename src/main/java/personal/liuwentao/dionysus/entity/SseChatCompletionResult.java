package personal.liuwentao.dionysus.entity;

import personal.liuwentao.dionysus.entity.SseChatCompletionChoice;

import java.util.List;

public class SseChatCompletionResult {

    /**
     * Unique id assigned to this chat completion.
     */
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    /**
     * The type of object returned, should be "chat.completion"
     */
    String object;

    /**
     * The creation time in epoch seconds.
     */
    long created;

    /**
     * The GPT-3.5 model used.
     */
    String model;

    public List<SseChatCompletionChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<SseChatCompletionChoice> choices) {
        this.choices = choices;
    }

    /**
     * A list of all generated completions.
     */
    List<SseChatCompletionChoice> choices;


}
