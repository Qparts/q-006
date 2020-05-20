package q.app.q006.model.quotation;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.json.bind.annotation.JsonbDateFormat;
import java.io.Serializable;
import java.util.Date;

public class PublicComment implements Serializable {

    private long id;
    @JsonbDateFormat(value = JsonbDateFormat.TIME_IN_MILLIS)
    private Date created;
    private String text;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
