package personal.liuwentao.dionysus.entity;

import personal.liuwentao.dionysus.entity.Delta;

public class SseChatCompletionChoice {

    /**
     * This index of this completion in the returned list.
     */
    Integer index;



    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getFinish_reason() {
        return finish_reason;
    }

    public void setFinish_reason(String finish_reason) {
        this.finish_reason = finish_reason;
    }

    public Delta delta;

    public Delta getDelta() {
        return delta;
    }

    public void setDelta(Delta delta) {
        this.delta = delta;
    }

    /**
     * The reason why GPT-3 stopped generating, for example "length".
     */
    String finish_reason;
}
