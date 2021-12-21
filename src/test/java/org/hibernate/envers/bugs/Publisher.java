package org.hibernate.envers.bugs;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Publisher {
    @EmbeddedId
    private PublisherId id;

    public PublisherId getId() {
        return id;
    }

    public void setId(PublisherId id) {
        this.id = id;
    }

    @Embeddable
    public static class PublisherId implements Serializable {
        private String medium;

        private String name;

        public PublisherId() {

        }

        public PublisherId(String medium, String name) {
            this.medium = medium;
            this.name = name;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
