
package org.acme;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Greeting {

    @Id
    private Long id;

    private String greeting;
    private String lang;

    public Long getId() {
      return this.id;
    }

    public void setId(Long id) {
       this.id=id; 
    }

    public String getGreeting() {
      return this.greeting;
    }

    public void setGreeting(String greeting) {
      this.greeting=greeting;
    }

    public String getLang() {
      return this.lang;
    }

    public void setLang(String lang) {
      this.lang=lang;
    }


}
